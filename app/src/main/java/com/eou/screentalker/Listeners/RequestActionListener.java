package com.eou.screentalker.Listeners;

import com.eou.screentalker.Models.RequestModel;

public interface RequestActionListener {
    void onAccept(RequestModel request);
    void onReject(RequestModel request);
}
