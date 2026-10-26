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
import com.tdmu.mobiledevicestoretdmu.activity.QuanLyDonHangActivity;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.DonHang;

import java.util.List;

public class QuanLyDonHangAdapter extends RecyclerView.Adapter<QuanLyDonHangAdapter.ViewHolder> {

    private Context context;
    private List<DonHang> listDonHang;
    private DatabaseHelper databaseHelper;
    private String[] mangTrangThai = {"Chờ xác nhận", "Đang giao hàng", "Đã giao thành công", "Đã hủy"};

    public QuanLyDonHangAdapter(Context context, List<DonHang> listDonHang) {
        this.context = context;
        this.listDonHang = listDonHang;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quan_ly_don_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonHang donHang = listDonHang.get(position);

        holder.tvMaDHQuanLy.setText("Mã ĐH: #" + donHang.getMaDH());
        holder.tvNgayDatQuanLy.setText(donHang.getNgayDat());

        // Hiển thị danh sách Tên sản phẩm trong Đơn Hàng
        String dsSanPham = databaseHelper.getChiTietDonHangString(donHang.getMaDH());
        holder.tvDanhSachSanPhamQuanLy.setText(dsSanPham);

        // Đọc thông tin người nhận chốt cứng từ bảng DonHang
        String tenNguoiNhan = (donHang.getTenNguoiNhan() != null && !donHang.getTenNguoiNhan().trim().isEmpty()) ? donHang.getTenNguoiNhan() : "Khách hàng";
        holder.tvThongTinKHQuanLy.setText("Người nhận: " + tenNguoiNhan + " (" + donHang.getSoDienThoai() + ")\nĐịa chỉ: " + donHang.getDiaChiNhan());

        String pttt = donHang.getPhuongThucThanhToan() != null ? donHang.getPhuongThucThanhToan() : "COD";
        holder.tvPhuongThucDHQuanLy.setText("Thanh toán: " + pttt);

        String giaFormat = String.format("%,d", donHang.getTongTien()).replace(',', '.');
        holder.tvTongTienDHQuanLy.setText("Tổng tiền: " + giaFormat + " đ");

        int status = donHang.getTrangThai();
        if (status >= 0 && status < mangTrangThai.length) {
            holder.tvTrangThaiDHQuanLy.setText(mangTrangThai[status]);
        } else {
            holder.tvTrangThaiDHQuanLy.setText("Đã hủy");
        }

        if (status == 0) holder.tvTrangThaiDHQuanLy.setTextColor(Color.parseColor("#FF9800"));
        else if (status == 1) holder.tvTrangThaiDHQuanLy.setTextColor(Color.parseColor("#1A73E8"));
        else if (status == 2) holder.tvTrangThaiDHQuanLy.setTextColor(Color.parseColor("#2E7D32"));
        else holder.tvTrangThaiDHQuanLy.setTextColor(Color.parseColor("#D32F2F"));

        holder.btnCapNhatTrangThaiDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienDialogCapNhatTrangThai(donHang);
            }
        });
    }

    private void hienDialogCapNhatTrangThai(DonHang donHang) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cập nhật trạng thái ĐH #" + donHang.getMaDH());

        builder.setSingleChoiceItems(mangTrangThai, donHang.getTrangThai(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.updateTrangThaiDonHang(donHang.getMaDH(), which);
                Toast.makeText(context, "Cập nhật trạng thái thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                if (context instanceof QuanLyDonHangActivity) {
                    ((QuanLyDonHangActivity) context).taiDanhSachDonHang();
                }
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return listDonHang != null ? listDonHang.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaDHQuanLy, tvNgayDatQuanLy, tvDanhSachSanPhamQuanLy, tvThongTinKHQuanLy, tvPhuongThucDHQuanLy, tvTongTienDHQuanLy, tvTrangThaiDHQuanLy;
        Button btnCapNhatTrangThaiDH;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDHQuanLy = itemView.findViewById(R.id.tvMaDHQuanLy);
            tvNgayDatQuanLy = itemView.findViewById(R.id.tvNgayDatQuanLy);
            tvDanhSachSanPhamQuanLy = itemView.findViewById(R.id.tvDanhSachSanPhamQuanLy);
            tvThongTinKHQuanLy = itemView.findViewById(R.id.tvThongTinKHQuanLy);
            tvPhuongThucDHQuanLy = itemView.findViewById(R.id.tvPhuongThucDHQuanLy);
            tvTongTienDHQuanLy = itemView.findViewById(R.id.tvTongTienDHQuanLy);
            tvTrangThaiDHQuanLy = itemView.findViewById(R.id.tvTrangThaiDHQuanLy);
            btnCapNhatTrangThaiDH = itemView.findViewById(R.id.btnCapNhatTrangThaiDH);
        }
    }
}
