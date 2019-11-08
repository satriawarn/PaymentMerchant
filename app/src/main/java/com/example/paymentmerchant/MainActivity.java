package com.example.paymentmerchant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void bayar(View view) {
        Intent intent = new Intent(MainActivity.this, BayarActivity.class);
        startActivity(intent);
        finish();
    }

    public void mutasi(View view) {
        Intent intent = new Intent(MainActivity.this, MutasiActivity.class);
        startActivity(intent);
        finish();
    }

    public void withdraw(View view) {
        Intent intent = new Intent(MainActivity.this, WithdrawActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik sekali lagi untuk keluar aplikasi.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void transfer(View view) {
        Intent intent = new Intent(MainActivity.this, TransferActivity.class);
        startActivity(intent);
        finish();
    }
}
