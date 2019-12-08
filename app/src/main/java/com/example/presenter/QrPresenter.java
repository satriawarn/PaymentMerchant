package com.example.presenter;

import com.example.helper.ApiInterface;
import com.example.response.QrResponse;
import com.example.view.QrView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class QrPresenter {
    private QrView view;
    private CompositeDisposable compositeDisposable;
    private ApiInterface apiInterface;

    public QrPresenter(QrView view, CompositeDisposable compositeDisposable, ApiInterface apiInterface){
        this.view = view;
        this.compositeDisposable = compositeDisposable;
        this.apiInterface = apiInterface;
    }

    public void payQr(String id_merchant, String nominal, String qr_code, String user_pin){
        compositeDisposable.add(apiInterface.qrPay(id_merchant, nominal, qr_code, user_pin)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<QrResponse>() {
                    @Override
                    public void onNext(QrResponse qrResponse) {
                        view.getPayQr(qrResponse);
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
