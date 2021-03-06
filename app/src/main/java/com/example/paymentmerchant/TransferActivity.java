package com.example.paymentmerchant;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.helper.ApiClient;
import com.example.helper.ApiInterface;
import com.example.helper.AutoCompleteAdapter;
import com.example.presenter.BankPresenter;
import com.example.presenter.TransferPresenter;
import com.example.response.BankResponse;
import com.example.response.TransferResponse;
import com.example.view.BankView;
import com.example.view.TransferView;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class TransferActivity extends AppCompatActivity implements BankView,TransferView {
    public static final String TAG="logv"+TransferActivity.class.getSimpleName();
    private EditText total,norek,nohp,namarek;
    private AutoCompleteTextView bank;
    private BankPresenter bankPresenter;
    private TransferPresenter transferPresenter;
    private ApiInterface apiInterface;
    private CompositeDisposable compositeDisposable;
    private ProgressBar progressBar;
    private List<BankResponse.DataBank> bankList;
    private String banktujuan,bankid,jumlah,namabank,norekin,nohpcard,namarekening,pin;
    private Boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        initLayout();
        bankPresenter.getBank();
    }

    @Override
    protected void onStart(){
        super.onStart();
        bank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textAllBank(adapterView, view, i, l);
            }
        });
    }

    private void initLayout(){
        total = findViewById(R.id.editText);
        bank = findViewById(R.id.editText1);
        norek = findViewById(R.id.editText2);
        nohp = findViewById(R.id.editText3);
        namarek = findViewById(R.id.editText4);
        progressBar = findViewById(R.id.progress_bar);

        compositeDisposable = new CompositeDisposable();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        bankPresenter = new BankPresenter(this,compositeDisposable,apiInterface);
        transferPresenter = new TransferPresenter(this,compositeDisposable,apiInterface);

    }

    private void transfer(){
        transferPresenter.transfer(jumlah,bankid,norekin,nohpcard,pin);
    }

    public void enableBT(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
        }
    }

    public void transfer(final View view) {
        bottomSheet();
    }


    private void textAllBank(AdapterView<?> parent, View view, int position, long id){
        banktujuan = parent.getItemAtPosition(position).toString();

        for (BankResponse.DataBank tag : bankList) {
            if (banktujuan.equalsIgnoreCase(tag.getNama())){
                bankid = tag.getId();
                return;
            } else {
                bankid = "";
            }
        }
    }

    @Override
    public void onSuccess() {
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onSuccessBank() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getBank(BankResponse bankResponse) {
        progressBar.setVisibility(View.VISIBLE);
        if (bankResponse.getData()!=null){
            bankList = bankResponse.getData();
            AutoCompleteAdapter.createAutoCompleteBank(this, bank, bankList);
        } else {
            Toast.makeText(this, "Gagal Mendapatkan Daftar Bank", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getTransferData(TransferResponse transferResponse) {
        if (transferResponse.getStatus()==1){
            Intent intent = new Intent(TransferActivity.this, PrintActivity.class);
            intent.putExtra("nominal",transferResponse.getNominal_transaksi());
            intent.putExtra("norek",transferResponse.getNorek());
            intent.putExtra("akhir",transferResponse.getSaldo_akhir());
            intent.putExtra("bank",banktujuan);
            intent.putExtra("namarek",namarekening);
            intent.putExtra("tanya","transfer");
            startActivity(intent);
            finish();
        } else {
            new iOSDialogBuilder(TransferActivity.this)
                    .setTitle("Transaksi Gagal")
                    .setSubtitle(transferResponse.getMessage())
                    .setBoldPositiveLabel(true)
                    .setCancelable(false)
                    .setPositiveListener("Baik",new iOSDialogClickListener() {
                        @Override
                        public void onClick(iOSDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .build().show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TransferActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validation() {
        jumlah = total.getText().toString();
        norekin = norek.getText().toString();
        nohpcard = nohp.getText().toString();
        banktujuan = bank.getText().toString();
        namarekening = namarek.getText().toString();

        if (jumlah.isEmpty()){
            Toast.makeText(this, "Silakan masukkan nominal", Toast.LENGTH_SHORT).show();
            total.requestFocus();
            return false;
        } else if (norekin.isEmpty()){
            Toast.makeText(this, "Silakan masukkan nomor rekening", Toast.LENGTH_SHORT).show();
            norek.requestFocus();
            return false;
        } else if (banktujuan.isEmpty()){
            Toast.makeText(this, "Silakan masukkan bank tujuan", Toast.LENGTH_SHORT).show();
            bank.requestFocus();
            return false;
        } else if (nohpcard.isEmpty()){
            Toast.makeText(this, "Silakan masukkan nomor hp/nomor kartu anda", Toast.LENGTH_SHORT).show();
            nohp.requestFocus();
            return false;
        } else if (namarekening.isEmpty()){
            Toast.makeText(this, "Silakan nama pemilik rekening", Toast.LENGTH_SHORT).show();
            namarek.requestFocus();
            return false;
        }
        return true;
    }

    private void bottomSheet(){
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_pin, null);

        final BottomSheetDialog dialog = new BottomSheetDialog(TransferActivity.this);
        dialog.setContentView(view);
        dialog.show();
        final EditText edtPin = view.findViewById(R.id.edtPin);

        Button btnTest = view.findViewById(R.id.ok);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin = edtPin.getText().toString();
                if (pin.isEmpty()){
                    Toast.makeText(TransferActivity.this, "Silakan Masukkan PIN", Toast.LENGTH_SHORT).show();
                    edtPin.requestFocus();
                } else {
                    doTransfer();
                    progressBar.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }

            }
        });

    }

    protected void doTransfer(){
        isValid = validation();
        if (isValid){
            jumlah = total.getText().toString();
            norekin = norek.getText().toString();
            nohpcard = nohp.getText().toString();
            namarekening = namarek.getText().toString();
            new iOSDialogBuilder(TransferActivity.this)
                    .setTitle("Peringatan")
                    .setSubtitle("Anda akan melakukan transfer dana"+"\n"
                            +"Nominal : "+jumlah+"\n"
                            +"Bank Tujuan : "+banktujuan+"\n"
                            +"Rekening Tujuan : "+norekin+"\n"
                            +"Nama Pemilik Rekening : "+namarekening+"\n"
                            +"Nomor Anda : "+nohpcard+"\n")
                    .setBoldPositiveLabel(true)
                    .setCancelable(false)
                    .setPositiveListener("Oke",new iOSDialogClickListener() {
                        @Override
                        public void onClick(iOSDialog dialog) {
                            enableBT();
                            transfer();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeListener("Tidak", new iOSDialogClickListener() {
                        @Override
                        public void onClick(iOSDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .build().show();
        }
    }
}
