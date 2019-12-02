package com.odelan.yang.rawaste.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.odelan.yang.rawaste.Adapter.ProductDetailsAdapter;
import com.odelan.yang.rawaste.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactSellerActivity extends AppCompatActivity implements ProductDetailsAdapter.EventListener {

    @BindView(R.id.img_product) ImageView img_product;
    @BindView(R.id.txt_title) TextView txt_title;
    @BindView(R.id.txt_bonus_count) TextView txt_bonus_count;
    @BindView(R.id.edit_currency) EditText edit_currency;
    @BindView(R.id.edit_message) EditText edit_message;
    @BindView(R.id.img_currency) ImageView img_currency;

    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    ProductDetailsAdapter adapter;
    List<String> urls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_seller);
        ButterKnife.bind(this);

        adapter = new ProductDetailsAdapter(this, urls, this);
        recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_view.setAdapter(adapter);
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @OnClick(R.id.btn_search) void onClickSearch() {

    }

    @OnClick(R.id.btn_share) void onClickShare() {

    }

    @OnClick(R.id.btn_bookmark) void onClickBookmark() {

    }

    @OnClick(R.id.btn_cart) void onClickCart() {

    }

    @OnClick(R.id.btn_free) void onClickFree() {

    }

    @OnClick(R.id.btn_paid) void onClickPaid() {

    }

    @OnClick(R.id.btn_bonuschain) void onClickBonuschain() {

    }

    @OnClick(R.id.btn_camera) void onClickCamera() {

    }

    @OnClick(R.id.btn_send) void onClickSend() {

    }

    @Override
    public void onClickItem(int index) {

    }
}
