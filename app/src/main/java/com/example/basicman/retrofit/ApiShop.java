package com.example.basicman.retrofit;



import com.example.basicman.model.NewProductModel;
import com.example.basicman.model.TypeProductModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiShop {

    @GET("gettypeproduct.php")
    Observable<TypeProductModel> getTypeProduct();

    @GET("getnewproduct.php")
    Observable<NewProductModel> getNewProduct();

    @POST("detail.php")
    @FormUrlEncoded
    Observable<NewProductModel>getProduct(
      @Field("page") int page,
      @Field("type") int type
    );

}
