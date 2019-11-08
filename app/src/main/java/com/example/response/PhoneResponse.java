package com.example.response;

import com.google.gson.annotations.SerializedName;

public class PhoneResponse {
    @SerializedName("status")
    private Integer status;
    @SerializedName("message")
    private String message;
    @SerializedName("nominal")
    private Integer nominal;
    @SerializedName("id_transaksi_user")
    private Integer id_transaksi_user;

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

    public Integer getNominal() {
        return nominal;
    }

    public void setNominal(Integer nominal) {
        this.nominal = nominal;
    }

    public Integer getId_transaksi_user() {
        return id_transaksi_user;
    }

    public void setId_transaksi_user(Integer id_transaksi_user) {
        this.id_transaksi_user = id_transaksi_user;
    }
}
