package com.example.view;

import com.example.response.MutasiResponse;

public interface MutasiView {
    void onSuccess();
    void onError(String message);
    void getMutasiUser(MutasiResponse mutasiResponse);
}
