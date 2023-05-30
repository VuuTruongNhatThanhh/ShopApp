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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
import com.example.basicman.model.User;
import com.example.basicman.retrofit.ApiShop;
import com.example.basicman.retrofit.RetrofitClient;
import com.example.basicman.utils.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
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
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiShop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShop.class);
        Paper.init(this);
        if(Paper.book().read("user") != null){
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }
        getToken();

        Anhxa();
        ActionBar();
//        ActionViewFlipper();
        if(isConnected(this)){
            ActionViewFlipper();
            Toast.makeText(getApplicationContext(),"Có kết nối internet",Toast.LENGTH_LONG).show();
            getTypeProduct();
            getNewProduct();
            getEventClick();
        }else{
            Toast.makeText(getApplicationContext(),"không có kết nối internet",Toast.LENGTH_LONG).show();
        }
        
    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if(!TextUtils.isEmpty(s)){
                            compositeDisposable.add(apiShop.updateToken(Utils.user_current.getId(),s)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            messageModel -> {

                                            },
                                            throwable -> {
                                                Log.d("log", throwable.getMessage());
                                            }
                                    ));
                        }
                    }
                });
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
                    case 8:
                        Intent bill = new Intent(getApplicationContext(),ViewBillActivity.class);
                        startActivity(bill);
                        break;
                    case 9:
                        // remove key user
                        Paper.book().delete("user");
                        Intent login = new Intent(getApplicationContext(),LogInActivity.class);
                        startActivity(login);
                        FirebaseAuth.getInstance().signOut();
                        finish();
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
                                arrayTypeProduct.add(new TypeProduct("Đăng xuất","data:image/jpeg;base64,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"));
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
        imgsearch = findViewById(R.id.imgsearch);
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
        badge = findViewById(R.id.menu_amount);
        frameLayout = findViewById(R.id.frameCart);
        //set list
        arrayTypeProduct = new ArrayList<>();
        arrayNewProduct = new ArrayList<>();
        if(Utils.arraycart== null){
            Utils.arraycart = new ArrayList<>();
        }else{
            int totalItem =0;
            for(int i =0; i<Utils.arraycart.size();i++){
                totalItem = totalItem + Utils.arraycart.get(i).getAmount();
            }
            //show number cart
            badge.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem =0;
        for(int i =0; i<Utils.arraycart.size();i++){
            totalItem = totalItem + Utils.arraycart.get(i).getAmount();
        }
        //show number cart
        badge.setText(String.valueOf(totalItem));
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