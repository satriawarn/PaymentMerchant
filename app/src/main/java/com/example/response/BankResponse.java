package com.example.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankResponse {
    @SerializedName("data")
    private List<DataBank> data;

    public List<DataBank> getData() {
        return data;
    }

    public void setData(List<DataBank> data) {
        this.data = data;
    }
        public class DataBank{
            @SerializedName("id")
            private String id;
            @SerializedName("nama")
            private String nama;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNama() {
                return nama;
            }

            public void setNama(String nama) {
                this.nama = nama;
            }
            @Override
            public String toString(){return nama;}
        }
}
