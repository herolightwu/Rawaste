package com.odelan.yang.rawaste.Activity.Main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.odelan.yang.rawaste.Activity.Ethereum.WalletActivity;
import com.odelan.yang.rawaste.Activity.Product.AnnouncementActivity;
import com.odelan.yang.rawaste.Activity.BonuschainActivity;
import com.odelan.yang.rawaste.Activity.Payment.CheckoutActivity;
import com.odelan.yang.rawaste.Activity.FavoriteActivity;
import com.odelan.yang.rawaste.Activity.LocationActivity;
import com.odelan.yang.rawaste.Activity.Auth.LoginActivity;
import com.odelan.yang.rawaste.Activity.Chat.MessageActivity;
import com.odelan.yang.rawaste.Activity.Profile.MyProfileActivity;
import com.odelan.yang.rawaste.Activity.Order.OrderActivity;
import com.odelan.yang.rawaste.Activity.Product.ProductActivity;
import com.odelan.yang.rawaste.Activity.SalesAndPurchaseActivity;
import com.odelan.yang.rawaste.Adapter.ProductAdapter;
import com.odelan.yang.rawaste.Model.Cart;
import com.odelan.yang.rawaste.Model.Favorite;
import com.odelan.yang.rawaste.Model.Product;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.EtherscanAPI;
import com.odelan.yang.rawaste.Util.Interface;
import com.odelan.yang.rawaste.Util.TinyDB;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements ProductAdapter.EventListener, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.img_profile) CircleImageView img_profile;
    @BindView(R.id.txt_name) TextView txt_name;
    @BindView(R.id.txt_wallet) TextView txt_wallet;
    @BindView(R.id.edit_search) EditText edit_search;
    @BindView(R.id.txt_chart_notification_count) TextView txt_chart_notification_count;
    @BindView(R.id.img_menu_notification) ImageView img_menu_notification;
    @BindView(R.id.layout_chart_notification) RelativeLayout layout_chart_notification;
    @BindView(R.id.refresh_layout) SmartRefreshLayout refresh_layout;
    @BindView(R.id.layout_search)LinearLayout layout_search;
    @BindView(R.id.layout_advanced)LinearLayout layout_advanced;
    @BindView(R.id.layout_filter)LinearLayout layout_filter;
    @BindView(R.id.layout_top)LinearLayout layout_top;


    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    ProductAdapter adapter;
    List<Product> products = new ArrayList<>();
    private int range = 20;

    private boolean isSearch = false;
    private boolean isAdvancedSearch = false;
    private boolean isFilterSearch = false;

    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        tinyDB = new TinyDB(this);
        setActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!AppData.user.photo.isEmpty()) {
            Glide.with(this).load(AppData.user.photo).into(img_profile);
        }
        txt_name.setText(AppData.user.firstname + " " + AppData.user.lastname);

        layout_chart_notification.setVisibility(View.GONE);
        Cart.getCartWithUID(AppData.user.ID, new Interface.OnResult<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                if (result.size() > 0) {
                    layout_chart_notification.setVisibility(View.VISIBLE);
                    txt_chart_notification_count.setText(String.valueOf(result.size()));
                }
            }
            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });
        EtherscanAPI.getTokenBalances(AppData.user.address, new Interface.OnResult<Double>() {
            @Override
            public void onSuccess(Double result) {
                txt_wallet.setText(String.format("%.4f ETH", result));
            }

            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });
    }

    void setActivity() {
        if (!AppData.user.photo.isEmpty()) {
            Glide.with(this).load(AppData.user.photo).into(img_profile);
        }
        txt_name.setText(AppData.user.firstname + " " + AppData.user.lastname);

        refresh_layout.setOnRefreshListener(this);
        refresh_layout.setOnLoadMoreListener(this);
        refresh_layout.autoRefresh();

        isSearch = false;
        layout_search.setVisibility(View.GONE);
        layout_top.setVisibility(View.VISIBLE);

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString();
                if (newText.isEmpty()) {
                    isSearch = false;
                    layout_search.setVisibility(View.GONE);
                    layout_top.setVisibility(View.VISIBLE);
                } else {
                    isSearch = true;
                    isAdvancedSearch = isFilterSearch = false;
                    layout_top.setVisibility(View.GONE);
                    layout_search.setVisibility(View.VISIBLE);
                    layout_advanced.setVisibility(View.GONE);
                    layout_filter.setVisibility(View.GONE);
                }
            }
        });
    }

    void getProducts(final boolean isRefresh) {
        Product.getAllProductsWithRange(AppData.user.ID, range, new Interface.OnResult<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                refresh_layout.finishRefresh();
                refresh_layout.finishLoadMore();
                products = result;
                range = Math.max(range, products.size());
                adapter = new ProductAdapter(MainActivity.this, products, MainActivity.this);
                recycler_view.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recycler_view.setAdapter(adapter);
                if (isRefresh == false) {
                    recycler_view.scrollToPosition(products.size() - 1);
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

    @OnClick(R.id.btn_advanced) void onClickAdvanced() {
        if (isAdvancedSearch) {
            isAdvancedSearch = false;
            layout_advanced.setVisibility(View.GONE);
        } else {
            isAdvancedSearch = true;
            isFilterSearch = false;
            layout_filter.setVisibility(View.GONE);
            layout_advanced.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_filter) void onClickFilter() {
        if (isFilterSearch) {
            isFilterSearch = false;
            layout_filter.setVisibility(View.GONE);
        } else {
            isFilterSearch = true;
            isAdvancedSearch = false;
            layout_advanced.setVisibility(View.GONE);
            layout_filter.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.menu_message) void onClickMessage() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.menu_announcements) void onClickAnnouncement() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(this, AnnouncementActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.menu_registered_products) void onClickRegisteredProducts() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.menu_sales_purchases) void onClickSalesPurchases() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(this, SalesAndPurchaseActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.menu_rawaste_bonuschain) void onClickRawasteBonusChain() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(this, BonuschainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.menu_customer_service) void onClickCustomerService() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @OnClick(R.id.btn_signout) void onClickSignout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        tinyDB.putBoolean(Constants.LOGIN_ISLOGGEDIN, false);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_menu) void onClickMenu() {
        drawer.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.btn_chart) void onClickChart() {
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }

//    @OnClick(R.id.btn_recommended_sellers) void onClickRecommendedSellers() {
//
//    }
//
//    @OnClick(R.id.btn_recent_browse) void onClickRecentBrowse() {
//
//    }

    @OnClick(R.id.btn_huge_discount_sale) void onClickHugeDiscountSale() {

    }

    @OnClick(R.id.btn_free_shipping) void onClickFreeShipping() {
    }

    @OnClick(R.id.img_profile) void onClickProfile() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(this, MyProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_bookmark) void onClickBookmark() {
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_location) void onClickLocation() {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.txt_wallet) void onClickWallet() {
        Intent intent = new Intent(this, WalletActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickItem(int index) {
        if (products.get(index).UID.equals(AppData.user.ID)) {
            showToast(getString(R.string.your_product));
            return;
        }
        if (products.get(index).status == 0) {
            showToast(getString(R.string.product_deleted));
            return;
        }
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra(Constants.INTENT_PRODUCT, products.get(index));
        startActivity(intent);
    }

    @Override
    public void onClickBookmark(final int index) {
        if (products.get(index).UID.equals(AppData.user.ID)) {
            showToast(getString(R.string.your_product));
            return;
        }
        Favorite.checkFavorite(AppData.user.ID, products.get(index).ID, new Interface.OnResult<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result == true) {
                    showToast(getString(R.string.favorite_already_added));
                } else {
                    Favorite.addFavorite(AppData.user.ID, products.get(index).ID);
                    showToast(getString(R.string.favorite_success));
                }
            }
            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getProducts(true);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        range += 20;
        getProducts(false);
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
