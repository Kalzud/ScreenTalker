//package com.eou.screentalker.Firebase;
//
//import com.eou.screentalker.Listeners.FirebaseService;
//import com.google.android.gms.tasks.Task;
//import com.google.android.gms.tasks.Tasks;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class FirebaseServiceImpl implements FirebaseService {
//    private final FirebaseFirestore db;
//
//    public FirebaseServiceImpl(FirebaseFirestore db) {
//        this.db = db;
//    }
//
//
//    @Override
//    public async Task<Boolean> areFriends(String userId, String personId) {
//        List<Query> friendQueries = new ArrayList<>();
//        friendQueries.add(db.collection("friends")
//                .whereEqualTo("friendId", userId)
//                .whereEqualTo("personId", personId));
//        friendQueries.add(db.collection("friends")
//                .whereEqualTo("friendId", personId)
//                .whereEqualTo("personId", userId));
//
//        // Execute all queries concurrently and check if any results are found
//        List<Task<QuerySnapshot>> tasks = friendQueries.stream()
//                .map(query -> query.get())
//                .collect(Collectors.toList());
//        List<QuerySnapshot> snapshots = await Tasks.whenAll(tasks)
//                .thenApply(tasks -> tasks.stream().map(Task::getResult).toList());
//
//        return snapshots.stream().anyMatch(snapshot -> !snapshot.getDocuments().isEmpty());
//    }
//
//
//    @Override
//    public async Task<Boolean> areRequesting(String userId, String otherUserId) {
//
//    }
//
//    @Override
//    public com.google.android.gms.tasks.Task<Boolean> areFriends(String userId, String otherUserId) {
//        return null;
//    }
//
//    @Override
//    public com.google.android.gms.tasks.Task<Boolean> getRequesting(String userId, String otherUserId) {
//        List<Query> requestQueries = new ArrayList<>();
//        requestQueries.add(db.collection("requests")
//                .whereEqualTo("requestSenderId", userId)
//                .whereEqualTo("requestReceiverId", otherUserId));
//        requestQueries.add(db.collection("requests")
//                .whereEqualTo("requestSenderId", otherUserId)
//                .whereEqualTo("requestReceiverId", userId));
//
//        // Execute all queries concurrently and check if any results are found
//        List<Task<QuerySnapshot>> tasks = requestQueries.stream()
//                .map(query -> query.get())
//                .collect(Collectors.toList());
//        List<QuerySnapshot> snapshots = await Tasks.whenAll(tasks)
//                .thenApply(tasks -> tasks.stream().map(Task::getResult).toList());
//
//        return snapshots.stream().anyMatch(snapshot -> !snapshot.getDocuments().isEmpty());
//    }
//}
