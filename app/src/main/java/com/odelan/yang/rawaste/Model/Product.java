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

public class Product implements Parcelable {
    public String ID = "";
    public String UID = "";
    public String name = "";
    public String description = "";
    public String email = "";
    public String country = "";
    public String country_name = "";
    public String city = "";
    public String phone = "";
    public String country_code = "";
    public int payment_type = 0; // 0 - free, 1 - paid
    public float price = 0;
    public int bonus_chain = 0;
    public int interest_exchange = 0;
    public long timestamp = 0;
    public int status = 1;
    public List<String> images = new ArrayList<>();

    public Product() {
    }

    protected Product(Parcel in) {
        ID = in.readString();
        UID = in.readString();
        name = in.readString();
        description = in.readString();
        email = in.readString();
        country = in.readString();
        country_name = in.readString();
        city = in.readString();
        phone = in.readString();
        country_code = in.readString();
        payment_type = in.readInt();
        price = in.readFloat();
        bonus_chain = in.readInt();
        interest_exchange = in.readInt();
        timestamp = in.readLong();
        status = in.readInt();
        images = in.createStringArrayList();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public static void getProductWithID(String ID, final Interface.OnResult<Product> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_PRODUCT).orderByKey().equalTo(ID)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Product product = snapshot.getValue(Product.class);
                                        callback.onSuccess(product);
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

    public static void getAllProductsWithUID(String UID, final Interface.OnResult<List<Product>> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_PRODUCT).orderByChild("UID").equalTo(UID)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    List<Product> products = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Product product = snapshot.getValue(Product.class);
                                        if (product.status == 1) products.add(0, product);
                                    }
                                    callback.onSuccess(products);
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

    public static void getAllProductsWithRange(String UID, int range, final Interface.OnResult<List<Product>> callback) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_PRODUCT).limitToFirst(range)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    List<Product> products = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Product product = snapshot.getValue(Product.class);
                                        if (product.status == 1) products.add(0, product);
                                    }
                                    callback.onSuccess(products);
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

    static void getProductList(final List<String> ids, final List<Product> products, final int index, final Interface.OnResult<List<Product>> callback) {
        if (index == ids.size()) {
            callback.onSuccess(products);
            return;
        }
        getProductWithID(ids.get(index), new Interface.OnResult<Product>() {
            @Override
            public void onSuccess(Product result) {
                if (result != null) {
                    products.add(result);
                }
                getProductList(ids, products, index + 1, callback);
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        });
    }

    public static void getProductList(List<String> ids, Interface.OnResult<List<Product>> callback) {
        getProductList(ids, new ArrayList<Product>(), 0, callback);
    }


    public static void addProduct(Product product) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_PRODUCT).child(product.ID).setValue(product);
    }

    public static void removeProduct(Product product) {
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_PRODUCT).child(product.ID).child("status").setValue(0);
    }

    public void createID() {
        ID = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_PRODUCT).push().getKey();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(UID);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(email);
        dest.writeString(country);
        dest.writeString(country_name);
        dest.writeString(city);
        dest.writeString(phone);
        dest.writeString(country_code);
        dest.writeInt(payment_type);
        dest.writeFloat(price);
        dest.writeInt(bonus_chain);
        dest.writeInt(interest_exchange);
        dest.writeLong(timestamp);
        dest.writeInt(status);
        dest.writeStringList(images);
    }
}
