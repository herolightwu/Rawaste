package com.odelan.yang.rawaste.Util;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigInteger;

public class API {
    public static final String ethBaseUrl = "http://132.148.20.129:8545";
    public static final String restapiBaseUrl = "http://vaultersapp.co.uk/ethereum/api";
    public static final String rateBaseUrl = "https://api.coinmarketcap.com/v1/ticker/ethereum/?convert=USD";
    public static final String signup = restapiBaseUrl + "/user/signup_email_password";
    public static final String login = restapiBaseUrl + "/user/login_email_password";

    public Context context;
    public KProgressHUD hud;

    public API(Context context) {
        this.context = context;
        hud = KProgressHUD.create(context);
    }

    public void getUSDRate(Interface.OnResult<Double> doubleInterface) {
        hud.show();
        AndroidNetworking.get(rateBaseUrl)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hud.dismiss();
                        try {
                            if (response != null && response.length() > 0) {
                                    JSONObject rate = response.getJSONObject(0);
                                    String price_usd = rate.getString("price_usd");
                                    doubleInterface.onSuccess(Double.parseDouble(price_usd));
                            } else {
                                doubleInterface.onFailure("Json parse error");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            doubleInterface.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hud.dismiss();
                        doubleInterface.onFailure(anError.getMessage());
                    }
                });
    }

    public void eth_getBalance(String address, Interface.OnResult<Double> doubleInterface) {
        hud.show();
        String json = String.format("{\"jsonrpc\":\"2.0\",\"method\":\"eth_getBalance\",\"params\":[\"%s\", \"latest\"],\"id\":1}", address);
        try {
            JSONObject object = new JSONObject(json);
            AndroidNetworking.post(ethBaseUrl)
                    .addJSONObjectBody(object)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hud.dismiss();
                            try {
                                if (!response.has("error")) {
                                    String value = response.getString("result").substring(2); // wei value
                                    BigInteger bigInteger = new BigInteger(value, 16);
                                    Double eth =  bigInteger.doubleValue() / 1000 / 1000 / 1000 / 1000 / 1000 / 1000;
                                    doubleInterface.onSuccess(eth);
                                } else {
                                    doubleInterface.onFailure(response.getJSONObject("error").getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                doubleInterface.onFailure("Json parse error");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            hud.dismiss();
                            doubleInterface.onFailure(anError.getMessage());
                        }
                    });
        } catch (JSONException e) {
            doubleInterface.onFailure("Json parse error");
        }
    }

    public void personal_sendTransaction(String from,
                                         String to,
                                         String gas,
                                         String gasPrice,
                                         String value,
                                         String password,
                                         Interface.OnResult<String> stringInterface) {
        hud.show();
        String json = String.format("{\"jsonrpc\":\"2.0\",\"method\":\"personal_sendTransaction\",\"params\":[{\"from\": \"%s\",\n" +
                " \"to\": \"%s\",\n" +
                " \"gas\": \"%s\", \n" +
                " \"gasPrice\": \"%s\",\n" +
                " \"value\": \"%s\"}, \"%s\"],\"id\":1}", from, to, gas, gasPrice, value, password);
        try {
            JSONObject object = new JSONObject(json);
            AndroidNetworking.post(ethBaseUrl)
                    .addJSONObjectBody(object)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hud.dismiss();
                            try {
                                if (!response.has("error")) {
                                    String value = response.getString("result");
                                    stringInterface.onSuccess(value);
                                } else {
                                    stringInterface.onFailure(response.getJSONObject("error").getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                stringInterface.onFailure("Json parse error");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            hud.dismiss();
                            stringInterface.onFailure(anError.getMessage());
                        }
                    });
        } catch (JSONException e) {
            stringInterface.onFailure("Json parse error");
        }
    }
}
