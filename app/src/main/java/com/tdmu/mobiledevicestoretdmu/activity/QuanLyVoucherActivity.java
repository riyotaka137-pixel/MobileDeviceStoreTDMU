package com.tdmu.mobiledevicestoretdmu.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.adapter.QuanLyVoucherAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.GiamGia;

import java.util.List;

public class QuanLyVoucherActivity extends AppCompatActivity {

    private RecyclerView rvQuanLyVoucher;
    private TextView tvEmptyQuanLyVoucher;
    private FloatingActionButton btnThemVoucher;
    private ImageView btnBackHeader;
    private DatabaseHelper databaseHelper;
    private QuanLyVoucherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_voucher);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        btnBackHeader.setOnClickListener(v -> finish());
        btnThemVoucher.setOnClickListener(v -> hienDialogThemSuaVoucher(null));

        taiDanhSachVoucher();
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        rvQuanLyVoucher = findViewById(R.id.rvQuanLyVoucher);
        tvEmptyQuanLyVoucher = findViewById(R.id.tvEmptyQuanLyVoucher);
        btnThemVoucher = findViewById(R.id.btnThemVoucher);
    }

    public void taiDanhSachVoucher() {
        List<GiamGia> list = databaseHelper.getAllGiamGia();

        if (list == null || list.isEmpty()) {
            tvEmptyQuanLyVoucher.setVisibility(View.VISIBLE);
            rvQuanLyVoucher.setVisibility(View.GONE);
        } else {
            tvEmptyQuanLyVoucher.setVisibility(View.GONE);
            rvQuanLyVoucher.setVisibility(View.VISIBLE);

            adapter = new QuanLyVoucherAdapter(this, list, new QuanLyVoucherAdapter.OnVoucherActionListener() {
                @Override
                public void onEdit(GiamGia gg) {
                    hienDialogThemSuaVoucher(gg);
                }

                @Override
                public void onDelete(GiamGia gg) {
                    xacNhanXoaVoucher(gg);
                }
            });

            rvQuanLyVoucher.setLayoutManager(new LinearLayoutManager(this));
            rvQuanLyVoucher.setAdapter(adapter);
        }
    }

    private void hienDialogThemSuaVoucher(GiamGia voucherToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_quan_ly_voucher, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView tvDialogTieuDeVoucher = dialogView.findViewById(R.id.tvDialogTieuDeVoucher);
        EditText edtDialogMaCode = dialogView.findViewById(R.id.edtDialogMaCode);
        EditText edtDialogPhanTramGiam = dialogView.findViewById(R.id.edtDialogPhanTramGiam);
        EditText edtDialogTienGiamToiDa = dialogView.findViewById(R.id.edtDialogTienGiamToiDa);
        EditText edtDialogDonToiThieu = dialogView.findViewById(R.id.edtDialogDonToiThieu);
        EditText edtDialogNgayHetHan = dialogView.findViewById(R.id.edtDialogNgayHetHan);
        EditText edtDialogMoTa = dialogView.findViewById(R.id.edtDialogMoTa);
        CheckBox cbDialogTrangThai = dialogView.findViewById(R.id.cbDialogTrangThai);
        Button btnDialogHuyVoucher = dialogView.findViewById(R.id.btnDialogHuyVoucher);
        Button btnDialogLuuVoucher = dialogView.findViewById(R.id.btnDialogLuuVoucher);

        boolean isEdit = (voucherToEdit != null);
        if (isEdit) {
            tvDialogTieuDeVoucher.setText("CẬP NHẬT MÃ GIẢM GIÁ");
            edtDialogMaCode.setText(voucherToEdit.getMaCode());
            edtDialogPhanTramGiam.setText(String.valueOf(voucherToEdit.getPhanTramGiam()));
            edtDialogTienGiamToiDa.setText(voucherToEdit.getTienGiamToiDa() > 0 ? String.valueOf(voucherToEdit.getTienGiamToiDa()) : "");
            edtDialogDonToiThieu.setText(voucherToEdit.getDonToiThieu() > 0 ? String.valueOf(voucherToEdit.getDonToiThieu()) : "");
            edtDialogNgayHetHan.setText(voucherToEdit.getNgayHetHan() != null ? voucherToEdit.getNgayHetHan() : "");
            edtDialogMoTa.setText(voucherToEdit.getMoTa() != null ? voucherToEdit.getMoTa() : "");
            cbDialogTrangThai.setChecked(voucherToEdit.getTrangThai() == 1);
        } else {
            tvDialogTieuDeVoucher.setText("THÊM MÃ GIẢM GIÁ MỚI");
            cbDialogTrangThai.setChecked(true);
        }

        btnDialogHuyVoucher.setOnClickListener(v -> dialog.dismiss());

        btnDialogLuuVoucher.setOnClickListener(v -> {
            String code = edtDialogMaCode.getText().toString().trim().toUpperCase();
            String phanTramStr = edtDialogPhanTramGiam.getText().toString().trim();
            String toiDaStr = edtDialogTienGiamToiDa.getText().toString().trim();
            String donToiThieuStr = edtDialogDonToiThieu.getText().toString().trim();
            String ngayHetHan = edtDialogNgayHetHan.getText().toString().trim();
            String moTa = edtDialogMoTa.getText().toString().trim();
            int trangThai = cbDialogTrangThai.isChecked() ? 1 : 0;

            if (code.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập Mã giảm giá (Code)!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phanTramStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập phần trăm giảm giá!", Toast.LENGTH_SHORT).show();
                return;
            }

            int phanTram;
            try {
                phanTram = Integer.parseInt(phanTramStr);
                if (phanTram <= 0 || phanTram > 100) {
                    Toast.makeText(this, "Phần trăm giảm giá phải từ 1 đến 100!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Phần trăm giảm giá không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            int tienGiamToiDa = 0;
            if (!toiDaStr.isEmpty()) {
                try {
                    tienGiamToiDa = Integer.parseInt(toiDaStr);
                } catch (NumberFormatException ignored) {}
            }

            int donToiThieu = 0;
            if (!donToiThieuStr.isEmpty()) {
                try {
                    donToiThieu = Integer.parseInt(donToiThieuStr);
                } catch (NumberFormatException ignored) {}
            }

            boolean thanhCong;
            if (isEdit) {
                thanhCong = databaseHelper.updateGiamGia(voucherToEdit.getMaGG(), code, phanTram, tienGiamToiDa, donToiThieu, ngayHetHan, moTa, trangThai);
            } else {
                GiamGia exist = databaseHelper.getGiamGiaByCode(code);
                if (exist != null) {
                    Toast.makeText(this, "Mã giảm giá này đã tồn tại, vui lòng dùng mã khác!", Toast.LENGTH_SHORT).show();
                    return;
                }
                thanhCong = databaseHelper.insertGiamGia(code, phanTram, tienGiamToiDa, donToiThieu, ngayHetHan, moTa, trangThai);
            }

            if (thanhCong) {
                Toast.makeText(this, isEdit ? "Cập nhật mã giảm giá thành công!" : "Thêm mã giảm giá thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                taiDanhSachVoucher();
            } else {
                Toast.makeText(this, "Thao tác thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void xacNhanXoaVoucher(GiamGia gg) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa mã giảm giá " + gg.getMaCode() + " không?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteGiamGia(gg.getMaGG());
                        Toast.makeText(QuanLyVoucherActivity.this, "Đã xóa mã giảm giá!", Toast.LENGTH_SHORT).show();
                        taiDanhSachVoucher();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
