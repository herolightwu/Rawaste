package com.odelan.yang.rawaste.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.odelan.yang.rawaste.Activity.Product.ProductActivity;
import com.odelan.yang.rawaste.Adapter.FavoriteAdapter;
import com.odelan.yang.rawaste.Model.Favorite;
import com.odelan.yang.rawaste.Model.Product;
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

public class FavoriteActivity extends AppCompatActivity implements FavoriteAdapter.EventListener, OnRefreshListener {

    @BindView(R.id.refresh_layout) SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    FavoriteAdapter adapter;
    List<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        setActivity();
    }

    void setActivity() {
        refresh_layout.setOnRefreshListener(this);
        refresh_layout.autoRefresh();
    }

    void getFavorite() {
        Favorite.getFavoriteWithUID(AppData.user.ID, new Interface.OnResult<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                refresh_layout.finishRefresh();
                products = result;
                adapter = new FavoriteAdapter(FavoriteActivity.this, products, FavoriteActivity.this);
                recycler_view.setLayoutManager(new LinearLayoutManager(FavoriteActivity.this));
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
    public void onClickDelete(int index) {
        Favorite.deleteFavorite(AppData.user.ID, products.get(index).ID);
        products.remove(index);
        adapter.notifyDataSetChanged();
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getFavorite();
    }
}
