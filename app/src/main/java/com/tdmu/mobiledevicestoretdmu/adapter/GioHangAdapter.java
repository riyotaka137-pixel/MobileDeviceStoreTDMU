package com.tdmu.mobiledevicestoretdmu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.ChiTietGioHang;
import com.tdmu.mobiledevicestoretdmu.model.SanPham;

import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHolder> {

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    private Context context;
    private List<ChiTietGioHang> listChiTiet;
    private DatabaseHelper databaseHelper;
    private OnCartChangeListener onCartChangeListener;

    public GioHangAdapter(Context context, List<ChiTietGioHang> listChiTiet, OnCartChangeListener listener) {
        this.context = context;
        this.listChiTiet = listChiTiet;
        this.databaseHelper = new DatabaseHelper(context);
        this.onCartChangeListener = listener;
    }

    public GioHangAdapter(Context context, List<ChiTietGioHang> listChiTiet) {
        this(context, listChiTiet, null);
    }

    public void setOnCartChangeListener(OnCartChangeListener listener) {
        this.onCartChangeListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gio_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChiTietGioHang ctgh = listChiTiet.get(position);
        SanPham sanPham = databaseHelper.getSanPhamById(ctgh.getMaSP());

        if (sanPham != null) {
            holder.tvTenSPGioHang.setText(sanPham.getTenSP());
            // Load hình ảnh qua Glide
            QuanLySanPhamAdapter.loadHinhAnhSanPham(context, sanPham.getHinhAnh(), holder.imgGioHang);
        } else {
            holder.tvTenSPGioHang.setText("Sản phẩm #" + ctgh.getMaSP());
            holder.imgGioHang.setImageResource(R.mipmap.ic_launcher);
        }

        String giaFormat = String.format("%,d", ctgh.getDonGia()).replace(',', '.');
        holder.tvDonGiaGioHang.setText("Đơn giá: " + giaFormat + " đ");
        holder.tvSoLuongGioHang.setText(String.valueOf(ctgh.getSoLuong()));

        holder.btnGiamSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctgh.getSoLuong() > 1) {
                    databaseHelper.updateSoLuongGioHang(ctgh.getMaCTGH(), ctgh.getSoLuong() - 1);
                    ctgh.setSoLuong(ctgh.getSoLuong() - 1);
                    notifyItemChanged(holder.getAdapterPosition());
                    if (onCartChangeListener != null) {
                        onCartChangeListener.onCartChanged();
                    }
                }
            }
        });

        holder.btnTangSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.updateSoLuongGioHang(ctgh.getMaCTGH(), ctgh.getSoLuong() + 1);
                ctgh.setSoLuong(ctgh.getSoLuong() + 1);
                notifyItemChanged(holder.getAdapterPosition());
                if (onCartChangeListener != null) {
                    onCartChangeListener.onCartChanged();
                }
            }
        });

        holder.btnXoaGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteChiTietGioHang(ctgh.getMaCTGH());
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && pos < listChiTiet.size()) {
                    listChiTiet.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, listChiTiet.size());
                }
                if (onCartChangeListener != null) {
                    onCartChangeListener.onCartChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listChiTiet != null ? listChiTiet.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGioHang;
        TextView tvTenSPGioHang, tvDonGiaGioHang, tvSoLuongGioHang;
        Button btnGiamSoLuong, btnTangSoLuong, btnXoaGioHang;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGioHang = itemView.findViewById(R.id.imgGioHang);
            tvTenSPGioHang = itemView.findViewById(R.id.tvTenSPGioHang);
            tvDonGiaGioHang = itemView.findViewById(R.id.tvDonGiaGioHang);
            tvSoLuongGioHang = itemView.findViewById(R.id.tvSoLuongGioHang);
            btnGiamSoLuong = itemView.findViewById(R.id.btnGiamSoLuong);
            btnTangSoLuong = itemView.findViewById(R.id.btnTangSoLuong);
            btnXoaGioHang = itemView.findViewById(R.id.btnXoaGioHang);
        }
    }
}
