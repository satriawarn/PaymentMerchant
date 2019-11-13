package com.example.paymentmerchant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.helper.PrintPic;
import com.example.helper.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bluetooth.miniprinter.library.BluetoothService;
import bluetooth.miniprinter.library.DeviceListActivity;
import bluetooth.miniprinter.library.PrintHelper;


public class PrintActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "logv" + PrintActivity.class.getSimpleName();
    Button print,connect;
    private static final int REQUEST_ENABLE_BLUETOOTH = 100;
    private static final int REQUEST_CONNECT_DEVICE = 101;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothService mBluetoothService = null;
    private boolean isRequestingBluetooth;
    private ProgressBar progressBar;
    private String nominal, bank, norek, sisa,tanya,jum,tot,belanja;
    private Bitmap imgBitmap;
    private ImageView imgprintable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        tanya = getIntent().getStringExtra("tanya");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available on your device", Toast.LENGTH_LONG).show();
            finish();
        }

        initUI();

        PermissionHandler.requestDefaultPermissions(this);
    }

    private void initUI() {
        print = findViewById(R.id.button9);
        progressBar = findViewById(R.id.progress_bar);

        imgprintable = new ImageView(this);

        imgBitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.logobpr);
        imgprintable.setImageBitmap(imgBitmap);

        nominal = getIntent().getStringExtra("nominal");
        bank = getIntent().getStringExtra("bank");
        norek = getIntent().getStringExtra("norek");
        sisa = getIntent().getStringExtra("akhir");

        jum = getIntent().getStringExtra("total");
