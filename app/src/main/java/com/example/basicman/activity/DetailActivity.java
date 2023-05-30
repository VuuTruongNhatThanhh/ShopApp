package com.example.basicman.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.basicman.R;
import com.example.basicman.model.Cart;
import com.example.basicman.model.NewProduct;
import com.example.basicman.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {
    TextView nameP, priceP, des;
    Button btnAdd;
    ImageView imgImage;
    Spinner spinner;
    Toolbar toolbar;
    NewProduct newProduct;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart();
            }
        });
    }

    private void addCart() {
        if(Utils.arraycart.size()>0){
            boolean flag = false;
            int amount = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i =0; i<Utils.arraycart.size(); i++){
                if(Utils.arraycart.get(i).getIdP() == newProduct.getId()){
                    Utils.arraycart.get(i).setAmount(amount+Utils.arraycart.get(i).getAmount());


                    flag = true;
                }
            }
            if(flag == false){
                long price = Long.parseLong(newProduct.getPrice());
                Cart cart = new Cart();
                cart.setPriceP(price);
                cart.setAmount(amount);
                cart.setIdP(newProduct.getId());
                cart.setNameP(newProduct.getName());
                cart.setImgP(newProduct.getImg());
                Utils.arraycart.add(cart);
            }

        }else{
            int amount = Integer.parseInt(spinner.getSelectedItem().toString());
            long price = Long.parseLong(newProduct.getPrice());
            Cart cart = new Cart();
            cart.setPriceP(price);
            cart.setAmount(amount);
            cart.setIdP(newProduct.getId());
            cart.setNameP(newProduct.getName());
            cart.setImgP(newProduct.getImg());
            Utils.arraycart.add(cart);


        }
        int totalItem =0;
        for(int i =0; i<Utils.arraycart.size();i++){
            totalItem = totalItem + Utils.arraycart.get(i).getAmount();
        }
        //show number cart
        badge.setText(String.valueOf(totalItem));
    }

    private void initData() {
         newProduct = (NewProduct) getIntent().getSerializableExtra("detail");
        nameP.setText(newProduct.getName());
        des.setText(newProduct.getDescrip());
//        Glide.with(getApplicationContext()).load(newProduct.getImg()).into(imgImage);
        if(newProduct.getImg().contains("http")){
            Glide.with(getApplicationContext()).load(newProduct.getImg()).into(imgImage);
        }else{
            String img = Utils.BASE_URL+"images/"+newProduct.getImg();
            Glide.with(getApplicationContext()).load(img).into(imgImage);
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        priceP.setText(decimalFormat.format(Double.parseDouble(newProduct.getPrice()))+" VND");
        Integer[] number = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,number);
        spinner.setAdapter(adapterspin);
    }

    private void initView() {
        nameP = findViewById(R.id.txtNameProduct);
        priceP = findViewById(R.id.txtPriceProduct);
        des = findViewById(R.id.txtDesDetail);
        btnAdd = findViewById(R.id.btnAddCart);
        spinner = findViewById(R.id.spinner);
        imgImage = findViewById(R.id.imgdetail);
        toolbar = findViewById(R.id.toolbar);
        badge = findViewById(R.id.menu_amount);
        FrameLayout frameLayoutCart = findViewById(R.id.frameCart);
        frameLayoutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });
        //number cart show
        if(Utils.arraycart !=null){
            int totalItem =0;
            for(int i =0; i<Utils.arraycart.size();i++){
                totalItem = totalItem + Utils.arraycart.get(i).getAmount();
            }
            badge.setText(String.valueOf(totalItem));
        }



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
    protected void onResume() {
        super.onResume();
        if(Utils.arraycart !=null){
            int totalItem =0;
            for(int i =0; i<Utils.arraycart.size();i++){
                totalItem = totalItem + Utils.arraycart.get(i).getAmount();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }
}