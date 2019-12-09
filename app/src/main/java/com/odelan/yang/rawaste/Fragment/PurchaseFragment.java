package com.odelan.yang.rawaste.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.odelan.yang.rawaste.Adapter.MessageAdapter;
import com.odelan.yang.rawaste.Adapter.PurchaseAdapter;
import com.odelan.yang.rawaste.Adapter.SalesAdapter;
import com.odelan.yang.rawaste.Model.Message;
import com.odelan.yang.rawaste.Model.Order;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Interface;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PurchaseFragment extends Fragment implements PurchaseAdapter.EventListener, OnRefreshListener, OnLoadMoreListener {
    private Context context;
    private Listener mListener;

    @BindView(R.id.refresh_layout) SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    PurchaseAdapter adapter;
    List<Order> purchases = new ArrayList<>();

    private int range = 20;

    public PurchaseFragment() {
    }

    @SuppressLint("ValidFragment")
    public PurchaseFragment(Context context, Listener listener) {
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);
        ButterKnife.bind(this, view);

        refresh_layout.setOnRefreshListener(this);
        refresh_layout.setOnLoadMoreListener(this);
        refresh_layout.autoRefresh();


        return view;
    }

    @Override
    public void onClickItem(int index) {
        if (mListener != null) {
            mListener.onPurchaseItemClicked(purchases.get(index));
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getPurchase(true);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        range += 20;
        getPurchase(false);
    }

    void getPurchase(final Boolean isRefresh) {
        Order.getAllOrderWithBID(AppData.user.ID, new Interface.OnResult<List<Order>>() {
            @Override
            public void onSuccess(List<Order> result) {
                refresh_layout.finishRefresh();
                refresh_layout.finishLoadMore();
                purchases = result;

                adapter = new PurchaseAdapter(context, purchases, PurchaseFragment.this);
                recycler_view.setLayoutManager(new LinearLayoutManager(context));
                recycler_view.setAdapter(adapter);
                if (isRefresh == false) {
                    recycler_view.scrollToPosition(purchases.size() - 1);
                }
            }

            @Override
            public void onFailure(String error) {
                refresh_layout.finishRefresh();
                refresh_layout.finishLoadMore();
                showToast(error);
            }
        });
    }

    void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public interface Listener {
        void onPurchaseItemClicked(Order order);
    }
}
