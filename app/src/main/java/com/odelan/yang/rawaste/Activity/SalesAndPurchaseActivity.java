package com.odelan.yang.rawaste.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.odelan.yang.rawaste.Activity.Payment.BillActivity;
import com.odelan.yang.rawaste.Adapter.PagerAdapter;
import com.odelan.yang.rawaste.Fragment.PurchaseFragment;
import com.odelan.yang.rawaste.Fragment.SalesFragment;
import com.odelan.yang.rawaste.Model.Order;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SalesAndPurchaseActivity extends AppCompatActivity implements SalesFragment.Listener, PurchaseFragment.Listener {

    @BindView(R.id.txt_sales) TextView txt_sales;
    @BindView(R.id.txt_purchase) TextView txt_purchase;

    @BindView(R.id.view_pager) ViewPager view_pager;
    PagerAdapter adapter;
    Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_and_purchase);
        ButterKnife.bind(this);

        fragments = new Fragment[] {
                new SalesFragment(this, this),
                new PurchaseFragment(this, this)
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
            TextViewCompat.setTextAppearance(txt_sales, R.style.activeTabText);
            TextViewCompat.setTextAppearance(txt_purchase, R.style.normalTabText);
        } else {
            TextViewCompat.setTextAppearance(txt_sales, R.style.normalTabText);
            TextViewCompat.setTextAppearance(txt_purchase, R.style.activeTabText);
        }
    }

    @OnClick(R.id.txt_sales) void onClickSales() {
        view_pager.setCurrentItem(0);
        selectPageTitle(0);
    }

    @OnClick(R.id.txt_purchase) void onClickPurchase() {
        view_pager.setCurrentItem(1);
        selectPageTitle(1);
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @Override
    public void onPurchaseItemClicked(Order order) {
        Intent intent = new Intent(this, BillActivity.class);
        intent.putExtra(Constants.INTENT_ORDER, order);
        startActivity(intent);
    }

    @Override
    public void onSalesItemClicked(Order order) {
        Intent intent = new Intent(this, BillActivity.class);
        intent.putExtra(Constants.INTENT_ORDER, order);
        startActivity(intent);
    }
}
