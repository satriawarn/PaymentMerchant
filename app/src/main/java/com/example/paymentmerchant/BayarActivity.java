package com.example.paymentmerchant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class BayarActivity extends AppCompatActivity {
    private EditText nominal;
    private String total;
    private Boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar);
        nominal = findViewById(R.id.editText);
    }

    public void qrcode(View view) {
        isValid = validation();
        if (isValid){
            Intent intent = new Intent(BayarActivity.this, QRCodeActivity.class);
            intent.putExtra("nominal", nominal.getText().toString());
            startActivity(intent);
            finish();
        }

    }

    public void nfc(View view) {
        isValid = validation();
        if (isValid){
            Intent intent = new Intent(BayarActivity.this, NFCActivity.class);
            intent.putExtra("nominal", nominal.getText().toString());
            startActivity(intent);
            finish();
        }

    }

    public void smartphone(View view) {
        isValid = validation();
        if (isValid){
            Intent intent = new Intent(BayarActivity.this, SmartphoneActivity.class);
            intent.putExtra("nominal", nominal.getText().toString());
            startActivity(intent);
            finish();
        }
    }

    private boolean validation() {
        total = nominal.getText().toString();
        if (total.isEmpty()){
            Toast.makeText(this, "Silakan masukkan nominal", Toast.LENGTH_SHORT).show();
            nominal.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BayarActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
