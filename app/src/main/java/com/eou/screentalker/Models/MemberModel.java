package com.eou.screentalker.Models;

import java.io.Serializable;

public class MemberModel implements Serializable {
    public String id, dp, name;
    public boolean canSendText, admin;
}
