package com.odelan.yang.rawaste.Activity.Product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hbb20.CountryCodePicker;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.yang.rawaste.Adapter.ProductDetailsAdapter;
import com.odelan.yang.rawaste.Model.Product;
import com.odelan.yang.rawaste.Model.Storage;
import com.odelan.yang.rawaste.Model.User;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.Interface;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnnouncementDetailsActivity extends AppCompatActivity implements ProductDetailsAdapter.EventListener {

    @BindView(R.id.edit_name) EditText edit_name;
    @BindView(R.id.edit_description) EditText edit_description;
    @BindView(R.id.edit_email) EditText edit_email;
    @BindView(R.id.txt_country) TextView txt_country;
    @BindView(R.id.edit_city) EditText edit_city;
    @BindView(R.id.edit_phone_number) EditText edit_phone_number;
    @BindView(R.id.edit_price) EditText edit_price;

    @BindView(R.id.country) CountryCodePicker country;
    @BindView(R.id.country_code) CountryCodePicker country_code;

    @BindView(R.id.btn_free) Button btn_free;
    @BindView(R.id.btn_paid) Button btn_paid;
    @BindView(R.id.btn_bonuschain) LinearLayout btn_bonuschain;
    @BindView(R.id.img_bonuschain) ImageView img_bonuschain;
    @BindView(R.id.txt_bonus_count) TextView txt_bonus_count;

    @BindView(R.id.img_currency) ImageView img_currency;
    @BindView(R.id.img_interested) ImageView img_interested;

    @BindView(R.id.layout_price) LinearLayout layout_price;

    @BindView(R.id.img_product) ImageView img_product;
    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    ProductDetailsAdapter adapter;
    List<String> urls = new ArrayList<>();

    private int payment_type = -1;
    private boolean interested = false;

    private Product mProduct = null;
    private String mMode = Constants.ADD_MODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mMode = intent.getStringExtra(Constants.ACTIVITY_MODE);
        if (mMode.equals(Constants.EDIT_MODE)) {
            mProduct = AppData.announcement_product;
        } else {
            mProduct = null;
        }
        setActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
            this.urls.addAll(mPaths);
            adapter.notifyDataSetChanged();
        }
    }

    void setActivity() {
        country.setOnCountryChangeListener(() -> txt_country.setText(country.getSelectedCountryName()));
        country_code.registerCarrierNumberEditText(edit_phone_number);
        country_code.setNumberAutoFormattingEnabled(true);
        edit_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (payment_type == 1) { // ETH
                    btn_paid.setText(edit_price.getText().toString());
                } else if (payment_type == 2) { //Bonuschain
                    txt_bonus_count.setText(edit_price.getText().toString());
                }
            }
        });

        if (mMode.equals(Constants.EDIT_MODE)) {
            urls = mProduct.images;
            edit_name.setText(mProduct.name);
            edit_description.setText(mProduct.description);
            edit_email.setText(mProduct.email);
            country.setCountryForPhoneCode(Integer.valueOf(mProduct.country));
            edit_city.setText(mProduct.city);
            edit_phone_number.setText(mProduct.phone);
            country_code.setCountryForPhoneCode(Integer.valueOf(mProduct.country_code));
            edit_phone_number.setText(mProduct.phone);
            if (mProduct.payment_type == 0) {
                onClickFree();
            } else if (mProduct.payment_type == 1) {
                onClickPaid();
                edit_price.setText(String.valueOf((int)mProduct.price));
            } else if (mProduct.payment_type == 2) {
                onClickBonuschain();
                edit_price.setText(String.valueOf(mProduct.bonus_chain));
            }
            interested = mProduct.interest_exchange > 0 ? true : false;
            if (interested) {
                img_interested.setImageResource(R.mipmap.check_checked);
            } else {
                img_interested.setImageResource(R.mipmap.check_empty);
            }
        } else {
            layout_price.setVisibility(View.GONE);
        }

        // setup recycler view
        adapter = new ProductDetailsAdapter(this, urls, this);
        recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_view.setAdapter(adapter);
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @OnClick(R.id.btn_free) void onClickFree() {
        btn_free.setBackgroundResource(R.mipmap.product_free_button);
        btn_paid.setBackgroundResource(R.mipmap.product_paid_default_button);
        img_bonuschain.setBackgroundResource(R.mipmap.product_bonus_default_button);
        layout_price.setVisibility(View.GONE);
        payment_type = 0;
    }

    @OnClick(R.id.btn_paid) void onClickPaid() {
        btn_free.setBackgroundResource(R.mipmap.product_free_default_button);
        btn_paid.setBackgroundResource(R.mipmap.product_paid_button);
        img_bonuschain.setBackgroundResource(R.mipmap.product_bonus_default_button);
        layout_price.setVisibility(View.VISIBLE);
        edit_price.setHint("ETH");
        edit_price.setText("");
        img_currency.setImageResource(R.mipmap.ethereum);
        txt_bonus_count.setText("");
        payment_type = 1;
    }

    @OnClick(R.id.btn_bonuschain) void onClickBonuschain() {
        btn_free.setBackgroundResource(R.mipmap.product_free_default_button);
        btn_paid.setBackgroundResource(R.mipmap.product_paid_default_button);
        img_bonuschain.setBackgroundResource(R.mipmap.product_bonus_button);
        layout_price.setVisibility(View.VISIBLE);
        edit_price.setHint("Bonuschain");
        edit_price.setText("");
        img_currency.setImageResource(R.mipmap.bonus_coin);
        btn_paid.setText(getString(R.string.paid));
        payment_type = 2;
    }

    @OnClick(R.id.img_interested) void onClickInterested() {
        if (interested) {
            img_interested.setImageResource(R.mipmap.check_empty);
            interested = false;
        } else {
            img_interested.setImageResource(R.mipmap.check_checked);
            interested = true;
        }
    }

    @OnClick(R.id.btn_add) void onClickAdd() {
        new ImagePicker.Builder(this)
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .allowMultipleImages(true)
                .build();
    }

    @OnClick(R.id.btn_post) void onClickPost() {
        if (edit_name.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_product_name));
            return;
        }
        if (edit_description.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_description));
            return;
        }
        if (edit_email.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_email));
            return;
        }
        if (txt_country.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_country));
            return;
        }
        if (edit_city.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_city));
            return;
        }
        if (edit_phone_number.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_phone_number));
            return;
        }
        if (payment_type == -1) {
            showToast(getString(R.string.select_price));
            return;
        }
        if (payment_type > 0 // paid or bonuschain
                && edit_price.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_price));
            return;
        }
        if (urls.size() == 0) {
            showToast(getString(R.string.add_image));
            return;
        }

        final Product product = new Product();
        product.UID = AppData.user.ID;
        product.name = edit_name.getText().toString();
        product.description = edit_description.getText().toString();
        product.email = edit_email.getText().toString();
        product.country = country.getSelectedCountryCode();
        product.country_name = txt_country.getText().toString();
        product.city = edit_city.getText().toString();
        product.phone = edit_phone_number.getText().toString();
        product.country_code = country_code.getSelectedCountryCode();
        product.payment_type = payment_type;
        if (payment_type == 1) product.price = Float.valueOf(edit_price.getText().toString());
        else if (payment_type == 2) product.bonus_chain = Integer.valueOf(edit_price.getText().toString());
        product.interest_exchange = interested ? 1 : 0;
        product.timestamp = System.currentTimeMillis() / 1000;


        if (mMode.equals(Constants.ADD_MODE)) {
            product.createID();
        } else {
            product.ID = mProduct.ID;
        }

        final KProgressHUD hud =  KProgressHUD.create(this).setLabel("Please wait");
        hud.show();
        Storage.uploadFiles(urls, new Interface.OnResult<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                hud.dismiss();
                product.images = result;
                Product.addProduct(product);
                AppData.user.product_count++;
                User.updateUser(AppData.user);
                finish();
            }

            @Override
            public void onFailure(String error) {
                hud.dismiss();
                showToast(error);
            }
        });
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickItem(int index) {
        Glide.with(this).load(urls.get(index)).into(img_product);
    }
}
