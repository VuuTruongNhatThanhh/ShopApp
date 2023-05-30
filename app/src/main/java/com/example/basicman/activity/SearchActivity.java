package com.example.basicman.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.basicman.R;
import com.example.basicman.adapter.CasioAdapter;
import com.example.basicman.model.NewProduct;
import com.example.basicman.retrofit.ApiShop;
import com.example.basicman.retrofit.RetrofitClient;
import com.example.basicman.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    EditText edtsearch;
    CasioAdapter adapterCoat;
    List<NewProduct> newProductList;
    ApiShop apiShop;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        ActionToolBar();

    }

    private void initView() {
        newProductList = new ArrayList<>();
        apiShop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShop.class);
        edtsearch = findViewById(R.id.edtsearch);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0){
                    newProductList.clear();
                    adapterCoat = new CasioAdapter(getApplicationContext(), newProductList);
                    recyclerView.setAdapter(adapterCoat);
                }else {
                    getDataSearch(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }

    private void getDataSearch( String s) {
        newProductList.clear();
        compositeDisposable.add(apiShop.search(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newProductModel -> {
                            if(newProductModel.isSuccess()){
                                newProductList = newProductModel.getResult();
                                adapterCoat = new CasioAdapter(getApplicationContext(), newProductList);
                                recyclerView.setAdapter(adapterCoat);


                            }else{
                                Toast.makeText(getApplicationContext(), newProductModel.getMessage(), Toast.LENGTH_SHORT).show();
                                newProductList.clear();
                                adapterCoat.notifyDataSetChanged();

                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));


    }

    private void ActionToolBar() {
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

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}