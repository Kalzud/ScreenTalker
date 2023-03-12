package com.eou.screentalker.Models;

/**
 *  Author: Emmanuel O. Uduma
 *
 * This class would be used for DataModel objects
 * these would be the trailer objects
 * which would consist of the thumbnail, name and video
 */
public class DataModel {
    //fields
    private String Ttitle;
    private String Turl;
    private String Tvid;


    //empty constructor required by firebase
    public DataModel() {
    }

    //constructor
    public DataModel(String ttitle, String turl, String tvid) {
        Ttitle = ttitle;
        Turl = turl;
        Tvid = tvid;
    }


    public String getTtitle() {
        return Ttitle;
    }

    public void setTtitle(String ttitle) {
        Ttitle = ttitle;
    }

    public String getTurl() {
        return Turl;
    }

    public void setTurl(String turl) {
        Turl = turl;
    }

    public String getTvid() {
        return Tvid;
    }

    public void setTvid(String tvid) {
        Tvid = tvid;
    }

}
