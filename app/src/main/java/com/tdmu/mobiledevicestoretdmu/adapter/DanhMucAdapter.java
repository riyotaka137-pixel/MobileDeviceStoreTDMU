package com.tdmu.mobiledevicestoretdmu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.activity.DanhSachSanPhamActivity;
import com.tdmu.mobiledevicestoretdmu.model.DanhMuc;

import java.util.List;

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.ViewHolder> {

    private Context context;
    private List<DanhMuc> listDanhMuc;

    public DanhMucAdapter(Context context, List<DanhMuc> listDanhMuc) {
        this.context = context;
        this.listDanhMuc = listDanhMuc;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_danh_muc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DanhMuc danhMuc = listDanhMuc.get(position);
        holder.tvTenDanhMuc.setText(danhMuc.getTenDanhMuc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DanhSachSanPhamActivity.class);
                intent.putExtra("MaDanhMuc", danhMuc.getMaDanhMuc());
                intent.putExtra("TenDanhMuc", danhMuc.getTenDanhMuc());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDanhMuc != null ? listDanhMuc.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenDanhMuc;
        ImageView imgLogoDanhMuc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenDanhMuc = itemView.findViewById(R.id.tvTenDanhMuc);
            imgLogoDanhMuc = itemView.findViewById(R.id.imgLogoDanhMuc);
        }
    }
}
