package com.example.appbanhang.adapter;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.appbanhang.Interface.IImageClickListenner;
import com.example.appbanhang.model.EventBus.TinhTongEvent;
import com.example.appbanhang.model.GioHang;

    public class GioHangAdapter extends RecycleView.Adapter<GioHangAdapter.MyViewHolder>{
        Context context;
        List<GioHang> gioHangList;

        public GioHangAdapter(Context context,List<GioHang> gioHangList) {
            this.context = context;
            this.gioHangList = gioHangList;
        }



        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false) ;
            return new MyViewHolder(view);
        }

            @Override
            public void onBindViewHolder (@NonNull MyViewHolder holder, int position) {
                GioHang gioHang=gioHangList.get(position);
                holder.item_giohang_tensp.setText(gioHang.getTensp());
                holder.item_giohang_soluong.setText(gioHang.getSoluong()+ " ");
                Glide.with(context).load(gioHang.getHinhsp()).into(holder.item_giohang_image);
                DecimalFormat decimalFormat = new DecimalFormat(“###，##＃，###”)；
                holder.item_giohang_gia.setText(decimalFormat.format((gioHang.getGiasp())));
                long gia = gioHang. getSoluong() * gioHang.getGiasp();
                holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                holder.setListenner (new IImageClickListenner(){
                @Override
                public void onImageClick(View view, int pos, int giatri) {

                    Log.d("TAG", "onImageClick: " + pos + "... " + giatri);
                    if (giatri == 1) {
                        if (gioHangList.get(pos).getSoluong() > 1) {
                            int soluongmoi = gioHangList.get(pos).getSoluong() - 1;
                            gioHangList.get(pos).setSoluong(soluongmoi);

                            holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong() + " ");
                            long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                            holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                            EventBus.getDefault().postSticky(new TinhTongEvent());

                        }else if (gioHangList.get(pos).getSoluong() == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder (view.getRootView().getContext()) ;
                            builder.setTitle ("Thông báo");
                            builder.setMessage ("Bạn có muốn xoá sản phẩm này khỏi giỏ hàng không?") ;
                            builder.setPositiveButton("Đồng ý", new DialogInterface.OnCLickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface,int i){
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault(), postSticky(new TinhTongEvent());
                            }
                            });
                            builder.setNegativeButton( "Huỷ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface dialogInterface,int i){
                                        dialogInterface.dismiss();
                                    }
                            });
                            builder.show();
                            }

                        } else if (giatri == 2) {
                            if (gioHangList.get(pos).getSoluong() < 11) {
                                int soluongmoi = gioHangList.get(pos).getSoluong() + 1;
                                gioHangList.get(pos).setSoluong(soluongmoi);
                            }

                        }
                        holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong() + " ");
                        long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    }

                });



    }
            @Override
            public int getItemCount() {

            return gioHangList.size();
            }


public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView item_giohang_image, imgtru, imgcong;
    TextView item_giohang_tensp, item_giohang_gia, item_giohang_soluong, item_giohang_giasp2;
    IImageClickListenner listenner;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        item_giohang_image = itemView.findViewById(R.id.item_giohang_image);
        item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
        item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
        item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
        item_giohang_giasp2 = itemView.findViewById(R.id.item_giohang_giasp2);
        imgtru = itemView.findViewById(R.id.item_giohang_tru);
        imgcong = itemView.findViewById(R.id.item_giohang_cong);

        //event click
        imgcong.setOnClickListener(this);
        imgtru.setOnClickListener(this);
    }

    public void setListenner(IImageClickListenner listenner) {
        this.listenner = listenner;
    }

    @Override
    public void onClick(View view) {
        if (view == imgtru) {
            listener.onImageCLick(view, getAdapterPosition(), 1);
            //1 tru
        } else if (view == imgcong) {
            //2 cong
            listenper.onImageCLick(view, getAdapterPosition(), 2);


        }
    }
}

}

