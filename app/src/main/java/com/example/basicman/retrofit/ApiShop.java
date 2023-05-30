package com.example.basicman.retrofit;



import com.example.basicman.model.BillModel;
import com.example.basicman.model.MessageModel;
import com.example.basicman.model.NewProductModel;
import com.example.basicman.model.TypeProductModel;
import com.example.basicman.model.UserModel;

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

    @POST("signup.php")
    @FormUrlEncoded
    Observable<UserModel>SignUp(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile,
            @Field("uid") String uid
    );
    @POST("updatetoken.php")
    @FormUrlEncoded
    Observable<MessageModel>updateToken(
            @Field("id") int id,
            @Field("token") String token


    );

    @POST("login.php")
    @FormUrlEncoded
    Observable<UserModel>login(
            @Field("email") String email,
            @Field("pass") String pass
    );

    @POST("reset.php")
    @FormUrlEncoded
    Observable<UserModel>resetPass(
            @Field("email") String email
    );

    @POST("bill.php")
    @FormUrlEncoded
    Observable<UserModel>createOrder(
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("total_price") String total_price,
            @Field("iduser") int id,
            @Field("address") String address,
            @Field("amount") int amount,
            @Field("detail") String detail
    );

    @POST("viewbill.php")
    @FormUrlEncoded
    Observable<BillModel>viewBill(
            @Field("iduser") int id
    );

    @POST("search.php")
    @FormUrlEncoded
    Observable<NewProductModel>search(
            @Field("search") String search
    );


    @POST("gettoken.php")
    @FormUrlEncoded
    Observable<UserModel>gettoken(
            @Field("status") int status
    );


}
