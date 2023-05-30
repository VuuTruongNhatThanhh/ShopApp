package com.example.basicman.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.basicman.Interface.IImageClickListener;
import com.example.basicman.R;
import com.example.basicman.model.Cart;
import com.example.basicman.model.EventBus.TotalEvent;
import com.example.basicman.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.internal.Util;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.item_cart_name.setText(cart.getNameP());
        holder.item_cart_amount.setText(cart.getAmount()+ " ");
//        Glide.with(context).load(cart.getImgP()).into(holder.item_cart_image);
        if(cart.getImgP().contains("http")){
            Glide.with(context).load(cart.getImgP()).into(holder.item_cart_image);
        }else{
            String img = Utils.BASE_URL+"images/"+cart.getImgP();
            Glide.with(context).load(img).into(holder.item_cart_image);
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_cart_price.setText(decimalFormat.format((cart.getPriceP())));
        long price = cart.getAmount() * cart.getPriceP();
        holder.item_cart_price2.setText(decimalFormat.format(price));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Utils.arraybuycart.add(cart);
                    EventBus.getDefault().postSticky(new TotalEvent());

                }else {
                    for(int i =0; i<Utils.arraybuycart.size(); i++){
                        if(Utils.arraybuycart.get(i).getIdP() == cart.getIdP()){
                            Utils.arraybuycart.remove(i);
                            EventBus.getDefault().postSticky(new TotalEvent());
                        }
                    }
                }
            }
        });

        holder.setListener(new IImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int value) {
                if(value ==1){
                    if(cartList.get(pos).getAmount()>1){
                        int newAmount = cartList.get(pos).getAmount()-1;
                        cartList.get(pos).setAmount(newAmount);

                        holder.item_cart_amount.setText(cartList.get(pos).getAmount()+ " ");
                        long price = cartList.get(pos).getAmount() * cartList.get(pos).getPriceP();
                        holder.item_cart_price2.setText(decimalFormat.format(price));
                        EventBus.getDefault().postSticky(new TotalEvent());
                    }else if(cartList.get(pos).getAmount() ==1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm khỏi giỏ hàng");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.arraybuycart.remove(cart);
                                Utils.arraycart.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TotalEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();

                    }
                }else if(value ==2){
                    if(cartList.get(pos).getAmount()<11){
                        int newAmount = cartList.get(pos).getAmount()+1;
                        cartList.get(pos).setAmount(newAmount);
                    }
                    holder.item_cart_amount.setText(cartList.get(pos).getAmount()+ " ");
                    long price = cartList.get(pos).getAmount() * cartList.get(pos).getPriceP();
                    holder.item_cart_price2.setText(decimalFormat.format(price));
                    EventBus.getDefault().postSticky(new TotalEvent());

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_cart_image, imgPlus, imgMinus;
        TextView item_cart_name, item_cart_price, item_cart_amount, item_cart_price2;
        IImageClickListener listener;
        CheckBox checkBox;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_cart_image = itemView.findViewById(R.id.item_cart_image);
            item_cart_name = itemView.findViewById(R.id.item_cart_name);
            item_cart_price = itemView.findViewById(R.id.item_cart_price);
            item_cart_amount = itemView.findViewById(R.id.item_cart_amount);
            item_cart_price2 = itemView.findViewById(R.id.item_cart_price2);
            imgMinus = itemView.findViewById(R.id.item_cart_minus);
            imgPlus = itemView.findViewById(R.id.item_cart_plus);
            checkBox = itemView.findViewById(R.id.item_cart_check);

            //event click
            imgPlus.setOnClickListener(this);
            imgMinus.setOnClickListener(this);

        }

        public void setListener(IImageClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
           if(view == imgMinus){
               listener.onImageClick(view, getAdapterPosition(),1);
               //  1 minus
           }else if(view == imgPlus){
               // 2 plus
               listener.onImageClick(view, getAdapterPosition(),2);
           }
        }
    }
}
