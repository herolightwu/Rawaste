package com.odelan.yang.rawaste.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.odelan.yang.rawaste.Activity.Profile.OtherProfileActivity;
import com.odelan.yang.rawaste.Adapter.SubscriptionAdapter;
import com.odelan.yang.rawaste.Model.Subscribe;
import com.odelan.yang.rawaste.Model.User;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.Interface;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscriptionFragment extends Fragment implements SubscriptionAdapter.EventListener, OnRefreshListener, OnLoadMoreListener {
    private Context context;
    private Listener mListener;

    @BindView(R.id.refresh_layout)SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    SubscriptionAdapter adapter;
    List<User> users = new ArrayList<>();

    private int range = 20;

    public SubscriptionFragment() {
    }

    @SuppressLint("ValidFragment")
    public SubscriptionFragment(Context context, Listener listener) {
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);
        ButterKnife.bind(this, view);

        refresh_layout.setOnRefreshListener(this);
        refresh_layout.setOnLoadMoreListener(this);
        refresh_layout.autoRefresh();
        return view;
    }

    void getSubscription(final Boolean isRefresh) {
        Subscribe.getSubscriptionsWithUIDAndRange(AppData.user.ID, range, new Interface.OnResult<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                refresh_layout.finishRefresh();
                refresh_layout.finishLoadMore();
                users = result;
                range = Math.max(range, users.size());
                adapter = new SubscriptionAdapter(context, users, SubscriptionFragment.this);
                recycler_view.setLayoutManager(new LinearLayoutManager(context));
                recycler_view.setAdapter(adapter);
                if (isRefresh == false) {
                    recycler_view.scrollToPosition(users.size() - 1);
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

    @Override
    public void onClickItem(int index) {
        Intent intent = new Intent(context, OtherProfileActivity.class);
        intent.putExtra(Constants.INTENT_USER, users.get(index));
        startActivity(intent);
    }

    @Override
    public void onClickUnsubscribe(int index) {
        Subscribe.deleteSubscribe(AppData.user.ID, users.get(index).ID);
        users.remove(index);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getSubscription(true);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        range += 20;
        getSubscription(false);
    }

    void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public interface Listener {
    }
}
