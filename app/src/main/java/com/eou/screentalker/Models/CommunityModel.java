package com.eou.screentalker.Models;

import java.util.List;

public class CommunityModel {
    //for community
    private String name;
    private String group_pImage;
    private List<UserModel> Members;

    public CommunityModel() {
    }

    public CommunityModel(String name, String group_pImage, List<UserModel> members) {
        this.name = name;
        this.group_pImage = group_pImage;
        Members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup_pImage() {
        return group_pImage;
    }

    public void setGroup_pImage(String group_pImage) {
        this.group_pImage = group_pImage;
    }

    public List<UserModel> getMembers() {
        return Members;
    }

    public void setMembers(List<UserModel> members) {
        Members = members;
    }
}
