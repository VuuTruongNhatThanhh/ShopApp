package com.example.basicman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicman.R;
import com.example.basicman.model.Bill;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.MyViewHolder> {
    private  RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<Bill> listbill;


    public BillAdapter(Context context, List<Bill> listbill) {
        this.context = context;
        this.listbill = listbill;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Bill bill = listbill.get(position);
        holder.txtbill.setText("Đơn hàng: "+bill.getId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
          holder.reDetail.getContext(),
          LinearLayoutManager.VERTICAL,
          false

        );
        layoutManager.setInitialPrefetchItemCount(bill.getItem().size());
        //adapter detail
        DetailAdapter detailAdapter = new DetailAdapter(context,bill.getItem());
        holder.reDetail.setLayoutManager(layoutManager);
        holder.reDetail.setAdapter(detailAdapter);
        holder.reDetail.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return listbill.size() ;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtbill;
        RecyclerView reDetail;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtbill = itemView.findViewById(R.id.idbill);
            reDetail = itemView.findViewById(R.id.recyclerview_detail);

        }
    }
}
