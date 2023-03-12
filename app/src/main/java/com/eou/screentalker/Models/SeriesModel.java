package com.eou.screentalker.Models;

public class SeriesModel {
    //for series
    private String Scast;
    private String Scover;
    private String Sdes;
    private String Slink;
    private String Sthumb;
    private String Stitle;
    private String Tlink;

    public SeriesModel() {
       // default constructor
    }

    //variable constructor
    public SeriesModel(String scast, String scover, String sdes, String slink, String sthumb, String stitle, String tlink) {
        Scast = scast;
        Scover = scover;
        Sdes = sdes;
        Slink = slink;
        Sthumb = sthumb;
        Stitle = stitle;
        Tlink = tlink;
    }

    //getter and setters

    public String getScast() {
        return Scast;
    }

    public void setScast(String scast) {
        Scast = scast;
    }

    public String getScover() {
        return Scover;
    }

    public void setScover(String scover) {
        Scover = scover;
    }

    public String getSdes() {
        return Sdes;
    }

    public void setSdes(String sdes) {
        Sdes = sdes;
    }

    public String getSlink() {
        return Slink;
    }

    public void setSlink(String slink) {
        Slink = slink;
    }

    public String getSthumb() {
        return Sthumb;
    }

    public void setSthumb(String sthumb) {
        Sthumb = sthumb;
    }

    public String getStitle() {
        return Stitle;
    }

    public void setStitle(String stitle) {
        Stitle = stitle;
    }

    public String getTlink() {
        return Tlink;
    }

    public void setTlink(String tlink) {
        Tlink = tlink;
    }
}
