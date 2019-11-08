package com.example.paymentmerchant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helper.ApiClient;
import com.example.helper.ApiInterface;
import com.example.presenter.MutasiPresenter;
import com.example.presenter.PhonePresenter;
import com.example.response.PhoneResponse;
import com.example.view.PhoneView;

import io.reactivex.disposables.CompositeDisposable;

public class SmartphoneActivity extends AppCompatActivity implements PhoneView {
    public static final String TAG="log"+SmartphoneActivity.class.getSimpleName();
    private CompositeDisposable compositeDisposable;
    private ApiInterface apiInterface;
    private PhonePresenter presenter;
    private ProgressBar progressBar;
    private EditText nomor;
    private String id,nominal,nohp;
    private TextView jumlah;
    private Boolean isValid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartphone);

        nomor = findViewById(R.id.editText2);
        jumlah = findViewById(R.id.textView19);
        progressBar = findViewById(R.id.progress_bar);

        nominal = getIntent().getStringExtra("nominal");
        id = "1";
        jumlah.setText("Rp "+getIntent().getStringExtra("nominal"));

        compositeDisposable = new CompositeDisposable();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        presenter = new PhonePresenter(this, compositeDisposable, apiInterface);
    }

    public void update(View view) {
        isValid = validation();
        if (isValid){
            payWithPhone();
        }
    }

    private void payWithPhone(){
        progressBar.setVisibility(View.VISIBLE);
        presenter.payPhone(id,nominal,nomor.getText().toString());
    }

    @Override
    public void onSuccess() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getPayPhone(PhoneResponse phoneResponse) {
        if (phoneResponse.getStatus()==1){
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(SmartphoneActivity.this, UpdatePembayaranActivity.class);
            intent.putExtra("nominal", String.valueOf(phoneResponse.getNominal()));
            intent.putExtra("status",phoneResponse.getMessage());
            intent.putExtra("id_transaksi_user", Integer.valueOf(phoneResponse.getId_transaksi_user()));
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, ""+phoneResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean validation() {
        nohp = nomor.getText().toString();
        if (nohp.isEmpty()){
            Toast.makeText(this, "Silakan masukkan nomor", Toast.LENGTH_SHORT).show();
            nomor.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SmartphoneActivity.this, BayarActivity.class);
        startActivity(intent);
        finish();
    }
}
