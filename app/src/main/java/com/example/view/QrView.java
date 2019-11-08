package com.example.view;

import com.example.response.QrResponse;

public interface QrView {
    void onSuccess();
    void onError(String message);
    void getPayQr(QrResponse qrResponse);
}
