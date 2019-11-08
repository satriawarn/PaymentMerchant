package com.example.presenter;

import com.example.helper.ApiInterface;
import com.example.response.CekBayarResponse;
import com.example.response.QrResponse;
import com.example.view.QrView;
import com.example.view.UpdateView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class UpdatePresenter {
    private UpdateView view;
    private CompositeDisposable compositeDisposable;
    private ApiInterface apiInterface;

    public UpdatePresenter(UpdateView view, CompositeDisposable compositeDisposable, ApiInterface apiInterface){
        this.view = view;
        this.compositeDisposable = compositeDisposable;
        this.apiInterface = apiInterface;
    }

    public void cekBayarPhone(String id_transaksi_user){
        compositeDisposable.add(apiInterface.cekBayar(id_transaksi_user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<CekBayarResponse>() {
                    @Override
                    public void onNext(CekBayarResponse cekBayarResponse) {
                        view.getCekStatus(cekBayarResponse);
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
