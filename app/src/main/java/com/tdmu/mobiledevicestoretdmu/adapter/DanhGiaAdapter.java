package com.tdmu.mobiledevicestoretdmu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.model.DanhGia;

import java.util.List;

public class DanhGiaAdapter extends RecyclerView.Adapter<DanhGiaAdapter.ViewHolder> {

    private Context context;
    private List<DanhGia> listDanhGia;

    public DanhGiaAdapter(Context context, List<DanhGia> listDanhGia) {
        this.context = context;
        this.listDanhGia = listDanhGia;
    }

    public void updateData(List<DanhGia> newList) {
        this.listDanhGia = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_danh_gia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DanhGia dg = listDanhGia.get(position);

        String tenNguoiDung = dg.getTenNguoiDung();
        if (tenNguoiDung == null || tenNguoiDung.trim().isEmpty()) {
            tenNguoiDung = "Khách hàng";
        }
        holder.tvTenNguoiDanhGia.setText(tenNguoiDung);

        String ngay = dg.getNgayDanhGia();
        if (ngay != null && ngay.length() >= 10) {
            holder.tvNgayDanhGia.setText(ngay.substring(0, 10));
        } else {
            holder.tvNgayDanhGia.setText(ngay != null ? ngay : "");
        }

        holder.rbItemDanhGia.setRating(dg.getSoSao());
        holder.tvNoiDungDanhGia.setText(dg.getNoiDung() != null ? dg.getNoiDung() : "");
    }

    @Override
    public int getItemCount() {
        return listDanhGia != null ? listDanhGia.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenNguoiDanhGia, tvNgayDanhGia, tvNoiDungDanhGia;
        RatingBar rbItemDanhGia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenNguoiDanhGia = itemView.findViewById(R.id.tvTenNguoiDanhGia);
            tvNgayDanhGia = itemView.findViewById(R.id.tvNgayDanhGia);
            tvNoiDungDanhGia = itemView.findViewById(R.id.tvNoiDungDanhGia);
            rbItemDanhGia = itemView.findViewById(R.id.rbItemDanhGia);
        }
    }
}
