package com.odelan.yang.rawaste.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.odelan.yang.rawaste.Adapter.NotificationAdapter;
import com.odelan.yang.rawaste.Model.Notification;
import com.odelan.yang.rawaste.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends Fragment implements NotificationAdapter.EventListener {
    private Context context;
    private NotificationFragment.Listener listener;

    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    NotificationAdapter adapter;
    List<Notification> notifications = new ArrayList<>();

    public NotificationFragment() {
    }

    @SuppressLint("ValidFragment")
    public NotificationFragment(Context context, NotificationFragment.Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);

        adapter = new NotificationAdapter(context, notifications, this);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClickItem(int index) {

    }

    @Override
    public void onDelete(int index) {

    }

    @Override
    public void onChat(int index) {

    }

    public interface Listener {
    }
}
