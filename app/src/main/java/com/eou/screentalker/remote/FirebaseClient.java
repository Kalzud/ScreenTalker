package com.eou.screentalker.remote;

import androidx.annotation.NonNull;

import com.eou.screentalker.Utilities.Data2Model;
import com.eou.screentalker.Utilities.ErrorCallback;
import com.eou.screentalker.Utilities.NewEventCallBack;
import com.eou.screentalker.Utilities.SuccessCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Objects;

public class FirebaseClient {
    private final Gson gson = new Gson();
    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private String currentUsername;
    private static final String LATEST_EVENT_FIELD_NAME = "latest_event";

    public void login(String username, SuccessCallback callback){
        dbRef.child(username).setValue("").addOnCompleteListener(task -> {
            currentUsername = username;
            callback.onSuccess();
        });
    }
    public void sendMessageToOtherUser(Data2Model data2Model, ErrorCallback errorCallback){
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(data2Model.getTarget()).exists()){
                    dbRef.child(data2Model.getTarget()).child(LATEST_EVENT_FIELD_NAME).setValue(gson.toJson(data2Model));

                }else{
                    errorCallback.onError();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorCallback.onError();
            }
        });
    }
    public void observeIncomingLatestEvent(NewEventCallBack callBack){
//        System.out.println(currentUsername);
//        dbRef.child(currentUsername).child(LATEST_EVENT_FIELD_NAME).addValueEventListener(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        try{
//                            String data = Objects.requireNonNull(snapshot.getValue()).toString();
//                            Data2Model data2Model = gson.fromJson(data,Data2Model.class);
//                            callBack.onNewEventReceived(data2Model);
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                }
//        );
    }
}
