package com.example.basicman.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.basicman.R;
import com.example.basicman.adapter.TypeProductAdapter;
import com.example.basicman.model.TypeProduct;
import com.example.basicman.model.TypeProductModel;
import com.example.basicman.retrofit.ApiShop;
import com.example.basicman.retrofit.RetrofitClient;
import com.example.basicman.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
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
    TypeProductModel typeProductModel;




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
        }else{
            Toast.makeText(getApplicationContext(),"not internet",Toast.LENGTH_LONG).show();
        }
        
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
        arrayAdvertise.add("https://lh6.googleusercontent.com/igI1pOYn1EhPkm8WaJb6TmGGP2Ya6cEztplx2jAhnl-y54VP2ZsHEbUl4zRIGNv2Nc52K03U4Sl0Pwq60o3ROxeqn9xYsaXm6Z4ToioTDDc6oeBUPRQLTw-VqVIDKr5QPNbwsLlD");
        arrayAdvertise.add("https://top10tphcm.com/wp-content/uploads/2018/11/1-1024x819-Copy.jpg");
        arrayAdvertise.add("https://mauthietkecuahang.com/wp-content/uploads/2019/04/huong-dan-set-up-shop-thoi-trang-nam-60m2-chuyen-nghiep-2-800x513.jpg");
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
        listViewHome = findViewById(R.id.listviewhome);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawerlayout);
        //set list
        arrayTypeProduct = new ArrayList<>();

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