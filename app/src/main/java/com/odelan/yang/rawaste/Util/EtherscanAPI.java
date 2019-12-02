package com.odelan.yang.rawaste.Util;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.yang.rawaste.Model.Price;
import com.odelan.yang.rawaste.Model.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;

public class EtherscanAPI {

    public Context context;
    public KProgressHUD hud;

    public EtherscanAPI(Context context) {
        this.context = context;
        hud = KProgressHUD.create(context);
    }

    public void getPriceChart(long starttime, int period, boolean usd, Callback b) throws IOException {

    }

    /**
     * Retrieve all internal transactions from address like contract calls, for normal transactions @see rehanced.com.simpleetherwallet.network.EtherscanAPI#getNormalTransactions() )
     *
     * @param address Ether address
     * @param transactionInterface Transaction result
     */
    public void getInternalTransactions(String address, Interface.OnResult<List<Transaction>> transactionInterface) {
        String url = "http://api.etherscan.io/api?module=account&action=txlistinternal&address=" + address + "&startblock=0&endblock=99999999&sort=asc";
        hud.show();
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hud.dismiss();
                        try {
                            if (response.has("result")) {
                                JSONArray results = response.getJSONArray("result");

                                List<Transaction> transactionList = new ArrayList();
                                for (int i = 0; i < results.length(); i++) {
                                    JSONObject result = (JSONObject) results.get(i);
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Transaction transaction = gson.fromJson(result.toString(), Transaction.class);
                                    transactionList.add(transactionList.size(), transaction);
                                }
                                transactionInterface.onSuccess(transactionList);
                            } else {
                                transactionInterface.onFailure("No result");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            transactionInterface.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hud.dismiss();
                        transactionInterface.onFailure(anError.getMessage());
                    }
                });
    }

    /**
     * Retrieve all internal transactions from address like contract calls, for normal transactions @see rehanced.com.simpleetherwallet.network.EtherscanAPI#getNormalTransactions() )
     *
     * @param address Ether address
     * @param transactionInterface Transaction result
     */
    public void getNormalTransactions(String address, Interface.OnResult<List<Transaction>> transactionInterface) {
        String url = "http://api.etherscan.io/api?module=account&action=txlist&address=" + address + "&startblock=0&endblock=99999999&sort=asc";
//        hud.show();
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        hud.dismiss();
                        try {
                            if (response.has("result")) {
                                JSONArray results = response.getJSONArray("result");

                                List<Transaction> transactionList = new ArrayList();
                                for (int i = 0; i < results.length(); i++) {
                                    JSONObject result = (JSONObject) results.get(i);
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    Transaction transaction = gson.fromJson(result.toString(), Transaction.class);
                                    transactionList.add(transaction);
                                }
                                transactionInterface.onSuccess(transactionList);
                            } else {
                                transactionInterface.onFailure("No result");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            transactionInterface.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
//                        hud.dismiss();
                        transactionInterface.onFailure(anError.getMessage());
                    }
                });
    }

    public void getEtherPrice(Interface.OnResult<Price> priceInterface) {
        String url = "http://api.etherscan.io/api?module=stats&action=ethprice";
        hud.show();
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hud.dismiss();
                        try {
                            if (response.has("result")) {
                                JSONObject price_json = response.getJSONObject("result");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Price price = gson.fromJson(price_json.toString(), Price.class);
                                priceInterface.onSuccess(price);
                            } else {
                                priceInterface.onFailure("No result");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            priceInterface.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hud.dismiss();
                        priceInterface.onFailure(anError.getMessage());
                    }
                });
    }

    public void getGasPrice(Interface.OnResult<String> stringInterface) {
        String url = "http://api.etherscan.io/api?module=proxy&action=eth_gasPrice";
        hud.show();
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hud.dismiss();
                        try {
                            if (response.has("result")) {
                                stringInterface.onSuccess(response.getString("result"));
                            } else {
                                stringInterface.onFailure("No result");
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
    }

    /**
     * Get token balances via ethplorer.io
     *
     * @param address Ether address
     * @param doubleInterface result
     */
    public static void getTokenBalances(String address, Interface.OnResult<Double> doubleInterface) {
        String url = "http://api.ethplorer.io/getAddressInfo/" + address + "?apiKey=freekey";
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("ETH")) {
                                JSONObject eth = response.getJSONObject("ETH");
                                doubleInterface.onSuccess(eth.getDouble("balance"));
                            } else {
                                doubleInterface.onFailure("No result");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            doubleInterface.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        doubleInterface.onFailure(anError.getMessage());
                    }
                });
    }

    public static void forwardTransaction(String raw, Interface.OnResult<Boolean> boolInterface) {
        String url = "http://api.etherscan.io/api?module=proxy&action=eth_sendRawTransaction&hex=" + raw;
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("result")) {
                                boolInterface.onSuccess(true);
                            } else {
                                boolInterface.onFailure(response.getJSONObject("error").getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            boolInterface.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        boolInterface.onFailure(anError.getMessage());
                    }
                });
    }

    public static void getNonceForAddress(String address, Interface.OnResult<BigInteger> bigIntegerInterface) {
        String url = "http://api.etherscan.io/api?module=proxy&action=eth_getTransactionCount&address=" + address + "&tag=latest";
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("result")) {
                                BigInteger nonce = new BigInteger(response.getString("result").substring(2), 16);
                                bigIntegerInterface.onSuccess(nonce);
                            } else {
                                bigIntegerInterface.onFailure("No result");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            bigIntegerInterface.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        bigIntegerInterface.onFailure(anError.getMessage());
                    }
                });
    }
}
