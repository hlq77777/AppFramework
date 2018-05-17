package com.android.kingwong.kingwongproject.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import com.android.kingwong.appframework.util.LogUtil;
import com.android.kingwong.appframework.util.StringUtil;
import com.android.kingwong.kingwongproject.BuildConfig;
import com.android.kingwong.kingwongproject.activity.UpDataActivity;
import com.android.kingwong.kingwongproject.response.UpDataResponse;
import com.android.kingwong.kingwongproject.util.SharedPreferencesUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created by KingWong on 2017/9/19.
 * 自动更新服务
 */

public class UpDataService extends Service {
    //更新进度---
    public static final String UPDATA_ACTION = "com.android.cwtz.shikedai.service.UpDataService";

    //更新----
    private HttpHandler httpHandler = null;
    private UpDataResponse upData;
    private String filepath = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (intent != null) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(UPDATA_ACTION)) {
                    upData = (UpDataResponse) intent.getSerializableExtra("data");
                    LogUtil.e("UpDataService", "请求下载");
                    startDownload();
                }
            }
        }
    }

    //开始下载
    private void startDownload() {
        if (httpHandler == null)
            filepath = Environment.getExternalStorageDirectory() + "/shikedai" + StringUtil.getNotNullString(upData.getVersion_name(), "") + ".apk";
            httpHandler = new HttpUtils().download(StringUtil.getNotNullString(upData.getPackage_path(), ""),
                Environment.getExternalStorageDirectory() + "/shikedai" + StringUtil.getNotNullString(upData.getVersion_name(), "") + ".apk",
                true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {
                    @Override
                    public void onCancelled() {
                        super.onCancelled();
                        LogUtil.e("UpDataService", "=================下载取消");
                        sendBroadcast(new Intent().setAction(UpDataActivity.UpData).putExtra("state", 3));
                    }
                    @Override
                    public void onStart() {
                        LogUtil.e("UpDataService", "==========================conn...开始下载");
                        sendBroadcast(new Intent().setAction(UpDataActivity.UpData).putExtra("state", 1));
                    }
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        LogUtil.e("UpDataService", "=================" + current + "/" + total);
                        float length = ((float) current / total);
                        LogUtil.e("UpDataService", "" + length);
                        Intent intent = new Intent(UpDataActivity.UpData);
                        intent.putExtra("state", 2);
                        intent.putExtra("length", length);
                        sendBroadcast(intent);
                    }
                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        LogUtil.e("UpDataService", "====================downloaded:" + responseInfo.result.getPath());
                        httpHandler = null;
                        SharedPreferencesUtil.saveUpData(UpDataService.this, responseInfo.result.getPath());
                        if(verifyInstallPackage(responseInfo.result.getPath(), StringUtil.getNotNullString(upData.getSignature(), ""))){
                            LogUtil.e("UpDataService", "onSuccess: 检验成功");
                            Intent intent = new Intent(UpDataActivity.UpData);
                            intent.putExtra("state", 5);
                            intent.putExtra("path", responseInfo.result.getPath());
                            sendBroadcast(intent);
                            Intent intent2 = new Intent(Intent.ACTION_VIEW);
                            //版本在7.0以上是不能直接通过uri访问的
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                File file = (new File(responseInfo.result.getPath()));
                                // 由于没有在Activity环境下启动Activity,设置下面的标签
                                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                                Uri apkUri = FileProvider.getUriForFile(UpDataService.this, BuildConfig.APPLICATION_ID + ".fileProvider", file);
                                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                                intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent2.setDataAndType(apkUri, "application/vnd.android.package-archive");
                            } else {
                                intent2.setDataAndType(Uri.fromFile(new File(responseInfo.result.getPath())),
                                        "application/vnd.android.package-archive");
                                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            }
                            startActivity(intent2);
                        }else {
                            LogUtil.e("UpDataService", "onSuccess: 检验失败");
                            if(DeleteFile(filepath)){
                                LogUtil.e("deleteFile", "删除成功");
                            }else{
                                LogUtil.e("deleteFile", "删除失败");
                            }
                            Intent intent = new Intent(UpDataActivity.UpData);
                            intent.putExtra("state", 4);
                            intent.putExtra("errow", "安装包下载出错");
                            sendBroadcast(intent);
                        }
                    }
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        LogUtil.e("UpDataService", "=================下载失败:" + msg);
                        httpHandler = null;
                        if (msg.equals("maybe the file has downloaded completely")) {
                            //String path = PreferenceManager.getDefaultSharedPreferences(UpDataService.this).getString(ConstantValue.SP_PATH, "");
                            if(verifyInstallPackage(filepath, StringUtil.getNotNullString(upData.getSignature(), ""))){
                                LogUtil.e("UpDataService", "onFailure: 检验成功");
                                Intent intent = new Intent(UpDataActivity.UpData);
                                intent.putExtra("state", 4);
                                intent.putExtra("errow", msg);
                                sendBroadcast(intent);
                                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                                //版本在7.0以上是不能直接通过uri访问的
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES. N) {
                                    File file = (new File(filepath));
                                    // 由于没有在Activity环境下启动Activity,设置下面的标签
                                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                                    Uri apkUri = FileProvider.getUriForFile(UpDataService.this, BuildConfig.APPLICATION_ID + ".fileProvider", file);
                                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                                    intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    intent2.setDataAndType(apkUri, "application/vnd.android.package-archive");
                                } else {
                                    intent2.setDataAndType(Uri.fromFile(new File(filepath)),
                                            "application/vnd.android.package-archive");
                                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                }
                                startActivity(intent2);
                            }else{
                                LogUtil.e("UpDataService", "onFailure: 检验失败");
                                if(DeleteFile(filepath)){
                                    LogUtil.e("deleteFile", "删除成功");
                                }else{
                                    LogUtil.e("deleteFile", "删除失败");
                                }
                                Intent intent = new Intent(UpDataActivity.UpData);
                                intent.putExtra("state", 4);
                                intent.putExtra("errow", "安装包下载出错");
                                sendBroadcast(intent);
                            }
                        }else if(msg.contains("org.apache.http.conn.ConnectTimeoutException")){
                            LogUtil.e("UpDataService", "onFailure: 连接超时");
                            if(DeleteFile(filepath)){
                                LogUtil.e("deleteFile", "删除成功");
                            }else{
                                LogUtil.e("deleteFile", "删除失败");
                            }
                            Intent intent = new Intent(UpDataActivity.UpData);
                            intent.putExtra("state", 4);
                            intent.putExtra("errow", "安装包下载出错");
                            sendBroadcast(intent);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (httpHandler != null)
            httpHandler.cancel();
        httpHandler = null;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        int i = 0;
        while (i < src.length) {
            int v;
            String hv;
            v = (src[i] >> 4) & 0x0F;
            hv = Integer.toHexString(v);
            stringBuilder.append(hv);
            v = src[i] & 0x0F;
            hv = Integer.toHexString(v);
            stringBuilder.append(hv);
            i++;
        }
        return stringBuilder.toString();
    }

    public static boolean verifyInstallPackage(String packagePath, String crc) {
        try {
            MessageDigest sig = MessageDigest.getInstance("MD5");
            File packageFile = new File(packagePath);
            InputStream signedData = new FileInputStream(packageFile);
            byte[] buffer = new byte[4096];//每次检验的文件区大小
            long toRead = packageFile.length();
            long soFar = 0;
            boolean interrupted = false;
            while (soFar < toRead) {
                interrupted = Thread.interrupted();
                if (interrupted) break;
                int read = signedData.read(buffer);
                soFar += read;
                sig.update(buffer, 0, read);
            }
            byte[] digest = sig.digest();
            String digestStr = bytesToHexString(digest);//将得到的MD5值进行移位转换
            digestStr = digestStr.toLowerCase();
            crc = crc.toLowerCase();
            LogUtil.e("verifyInstallPackage", "digestStr: " + digestStr);
            LogUtil.e("verifyInstallPackage", "crc: " + crc);
            if (digestStr.equals(crc)) {//比较两个文件的MD5值，如果一样则返回true
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return false;
    }

    public boolean DeleteFile (String filePath){
        LogUtil.e("deleteFile", filePath);
        File file = new File(filePath);
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                return file.delete(); // delete()方法 你应该知道 是删除的意思;
            }
        }
        return false;
    }
}
