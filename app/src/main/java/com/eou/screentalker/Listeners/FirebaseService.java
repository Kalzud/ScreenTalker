package com.eou.screentalker.Listeners;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;

public interface FirebaseService {
    Task<Boolean> areFriends (String userId, String otherUserId);
    Task<Boolean> getRequesting (String userId, String otherUserId);
}
