package com.odelan.yang.rawaste.Activity.Profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.odelan.yang.rawaste.Activity.Product.ProductActivity;
import com.odelan.yang.rawaste.Adapter.ProductAdapter;
import com.odelan.yang.rawaste.Model.Favorite;
import com.odelan.yang.rawaste.Model.Product;
import com.odelan.yang.rawaste.Model.Subscribe;
import com.odelan.yang.rawaste.Model.User;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.Interface;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class OtherProfileActivity extends AppCompatActivity implements OnRefreshListener, ProductAdapter.EventListener {


    @BindView(R.id.img_profile) ImageView img_profile;
    @BindView(R.id.rating_review) MaterialRatingBar rating_review;
    @BindView(R.id.txt_review) TextView txt_review;
    @BindView(R.id.txt_name) TextView txt_name;
    @BindView(R.id.txt_type) TextView txt_type;
    @BindView(R.id.txt_duns) TextView txt_duns;
    @BindView(R.id.txt_activity_area) TextView txt_activity_area;
    @BindView(R.id.txt_type_waste) TextView txt_type_waste;

    @BindView(R.id.refresh_layout) SmartRefreshLayout refresh_layout;

    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    ProductAdapter adapter;
    List<Product> products = new ArrayList<>();

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        user = intent.getParcelableExtra(Constants.INTENT_USER);

        setActivity();
    }

    void setActivity() {
        if (!user.photo.isEmpty()) {
            Glide.with(this).load(user.photo).into(img_profile);
        }
        rating_review.setProgress(user.review);
        txt_review.setText(user.review + "%");
        txt_name.setText(user.firstname + " " + user.lastname);
        txt_type.setText(user.type);
        txt_duns.setText("Duns : " + user.duns);
        txt_activity_area.setText("Activity Area : " + user.activity_area);
        txt_type_waste.setText("Type of Main waste : " + user.type_waste);

        refresh_layout.setOnRefreshListener(this);
        refresh_layout.autoRefresh();
    }

    void getProducts() {
        Product.getAllProductsWithUID(user.ID, new Interface.OnResult<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                refresh_layout.finishRefresh();
                products = result;
                adapter = new ProductAdapter(OtherProfileActivity.this, products, OtherProfileActivity.this);
                recycler_view.setLayoutManager(new LinearLayoutManager(OtherProfileActivity.this));
                recycler_view.setAdapter(adapter);
            }
            @Override
            public void onFailure(String error) {
                refresh_layout.finishRefresh();
                showToast(error);
            }
        });
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getProducts();
    }

    @OnClick(R.id.btn_subscribe) void onClickSubscribe() {
        Subscribe.checkSubscription(AppData.user.ID, user.ID, new Interface.OnResult<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result == true) {
                    showToast(getString(R.string.subscribe_already_added));
                } else {
                    Subscribe.addSubscribe(AppData.user.ID, user.ID);
                    showToast(getString(R.string.subscribe_success));
                }
            }

            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickItem(int index) {
        if (products.get(index).status == 0) {
            showToast(getString(R.string.product_deleted));
            return;
        }
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra(Constants.INTENT_PRODUCT, products.get(index));
        startActivity(intent);
    }

    @Override
    public void onClickBookmark(final int index) {
        Favorite.checkFavorite(AppData.user.ID, products.get(index).ID, new Interface.OnResult<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result == true) {
                    showToast(getString(R.string.favorite_already_added));
                } else {
                    Favorite.addFavorite(AppData.user.ID, products.get(index).ID);
                    showToast(getString(R.string.favorite_success));
                }
            }
            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });
    }
}