//        tot = getIntent().getStringExtra("psn");
        belanja = getIntent().getStringExtra("belanja");
        mBluetoothService = new BluetoothService(mHandler);

        print.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        /* if Bluetooth is not on, request that it be enabled. */
        /* otherwise, setup the session */
        if (!mBluetoothAdapter.isEnabled() && !isRequestingBluetooth) {
            isRequestingBluetooth = true;
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        } else {
            if (mBluetoothService == null) {
                initUI();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mBluetoothService != null) {
            if (mBluetoothService.getState() == BluetoothService.STATE_NONE) {
                mBluetoothService.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /* stop the Bluetooth services */
        if (mBluetoothService != null) {
            mBluetoothService.stop();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionHandler.PERMISSION_REQUEST_CODE) {
            /* check if all permissions has been granted */
            PermissionHandler.requestDefaultPermissions(this);
        }
    }

    private void printImage() {
        if (imgprintable != null && imgBitmap != null) {
            byte[] data = PrintHelper.getBytesToPrint(imgBitmap);

            /* write data with default bytes */
            PrintHelper.print(mBluetoothService, data);

            /* manual write all bytes */
            //PrintHelper.write(mBluetoothService, PrintHelper.ESC_Init);
            //PrintHelper.write(mBluetoothService, PrintHelper.LF);
            //PrintHelper.write(mBluetoothService, data);
            //PrintHelper.write(mBluetoothService, PrintHelper.setPrintAndFeed(30));
            //PrintHelper.write(mBluetoothService, PrintHelper.setPaperCut(1));
            //PrintHelper.write(mBluetoothService, PrintHelper.setPrinterInit());
        } else {
            Toast.makeText(this, "tidak ada gambar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        String printTransfer = "         TRANSFER DANA  \n"+"     "
                +"  Berikut Detail Anda"+ "\n\n" +
                "--------------------------------\n" +
                "Nama Bank :      "+bank+"\n"+
                "Nominal :            "+nominal+"\n"+
                "No Rek :      "+norek+"\n"+
                "Sisa Saldo :   "+sisa+"\n\n"+
                "Simpan struk ini sebagai bukti transfer yang sah."+"\n\n"+
                "       TERIMA KASIH\n";

        String printBelanja ="         PEMBAYARAN  \n"+"     "
                +"  Merchant BPR"+ "\n\n" +
                "--------------------------------\n" +
                "Keterangan               " +belanja+"\n"+
                "Harga                "+jum+ "\n\n"+
                "Simpan struk ini sebagai bukti pembayaran yang sah."+"\n\n"+
                "       TERIMA KASIH ATAS\n" +
                "         KUNJUNGAN ANDA     \n";

        if (mBluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
            Intent intent = new Intent(PrintActivity.this, DeviceListActivity.class);
            startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
        } else {
            if (tanya.equalsIgnoreCase("transfer")){
                printImage();
                PrintHelper.print(mBluetoothService, printTransfer);
            } else if (tanya.equalsIgnoreCase("belanja")){
                printImage();
                PrintHelper.print(mBluetoothService, printBelanja);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE: {
                /* when DeviceListActivity1 returns with a device to connect */
                if (resultCode == Activity.RESULT_OK) {
                    /* start loading animation */
                    progressBar.setVisibility(View.VISIBLE);

                    /* get the device address */
                    if (data.getExtras() != null) {
                        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

                        /* get the bluetooth device object */
                        if (BluetoothAdapter.checkBluetoothAddress(address)) {
                            /* attempt to connect to the device */
                            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                            mBluetoothService.connect(device);
                        }
                    }
                }
                break;
            }
            case REQUEST_ENABLE_BLUETOOTH: {
                isRequestingBluetooth = false;

                /* when the request to enable Bluetooth returns */
                if (resultCode == Activity.RESULT_OK) {
                    /* bluetooth is now enabled, so set up a session */
                    initUI();
                } else {
                    /* user did not enable Bluetooth or an error occurred */
                    Toast.makeText(this, "Bluetooth Tidak Berfungsi", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MSG_STATE_CHANGE: {
                    /* disable btn and change text */
                    if (msg.arg1 == BluetoothService.STATE_CONNECTED) {
                        /* finish loading animation */
                        progressBar.setVisibility(View.GONE);


                        /* enable interface */
                        print.setEnabled(true);

                    }
                    break;
                }
                case BluetoothService.MSG_DEVICE_NAME: {
                    String deviceName = msg.getData().getString(BluetoothService.KEY_DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to " + deviceName, Toast.LENGTH_SHORT).show();
                    break;
                }
                case BluetoothService.MSG_TOAST: {
                    /* finish loading animation */
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), msg.getData().getString(BluetoothService.KEY_TOAST_MSG), Toast.LENGTH_SHORT).show();
                    break;
                }
                case BluetoothService.MSG_CONNECTION_LOST: {
                    /* finish loading animation */
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Device connection was lost", Toast.LENGTH_SHORT).show();

                    /* disable interface until select printer again */
                    break;
                }
                case BluetoothService.MSG_UNABLE_CONNECT: {
                    /* finish loading animation */
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Unable to connect device", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

    public void home(View view) {
        if (mBluetoothService != null) {
            mBluetoothService.stop();
        }
        Intent intent = new Intent(PrintActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private static class PermissionHandler {

        private static final int PERMISSION_REQUEST_CODE = 99;

        private static void requestDefaultPermissions(Activity activity) {
            List<String> permissionsToRequest;

            /* check permission list */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                /* set permission list to request */
                String[] permissionList = new String[] {
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                };

                /* validate and get not allowed permissions */
                permissionsToRequest = PermissionHandler.getNotAllowedList(activity, permissionList);

                /* request permissions */
                if (permissionsToRequest.size() > 0) {
                    /* request permission list */
                    PermissionHandler.requestPermissions(activity, permissionsToRequest);
                }
            }
        }

        private static List<String> getNotAllowedList(Activity activity, String[] permissionList) {
            List<String> requestList = new ArrayList<>();

            if (permissionList.length > 0) {
                for (String permission : permissionList) {
                    if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                        requestList.add(permission);
                    }
                }
            }

            return requestList;
        }

        private static void requestPermissions(Activity activity, List<String> permissions) {
            if (permissions.size() > 0) {
                /* set permission array list to request */
                String[] array = new String[permissions.size()];
                for (int i = 0; i < permissions.size(); i++) {
                    array[i] = permissions.get(i);
                }

                /* request permission */
                ActivityCompat.requestPermissions(activity, array, PermissionHandler.PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onBackPressed() {
    }
}
