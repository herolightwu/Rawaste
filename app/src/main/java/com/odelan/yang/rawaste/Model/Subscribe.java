package com.odelan.yang.rawaste.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.Interface;

import java.util.ArrayList;
import java.util.List;

public class Subscribe {

    public static void getSubscribersWithUID(String UID, final Interface.OnResult<List<User>> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_SUBSCRIBER).child(UID)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    List<String> ids = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        ids.add(0, snapshot.getKey());
                                    }
                                    User.getUserList(ids, callback);
                                } else {
                                    callback.onSuccess(new ArrayList<User>());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                callback.onFailure(databaseError.getMessage());
                            }
                        }
                );
    }

    public static void getSubscribersWithUIDAndRange(String UID, int range, final Interface.OnResult<List<User>> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_SUBSCRIBER).child(UID).limitToFirst(range)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    List<String> ids = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        ids.add(0, snapshot.getKey());
                                    }
                                    User.getUserList(ids, callback);
                                } else {
                                    callback.onSuccess(new ArrayList<User>());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                callback.onFailure(databaseError.getMessage());
                            }
                        }
                );
    }

    public static void checkSubscriber(String UID, String SID, final Interface.OnResult<Boolean> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_SUBSCRIBER).child(UID).orderByKey().equalTo(SID)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    callback.onSuccess(true);
                                } else {
                                    callback.onSuccess(false);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                callback.onFailure(databaseError.getMessage());
                            }
                        }
                );
    }

    public static void getSubscriptionsWithUID(String UID, final Interface.OnResult<List<User>> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_SUBSCRIPTION).child(UID)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    List<String> ids = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        ids.add(0, snapshot.getKey());
                                    }
                                    User.getUserList(ids, callback);
                                } else {
                                    callback.onSuccess(new ArrayList<User>());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                callback.onFailure(databaseError.getMessage());
                            }
                        }
                );
    }

    public static void getSubscriptionsWithUIDAndRange(String UID, int range, final Interface.OnResult<List<User>> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_SUBSCRIPTION).child(UID).limitToFirst(range)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    List<String> ids = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        ids.add(0, snapshot.getKey());
                                    }
                                    User.getUserList(ids, callback);
                                } else {
                                    callback.onSuccess(new ArrayList<User>());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                callback.onFailure(databaseError.getMessage());
                            }
                        }
                );
    }

    public static void checkSubscription(String UID, String SID, final Interface.OnResult<Boolean> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_SUBSCRIPTION).child(UID).orderByKey().equalTo(SID)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    callback.onSuccess(true);
                                } else {
                                    callback.onSuccess(false);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                callback.onFailure(databaseError.getMessage());
                            }
                        }
                );
    }

    public static void deleteSubscribe(String UID, String SID) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_SUBSCRIPTION).child(UID).child(SID).setValue(null);
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_SUBSCRIBER).child(SID).child(UID).setValue(null);
    }

    public static void addSubscribe(String UID, String SID) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_SUBSCRIPTION).child(UID).child(SID).setValue(true);
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_SUBSCRIBER).child(SID).child(UID).setValue(true);
    }
}
