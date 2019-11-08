package com.example.view;

import com.example.response.TransferResponse;

public interface TransferView {
    void onSuccess();
    void onError(String message);
    void getTransferData(TransferResponse transferResponse);
}
