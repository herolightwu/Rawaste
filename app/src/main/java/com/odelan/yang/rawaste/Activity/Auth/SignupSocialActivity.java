package com.odelan.yang.rawaste.Activity.Auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.odelan.yang.rawaste.Activity.Main.MainActivity;
import com.odelan.yang.rawaste.Model.User;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupSocialActivity extends AppCompatActivity {

    @BindView(R.id.txt_title) TextView txt_title;
    @BindView(R.id.txt_email) TextView txt_email;
    @BindView(R.id.txt_firstname) TextView txt_firstname;
    @BindView(R.id.txt_lastname) TextView txt_lastname;
    @BindView(R.id.txt_type) TextView txt_type;

    @BindView(R.id.edit_duns) EditText edit_duns;
    @BindView(R.id.edit_activity_area) EditText edit_activity_area;
    @BindView(R.id.edit_waste_type) EditText edit_waste_type;
    @BindView(R.id.edit_phone_number) EditText edit_phone_number;
    @BindView(R.id.country_code) CountryCodePicker country_code;
    @BindView(R.id.chk_accept) CheckBox chk_accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_social);
        ButterKnife.bind(this);

        setActivity();
    }

    void setActivity() {
        // social information
        if (!AppData.user.facebookID.isEmpty()) { // facebook user
            txt_title.setText(getResources().getString(R.string.register_facebook));
        } else { // google user
            txt_title.setText(getResources().getString(R.string.register_google));
        }
        txt_email.setText(AppData.user.email);
        txt_firstname.setText(AppData.user.firstname);
        txt_lastname.setText(AppData.user.lastname);

        txt_type.setText(Constants.USER_TYPE_INDIVIDUAL);
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @OnClick(R.id.txt_type) void onClickType() {
        PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), txt_type);
        dropDownMenu.getMenuInflater().inflate(R.menu.user_type, dropDownMenu.getMenu());
        dropDownMenu.setOnMenuItemClickListener(menuItem -> {
            txt_type.setText(menuItem.getTitle());
            return true;
        });
        dropDownMenu.show();
    }

    @OnClick(R.id.btn_terms_and_conditions) void onClickTerms() {

    }

    @OnClick(R.id.btn_register) void onClickRegister() {
        if (edit_phone_number.getText().toString().isEmpty()) {
            showToast("Please input phone number");
            return;
        }
        if (!chk_accept.isChecked()) {
            showToast("Please accept terms and conditions");
            return;
        }

        AppData.user.type = txt_type.getText().toString();
        AppData.user.country_code = country_code.getSelectedCountryCodeWithPlus();
        AppData.user.phone = edit_phone_number.getText().toString();
        AppData.user.duns = edit_duns.getText().toString();
        AppData.user.activity_area = edit_activity_area.getText().toString();
        AppData.user.type_waste = edit_waste_type.getText().toString();

        User.createUser(AppData.user);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
