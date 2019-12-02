package com.odelan.yang.rawaste.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.yang.rawaste.Activity.Order.OrderActivity;
import com.odelan.yang.rawaste.Adapter.BonuschainAdapter;
import com.odelan.yang.rawaste.Adapter.OrderAdapter;
import com.odelan.yang.rawaste.Model.Bonus;
import com.odelan.yang.rawaste.Model.Order;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Interface;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BonuschainActivity extends BaseActivity implements BonuschainAdapter.EventListener {

    @BindView(R.id.txt_bonus) TextView txt_bonus;
    @BindView(R.id.refresh_layout) SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    BonuschainAdapter adapter;
    List<Bonus> bonuses = new ArrayList<>();

    OnRefreshListener refreshListener = refreshLayout -> getBonues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonuschain);

        ButterKnife.bind(this);

        setActivity();
    }

    void setActivity() {
        txt_bonus.setText(String.valueOf(AppData.user.bonuschain));
        refresh_layout.setOnRefreshListener(refreshListener);
    }

    void getBonues() {
        Bonus.getBonuschainsWithUID(AppData.user.ID, new Interface.OnResult<List<Bonus>>() {
            @Override
            public void onSuccess(List<Bonus> result) {
                refresh_layout.finishRefresh();
                bonuses = result;
                adapter = new BonuschainAdapter(BonuschainActivity.this, bonuses, BonuschainActivity.this);
                recycler_view.setLayoutManager(new LinearLayoutManager(BonuschainActivity.this));
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

    }
}
