package com.android.kingwong.kingwongproject.response.base;

import java.io.Serializable;

/**
 * Created by KingWong on 2017/9/16.
 * 列表基类Response
 */

public class BaseListResponse implements Serializable {
    private String result;
    private String errcode;
    private String errmsg;
    private String total; //表示数据的总数
    private String per_page; //表示每一页显示的数据条目
    private String current_page; //表示当前页面的页码
    private String last_page; //表示数据的总页数
    private String next_page_url; //表示下一页的链接
    private String prev_page_url; //表示上一页的链接
    private String from; //获取切片中第一个条目的数量
    private String to; //获取切片中最后一个条目的数量

    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }

    public String getErrcode() {
        return errcode;
    }
    public void setErr_code(String err_code) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }

    public String getPer_page() {
        return per_page;
    }
    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public String getCurrent_page() {
        return current_page;
    }
    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public String getLast_page() {
        return last_page;
    }
    public void setLast_page(String last_page) {
        this.last_page = last_page;
    }

    public String getNext_page_url() {
        return next_page_url;
    }
    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }
    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
}
