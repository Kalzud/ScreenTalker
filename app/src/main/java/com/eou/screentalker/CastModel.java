package com.eou.screentalker;

public class CastModel {
    String cname;
    String cimageurl;

    public CastModel() {
    }

    public CastModel(String cname, String cimageurl) {
        this.cname = cname;
        this.cimageurl = cimageurl;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCimageurl() {
        return cimageurl;
    }

    public void setCimageurl(String cimageurl) {
        this.cimageurl = cimageurl;
    }
}
