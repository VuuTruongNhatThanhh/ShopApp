package com.example.basicman.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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

public class CasioActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiShop apiShop;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page =1;
    int type;
    CasioAdapter adapterCoat;
    List<NewProduct> newProductList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coat);
        apiShop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShop.class);
        type = getIntent().getIntExtra("type",1);

        Anhxa();
        ActionToolBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false){
                    if(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == newProductList.size()-1){
                       isLoading = true;
                       loadMore();
                    }
                }
            }
        });

    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //add null
                newProductList.add(null);
                adapterCoat.notifyItemInserted(newProductList.size()-1);

            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //remove null
                newProductList.remove(newProductList.size()-1);
                adapterCoat.notifyItemRemoved(newProductList.size());
                page = page +1;
                getData(page);
                adapterCoat.notifyDataSetChanged();
                isLoading = false;
            }
        },2000);
    }

    private void getData( int page) {
        compositeDisposable.add(apiShop.getProduct(page, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newProductModel -> {
                            if(newProductModel.isSuccess()){
                                if(adapterCoat==null){
                                    newProductList = newProductModel.getResult();
                                    adapterCoat = new CasioAdapter(getApplicationContext(), newProductList);
                                    recyclerView.setAdapter(adapterCoat);
                                }else{
                                    int location = newProductList.size()-1;
                                    int amountadd = newProductModel.getResult().size();
                                    for(int i=0;i<amountadd;i++){
                                        newProductList.add(newProductModel.getResult().get(i));
                                    }
                                        adapterCoat.notifyItemRangeInserted(location, amountadd);

                                }


                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"not connect sever", Toast.LENGTH_LONG).show();
                        }
                )
        );
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




    private void Anhxa() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview_coat);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        newProductList = new ArrayList<>();


    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}