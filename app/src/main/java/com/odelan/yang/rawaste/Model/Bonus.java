package com.odelan.yang.rawaste.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.Interface;

import java.util.ArrayList;
import java.util.List;

public class Bonus {
    public int amount;
    public String OID;
    public String UID;
    public long timestamp;

    public static void createBonus(Bonus bonus) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_BONUSCHAIN).push().setValue(bonus);
    }

    public static void getBonuschainsWithUID(final String UID, final Interface.OnResult<List<Bonus>> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_BONUSCHAIN).orderByChild("timestamp")
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    List<Bonus> bonuses = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Bonus bonus = snapshot.getValue(Bonus.class);
                                        if (bonus.UID.equals(UID)) bonuses.add(0, bonus);
                                    }
                                    callback.onSuccess(bonuses);
                                } else {
                                    callback.onSuccess(new ArrayList<>());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                callback.onFailure(databaseError.getMessage());
                            }
                        }
                );
    }
}
