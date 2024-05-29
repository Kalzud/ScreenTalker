package com.eou.screentalker.Models;

import java.io.Serializable;

public class FriendModel implements Serializable {
    public String friend_id, friend_pImage, friend_username, friend_token, person_id, person_username;
    public boolean canSendText, admin;
}
