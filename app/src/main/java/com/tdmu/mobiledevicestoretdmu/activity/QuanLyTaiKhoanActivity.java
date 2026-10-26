package com.tdmu.mobiledevicestoretdmu.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.adapter.QuanLyTaiKhoanAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.TaiKhoan;

import java.util.List;

public class QuanLyTaiKhoanActivity extends AppCompatActivity {

    private RecyclerView rvQuanLyTaiKhoan;
    private TextView tvEmptyQuanLyTaiKhoan;
    private ImageView btnBackHeader;
    private DatabaseHelper databaseHelper;
    private QuanLyTaiKhoanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_tai_khoan);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        btnBackHeader.setOnClickListener(v -> finish());

        taiDanhSachTaiKhoan();
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        rvQuanLyTaiKhoan = findViewById(R.id.rvQuanLyTaiKhoan);
        tvEmptyQuanLyTaiKhoan = findViewById(R.id.tvEmptyQuanLyTaiKhoan);
    }

    public void taiDanhSachTaiKhoan() {
        List<TaiKhoan> listKhachHang = databaseHelper.getAllKhachHang();

        if (listKhachHang == null || listKhachHang.isEmpty()) {
            tvEmptyQuanLyTaiKhoan.setVisibility(View.VISIBLE);
            rvQuanLyTaiKhoan.setVisibility(View.GONE);
        } else {
            tvEmptyQuanLyTaiKhoan.setVisibility(View.GONE);
            rvQuanLyTaiKhoan.setVisibility(View.VISIBLE);

            adapter = new QuanLyTaiKhoanAdapter(this, listKhachHang, new QuanLyTaiKhoanAdapter.OnTaiKhoanClickListener() {
                @Override
                public void onTaiKhoanClick(TaiKhoan tk) {
                    hienDialogChiTietTaiKhoan(tk);
                }
            });

            rvQuanLyTaiKhoan.setLayoutManager(new LinearLayoutManager(this));
            rvQuanLyTaiKhoan.setAdapter(adapter);
        }
    }

    private void hienDialogChiTietTaiKhoan(TaiKhoan tk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_chi_tiet_tai_khoan, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView tvDialogMaTK = dialogView.findViewById(R.id.tvDialogMaTK);
        TextView tvDialogHoTen = dialogView.findViewById(R.id.tvDialogHoTen);
        TextView tvDialogEmail = dialogView.findViewById(R.id.tvDialogEmail);
        TextView tvDialogSoDienThoai = dialogView.findViewById(R.id.tvDialogSoDienThoai);
        TextView tvDialogDiaChi = dialogView.findViewById(R.id.tvDialogDiaChi);
        TextView tvDialogTrangThai = dialogView.findViewById(R.id.tvDialogTrangThai);

        Button btnDialogKhoaMoKhoa = dialogView.findViewById(R.id.btnDialogKhoaMoKhoa);
        Button btnDialogXoaTK = dialogView.findViewById(R.id.btnDialogXoaTK);
        Button btnDialogDong = dialogView.findViewById(R.id.btnDialogDong);

        tvDialogMaTK.setText("Mã tài khoản: #" + tk.getMaTK());
        tvDialogHoTen.setText("Họ tên: " + tk.getHoTen());
        tvDialogEmail.setText("Email: " + tk.getEmail());

        String sdt = (tk.getSoDienThoai() != null && !tk.getSoDienThoai().trim().isEmpty()) ? tk.getSoDienThoai() : "Chưa cập nhật";
        tvDialogSoDienThoai.setText("Số điện thoại: " + sdt);

        String diaChi = (tk.getDiaChi() != null && !tk.getDiaChi().trim().isEmpty()) ? tk.getDiaChi() : "Chưa cập nhật";
        tvDialogDiaChi.setText("Địa chỉ: " + diaChi);

        if (tk.getTrangThai() == 1) {
            tvDialogTrangThai.setText("Trạng thái: Hoạt động");
            tvDialogTrangThai.setTextColor(Color.parseColor("#2E7D32"));

            btnDialogKhoaMoKhoa.setText("KHÓA TÀI KHOẢN");
            btnDialogKhoaMoKhoa.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F57C00")));
        } else {
            tvDialogTrangThai.setText("Trạng thái: Đã khóa");
            tvDialogTrangThai.setTextColor(Color.parseColor("#D32F2F"));

            btnDialogKhoaMoKhoa.setText("MỞ KHÓA TÀI KHOẢN");
            btnDialogKhoaMoKhoa.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#388E3C")));
        }

        btnDialogKhoaMoKhoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newStatus = (tk.getTrangThai() == 1) ? 0 : 1;
                boolean isUpdated = databaseHelper.updateTrangThaiTaiKhoan(tk.getMaTK(), newStatus);
                if (isUpdated) {
                    String msg = (newStatus == 0) ? "Đã khóa tài khoản thành công!" : "Đã mở khóa tài khoản thành công!";
                    Toast.makeText(QuanLyTaiKhoanActivity.this, msg, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    taiDanhSachTaiKhoan();
                } else {
                    Toast.makeText(QuanLyTaiKhoanActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDialogXoaTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(QuanLyTaiKhoanActivity.this)
                        .setTitle("Xác nhận xóa tài khoản")
                        .setMessage("Bạn có chắc chắn muốn xóa tài khoản của " + tk.getHoTen() + " không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean isDeleted = databaseHelper.deleteTaiKhoan(tk.getMaTK());
                                if (isDeleted) {
                                    Toast.makeText(QuanLyTaiKhoanActivity.this, "Đã xóa tài khoản!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    taiDanhSachTaiKhoan();
                                } else {
                                    Toast.makeText(QuanLyTaiKhoanActivity.this, "Xóa tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });

        btnDialogDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
