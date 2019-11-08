package com.example.helper;

import com.example.response.BankResponse;
import com.example.response.CekBayarResponse;
import com.example.response.MutasiResponse;
import com.example.response.PhoneResponse;
import com.example.response.QrResponse;
import com.example.response.TransferResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("mutasi_merchant")
    Observable<MutasiResponse> mutasi(@Field("id_merchant") String id_merchant);

    @FormUrlEncoded
    @POST("bayar_dengan_qr_code")
    Observable<QrResponse> qrPay(@Field("id_merchant") String id_merchant,
                                 @Field("nominal") String nominal,
                                 @Field("qr_code") String qr_code);

    @FormUrlEncoded
    @POST("bayar_input_hp_user")
    Observable<PhoneResponse> phone(@Field("id_merchant") String id_merchant,
                                    @Field("nominal") String nominal,
                                    @Field("no_hp") String no_hp);

    @FormUrlEncoded
    @POST("cek_bayar_dengan_smartphone")
    Observable<CekBayarResponse> cekBayar(@Field("id_transaksi_user") String id_transaksi_user);

    @POST("data_bank")
    Observable<BankResponse> bank();

    @FormUrlEncoded
    @POST("transfer_dana")
    Observable<TransferResponse> transfer(@Field("total") String total,
                                       @Field("id_bank") String nominal,
                                       @Field("norek") String norek,
                                       @Field("from") String from);


}
