package com.odelan.yang.rawaste.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.Interface;

import java.util.ArrayList;
import java.util.List;

public class Favorite {

    public static void getFavoriteWithUID(String UID, final Interface.OnResult<List<Product>> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_FAVORITE).child(UID)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    List<String> ids = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        ids.add(0, snapshot.getKey());
                                    }
                                    Product.getProductList(ids, callback);
                                } else {
                                    callback.onSuccess(new ArrayList<Product>());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                callback.onFailure(databaseError.getMessage());
                            }
                        }
                );
    }

    public static void checkFavorite(String UID, String PID, final Interface.OnResult<Boolean> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_FAVORITE).child(UID).orderByKey().equalTo(PID)
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

    public static void deleteFavorite(String UID, String ID) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_FAVORITE).child(UID).child(ID).setValue(null);
    }

    public static void addFavorite(String UID, String ID) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_FAVORITE).child(UID).child(ID).setValue(true);
    }
}
