package com.example.view;

import com.example.response.CekBayarResponse;
import com.example.response.PhoneResponse;

public interface UpdateView {
    void onSuccess();
    void onError(String message);
    void getCekStatus(CekBayarResponse cekBayarResponse);
}
