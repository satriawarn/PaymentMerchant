package com.example.presenter;

import com.example.helper.ApiInterface;
import com.example.response.MutasiResponse;
import com.example.view.MutasiView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MutasiPresenter {
    private MutasiView view;
    private CompositeDisposable compositeDisposable;
    private ApiInterface apiInterface;

    public MutasiPresenter(MutasiView view, CompositeDisposable compositeDisposable, ApiInterface apiInterface){
        this.view = view;
        this.compositeDisposable = compositeDisposable;
        this.apiInterface = apiInterface;
    }

    public void getMutasi(String id){
        compositeDisposable.add(apiInterface.mutasi(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<MutasiResponse>() {
                    @Override
                    public void onNext(MutasiResponse mutasiResponse) {
                        view.getMutasiUser(mutasiResponse);
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
