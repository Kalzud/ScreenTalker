package com.eou.screentalker.Models;

public class PartModel {
    String part;
    String thumburl;
    String vidurl;
    String type;

    public PartModel() {
    }

    public PartModel(String part, String thumburl, String vidurl, String type) {
        this.part = part;
        this.thumburl = thumburl;
        this.vidurl = vidurl;
        this.type = type;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getThumburl() {
        return thumburl;
    }

    public void setThumburl(String thumburl) {
        this.thumburl = thumburl;
    }

    public String getVidurl() {
        return vidurl;
    }

    public void setVidurl(String vidurl) {
        this.vidurl = vidurl;
    }

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}
}
