package com.example.presenter;

import com.example.helper.ApiInterface;
import com.example.response.BankResponse;
import com.example.view.BankView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class BankPresenter {
    private BankView view;
    private CompositeDisposable compositeDisposable;
    private ApiInterface apiInterface;

    public BankPresenter(BankView view, CompositeDisposable compositeDisposable, ApiInterface apiInterface){
        this.view = view;
        this.compositeDisposable = compositeDisposable;
        this.apiInterface = apiInterface;
    }

    public void getBank(){
        compositeDisposable.add(apiInterface.bank()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<BankResponse>(){
                    @Override
                    public void onNext(BankResponse bankResponse) {
                        view.getBank(bankResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        view.onSuccessBank();
                    }
                })
        );
    }
}
