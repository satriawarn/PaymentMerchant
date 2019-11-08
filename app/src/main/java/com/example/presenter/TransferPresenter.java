package com.example.presenter;

import com.example.helper.ApiInterface;
import com.example.response.TransferResponse;
import com.example.view.TransferView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class TransferPresenter {
    private TransferView view;
    private CompositeDisposable compositeDisposable;
    private ApiInterface apiInterface;

    public TransferPresenter (TransferView view, CompositeDisposable compositeDisposable, ApiInterface apiInterface){
        this.view = view;
        this.compositeDisposable = compositeDisposable;
        this. apiInterface = apiInterface;
    }

    public void transfer(String total, String id_bank, String norek, String from){
        compositeDisposable.add(apiInterface.transfer(total,id_bank,norek,from)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<TransferResponse>() {
                    @Override
                    public void onNext(TransferResponse transferResponse) {
                        view.getTransferData(transferResponse);
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
