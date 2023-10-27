package com.eou.screentalker.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommunityModel implements Serializable {
    //for community
    private String name;
    private String dp_url;
    private List<MemberModel> members;
    private String id;
    private boolean is_public;

    public CommunityModel() {
    }

    public CommunityModel(String name, String dp_url, List<MemberModel> members, boolean is_public, String id) {
        this.name = name;
        this.dp_url = dp_url;
        this.members = members;
        this.is_public = is_public;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDp_url() {
        return dp_url;
    }

    public void setDp_url(String dp_url) {
        this.dp_url = dp_url;
    }

    public List<MemberModel> getMembers() {
        return members;
    }

    public void setMembers(List<MemberModel> members) {
        this.members = members;
    }

    public boolean isIs_public() {
        return is_public;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
