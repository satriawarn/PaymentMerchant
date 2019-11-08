package com.example.helper;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.response.BankResponse;

import java.util.List;

public class AutoCompleteAdapter {
    public static void createAutoCompleteBank(Context context, AutoCompleteTextView autoCompleteTextView, List<BankResponse.DataBank> data){
        ArrayAdapter<BankResponse.DataBank> arrayAdapter = new ArrayAdapter<BankResponse.DataBank>(context, android.R.layout.simple_spinner_item, data);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setThreshold(0);
    }
}
