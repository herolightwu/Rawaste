package com.odelan.yang.rawaste.Model;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.odelan.yang.rawaste.Util.Interface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class EtherscanAPI {
    public static void getInternalTransactions(String address, final Interface.OnResult<List<Transaction>> callback) {
        String url = "http://api.etherscan.io/api?module=account&action=txlistinternal&address=" + address + "&startblock=0&endblock=99999999&sort=asc";
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                                callback.onSuccess(transactionList);
                            } else {
                                callback.onFailure("No result");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getMessage());
                    }
                });
    }

    public static void getNormalTransactions(String address, final Interface.OnResult<List<Transaction>> callback) {
        String url = "http://api.etherscan.io/api?module=account&action=txlist&address=" + address + "&startblock=0&endblock=99999999&sort=asc";
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                                callback.onSuccess(transactionList);
                            } else {
                                callback.onFailure("No result");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getMessage());
                    }
                });
    }

    public static void getEtherPrice(final Interface.OnResult<Price> callback) {
        String url = "http://api.etherscan.io/api?module=stats&action=ethprice";
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("result")) {
                                JSONObject price_json = response.getJSONObject("result");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                Price price = gson.fromJson(price_json.toString(), Price.class);
                                callback.onSuccess(price);
                            } else {
                                callback.onFailure("No result");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getMessage());
                    }
                });
    }

    public static void getGasPrice(final Interface.OnResult<String> callback) {
        String url = "http://api.etherscan.io/api?module=proxy&action=eth_gasPrice";
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("result")) {
                                callback.onSuccess(response.getString("result"));
                            } else {
                                callback.onFailure("No result");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getMessage());
                    }
                });
    }

    public static void getTokenBalances(String address, final Interface.OnResult<Double> callback) {
        String url = "http://api.ethplorer.io/getAddressInfo/" + address + "?apiKey=freekey";
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("ETH")) {
                                JSONObject eth = response.getJSONObject("ETH");
                                callback.onSuccess(eth.getDouble("balance"));
                            } else {
                                callback.onFailure("No result");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getMessage());
                    }
                });
    }

    public static void forwardTransaction(String raw, final Interface.OnResult<Boolean> callback) {
        String url = "http://api.etherscan.io/api?module=proxy&action=eth_sendRawTransaction&hex=" + raw;
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("result")) {
                                callback.onSuccess(true);
                            } else {
                                callback.onFailure(response.getJSONObject("error").getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getMessage());
                    }
                });
    }

    public static void getNonceForAddress(String address, final Interface.OnResult<BigInteger> callback) {
        String url = "http://api.etherscan.io/api?module=proxy&action=eth_getTransactionCount&address=" + address + "&tag=latest";
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("result")) {
                                BigInteger nonce = new BigInteger(response.getString("result").substring(2), 16);
                                callback.onSuccess(nonce);
                            } else {
                                callback.onFailure("No result");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Json parse error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailure(anError.getMessage());
                    }
                });
    }
}
