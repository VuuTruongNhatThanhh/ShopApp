package com.example.basicman.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.basicman.R;

import com.example.basicman.adapter.BillAdapter;
import com.example.basicman.retrofit.ApiShop;
import com.example.basicman.retrofit.RetrofitClient;
import com.example.basicman.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ViewBillActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiShop apiShop;
    RecyclerView rebill;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);
        initView();
        initToolbar();
        getOrder();






    }

    private void getOrder() {
        compositeDisposable.add(apiShop.viewBill(Utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        billModel -> {
                            BillAdapter adapter = new BillAdapter(getApplicationContext(), billModel.getResult());
                            rebill.setAdapter(adapter);

                        },
                        throwable -> {

                        }
                ));


    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            //comeback type to home
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        apiShop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShop.class);
        rebill = findViewById(R.id.recyclerview_bill);
        toolbar = findViewById(R.id.toolbar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rebill.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}