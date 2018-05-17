package com.android.kingwong.kingwongproject.response.base;

import java.io.Serializable;

/**
 * Created by KingWong on 2017/9/8.
 *基类Response
 */

public class BaseResponse implements Serializable {
    private String result;
    private String errcode;
    private String errmsg;
    private String sinapay_error; //新浪错误
    private String risk_error; //风控审核错误
    private String message; //运营商错误
    private String face_error; //人脸识别错误

    public String getResult(){
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }

    public String getErrcode() {
        return errcode;
    }
    public void setErrcode(String errcode){
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getSinapay_error() {
        return sinapay_error;
    }
    public void setSinapay_error(String sinapay_error) {
        this.sinapay_error = sinapay_error;
    }

    public String getRisk_error(){
        return risk_error;
    }
    public void setRisk_error(String risk_error) {
        this.risk_error = risk_error;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getFace_error(){
        return face_error;
    }
    public void setFace_error(String face_error) {
        this.face_error = face_error;
    }
}
