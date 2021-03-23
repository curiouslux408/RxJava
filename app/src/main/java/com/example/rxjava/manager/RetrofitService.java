package com.example.rxjava.manager;

import com.example.rxjava.obj.SurfaceObj;

import io.reactivex.Observable;
import retrofit2.http.GET;


public interface RetrofitService {

    String baseUrl = "https://coa.emct.tw/S2K5/api/Code/";

    @GET("Surface")
    Observable<SurfaceObj> getSurface();
}
