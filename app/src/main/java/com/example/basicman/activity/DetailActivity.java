package com.example.basicman.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.basicman.R;
import com.example.basicman.model.NewProduct;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {
    TextView nameP, priceP, des;
    Button btnAdd;
    ImageView imgImage;
    Spinner spinner;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        ActionToolBar();
        initData();
    }

    private void initData() {
        NewProduct newProduct = (NewProduct) getIntent().getSerializableExtra("detail");
        nameP.setText(newProduct.getName());
        des.setText(newProduct.getDescrip());
        Glide.with(getApplicationContext()).load(newProduct.getImg()).into(imgImage);
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
}