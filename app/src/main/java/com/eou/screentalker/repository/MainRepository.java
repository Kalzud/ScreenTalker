package com.eou.screentalker.repository;

import com.eou.screentalker.Utilities.Data2Model;
import com.eou.screentalker.Utilities.DataModelType;
import com.eou.screentalker.Utilities.ErrorCallback;
import com.eou.screentalker.Utilities.NewEventCallBack;
import com.eou.screentalker.Utilities.SuccessCallback;
import com.eou.screentalker.remote.FirebaseClient;

public class MainRepository {

    private FirebaseClient firebaseClient;
    private String currentUsername;
    private void updateCurrentUsername(String username){
        this.currentUsername = username;
    }

    private MainRepository(){
        this.firebaseClient = new FirebaseClient();
    }

    private static MainRepository instance;
    public static MainRepository getInstance(){
        if (instance == null){
            instance = new MainRepository();
        }
        return instance;
    }

    public void login(String username, SuccessCallback callback){
        firebaseClient.login(username, ()->{
            updateCurrentUsername(username);
            callback.onSuccess();
        });
    }

    public void sendCallRequest(String target, ErrorCallback errorCallback){
        System.out.println("sentrequets");
        firebaseClient.sendMessageToOtherUser(
                new Data2Model(target, currentUsername, null, DataModelType.StartCall), errorCallback
        );
    }

    public void subscribeForLatestEvent(NewEventCallBack callBack){
        firebaseClient.observeIncomingLatestEvent(model -> {
            switch (model.getType()){
                case Offer:
                    break;
                case Answer:
                    break;
                case IceCandidate:
                    break;
                case StartCall:
                    callBack.onNewEventReceived(model);
                    break;
            }
        });
    }
}
