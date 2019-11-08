package com.example.view;

import com.example.response.BankResponse;

public interface BankView {
    void onSuccessBank();
    void onError(String message);
    void getBank(BankResponse bankResponse);
}
