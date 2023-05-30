package com.example.basicman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.basicman.R;
import com.example.basicman.model.Item;
import com.example.basicman.utils.Utils;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {

    Context context;
    List<Item> itemList;

    public DetailAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txtname.setText(item.getName()+ "");
        holder.txtamount.setText("Số lượng: "+item.getAmount()+ "");
//        Glide.with(context).load(item.getImg()).into(holder.imageDetail);
        if(item.getImg().contains("http")){
            Glide.with(context).load(item.getImg()).into(holder.imageDetail);
        }else{
            String img = Utils.BASE_URL+"images/"+item.getImg();
            Glide.with(context).load(img).into(holder.imageDetail);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageDetail;
        TextView txtname, txtamount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageDetail = itemView.findViewById(R.id.item_imgdetail);
            txtname = itemView.findViewById(R.id.item_namePdetail);
            txtamount = itemView.findViewById(R.id.item_amountdetail);
        }
    }
}
