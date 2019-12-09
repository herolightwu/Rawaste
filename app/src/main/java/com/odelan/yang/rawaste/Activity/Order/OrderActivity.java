package com.odelan.yang.rawaste.Activity.Order;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.odelan.yang.rawaste.Adapter.OrderAdapter;
import com.odelan.yang.rawaste.Model.Order;
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

public class OrderActivity extends AppCompatActivity implements OrderAdapter.EventListener {

    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    @BindView(R.id.refresh_layout) SmartRefreshLayout refresh_layout;
    OrderAdapter adapter;
    List<Order> orders = new ArrayList<>();

    OnRefreshListener refreshListener = refreshLayout -> getOrders();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        setActivity();
    }

    @Override
    protected  void onResume() {
        super.onResume();
        refresh_layout.autoRefresh();
    }

    void setActivity() {
        refresh_layout.setOnRefreshListener(refreshListener);
    }

    void getOrders() {
        Order.getAllOrderWithBID(AppData.user.ID, new Interface.OnResult<List<Order>>() {
            @Override
            public void onSuccess(List<Order> result) {
                refresh_layout.finishRefresh();
                orders = result;
                adapter = new OrderAdapter(OrderActivity.this, orders, OrderActivity.this);
                recycler_view.setLayoutManager(new LinearLayoutManager(OrderActivity.this));
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
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra(Constants.INTENT_ORDER, orders.get(index));
        startActivity(intent);
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
