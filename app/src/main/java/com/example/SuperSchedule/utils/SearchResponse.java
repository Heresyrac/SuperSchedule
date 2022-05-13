package com.example.SuperSchedule.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {
    //No need to map all keys, only those the fields you need
    @SerializedName("items")
    public List<Items> items;

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }
}