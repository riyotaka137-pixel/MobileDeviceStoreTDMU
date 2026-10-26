package com.tdmu.mobiledevicestoretdmu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.model.TaiKhoan;

import java.util.List;

public class QuanLyTaiKhoanAdapter extends RecyclerView.Adapter<QuanLyTaiKhoanAdapter.ViewHolder> {

    private Context context;
    private List<TaiKhoan> listTaiKhoan;
    private OnTaiKhoanClickListener listener;

    public interface OnTaiKhoanClickListener {
        void onTaiKhoanClick(TaiKhoan tk);
    }

    public QuanLyTaiKhoanAdapter(Context context, List<TaiKhoan> listTaiKhoan, OnTaiKhoanClickListener listener) {
        this.context = context;
        this.listTaiKhoan = listTaiKhoan;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quan_ly_tai_khoan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaiKhoan tk = listTaiKhoan.get(position);

        holder.tvTenTKQuanLy.setText(tk.getHoTen());
        holder.tvEmailTKQuanLy.setText("Email: " + tk.getEmail());

        String sdt = tk.getSoDienThoai();
        if (sdt == null || sdt.trim().isEmpty()) {
            sdt = "Chưa cập nhật";
        }
        holder.tvSdtTKQuanLy.setText("SĐT: " + sdt);

        if (tk.getTrangThai() == 1) {
            holder.tvTrangThaiTKQuanLy.setText("Hoạt động");
            holder.tvTrangThaiTKQuanLy.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            holder.tvTrangThaiTKQuanLy.setText("Đã khóa");
            holder.tvTrangThaiTKQuanLy.setTextColor(Color.parseColor("#D32F2F"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onTaiKhoanClick(listTaiKhoan.get(pos));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTaiKhoan != null ? listTaiKhoan.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenTKQuanLy, tvEmailTKQuanLy, tvSdtTKQuanLy, tvTrangThaiTKQuanLy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenTKQuanLy = itemView.findViewById(R.id.tvTenTKQuanLy);
            tvEmailTKQuanLy = itemView.findViewById(R.id.tvEmailTKQuanLy);
            tvSdtTKQuanLy = itemView.findViewById(R.id.tvSdtTKQuanLy);
            tvTrangThaiTKQuanLy = itemView.findViewById(R.id.tvTrangThaiTKQuanLy);
        }
    }
}
