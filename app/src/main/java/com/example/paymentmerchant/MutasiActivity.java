package com.example.paymentmerchant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helper.ApiClient;
import com.example.helper.ApiInterface;
import com.example.presenter.MutasiPresenter;
import com.example.response.MutasiResponse;
import com.example.view.MutasiView;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;

public class MutasiActivity extends AppCompatActivity implements MutasiView {
    public static final String TAG="log"+MutasiActivity.class.getSimpleName();
    private TableLayout tableLayout;
    private CompositeDisposable compositeDisposable;
    private ApiInterface apiInterface;
    private MutasiPresenter presenter;
    private String id;
    private ProgressBar progressBar;
    private TextView ket,nominal,tgl,mutasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutasi);

        tableLayout = findViewById(R.id.tableLayout1);
        progressBar = findViewById(R.id.progress_bar);
        ket = findViewById(R.id.ket);
        nominal = findViewById(R.id.nominal);
        tgl = findViewById(R.id.tgl);
        mutasi = findViewById(R.id.mutasi);


        compositeDisposable = new CompositeDisposable();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        presenter = new MutasiPresenter(this, compositeDisposable, apiInterface);
        id = "1";
        getMutasi();

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());

        tr.addView(getTextView(0, "NO", Color.BLACK, Typeface.BOLD, Color.GRAY, Gravity.CENTER));
        tr.addView(getTextView(0, "Detail", Color.BLACK, Typeface.BOLD, Color.GRAY, Gravity.CENTER));
        tr.addView(getTextView(0, "Jumlah", Color.BLACK, Typeface.BOLD, Color.GRAY, Gravity.CENTER));
        tr.addView(getTextView(0, "Tanggal", Color.BLACK, Typeface.BOLD, Color.GRAY, Gravity.CENTER));

        tableLayout.addView(tr, getTblLayoutParams());
    }

    @NonNull
    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(1, 1, 1, 1);
        params.weight = 1;
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor, int gravity) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setPadding(5, 5, 5, 5);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        tv.setGravity(gravity);
        return tv;
    }

    private TextView getRowsTextView(int id, String title, int color, int typeface, int bgColor, int gravity) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(5, 10, 5, 5);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        tv.setGravity(gravity);
        return tv;
    }

    private void getMutasi(){
        progressBar.setVisibility(View.VISIBLE);
        presenter.getMutasi(id);
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
    public void getMutasiUser(MutasiResponse mutasiResponse) {
        if (mutasiResponse != null){
            progressBar.setVisibility(View.GONE);
            nominal.setText(mutasiResponse.getData().getSaldo_merchant());
            tgl.setText(mutasiResponse.getData().getData().get(0).getTgl());

            int j = 1;
            for (int i = 0; i < mutasiResponse.getData().getData().size(); i++) {
                Log.d(TAG, "addRows: "+mutasiResponse.getData().getData().size());
                TableRow tr = new TableRow(MutasiActivity.this);
                tr.setLayoutParams(getLayoutParams());

                tr.addView(getRowsTextView(0, ""+j, Color.BLACK, Typeface.BOLD, Color.LTGRAY, Gravity.CENTER));
                tr.addView(getRowsTextView(0, mutasiResponse.getData().getData().get(i).getDetail(), Color.BLACK, Typeface.BOLD, Color.LTGRAY, Gravity.CENTER));
                tr.addView(getRowsTextView(0, mutasiResponse.getData().getData().get(i).getJumlah(), Color.BLACK, Typeface.BOLD, Color.LTGRAY, Gravity.CENTER));
                tr.addView(getRowsTextView(0, mutasiResponse.getData().getData().get(i).getTgl(), Color.BLACK, Typeface.BOLD, Color.LTGRAY, Gravity.CENTER));

                tableLayout.addView(tr, getTblLayoutParams());
                j++;

            }
        } else {
            mutasi.setText("Mutasi Kosong");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MutasiActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
