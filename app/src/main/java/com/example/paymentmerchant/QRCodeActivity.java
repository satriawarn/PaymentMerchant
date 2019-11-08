package com.example.paymentmerchant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helper.ApiClient;
import com.example.helper.ApiInterface;
import com.example.presenter.QrPresenter;
import com.example.response.QrResponse;
import com.example.view.QrView;
import com.google.zxing.Result;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, QrView {
    public static final String TAG="log"+QRCodeActivity.class.getSimpleName();
    private ZXingScannerView mScannerView;
    static final Integer CAMERA = 0x1;
    private String kodeKartu,id,nominal;
    private CompositeDisposable compositeDisposable;
    private ProgressBar progressBar;
    private QrPresenter presenter;
    private ApiInterface apiInterface;
    private TextView jumlah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        progressBar = findViewById(R.id.progress_bar);
        jumlah = findViewById(R.id.textView9);
        mScannerView = new ZXingScannerView(this);
        compositeDisposable = new CompositeDisposable();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        presenter = new QrPresenter(this,compositeDisposable,apiInterface);
        contentFrame.addView(mScannerView);
        askForPermission(Manifest.permission.CAMERA, CAMERA);

        nominal = getIntent().getStringExtra("nominal");
        jumlah.setText("Rp "+getIntent().getStringExtra("nominal"));
        id = "1";
    }

    private void askForPermission(String permission, Integer requestCode) {

        if (ContextCompat.checkSelfPermission(QRCodeActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(QRCodeActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(QRCodeActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(QRCodeActivity.this, new String[]{permission}, requestCode);
            }
        } else {

            // Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Camera
                case 1:
                    mScannerView.setResultHandler(this);
                    mScannerView.startCamera();
                    break;


            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void handleResult(Result result) {
        progressBar.setVisibility(View.VISIBLE);
        kodeKartu = result.getText();

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
        } else {
            payWithQr();
        }

    }

    private void payWithQr(){
        Log.d(TAG, "payWithQr: ");
        presenter.payQr(id,nominal,kodeKartu);
    }
    @Override
    public void onSuccess() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void getPayQr(QrResponse qrResponse) {
        if (qrResponse.getStatus()==1){
            Intent intent = new Intent(QRCodeActivity.this, PrintActivity.class);
            intent.putExtra("belanja", qrResponse.getMessage());
            intent.putExtra("total", qrResponse.getNominal_transaksi());
            intent.putExtra("tanya","belanja");
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, ""+qrResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QRCodeActivity.this, BayarActivity.class);
        startActivity(intent);
        finish();
    }
}
