package com.example.rxjava.obj;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PhenologyObj implements Serializable {

    @SerializedName("Phenology_id")
    private int phenologyId;
    @SerializedName("Name")
    private String name;

    public int getPhenologyId() {
        return phenologyId;
    }

    public String getName() {
        return name;
    }
}
