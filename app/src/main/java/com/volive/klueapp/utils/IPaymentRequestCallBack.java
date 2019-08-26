package com.volive.klueapp.utils;

public interface IPaymentRequestCallBack {
    void onPaymentRequestResponse(int responseType, PayFortData responseData);
}
