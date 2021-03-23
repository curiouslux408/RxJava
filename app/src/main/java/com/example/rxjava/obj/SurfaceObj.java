package com.example.rxjava.obj;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SurfaceObj implements Serializable {

    @SerializedName("Surface_id")
    private int surfaceId;
    @SerializedName("Name")
    private String name;

    public void setSurfaceId(int surfaceId) {
        this.surfaceId = surfaceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSurfaceId() {
        return surfaceId;
    }

    public String getName() {
        return name;
    }
}
