package com.odelan.yang.rawaste.Activity.Chat;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.odelan.yang.rawaste.Adapter.PagerAdapter;
import com.odelan.yang.rawaste.Fragment.MessageFragment;
import com.odelan.yang.rawaste.Fragment.NotificationFragment;
import com.odelan.yang.rawaste.Model.User;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends AppCompatActivity implements MessageFragment.Listener, NotificationFragment.Listener {

    @BindView(R.id.txt_message) TextView txt_message;
    @BindView(R.id.txt_notification) TextView txt_notification;

    @BindView(R.id.view_pager) ViewPager view_pager;
    PagerAdapter adapter;
    Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ButterKnife.bind(this);
        fragments = new Fragment[] {
                new MessageFragment(this, this),
                new NotificationFragment(this, this)
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
            TextViewCompat.setTextAppearance(txt_message, R.style.activeTabText);
            TextViewCompat.setTextAppearance(txt_notification, R.style.normalTabText);
        } else {
            TextViewCompat.setTextAppearance(txt_message, R.style.normalTabText);
            TextViewCompat.setTextAppearance(txt_notification, R.style.activeTabText);
        }
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @OnClick(R.id.txt_message) void onClickMessage() {
        view_pager.setCurrentItem(0);
        selectPageTitle(0);
    }

    @OnClick(R.id.txt_notification) void onClickNotification() {
        view_pager.setCurrentItem(1);
        selectPageTitle(1);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public void onChat(User user) {
        Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
        intent.putExtra(Constants.INTENT_USER, user);
        startActivity(intent);
    }
}
