package com.example.paymentmerchant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helper.ApiClient;
import com.example.helper.ApiInterface;
import com.example.presenter.PhonePresenter;
import com.example.presenter.UpdatePresenter;
import com.example.response.CekBayarResponse;
import com.example.view.UpdateView;

import io.reactivex.disposables.CompositeDisposable;

public class UpdatePembayaranActivity extends AppCompatActivity implements UpdateView {
    public static final String TAG="logv"+UpdatePembayaranActivity.class.getSimpleName();
    private CompositeDisposable compositeDisposable;
    private ApiInterface apiInterface;
    private UpdatePresenter presenter;
    private ProgressBar progressBar;
    private TextView jumlah,ket;
    private String jum,mes;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pembayaran);

        jumlah = findViewById(R.id.textView23);
        ket = findViewById(R.id.textView22);

        compositeDisposable = new CompositeDisposable();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        presenter = new UpdatePresenter(this, compositeDisposable, apiInterface);

        jumlah.setText("Rp "+getIntent().getStringExtra("nominal"));
        ket.setText(getIntent().getStringExtra("status"));
        id = getIntent().getIntExtra("id_transaksi_user",0);
    }

    public void confirmation(View view) {
        updateStatus();
    }

    private void updateStatus(){
        presenter.cekBayarPhone(String.valueOf(id));
    }
    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void getCekStatus(CekBayarResponse cekBayarResponse) {
        if (cekBayarResponse.getStatus()==1){
            Toast.makeText(this, ""+cekBayarResponse.getMessage(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdatePembayaranActivity.this, PrintActivity.class);
            intent.putExtra("tanya","belanja");
            intent.putExtra("total",cekBayarResponse.getNominal());
            intent.putExtra("belanja",cekBayarResponse.getMessage());
                startActivity(intent);
                finish();
        } else {
            Toast.makeText(this, ""+cekBayarResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdatePembayaranActivity.this, SmartphoneActivity.class);
        startActivity(intent);
        finish();
    }
}
