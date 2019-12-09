package com.odelan.yang.rawaste.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.odelan.yang.rawaste.Util.Interface;
import com.odelan.yang.rawaste.Util.Constants;
import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {
    public String ID = "";
    public String facebookID = "";
    public String googleID = "";
    public String email = "";
    public String firstname = "";
    public String lastname = "";
    public String type = Constants.USER_TYPE_INDIVIDUAL; // "Individual", "Company"
    public String phone = "";
    public String country_code = "";
    public String duns = "";
    public String activity_area = "";
    public String type_waste = "";
    public boolean online = false;
    public Double latitude = 0.0;
    public Double longitude = 0.0;
    public String photo = "";

    public double balance = 0;
    public int bonuschain = 0;

    public int product_count = 0;
    public int review = 0;
    public int evaluation = 0;

    public String privateKey = "";
    public String publicKey = "";
    public String address = "";

    public User() { }

    protected User(Parcel in) {
        ID = in.readString();
        facebookID = in.readString();
        googleID = in.readString();
        email = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        type = in.readString();
        phone = in.readString();
        country_code = in.readString();
        duns = in.readString();
        activity_area = in.readString();
        type_waste = in.readString();
        online = in.readByte() != 0;
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        photo = in.readString();
        bonuschain = in.readInt();
        balance = in.readDouble();
        product_count = in.readInt();
        review = in.readInt();
        evaluation = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static void checkUserWithID(String ID, final Interface.OnResult<User> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USER).orderByKey().equalTo(ID)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        User user = snapshot.getValue(User.class);
                                        callback.onSuccess(user);
                                    }
                                } else {
                                    callback.onSuccess(null);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                callback.onFailure(databaseError.getMessage());
                            }
                        }
                );
    }

    public static void checkUserWithFacebook(String facebookID, final Interface.OnResult<User> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USER).orderByChild(Constants.FIREBASE_FACEBOOK).equalTo(facebookID)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        User user = snapshot.getValue(User.class);
                                        callback.onSuccess(user);
                                    }
                                } else {
                                    callback.onSuccess(null);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                callback.onFailure(databaseError.getMessage());
                            }
                        }
                    );
    }

    public static void checkUserWithGoogle(String googleID, final Interface.OnResult<User> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USER).orderByChild(Constants.FIREBASE_GOOGLE).equalTo(googleID)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        User user = snapshot.getValue(User.class);
                                        callback.onSuccess(user);
                                    }
                                } else {
                                    callback.onSuccess(null);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                callback.onFailure(databaseError.getMessage());
                            }
                        }
                );
    }

    public static void createUser(User user) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USER).child(user.ID).setValue(user);
    }

    public static void updateUser(User user) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USER).child(user.ID).setValue(user);
    }

    static void getUserList(final List<String> ids, final List<User> users, final int index, final Interface.OnResult<List<User>> callback) {
        if (index == ids.size()) {
            callback.onSuccess(users);
            return;
        }
        checkUserWithID(ids.get(index), new Interface.OnResult<User>() {
            @Override
            public void onSuccess(User result) {
                if (result != null) {
                    users.add(result);
                }
                getUserList(ids, users, index + 1, callback);
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        });
    }

    public static void getUserList(List<String> ids, Interface.OnResult<List<User>> callback) {
        getUserList(ids, new ArrayList<User>(), 0, callback);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(facebookID);
        dest.writeString(googleID);
        dest.writeString(email);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(type);
        dest.writeString(phone);
        dest.writeString(country_code);
        dest.writeString(duns);
        dest.writeString(activity_area);
        dest.writeString(type_waste);
        dest.writeByte((byte) (online ? 1 : 0));
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(photo);
        dest.writeInt(bonuschain);
        dest.writeDouble(balance);
        dest.writeInt(product_count);
        dest.writeInt(review);
        dest.writeInt(evaluation);
    }
}
