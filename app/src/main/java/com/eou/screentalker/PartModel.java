package com.eou.screentalker;

public class PartModel {
    String part;
    String thumburl;
    String vidurl;

    public PartModel() {
    }

    public PartModel(String part, String thumburl, String vidurl) {
        this.part = part;
        this.thumburl = thumburl;
        this.vidurl = vidurl;
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
}
