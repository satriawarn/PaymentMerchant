package com.example.paymentmerchant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.helper.CustomProgressDialog;
import com.example.helper.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class ConfirmationActivity extends AppCompatActivity implements Runnable {
    public static final String TAG="logv"+ConfirmationActivity.class.getSimpleName();
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter bluetoothAdapter;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressBar progressBar;
    private BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;

    String mDeviceAddress,jum,tot,total,norek,sisa,bank,tanya;
    private ProgressDialog mBluetoothConnectProgressDialog;
    private CustomProgressDialog progressDialog;
    Button print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        progressDialog = new CustomProgressDialog(this);

        print = findViewById(R.id.button9);

        tanya = getIntent().getStringExtra("mau print apa");

        total = getIntent().getStringExtra("nominal");
        norek = getIntent().getStringExtra("norek");
        sisa = getIntent().getStringExtra("akhir");
        bank = getIntent().getStringExtra("bank");

        jum = getIntent().getStringExtra("total");
        tot = getIntent().getStringExtra("psn");


        Toast.makeText(this, tanya, Toast.LENGTH_SHORT).show();
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothSocket.isConnected()){
                    Log.d(TAG, "print: socket bluetooth null");
                    getDevice();
                } else {
                    if (tanya.equalsIgnoreCase("qrcode")){
                        printin();
                    } else if (tanya.equalsIgnoreCase("transfer")){
                        printinTransfer();
                    }
                }
            }
        });
    }

    public void home(View view) {
        Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    protected void getDevice(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(ConfirmationActivity.this, "BluetoothAdapter null", Toast.LENGTH_SHORT).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Log.v(TAG, "LOGV getDevice: "+"!mBluetoothAdapter.isEnabled()" );
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            } else {
                mDeviceAddress=Utils.readSharedSetting(this,"deviceaddress","");
                if (mDeviceAddress.equalsIgnoreCase(""))
                {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(ConfirmationActivity.this, DeviceListActivity.class);
                    startActivityForResult(connectIntent,
                            REQUEST_CONNECT_DEVICE);
                }else {
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this, "Printing ...", bluetoothDevice.getName() + " : "
                            + bluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                }
            }
        }
    }

    protected void printin()
    {


        Thread t = new Thread() {
            public void run() {
                try {
                    OutputStream os = bluetoothSocket.getOutputStream();
                    String BILL = "";

                        BILL = "         PEMBAYARAN  \n"+"     "
                                +"  Cafetaria UGM"+ "\n\n" +
                                "   \n\n" +
                                "--------------------------------\n" +
                                "Item                " +"Jus Jambu"+
                                "Harga                "+jum+
                                "Ket                "+tot+"\n\n"+
                                "       TERIMA KASIH ATAS\n" +
                                "         KUNJUNGAN ANDA     \n\n\n";



                    byte[] cc = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
                    byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
                    byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
                    byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
                    os.write(bb);
                    //This is printer specific code you can comment ==== > Start

                    // Setting height
//                    int gs = 29;
//                    os.write(intToByteArray(gs));
//                    int h = 104;
//                    os.write(intToByteArray(h));
//                    int n = 162;
//                    os.write(intToByteArray(n));
//
//                    // Setting Width
//                    int gs_width = 29;
//                    os.write(intToByteArray(gs_width));
//                    int w = 119;
//                    os.write(intToByteArray(w));
//                    int n_width = 2;
//                    os.write(intToByteArray(n_width));
//                    byte[] format = { 27, 33, 0 };
//                    byte[] arrayOfByte1 = { 27, 33, 0 };
//                    format[2] = ((byte)(0x8 | arrayOfByte1[2]));
//
//                    os.write(format);
//
//                    os.write(BILL.getBytes(),0,BILL.getBytes().length);
                    os.write(BILL.getBytes());

                } catch (Exception e) {
                    Log.e(TAG,"LOGV print()"+e.getLocalizedMessage());
                }
            }
        };
        t.start();
    }
    protected void printinTransfer()
    {


        Thread t1 = new Thread() {
            public void run() {
                try {
                    OutputStream os = bluetoothSocket.getOutputStream();
                    String BILL = "";

                    BILL = "         TRANSFER DANA  \n"+"     "
                            +"  Berikut Detail Anda"+ "\n\n" +
                            "   \n\n" +
                            "--------------------------------\n" +
                            "Anda Transfer ke Bank      "+bank+
                            "Sejumlah                "+total+
                            "No Rekeningnya          "+norek+
                            "Terus Saldomu Tinggal    "+sisa+"\n\n"+
                            "       TERIMA KASIH ATAS\n" +
                            "         DAAADAAAAA     \n\n\n";



                    byte[] cc = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
                    byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
                    byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
                    byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
                    os.write(bb);
                    //This is printer specific code you can comment ==== > Start

                    // Setting height
//                    int gs = 29;
//                    os.write(intToByteArray(gs));
//                    int h = 104;
//                    os.write(intToByteArray(h));
//                    int n = 162;
//                    os.write(intToByteArray(n));
//
//                    // Setting Width
//                    int gs_width = 29;
//                    os.write(intToByteArray(gs_width));
//                    int w = 119;
//                    os.write(intToByteArray(w));
//                    int n_width = 2;
//                    os.write(intToByteArray(n_width));
//                    byte[] format = { 27, 33, 0 };
//                    byte[] arrayOfByte1 = { 27, 33, 0 };
//                    format[2] = ((byte)(0x8 | arrayOfByte1[2]));
//
//                    os.write(format);
//
//                    os.write(BILL.getBytes(),0,BILL.getBytes().length);
                    os.write(BILL.getBytes());

                } catch (Exception e) {
                    Log.e(TAG,"LOGV print()"+e.getLocalizedMessage());
                }
            }
        };
        t1.start();
    }
    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = bluetoothAdapter.getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "LOGV PairedDevices: " + mDevice.getName() + "  " + mDevice.getAddress());
            }
        }
    }

    @Override
    public void run() {
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
            bluetoothAdapter.cancelDiscovery();
            bluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
            Log.v(TAG,"LOGV run mBluetoothDevice.createRfcommSocketToServiceRecord");
            printin();
        } catch (IOException eConnectException) {
            Log.e(TAG, "LOGV CouldNotConnectToSocket", eConnectException);
            closeSocket(bluetoothSocket);
//            Toast.makeText(PaymentActivity.this,"Device Tidak Ditemukan",Toast.LENGTH_SHORT).show();
            mBluetoothConnectProgressDialog.dismiss();
            handler.sendEmptyMessage(0);
            return;
        }
    }

    @Override
    protected void onActivityResult(int mRequestCode, int mResultCode, Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);
        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "LOGV Coming incoming address " + mDeviceAddress);

                    bluetoothDevice = bluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", bluetoothDevice.getName() + " : "
                                    + bluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(ConfirmationActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(ConfirmationActivity.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(ConfirmationActivity.this, "Print...", Toast.LENGTH_SHORT).show();
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(ConfirmationActivity.this, "Device Not Found", Toast.LENGTH_SHORT).show();
        }
    };

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.e(TAG, "LOGV SocketClosed");
        } catch (IOException ex) {
            Log.e(TAG, "LOGV CouldNotCloseSocket");
        }
    }
}
