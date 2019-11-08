package com.example.presenter;

import com.example.helper.ApiInterface;
import com.example.response.PhoneResponse;
import com.example.response.QrResponse;
import com.example.view.PhoneView;
import com.example.view.QrView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class PhonePresenter {
    private PhoneView view;
    private CompositeDisposable compositeDisposable;
    private ApiInterface apiInterface;

    public PhonePresenter(PhoneView view, CompositeDisposable compositeDisposable, ApiInterface apiInterface){
        this.view = view;
        this.compositeDisposable = compositeDisposable;
        this.apiInterface = apiInterface;
    }

    public void payPhone(String id_merchant, String nominal, String no_hp){
        compositeDisposable.add(apiInterface.phone(id_merchant, nominal, no_hp)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<PhoneResponse>() {
                    @Override
                    public void onNext(PhoneResponse phoneResponse) {
                        view.getPayPhone(phoneResponse);
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
