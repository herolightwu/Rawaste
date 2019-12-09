package com.odelan.yang.rawaste.Activity.Product;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.odelan.yang.rawaste.Adapter.AnnouncementAdapter;
import com.odelan.yang.rawaste.Model.Product;
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

public class AnnouncementActivity extends AppCompatActivity implements AnnouncementAdapter.EventListener, OnRefreshListener {

    @BindView(R.id.txt_count) TextView txt_count;
    @BindView(R.id.refresh_layout) SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    AnnouncementAdapter adapter;
    List<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        ButterKnife.bind(this);
        setActivity();
    }

    void setActivity() {
        refresh_layout.setOnRefreshListener(this);
    }

    @Override
    protected  void onResume() {
        super.onResume();
        refresh_layout.autoRefresh();
    }

    void getProducts() {
        Product.getAllProductsWithUID(AppData.user.ID, new Interface.OnResult<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                refresh_layout.finishRefresh();
                products = result;
                txt_count.setText(products.size() + " items");
                adapter = new AnnouncementAdapter(AnnouncementActivity.this, products, AnnouncementActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(AnnouncementActivity.this));
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(String error) {
                refresh_layout.finishRefresh();
                showToast(error);
            }
        });
    }

    @Override
    public void onClickItem(int index) {
    }

    @Override
    public void onClickModify(int index) {
        Intent intent = new Intent(this, AnnouncementDetailsActivity.class);
        intent.putExtra(Constants.ACTIVITY_MODE, Constants.EDIT_MODE);
        AppData.announcement_product = products.get(index);
        startActivity(intent);
    }

    @Override
    public void onClickDelete(int index) {
        Product.removeProduct(products.get(index));
        products.remove(index);
        txt_count.setText(products.size() + " items");
        AppData.user.product_count--;
        User.updateUser(AppData.user);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @OnClick(R.id.btn_plus) void onClickPlus() {
        Intent intent = new Intent(this, AnnouncementDetailsActivity.class);
        intent.putExtra(Constants.ACTIVITY_MODE, Constants.ADD_MODE);
        startActivity(intent);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getProducts();
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
