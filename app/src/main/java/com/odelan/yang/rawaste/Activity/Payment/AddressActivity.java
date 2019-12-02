package com.odelan.yang.rawaste.Activity.Payment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.odelan.yang.rawaste.Model.Order;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.TinyDB;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressActivity extends AppCompatActivity {

    @BindView(R.id.edit_apartment_building_name) EditText edit_apartment_building_name;
    @BindView(R.id.edit_street_address) EditText edit_street_address;
    @BindView(R.id.edit_city) EditText edit_city;
    @BindView(R.id.txt_country) TextView txt_country;
    @BindView(R.id.edit_phone_number) EditText edit_phone_number;
    @BindView(R.id.edit_pincode) EditText edit_pincode;
    @BindView(R.id.chk_accept) CheckBox chk_accept;

    @BindView(R.id.country_code) CountryCodePicker country_code;
    @BindView(R.id.phone_code) CountryCodePicker phone_code;

    TinyDB tinyDB;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        tinyDB = new TinyDB(this);

        Intent intent = getIntent();
        order = intent.getParcelableExtra(Constants.INTENT_ORDER);

        setActivity();
    }

    void setActivity() {
        country_code.setOnCountryChangeListener(() -> txt_country.setText(country_code.getSelectedCountryName()));
        phone_code.registerCarrierNumberEditText(edit_phone_number);
        phone_code.setNumberAutoFormattingEnabled(true);
        if (tinyDB.getBoolean(Constants.ADDRESS_ISSAVED)) {
            edit_apartment_building_name.setText(tinyDB.getString(Constants.ADDRESS_APARTMENT));
            edit_street_address.setText(tinyDB.getString(Constants.ADDRESS_STREET));
            edit_city.setText(tinyDB.getString(Constants.ADDRESS_CITY));
            txt_country.setText(tinyDB.getString(Constants.ADDRESS_COUNTRY));
            edit_phone_number.setText(tinyDB.getString(Constants.ADDRESS_PHONE));
            edit_pincode.setText(tinyDB.getString(Constants.ADDRESS_PINCODE));
            country_code.setCountryForPhoneCode(tinyDB.getInt(Constants.ADDRESS_COUNTRY_CODE));
            phone_code.setCountryForPhoneCode(tinyDB.getInt(Constants.ADDRESS_PHONE_CODE));
            chk_accept.setChecked(true);
        } else {
            chk_accept.setChecked(false);
        }
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @OnClick(R.id.btn_continue) void onClickContinue() {
        if (edit_apartment_building_name.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_apartment));
            return;
        }
        if (edit_street_address.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_street));
            return;
        }
        if (edit_city.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_city));
            return;
        }
        if (txt_country.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_country));
            return;
        }
        if (edit_phone_number.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_phone_number));
            return;
        }
        if (edit_pincode.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_pincode));
            return;
        }
        if (chk_accept.isChecked() == true) {
            tinyDB.putString(Constants.ADDRESS_APARTMENT, edit_apartment_building_name.getText().toString());
            tinyDB.putString(Constants.ADDRESS_STREET, edit_street_address.getText().toString());
            tinyDB.putString(Constants.ADDRESS_CITY, edit_city.getText().toString());
            tinyDB.putString(Constants.ADDRESS_COUNTRY, txt_country.getText().toString());
            tinyDB.putInt(Constants.ADDRESS_COUNTRY_CODE, country_code.getSelectedCountryCodeAsInt());
            tinyDB.putString(Constants.ADDRESS_PHONE, edit_phone_number.getText().toString());
            tinyDB.putInt(Constants.ADDRESS_PHONE_CODE, phone_code.getSelectedCountryCodeAsInt());
            tinyDB.putString(Constants.ADDRESS_PINCODE, edit_pincode.getText().toString());
            tinyDB.putBoolean(Constants.ADDRESS_ISSAVED, true);
        } else {
            tinyDB.putBoolean(Constants.ADDRESS_ISSAVED, false);
        }

        order.apartment = edit_apartment_building_name.getText().toString();
        order.street = edit_street_address.getText().toString();
        order.city = edit_city.getText().toString();
        order.country = txt_country.getText().toString();
        order.phone_number = edit_phone_number.getText().toString();
        order.pincode = edit_pincode.getText().toString();

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(Constants.INTENT_ORDER, order);
        startActivity(intent);
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
