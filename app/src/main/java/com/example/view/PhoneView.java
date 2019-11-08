package com.example.view;

import com.example.response.PhoneResponse;

public interface PhoneView {
    void onSuccess();
    void onError(String message);
    void getPayPhone(PhoneResponse phoneResponse);
}
