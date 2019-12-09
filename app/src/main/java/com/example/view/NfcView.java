package com.example.view;

import com.example.response.QrResponse;

public interface NfcView {
    void onSuccess();
    void onError(String message);
    void getNfcPay(QrResponse qrResponse);
}
