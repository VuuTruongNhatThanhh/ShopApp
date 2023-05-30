package com.example.basicman.activity;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.basicman.R;
import com.example.basicman.adapter.CartAdapter;
import com.example.basicman.model.Cart;
import com.example.basicman.model.EventBus.TotalEvent;
import com.example.basicman.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    TextView emptyCart, totalMoney;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnBuy;

    CartAdapter adapter;
    long totalmoneyP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();
        initControl();

        if(Utils.arraybuycart !=null){
            Utils.arraybuycart.clear();
        }
        totalMoney();
    }

    private void totalMoney() {
         totalmoneyP = 0;
        for(int i=0; i<Utils.arraybuycart.size(); i++){
            totalmoneyP = totalmoneyP +(Utils.arraybuycart.get(i).getPriceP() * Utils.arraybuycart.get(i).getAmount());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        totalMoney.setText(decimalFormat.format(totalmoneyP));
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
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Utils.arraycart.size()==0){
            emptyCart.setVisibility(View.VISIBLE);
        }else{
            adapter = new CartAdapter(getApplicationContext(), Utils.arraycart);
            recyclerView.setAdapter(adapter);
        }

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);
                intent.putExtra("totalPrice",totalmoneyP);
                Utils.arraycart.clear();
                startActivity(intent);
            }
        });

    }

    private void initView() {
        emptyCart = findViewById(R.id.txtemptycart);
        totalMoney = findViewById(R.id.txtTotalMoney);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerviewcart);
        btnBuy = findViewById(R.id.btnBuy);

    }
// total money
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTotal (TotalEvent event){
        if (event != null){
            totalMoney();
        }
    }

}