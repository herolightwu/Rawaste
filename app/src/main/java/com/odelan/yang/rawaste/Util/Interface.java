package com.odelan.yang.rawaste.Util;

public class Interface {
    public interface OnResult<T> {
        void onSuccess(T result);
        void onFailure(String error);
    }
}
