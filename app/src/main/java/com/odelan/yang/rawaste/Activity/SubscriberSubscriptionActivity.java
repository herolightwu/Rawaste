package com.odelan.yang.rawaste.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.odelan.yang.rawaste.Adapter.PagerAdapter;
import com.odelan.yang.rawaste.Fragment.PurchaseFragment;
import com.odelan.yang.rawaste.Fragment.SalesFragment;
import com.odelan.yang.rawaste.Fragment.SubscriberFragment;
import com.odelan.yang.rawaste.Fragment.SubscriptionFragment;
import com.odelan.yang.rawaste.Model.Order;
import com.odelan.yang.rawaste.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubscriberSubscriptionActivity extends AppCompatActivity implements SubscriberFragment.Listener, SubscriptionFragment.Listener {

    @BindView(R.id.txt_subscribers) TextView txt_subscribers;
    @BindView(R.id.txt_subscriptions) TextView txt_subscriptions;

    @BindView(R.id.view_pager)
    ViewPager view_pager;
    PagerAdapter adapter;
    Fragment[] fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_subscription);
        ButterKnife.bind(this);

        fragments = new Fragment[] {
                new SubscriberFragment(this, this),
                new SubscriptionFragment(this, this)
        };
        adapter = new PagerAdapter(this, fragments, getSupportFragmentManager());
        view_pager.setAdapter(adapter);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                selectPageTitle(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    void selectPageTitle(int position) {
        if (position == 0) {
            TextViewCompat.setTextAppearance(txt_subscribers, R.style.activeTabText);
            TextViewCompat.setTextAppearance(txt_subscriptions, R.style.normalTabText);
        } else {
            TextViewCompat.setTextAppearance(txt_subscribers, R.style.normalTabText);
            TextViewCompat.setTextAppearance(txt_subscriptions, R.style.activeTabText);
        }
    }

    @OnClick(R.id.txt_subscribers) void onClickSubscriber() {
        view_pager.setCurrentItem(0);
        selectPageTitle(0);
    }

    @OnClick(R.id.txt_subscriptions) void onClickSubscription() {
        view_pager.setCurrentItem(1);
        selectPageTitle(1);
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
