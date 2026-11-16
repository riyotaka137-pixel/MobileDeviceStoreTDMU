package com.tdmu.mobiledevicestoretdmu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.model.GiamGia;

import java.util.List;

public class QuanLyVoucherAdapter extends RecyclerView.Adapter<QuanLyVoucherAdapter.ViewHolder> {

    public interface OnVoucherActionListener {
        void onEdit(GiamGia gg);
        void onDelete(GiamGia gg);
    }

    private Context context;
    private List<GiamGia> listVoucher;
    private OnVoucherActionListener listener;

    public QuanLyVoucherAdapter(Context context, List<GiamGia> listVoucher, OnVoucherActionListener listener) {
        this.context = context;
        this.listVoucher = listVoucher;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quan_ly_voucher, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GiamGia gg = listVoucher.get(position);

        holder.tvMaCodeVoucher.setText(gg.getMaCode());

        if (gg.getTrangThai() == 1) {
            holder.tvTrangThaiVoucher.setText("Đang áp dụng");
            holder.tvTrangThaiVoucher.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            holder.tvTrangThaiVoucher.setText("Đang khóa");
            holder.tvTrangThaiVoucher.setTextColor(Color.parseColor("#D32F2F"));
        }

        String toiDaStr = gg.getTienGiamToiDa() > 0 ? " (Tối đa: " + String.format("%,d", gg.getTienGiamToiDa()).replace(',', '.') + " đ)" : "";
        holder.tvMucGiamVoucher.setText("Giảm " + gg.getPhanTramGiam() + "%" + toiDaStr);

        String donToiThieuStr = gg.getDonToiThieu() > 0 ? String.format("%,d", gg.getDonToiThieu()).replace(',', '.') + " đ" : "Không yêu cầu";
        holder.tvDieuKienVoucher.setText("Đơn tối thiểu: " + donToiThieuStr);

        holder.tvHSDVoucher.setText("Hạn sử dụng: " + (gg.getNgayHetHan() != null && !gg.getNgayHetHan().isEmpty() ? gg.getNgayHetHan() : "Không thời hạn"));
        holder.tvMoTaVoucher.setText(gg.getMoTa() != null && !gg.getMoTa().isEmpty() ? gg.getMoTa() : "Không có mô tả");

        holder.btnSuaVoucher.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(gg);
        });

        holder.btnXoaVoucher.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(gg);
        });
    }

    @Override
    public int getItemCount() {
        return listVoucher != null ? listVoucher.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaCodeVoucher, tvTrangThaiVoucher, tvMucGiamVoucher, tvDieuKienVoucher, tvHSDVoucher, tvMoTaVoucher;
        Button btnSuaVoucher, btnXoaVoucher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaCodeVoucher = itemView.findViewById(R.id.tvMaCodeVoucher);
            tvTrangThaiVoucher = itemView.findViewById(R.id.tvTrangThaiVoucher);
            tvMucGiamVoucher = itemView.findViewById(R.id.tvMucGiamVoucher);
            tvDieuKienVoucher = itemView.findViewById(R.id.tvDieuKienVoucher);
            tvHSDVoucher = itemView.findViewById(R.id.tvHSDVoucher);
            tvMoTaVoucher = itemView.findViewById(R.id.tvMoTaVoucher);
            btnSuaVoucher = itemView.findViewById(R.id.btnSuaVoucher);
            btnXoaVoucher = itemView.findViewById(R.id.btnXoaVoucher);
        }
    }
}
