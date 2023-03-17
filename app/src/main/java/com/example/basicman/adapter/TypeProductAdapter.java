package com.example.basicman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.basicman.R;
import com.example.basicman.model.TypeProduct;

import java.util.List;

public class TypeProductAdapter extends BaseAdapter {
    List<TypeProduct> array;
    Context context;

    public TypeProductAdapter( Context context, List<TypeProduct> array) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class ViewHolder{
        TextView textNameProduct;
        ImageView imgImage;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view==null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_product, null);
            viewHolder.textNameProduct = view.findViewById(R.id.item_nameproduct);
            viewHolder.imgImage = view.findViewById(R.id.item_image);
            view.setTag(viewHolder);

        }else{
            viewHolder =(ViewHolder)  view.getTag();

        }
        viewHolder.textNameProduct.setText(array.get(i).getName());
        Glide.with(context).load(array.get(i).getImg()).into(viewHolder.imgImage);
        return view;
    }
}
