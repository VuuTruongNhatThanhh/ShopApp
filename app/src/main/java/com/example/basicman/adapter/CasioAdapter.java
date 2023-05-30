package com.example.basicman.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.basicman.Interface.ItemClickListener;
import com.example.basicman.R;
import com.example.basicman.activity.DetailActivity;
import com.example.basicman.model.NewProduct;
import com.example.basicman.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

public class CasioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<NewProduct> array;
    private static final int VIEW_TYPE_DATA =0;
    private static final int VIEW_TYPE_LOADING =1;

    public CasioAdapter(Context context, List<NewProduct> array) {
        this.context = context;
        this.array = array;
    }







    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coat,parent,false);

            return new MyViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  MyViewHolder){
            MyViewHolder myViewHolder =(MyViewHolder)holder;
            NewProduct product = array.get(position);
            myViewHolder.name.setText(product.getName());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.price.setText(decimalFormat.format(Double.parseDouble(product.getPrice()))+" VND");
            myViewHolder.des.setText(product.getDescrip());
//            Glide.with(context).load(product.getImg()).into(myViewHolder.image);
            if(product.getImg().contains("http")){
                Glide.with(context).load(product.getImg()).into(((MyViewHolder) holder).image);
            }else{
                String img = Utils.BASE_URL+"images/"+product.getImg();
                Glide.with(context).load(img).into(((MyViewHolder) holder).image);
            }
            //click to detail
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(!isLongClick){
                        //click
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("detail",product);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }else{
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder)holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position)== null ? VIEW_TYPE_LOADING:VIEW_TYPE_DATA ;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,price,des;
        ImageView image;
        private ItemClickListener itemClickListener;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemcoat_name);
            price = itemView.findViewById(R.id.itemcoat_price);
            des = itemView.findViewById(R.id.itemcoat_des);
            image = itemView.findViewById(R.id.itemcoat_image);
            itemView.setOnClickListener(this);


        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }
}
