package com.example.basicman.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.basicman.R;
import com.example.basicman.adapter.NewProductAdapter;
import com.example.basicman.adapter.TypeProductAdapter;
import com.example.basicman.model.NewProduct;
import com.example.basicman.model.TypeProduct;
import com.example.basicman.retrofit.ApiShop;
import com.example.basicman.retrofit.RetrofitClient;
import com.example.basicman.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;




public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewHome;
    NavigationView navigationView;
    ListView listViewHome;
    DrawerLayout drawerLayout;
    TypeProductAdapter typeProductAdapter;
    List<TypeProduct> arrayTypeProduct;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiShop apiShop;
//    TypeProductModel typeProductModel;
    List<NewProduct> arrayNewProduct;
    NewProductAdapter productAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiShop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShop.class);
        Anhxa();
        ActionBar();
//        ActionViewFlipper();
        if(isConnected(this)){
            ActionViewFlipper();
            Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();
            getTypeProduct();
            getNewProduct();
            getEventClick();
        }else{
            Toast.makeText(getApplicationContext(),"not internet",Toast.LENGTH_LONG).show();
        }
        
    }

    private void getEventClick() {
        listViewHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent homepage = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(homepage);
                        break;
                    case 1:
                        Intent casio = new Intent(getApplicationContext(), CasioActivity.class);
                        casio.putExtra("type",2);
                        startActivity(casio);
                        break;
                    case 2:
                        Intent tissot = new Intent(getApplicationContext(), CasioActivity.class);
                        tissot.putExtra("type",3);
                        startActivity(tissot);
                        break;
                    case 3:
                        Intent ck = new Intent(getApplicationContext(),CasioActivity.class);
                        ck.putExtra("type",4);
                        startActivity(ck);
                        break;
                    case 4:
                        Intent movado = new Intent(getApplicationContext(),CasioActivity.class);
                        movado.putExtra("type",5);
                        startActivity(movado);
                        break;
                    case 5:
                        Intent doxa = new Intent(getApplicationContext(),CasioActivity.class);
                        doxa.putExtra("type",6);
                        startActivity(doxa);
                        break;
                    case 6:
                        Intent longines = new Intent(getApplicationContext(),CasioActivity.class);
                        longines.putExtra("type",7);
                        startActivity(longines);
                        break;
                    case 7:
                        Intent frederique = new Intent(getApplicationContext(),CasioActivity.class);
                        frederique.putExtra("type",8);
                        startActivity(frederique);
                        break;



                }
            }
        });
    }

    private void getNewProduct() {
        compositeDisposable.add(apiShop.getNewProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    newProductModel -> {
                        if(newProductModel.isSuccess()){
                            arrayNewProduct = newProductModel.getResult();
                            productAdapter = new NewProductAdapter(getApplicationContext(),arrayNewProduct);
                            recyclerViewHome.setAdapter(productAdapter);
                        }

                    },
                        throwable -> {
                        Toast.makeText(getApplicationContext(),"not connect sever"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    private void getTypeProduct() {
        compositeDisposable.add(apiShop.getTypeProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        typeProductModel ->{
                            if(typeProductModel.isSuccess()){
                                arrayTypeProduct = typeProductModel.getResult();
                                //set adapter
                                typeProductAdapter = new TypeProductAdapter(getApplicationContext(),arrayTypeProduct);
                                listViewHome.setAdapter(typeProductAdapter);

                            }
                        }
                ));



    }

    private void ActionViewFlipper() {
        List<String> arrayAdvertise = new ArrayList<>();
        arrayAdvertise.add("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/5b46a005ceeb89952cfc4a58b1cde904-1653077619.jpg");
        arrayAdvertise.add("https://www.mallatmillenia.com/wp-content/uploads/2022/12/KBPHOTO_Rolex_MayorsRolexBoutique_Orlando-FL__20210416__285_01-scaled_1200x600_acf_cropped.jpg");
        arrayAdvertise.add("https://www.theluxuryhut.com/admin/upload/1675760240most-popular-rolex.jpg");
        for(int i=0; i<arrayAdvertise.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(arrayAdvertise.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);

        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);


    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarhome);
//        setSupportActionBar(toolbar);

        viewFlipper = findViewById(R.id.viewflipper);
        recyclerViewHome = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewHome.setLayoutManager(layoutManager);
        recyclerViewHome.setHasFixedSize(true);
        listViewHome = findViewById(R.id.listviewhome);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawerlayout);
        //set list
        arrayTypeProduct = new ArrayList<>();
        arrayNewProduct = new ArrayList<>();


    }
    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi!=null && wifi.isConnected())||(mobile!=null && mobile.isConnected())){
            return true;
        }else{
            return false;
        }


    }
    public void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}