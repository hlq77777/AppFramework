package com.android.kingwong.appframework.util;

import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Locale;

public class FileUtil {

    public static boolean saveFile(InputStream inputStream, File fileDir, String fileName) {
        return saveFile(inputStream, fileDir, fileName, true);
    }

    public static boolean saveFile(InputStream inputStream, File fileDir, String fileName, boolean recreate) {
        OutputStream outputStream = null;
        try {
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File targetFile = new File(fileDir, fileName);
            if (targetFile.exists() && recreate) {
                targetFile.delete();
            }
            try {
                byte[] fileReader = new byte[4096];
                outputStream = new FileOutputStream(targetFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();
                return true;
            } catch (FileNotFoundException e) {//文件不存在
                LogUtil.d("FileUtil", e.getMessage());
                return false;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    public static String getMimeType(File file) {
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return "file/*";
    }

    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if(!f.exists()) {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
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

    public static boolean deleteFile (String filePath){
        File file = new File(filePath);
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                return file.delete(); // delete()方法 你应该知道 是删除的意思;
            }
        }
        return false;
    }
}
