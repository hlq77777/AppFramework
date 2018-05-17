package com.android.kingwong.kingwongproject.response;

import android.text.TextUtils;

import com.android.kingwong.kingwongproject.response.base.BaseResponse;

/**
 * Created by KingWong on 2017/9/19.
 * 自动更新
 */

public class UpDataResponse extends BaseResponse {

    private String package_path; //下载地址
    private String version_name; //版本名称
    private String version_number; //版本序号--按这个判断是否更新
    private String version_content; //更新内容
    private String mandatory_status; //是否强制更新1:是   0：否
    private String status; //是否更新1:是   0：否
    private String signature; //MD5检验

    public String getVersion_name() {
        return version_name;
    }
    public void Version_name(String version_name) {
        this.version_name = version_name;
    }

    public String getVersion_content() {
        return version_content;
    }
    public void setVersion_content(String version_content) {
        this.version_content = version_content;
    }

    public int getMandatory_status() {
        if(TextUtils.isEmpty(mandatory_status)){
            return 0;
        }else{
            return Integer.parseInt(mandatory_status);
        }
    }
    public void setMandatory_status(int mandatory_status) {
        this.mandatory_status = String.valueOf(mandatory_status);
    }

    public int getStatus() {
        if(TextUtils.isEmpty(status)){
            return 0;
        }else{
            return Integer.parseInt(status);
        }
    }
    public void setStatus(int status) {
        this.status = String.valueOf(status);
    }

    public int getVersion_number() {
        if(TextUtils.isEmpty(version_number)){
            return 0;
        }else{
            return Integer.parseInt(version_number);
        }
    }
    public void setVersion_number(int version_number) {
        this.version_number = String.valueOf(version_number);
    }

    public String getPackage_path() {
        return package_path;
    }
    public void setPackage_path(String package_path) {
        this.package_path = package_path;
    }

    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
}
