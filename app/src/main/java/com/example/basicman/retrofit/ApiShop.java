package com.example.basicman.retrofit;



import com.example.basicman.model.TypeProductModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiShop {

    @GET("gettypeproduct.php")
    Observable<TypeProductModel> getTypeProduct();
}
