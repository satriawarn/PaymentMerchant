package com.example.paymentmerchant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helper.ApiClient;
import com.example.helper.ApiInterface;
import com.example.helper.Utils;
import com.example.presenter.NfcPresenter;
import com.example.presenter.QrPresenter;
import com.example.response.QrResponse;
import com.example.view.NfcView;
import com.example.view.QrView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.UnsupportedEncodingException;

import io.reactivex.disposables.CompositeDisposable;

public class NFCActivity extends AppCompatActivity implements NfcView {
    public static final String TAG = "logv" + NFCActivity.class.getSimpleName();

    Tag detectedTag;
    NfcAdapter nfcAdapter;
    IntentFilter[] readTagFilters;
    PendingIntent pendingIntent;
    TextView total, hasil;
    String amount, hasilnya, user_pin, akhir, id, nominal;
    ProgressBar progressBar;
    CompositeDisposable compositeDisposable;
    ApiInterface apiInterface;
    NfcPresenter nfcPresenter;


    private final String[][] techList = new String[][]{
            new String[]{
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        total = findViewById(R.id.textView16);
        hasil = findViewById(R.id.textView14);
        progressBar = findViewById(R.id.progress_bar);

        amount = getIntent().getStringExtra("nominal");
        id = "1";
        total.setText("Rp " + amount);

        compositeDisposable = new CompositeDisposable();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        nfcPresenter = new NfcPresenter(this, compositeDisposable, apiInterface);

//        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        detectedTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
//
//        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
//                new Intent(this,getClass()).
//                        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//
//        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
//        IntentFilter filter2     = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        readTagFilters = new IntentFilter[]{tagDetected,filter2};
    }


    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            hasilnya = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            akhir = removeLastChar(hasilnya);
//            hasil.setText(akhir);
            bottomSheet();
        }
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i] + ":";
        }
        return out;
    }

    public void readFromTag(Intent intent) {
        Ndef ndef = Ndef.get(detectedTag);
        try {
            ndef.connect();

            Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (messages != null) {
                NdefMessage[] ndefMessages = new NdefMessage[messages.length];
                for (int i = 0; i < messages.length; i++) {
                    ndefMessages[i] = (NdefMessage) messages[i];
                }
                NdefRecord record = ndefMessages[0].getRecords()[0];

                byte[] payload = record.getPayload();
                String text = new String(payload);
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                ndef.close();

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Cannot Read From Tag.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    public void confirmation(View view) {
        Intent intent = new Intent(NFCActivity.this, PrintActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NFCActivity.this, BayarActivity.class);
        startActivity(intent);
        finish();
    }

    private void bottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_pin, null);

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();

        final EditText editPin = view.findViewById(R.id.edtPin);
        Button btnTest = view.findViewById(R.id.ok);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_pin = editPin.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                nfcPresenter.nfcPay(id, amount, akhir, user_pin);
                dialog.dismiss();
            }
        });
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
    public void getNfcPay(QrResponse qrResponse) {
        if (qrResponse.getStatus() == 1) {
            Intent intent = new Intent(NFCActivity.this, PrintActivity.class);
            intent.putExtra("belanja", qrResponse.getMessage());
            intent.putExtra("total", qrResponse.getNominal_transaksi());
            intent.putExtra("tanya", "belanja");
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "" + qrResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
