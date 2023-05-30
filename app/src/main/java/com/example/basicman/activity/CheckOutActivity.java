package com.example.basicman.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.GameState;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basicman.model.NotiSendData;
import com.example.basicman.retrofit.ApiPushNotification;
import com.example.basicman.retrofit.ApiShop;
import com.example.basicman.retrofit.RetrofitClient;

import com.example.basicman.R;
import com.example.basicman.retrofit.RetrofitClientNoti;
import com.example.basicman.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CheckOutActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtTotalMoney, txtPhone, txtEmail;
    EditText edtAddress;
    AppCompatButton btnOrder;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiShop apiShop;
    long totalPrice;
    int totalItem;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        initView();
        countItem();
        initControl();
    }

    private void countItem() {
         totalItem =0;
        for(int i =0; i<Utils.arraybuycart.size();i++){
            totalItem = totalItem + Utils.arraybuycart.get(i).getAmount();
        }
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        totalPrice = getIntent().getLongExtra("totalPrice",0);
        txtTotalMoney.setText(decimalFormat.format(totalPrice));
        txtEmail.setText(Utils.user_current.getEmail());
        txtPhone.setText(Utils.user_current.getMobile());


        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_address = edtAddress.getText().toString().trim();
                if(TextUtils.isEmpty(str_address)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                }else{
                    //post data
                    String str_email = Utils.user_current.getEmail();
                    String str_mobile = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();
                    Log.d("test", new Gson().toJson(Utils.arraybuycart));  // check
                    compositeDisposable.add(apiShop.createOrder(str_email, str_mobile,String.valueOf(totalPrice),id,str_address,totalItem,new Gson().toJson(Utils.arraybuycart))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                 userModel -> {
                                     pushNotiUser();
                                     Toast.makeText(getApplicationContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                     Utils.arraybuycart.clear();
                                     Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                     startActivity(intent);
                                     finish();
                                 },
                                 throwable -> {
                                     Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                 }
                            ));
                }
            }
        });
    }

    private void pushNotiUser() {
        //get token
        compositeDisposable.add(apiShop.gettoken(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                   userModel -> {
                        if(userModel.isSuccess()){
                            for(int i=0; i<userModel.getResult().size(); i++){
                                Map<String, String > data = new HashMap<>();
                                data.put("title","thong bao");
                                data.put("body", "Ban co don hang moi");
                                NotiSendData notiSendData = new NotiSendData(userModel.getResult().get(i).getToken(), data);
                                ApiPushNotification apiPushNotification = RetrofitClientNoti.getInstance().create(ApiPushNotification.class);
                                compositeDisposable.add(apiPushNotification.sendNotification(notiSendData)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                notiResponse -> {

                                                },throwable -> {
                                                    Log.d("logg", throwable.getMessage());
                                                }
                                        ));
                            }

                        }
                   },
                        throwable -> {
                            Log.d("logg", throwable.getMessage());
                        }
                ));



    }

    private void initView() {
        apiShop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShop.class);
        toolbar = findViewById(R.id.toolbar);
        txtTotalMoney = findViewById(R.id.txtTotalMoney);
        txtPhone = findViewById(R.id.txtphone);
        txtEmail = findViewById(R.id.txtemail);
        edtAddress = findViewById(R.id.edtaddress);
        btnOrder = findViewById(R.id.btnorder);

    }

    @Override
    protected void onDestroy() {
       compositeDisposable.clear();
        super.onDestroy();
    }
}