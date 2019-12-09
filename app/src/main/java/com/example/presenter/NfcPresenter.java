package com.example.presenter;

import com.example.helper.ApiInterface;
import com.example.response.QrResponse;
import com.example.view.NfcView;
import com.example.view.QrView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class NfcPresenter {
    private NfcView view;
    private CompositeDisposable compositeDisposable;
    private ApiInterface apiInterface;

    public NfcPresenter(NfcView view, CompositeDisposable compositeDisposable, ApiInterface apiInterface){
        this.view = view;
        this.compositeDisposable = compositeDisposable;
        this.apiInterface = apiInterface;
    }

    public void nfcPay(String id_merchant, String nominal, String mac_address_kartu, String user_pin){
        compositeDisposable.add(apiInterface.nfcPay(id_merchant, nominal, mac_address_kartu, user_pin)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<QrResponse>() {
                    @Override
                    public void onNext(QrResponse qrResponse) {
                        view.getNfcPay(qrResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        view.onSuccess();
                    }
                })
        );

    }
}
