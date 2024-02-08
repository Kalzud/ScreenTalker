package com.eou.screentalker.Models;

import java.io.Serializable;

public class FriendModel implements Serializable {
    public String friend_id, friend_pImage, friend_username, person_id;
    public boolean canSendText, admin;
}
