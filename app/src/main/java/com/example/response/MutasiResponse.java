package com.example.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MutasiResponse {
    @SerializedName("data")
    private Data data;
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

        public class Data{
            @SerializedName("saldo_merchant")
            private String saldo_merchant;

            public String getSaldo_merchant() {
                return saldo_merchant;
            }

            public void setSaldo_merchant(String saldo_merchant) {
                this.saldo_merchant = saldo_merchant;
            }

            @SerializedName("data")
            private List<Mutasi> data;

            public List<Mutasi> getData() {
                return data;
            }

            public void setData(List<Mutasi> data) {
                this.data = data;
            }

            public class Mutasi {
                @SerializedName("no")
                private Integer no;
                @SerializedName("detail")
                private String detail;
                @SerializedName("jumlah")
                private String jumlah;
                @SerializedName("tgl")
                private String tgl;

                public Integer getNo() {
                    return no;
                }

                public void setNo(Integer no) {
                    this.no = no;
                }

                public String getDetail() {
                    return detail;
                }

                public void setDetail(String detail) {
                    this.detail = detail;
                }

                public String getJumlah() {
                    return jumlah;
                }

                public void setJumlah(String jumlah) {
                    this.jumlah = jumlah;
                }

                public String getTgl() {
                    return tgl;
                }

                public void setTgl(String tgl) {
                    this.tgl = tgl;
                }
            }
        }

}
