package com.tdmu.mobiledevicestoretdmu.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.activity.LichSuMuaHangActivity;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.DonHang;

import java.util.List;

public class LichSuDonHangAdapter extends RecyclerView.Adapter<LichSuDonHangAdapter.ViewHolder> {

    private Context context;
    private List<DonHang> listDonHang;
    private DatabaseHelper databaseHelper;
    private String[] mangTrangThai = {"Chờ xác nhận", "Đang giao hàng", "Đã giao thành công", "Đã hủy"};

    public LichSuDonHangAdapter(Context context, List<DonHang> listDonHang) {
        this.context = context;
        this.listDonHang = listDonHang;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lich_su_don_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonHang donHang = listDonHang.get(position);

        holder.tvMaDonHangLichSu.setText("Mã ĐH: #" + donHang.getMaDH());
        holder.tvNgayDatLichSu.setText(donHang.getNgayDat());

        // Hiển thị danh sách Tên sản phẩm
        String dsSanPham = databaseHelper.getChiTietDonHangString(donHang.getMaDH());
        holder.tvDanhSachSanPhamLichSu.setText(dsSanPham);

        // Đọc thông tin người nhận chốt cứng từ bảng DonHang
        String tenNguoiNhan = (donHang.getTenNguoiNhan() != null && !donHang.getTenNguoiNhan().trim().isEmpty()) ? donHang.getTenNguoiNhan() : "Khách hàng";
        holder.tvDiaChiLichSu.setText("Người nhận: " + tenNguoiNhan + " (" + donHang.getSoDienThoai() + ")\nĐịa chỉ: " + donHang.getDiaChiNhan());

        String pttt = donHang.getPhuongThucThanhToan() != null ? donHang.getPhuongThucThanhToan() : "COD";
        holder.tvPhuongThucLichSu.setText("Thanh toán: " + pttt);

        String giaFormat = String.format("%,d", donHang.getTongTien()).replace(',', '.');
        holder.tvTongTienLichSu.setText("Tổng tiền: " + giaFormat + " đ");

        int status = donHang.getTrangThai();
        if (status >= 0 && status < mangTrangThai.length) {
            holder.tvTrangThaiLichSu.setText(mangTrangThai[status]);
        } else {
            holder.tvTrangThaiLichSu.setText("Đã hủy");
        }

        if (status == 0) holder.tvTrangThaiLichSu.setTextColor(Color.parseColor("#FF9800"));
        else if (status == 1) holder.tvTrangThaiLichSu.setTextColor(Color.parseColor("#1A73E8"));
        else if (status == 2) holder.tvTrangThaiLichSu.setTextColor(Color.parseColor("#2E7D32"));
        else holder.tvTrangThaiLichSu.setTextColor(Color.parseColor("#D32F2F"));

        // Nếu đơn đang ở trạng thái 0 (Chờ xác nhận) -> Cho phép Khách tự bấm HỦY ĐƠN
        if (status == 0) {
            holder.btnHuyDonHangLichSu.setVisibility(View.VISIBLE);
            holder.btnHuyDonHangLichSu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Xác nhận hủy đơn hàng")
                            .setMessage("Bạn có chắc chắn muốn hủy đơn hàng #" + donHang.getMaDH() + " không?")
                            .setPositiveButton("Hủy đơn", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean thanhCong = databaseHelper.huyDonHang(donHang.getMaDH());
                                    if (thanhCong) {
                                        Toast.makeText(context, "Đã hủy đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                                        if (context instanceof LichSuMuaHangActivity) {
                                            ((LichSuMuaHangActivity) context).taiLichSuDonHang();
                                        }
                                    } else {
                                        Toast.makeText(context, "Hủy đơn hàng thất bại!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Quay lại", null)
                            .show();
                }
            });
        } else {
            holder.btnHuyDonHangLichSu.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listDonHang != null ? listDonHang.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaDonHangLichSu, tvNgayDatLichSu, tvDanhSachSanPhamLichSu, tvDiaChiLichSu, tvPhuongThucLichSu, tvTongTienLichSu, tvTrangThaiLichSu;
        Button btnHuyDonHangLichSu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDonHangLichSu = itemView.findViewById(R.id.tvMaDonHangLichSu);
            tvNgayDatLichSu = itemView.findViewById(R.id.tvNgayDatLichSu);
            tvDanhSachSanPhamLichSu = itemView.findViewById(R.id.tvDanhSachSanPhamLichSu);
            tvDiaChiLichSu = itemView.findViewById(R.id.tvDiaChiLichSu);
            tvPhuongThucLichSu = itemView.findViewById(R.id.tvPhuongThucLichSu);
            tvTongTienLichSu = itemView.findViewById(R.id.tvTongTienLichSu);
            tvTrangThaiLichSu = itemView.findViewById(R.id.tvTrangThaiLichSu);
            btnHuyDonHangLichSu = itemView.findViewById(R.id.btnHuyDonHangLichSu);
        }
    }
}
