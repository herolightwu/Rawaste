package com.odelan.yang.rawaste.Activity;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.yang.rawaste.Model.User;

public class BaseActivity extends AppCompatActivity {

    private KProgressHUD hud;

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showProgress() {
        showProgress("Please wait");
    }

    public void showProgress(String message) {
        hud = KProgressHUD.create(this).setLabel(message);
        hud.show();
    }

    public void dismissProgress() {
        if (hud != null) {
            hud.dismiss();
        }
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidDate(int year, int month, int day) {
        if (year < 1900 || year >= 2100) return false;
        if (month < 1 || month > 12) return false;
        if (day < 1 || day > 31) return false;
        if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
            return false;
        }
        if (month == 2) {
            if (year%400 == 0 || (year%100 != 0 && year%4 == 0)) {
                if(day > 29) {
                    return false;
                } else {
                    return true;
                }
            } else{
                if(day > 28) {
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    public static String deltaTimeString(long delta) {
        long min = delta / 60;
        long hour = min / 60;
        long day = hour / 24;
        if (day > 30) {
            return "";
        }
        if (day > 0) {
            return String.format("%d days ago", day);
        }
        if (hour > 0) {
            return String.format("%d hours ago", hour);
        }
        if (min == 0) {
            return "Now";
        }
        return String.format("%d mins ago", min);
    }

    public static String getChatId(User a, User b) {
        if (a.ID.compareTo(b.ID) < 0) {
            return "room" + a.ID + "_" + b.ID;
        } else {
            return "room" + b.ID + "_" + a.ID;
        }
    }
}