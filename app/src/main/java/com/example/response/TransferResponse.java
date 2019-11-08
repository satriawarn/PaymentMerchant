package com.example.response;

import com.google.gson.annotations.SerializedName;

public class TransferResponse {
    @SerializedName("status")
    private Integer status;
    @SerializedName("message")
    private String message;
    @SerializedName("nominal_transaksi")
    private String nominal_transaksi;
    @SerializedName("saldo_akhir")
    private String saldo_akhir;
    @SerializedName("norek")
    private String norek;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNominal_transaksi() {
        return nominal_transaksi;
    }

    public void setNominal_transaksi(String nominal_transaksi) {
        this.nominal_transaksi = nominal_transaksi;
    }

    public String getSaldo_akhir() {
        return saldo_akhir;
    }

    public void setSaldo_akhir(String saldo_akhir) {
        this.saldo_akhir = saldo_akhir;
    }

    public String getNorek() {
        return norek;
    }

    public void setNorek(String norek) {
        this.norek = norek;
    }
}
