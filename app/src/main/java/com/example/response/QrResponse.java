package com.example.response;

import com.google.gson.annotations.SerializedName;

public class QrResponse {
    @SerializedName("status")
    private Integer status;
    @SerializedName("message")
    private String message;
    @SerializedName("nominal_transaksi")
    private String nominal_transaksi;

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
}
