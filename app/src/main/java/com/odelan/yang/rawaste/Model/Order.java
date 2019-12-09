package com.odelan.yang.rawaste.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.Interface;

import java.util.ArrayList;
import java.util.List;

public class Order implements Parcelable {
    public String ID = "";
    public String SID = "";
    public String BID = "";
    public String PID = "";
    public String apartment = "";
    public String street = "";
    public String city = "";
    public String country = "";
    public String phone_number = "";
    public String pincode = "";
    public String delivery = "";
    public int type = 0;
    public int count = 0;
    public double subtotal = 0;
    public double tax = 0;
    public double total = 0;
    public int status = 1;
    public long timestamp = 0;

    public Order() {
    }

    protected Order(Parcel in) {
        ID = in.readString();
        SID = in.readString();
        BID = in.readString();
        PID = in.readString();
        apartment = in.readString();
        street = in.readString();
        city = in.readString();
        country = in.readString();
        phone_number = in.readString();
        pincode = in.readString();
        delivery = in.readString();
        type = in.readInt();
        count = in.readInt();
        subtotal = in.readDouble();
        tax = in.readDouble();
        total = in.readDouble();
        status = in.readInt();
        timestamp = in.readLong();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public static void getOrderWithID(String ID, final Interface.OnResult<Order> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ORDER).orderByKey().equalTo(ID)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Order order = snapshot.getValue(Order.class);
                                        callback.onSuccess(order);
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

    public static void getAllOrderWithSID(final String SID, final Interface.OnResult<List<Order>> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ORDER).orderByChild("timestamp")
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    List<Order> orders = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Order order = snapshot.getValue(Order.class);
                                        if (order.SID.equals(SID)) orders.add(0, order);
                                    }
                                    callback.onSuccess(orders);
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

    public static void getAllOrderWithBID(final String BID, final Interface.OnResult<List<Order>> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ORDER).orderByChild("timestamp")
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    List<Order> orders = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Order order = snapshot.getValue(Order.class);
                                        if (order.BID.equals(BID)) orders.add(0, order);
                                    }
                                    callback.onSuccess(orders);
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

    public static void createOrder(Order order) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ORDER).child(order.ID).setValue(order);
    }

    public static void completeOrder(Order order) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ORDER).child(order.ID).child("status").setValue(0);
    }

    public void createID() {
        String key = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_ORDER).push().getKey();
        long hash = 0;
        for (char ch : key.toCharArray()) {
            hash = hash * 128 + ch;
            hash = hash % 1000000000000000L;
        }
        ID = String.valueOf(hash);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(SID);
        dest.writeString(BID);
        dest.writeString(PID);
        dest.writeString(apartment);
        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(phone_number);
        dest.writeString(pincode);
        dest.writeString(delivery);
        dest.writeInt(type);
        dest.writeInt(count);
        dest.writeDouble(subtotal);
        dest.writeDouble(tax);
        dest.writeDouble(total);
        dest.writeInt(status);
        dest.writeLong(timestamp);
    }
}
