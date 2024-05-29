package com.eou.screentalker;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import com.eou.screentalker.Activities.ProfileActivity;
import com.eou.screentalker.Models.FriendModel;
import com.eou.screentalker.Models.RequestModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class FriendStatusTest {
    private ProfileActivity profileActivity = new ProfileActivity();
    @Test
    public void testIsNotFriendOrRequested(){
        List<FriendModel> friends = new ArrayList<>();
        List<RequestModel> requests = new ArrayList<>();
        String profileOwnerid = "WWFGEFTDDVC";
        String micmiccurrentUserId = "QQEDFTGVCSWEIITED";

        // Create a list of friend models to be passed to the test
        FriendModel friend1 = new FriendModel();
        friend1.friend_id = profileOwnerid;
        friend1.friend_pImage = "url";
        friend1.friend_username = "mark";
        friend1.friend_token = "";
        friend1.person_id = "EWQSDVOOK";
        friend1.person_username = "mary";
        friend1.canSendText = false;
        friend1.admin = false;

        // Create a list of friend models to be passed to the test
        FriendModel friend2 = new FriendModel();
        friend1.friend_id = "FGGGTEDDVCEH";
        friend1.friend_pImage = "url";
        friend1.friend_username = "stacy";
        friend1.friend_token = "";
        friend1.person_id = "WWFGEFTDDVC";
        friend1.person_username = "mark";
        friend1.canSendText = false;
        friend1.admin = false;

        // Create a list of friend models to be passed to the test
        FriendModel friend3 = new FriendModel();
        friend1.friend_id = "ASDCXTREFH";
        friend1.friend_pImage = "url";
        friend1.friend_username = "james";
        friend1.friend_token = "";
        friend1.person_id = "FGGGTEDDVCEH";
        friend1.person_username = "stacy";
        friend1.canSendText = false;
        friend1.admin = false;

        friends.add(friend1);
        friends.add(friend2);
        friends.add(friend3);

        // Create a list of request models to be passed to the test
        RequestModel request1 = new RequestModel();
        request1.receiver_id = "ASDCTRY";
        request1.sender_id = "SDFEVCTY";
        request1.sender_pImage = "url";
        request1.sender_username = "jason";
        request1.sender_token = "";

        // Create a list of request models to be passed to the test
        RequestModel request2 = new RequestModel();
        request2.receiver_id = "SDFEVCTY";
        request2.sender_id = "FGGGTEDDVCEH";
        request2.sender_pImage = "url";
        request2.sender_username = "stacy";
        request2.sender_token = "";


        // Create a list of request models to be passed to the test
        RequestModel request3 = new RequestModel();
        request3.receiver_id = "ASDCXTREFH";
        request3.sender_id = "ASDCTRY";
        request3.sender_pImage = "url";
        request3.sender_username = "dami";
        request3.sender_token = "";

        requests.add(request1);
        requests.add(request2);
        requests.add(request3);

        for (FriendModel friend : friends) {
            String friendId = friend.friend_id;
            String personId = friend.person_id;
            profileActivity.checkFriendStatus(friendId,personId, micmiccurrentUserId, profileOwnerid);
            profileActivity.assignStatus();
            // Do something with friendId and personId here
            System.out.println("Friend ID: " + friendId + ", Person ID: " + personId); // Example usage
        }

        for (RequestModel request : requests) {
            String receiverId = request.receiver_id;
            String senderId = request.sender_id;
            profileActivity.checkRequestStatus(senderId, receiverId, micmiccurrentUserId, profileOwnerid);
            profileActivity.assignStatus();
            // Do something with friendId and personId here
            System.out.println("Friend ID: " + receiverId + ", Person ID: " + senderId); // Example usage
        }
        assertFalse(profileActivity.isFriend);
        assertFalse(profileActivity.isRequested);
        assertFalse(profileActivity.isRequestedYou);
    }

    @Test
    public void testIsFriendNotRequested(){
        List<FriendModel> friends = new ArrayList<>();
        List<RequestModel> requests = new ArrayList<>();
        String profileOwnerid = "WWFGEFTDDVC";
        String micmiccurrentUserId = "QQEDFTGVCSWEIITED";

        // Create a list of friend models to be passed to the test
        FriendModel friend1 = new FriendModel();
        friend1.friend_id = "WWFGEFTDDVC";
        friend1.friend_pImage = "url";
        friend1.friend_username = "mark";
        friend1.friend_token = "";
        friend1.person_id = "QQEDFTGVCSWEIITED";
        friend1.person_username = "user";
        friend1.canSendText = false;
        friend1.admin = false;

        // Create a list of friend models to be passed to the test
        FriendModel friend2 = new FriendModel();
        friend1.friend_id = "WWFGEFTDDVC";
        friend1.friend_pImage = "url";
        friend1.friend_username = "stacy";
        friend1.friend_token = "";
        friend1.person_id = "QQEDFTGVCSWEIITED";
        friend1.person_username = "mark";
        friend1.canSendText = false;
        friend1.admin = false;

        // Create a list of friend models to be passed to the test
        FriendModel friend3 = new FriendModel();
        friend1.friend_id = "WWFGEFTDDVC";
        friend1.friend_pImage = "url";
        friend1.friend_username = "james";
        friend1.friend_token = "";
        friend1.person_id = "QQEDFTGVCSWEIITED";
        friend1.person_username = "stacy";
        friend1.canSendText = false;
        friend1.admin = false;

        friends.add(friend1);
        friends.add(friend2);
        friends.add(friend3);

        // Create a list of request models to be passed to the test
        RequestModel request1 = new RequestModel();
        request1.receiver_id = "ASDCTRY";
        request1.sender_id = "SDFEVCTY";
        request1.sender_pImage = "url";
        request1.sender_username = "jason";
        request1.sender_token = "";

        // Create a list of request models to be passed to the test
        RequestModel request2 = new RequestModel();
        request2.receiver_id = "SDFEVCTY";
        request2.sender_id = "FGGGTEDDVCEH";
        request2.sender_pImage = "url";
        request2.sender_username = "stacy";
        request2.sender_token = "";


        // Create a list of request models to be passed to the test
        RequestModel request3 = new RequestModel();
        request3.receiver_id = "ASDCXTREFH";
        request3.sender_id = "ASDCTRY";
        request3.sender_pImage = "url";
        request3.sender_username = "dami";
        request3.sender_token = "";

        requests.add(request1);
        requests.add(request2);
        requests.add(request3);

        for (FriendModel friend : friends) {
            String friendId = friend.friend_id;
            String personId = friend.person_id;
            profileActivity.checkFriendStatus(friendId,personId, micmiccurrentUserId, profileOwnerid);
            System.out.println("isFriend after checkFriendStatus: " + profileActivity.isFriend);
            profileActivity.assignStatus();
            System.out.println("Friend ID: " + friendId + ", Person ID: " + personId);
            // Do something with friendId and personId here
            System.out.println("Friend ID: " + friendId + ", Person ID: " + personId); // Example usage
        }


        for (RequestModel request : requests) {
            String receiverId = request.receiver_id;
            String senderId = request.sender_id;
            profileActivity.checkRequestStatus(senderId, receiverId, micmiccurrentUserId, profileOwnerid);
            profileActivity.assignStatus();
            // Do something with friendId and personId here
            System.out.println("Friend ID: " + receiverId + ", Person ID: " + senderId); // Example usage
        }
        assertTrue(profileActivity.isFriend);
        assertFalse(profileActivity.isRequested);
        assertFalse(profileActivity.isRequestedYou);
    }

    @Test
    public void testNotFriendisRequested(){
        List<FriendModel> friends = new ArrayList<>();
        List<RequestModel> requests = new ArrayList<>();
        String profileOwnerid = "WWFGEFTDDVC";
        String micmiccurrentUserId = "QQEDFTGVCSWEIITED";

        // Create a list of friend models to be passed to the test
        FriendModel friend1 = new FriendModel();
        friend1.friend_id = "WWFGEFTDVC";
        friend1.friend_pImage = "url";
        friend1.friend_username = "mark";
        friend1.friend_token = "";
        friend1.person_id = "QQEDFTGVCSWITED";
        friend1.person_username = "user";
        friend1.canSendText = false;
        friend1.admin = false;

        // Create a list of friend models to be passed to the test
        FriendModel friend2 = new FriendModel();
        friend1.friend_id = "WWFGEFDVC";
        friend1.friend_pImage = "url";
        friend1.friend_username = "stacy";
        friend1.friend_token = "";
        friend1.person_id = "QQEDFTCSWEIITED";
        friend1.person_username = "mark";
        friend1.canSendText = false;
        friend1.admin = false;

        // Create a list of friend models to be passed to the test
        FriendModel friend3 = new FriendModel();
        friend1.friend_id = "WWFGEFDDVC";
        friend1.friend_pImage = "url";
        friend1.friend_username = "james";
        friend1.friend_token = "";
        friend1.person_id = "QQEDFTGWEIITED";
        friend1.person_username = "stacy";
        friend1.canSendText = false;
        friend1.admin = false;

        friends.add(friend1);
        friends.add(friend2);
        friends.add(friend3);

        // Create a list of request models to be passed to the test
        RequestModel request1 = new RequestModel();
        request1.receiver_id = "WWFGEFTDDVC";
        request1.sender_id = "QQEDFTGVCSWEIITED";
        request1.sender_pImage = "url";
        request1.sender_username = "jason";
        request1.sender_token = "";

        // Create a list of request models to be passed to the test
        RequestModel request2 = new RequestModel();
        request2.receiver_id = "WWFGEFTDDVC";
        request2.sender_id = "QQEDFTGVCSWEIITED";
        request2.sender_pImage = "url";
        request2.sender_username = "stacy";
        request2.sender_token = "";


        // Create a list of request models to be passed to the test
        RequestModel request3 = new RequestModel();
        request3.receiver_id = "WWFGEFTDDVC";
        request3.sender_id = "QQEDFTGVCSWEIITED";
        request3.sender_pImage = "url";
        request3.sender_username = "dami";
        request3.sender_token = "";

        requests.add(request1);
        requests.add(request2);
        requests.add(request3);

        for (FriendModel friend : friends) {
            String friendId = friend.friend_id;
            String personId = friend.person_id;
            profileActivity.checkFriendStatus(friendId,personId, micmiccurrentUserId, profileOwnerid);
            System.out.println("isFriend after checkFriendStatus: " + profileActivity.isFriend);
            profileActivity.assignStatus();
            System.out.println("Friend ID: " + friendId + ", Person ID: " + personId);
            // Do something with friendId and personId here
            System.out.println("Friend ID: " + friendId + ", Person ID: " + personId); // Example usage
        }


        for (RequestModel request : requests) {
            String receiverId = request.receiver_id;
            String senderId = request.sender_id;
            profileActivity.checkRequestStatus(senderId, receiverId, micmiccurrentUserId, profileOwnerid);
            profileActivity.assignStatus();
            // Do something with friendId and personId here
            System.out.println("Friend ID: " + receiverId + ", Person ID: " + senderId); // Example usage
        }
        assertFalse(profileActivity.isFriend);
        assertTrue(profileActivity.isRequested);
        assertFalse(profileActivity.isRequestedYou);
    }
}
