package com.tdmu.mobiledevicestoretdmu.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.ChiTietGioHang;
import com.tdmu.mobiledevicestoretdmu.model.GiamGia;
import com.tdmu.mobiledevicestoretdmu.model.TaiKhoan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThanhToanActivity extends AppCompatActivity {

    private EditText edtHoTenThanhToan, edtSoDienThoaiThanhToan, edtDiaChiThanhToan, edtMaVoucher;
    private TextView tvTamTinhThanhToan, tvTienGiamGia, tvTongTienThanhToan;
    private Button btnApDungVoucher, btnXacNhanDatHang;
    private RadioGroup rgPhuongThucThanhToan;
    private RadioButton rbCOD, rbChuyenKhoan;
    private ImageView btnBackHeader;

    private DatabaseHelper databaseHelper;
    private List<ChiTietGioHang> listCart;
    private int maTK = -1;
    private long tongTienGoc = 0;
    private long tienGiam = 0;
    private long tongTienThanhToan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        btnBackHeader.setOnClickListener(v -> finish());

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        maTK = sharedPreferences.getInt("MaTK", -1);

        if (maTK != -1) {
            taiThongTinNguoiDung();
            tinhTongTien();
        }

        btnApDungVoucher.setOnClickListener(v -> apDungVoucher());

        btnXacNhanDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDatHang();
            }
        });
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        edtHoTenThanhToan = findViewById(R.id.edtHoTenThanhToan);
        edtSoDienThoaiThanhToan = findViewById(R.id.edtSoDienThoaiThanhToan);
        edtDiaChiThanhToan = findViewById(R.id.edtDiaChiThanhToan);

        edtMaVoucher = findViewById(R.id.edtMaVoucher);
        btnApDungVoucher = findViewById(R.id.btnApDungVoucher);
        tvTamTinhThanhToan = findViewById(R.id.tvTamTinhThanhToan);
        tvTienGiamGia = findViewById(R.id.tvTienGiamGia);
        tvTongTienThanhToan = findViewById(R.id.tvTongTienThanhToan);

        rgPhuongThucThanhToan = findViewById(R.id.rgPhuongThucThanhToan);
        rbCOD = findViewById(R.id.rbCOD);
        rbChuyenKhoan = findViewById(R.id.rbChuyenKhoan);
        btnXacNhanDatHang = findViewById(R.id.btnXacNhanDatHang);
    }

    private void taiThongTinNguoiDung() {
        TaiKhoan tk = databaseHelper.getTaiKhoanById(maTK);
        if (tk != null) {
            edtHoTenThanhToan.setText(tk.getHoTen());
            edtSoDienThoaiThanhToan.setText(tk.getSoDienThoai());
            edtDiaChiThanhToan.setText(tk.getDiaChi());
        }
    }

    private void tinhTongTien() {
        listCart = databaseHelper.getListCartItem(maTK);
        tongTienGoc = 0;
        if (listCart != null) {
            for (ChiTietGioHang ctgh : listCart) {
                tongTienGoc += (long) ctgh.getDonGia() * ctgh.getSoLuong();
            }
        }
        tienGiam = 0;
        tongTienThanhToan = tongTienGoc;

        String giaGocFormat = String.format("%,d", tongTienGoc).replace(',', '.');
        tvTamTinhThanhToan.setText(giaGocFormat + " đ");
        tvTienGiamGia.setText("-0 đ");
        tvTongTienThanhToan.setText(giaGocFormat + " đ");
    }

    private void apDungVoucher() {
        String code = edtMaVoucher.getText().toString().trim().toUpperCase();
        if (code.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã giảm giá!", Toast.LENGTH_SHORT).show();
            return;
        }

        GiamGia voucher = databaseHelper.getGiamGiaByCode(code);
        if (voucher == null) {
            Toast.makeText(this, "Mã giảm giá không tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (voucher.getTrangThai() != 1) {
            Toast.makeText(this, "Mã giảm giá này hiện đang bị khóa hoặc ngừng áp dụng!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (voucher.getNgayHetHan() != null && !voucher.getNgayHetHan().trim().isEmpty()) {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            if (today.compareTo(voucher.getNgayHetHan().trim()) > 0) {
                Toast.makeText(this, "Mã giảm giá này đã hết hạn sử dụng (" + voucher.getNgayHetHan() + ")!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (tongTienGoc < voucher.getDonToiThieu()) {
            String minFormat = String.format("%,d", voucher.getDonToiThieu()).replace(',', '.');
            Toast.makeText(this, "Đơn hàng tối thiểu phải từ " + minFormat + " đ để dùng mã này!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tính số tiền được giảm
        tienGiam = ((long) tongTienGoc * voucher.getPhanTramGiam()) / 100;
        if (voucher.getTienGiamToiDa() > 0 && tienGiam > voucher.getTienGiamToiDa()) {
            tienGiam = voucher.getTienGiamToiDa();
        }

        tongTienThanhToan = Math.max(0L, tongTienGoc - tienGiam);

        tvTienGiamGia.setText("-" + String.format("%,d", tienGiam).replace(',', '.') + " đ");
        tvTongTienThanhToan.setText(String.format("%,d", tongTienThanhToan).replace(',', '.') + " đ");

        Toast.makeText(this, "Áp dụng mã " + voucher.getMaCode() + " thành công! Giảm " + String.format("%,d", tienGiam).replace(',', '.') + " đ", Toast.LENGTH_SHORT).show();
    }

    private void xuLyDatHang() {
        String hoTen = edtHoTenThanhToan.getText().toString().trim();
        String sdt = edtSoDienThoaiThanhToan.getText().toString().trim();
        String diaChi = edtDiaChiThanhToan.getText().toString().trim();

        if (hoTen.isEmpty() || sdt.isEmpty() || diaChi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ Họ tên, Số điện thoại và Địa chỉ nhận hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (listCart == null || listCart.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng rỗng!", Toast.LENGTH_SHORT).show();
            return;
        }

        String phuongThucThanhToan = "COD";
        if (rbChuyenKhoan.isChecked()) {
            phuongThucThanhToan = "Chuyển khoản ngân hàng";
        }

        long maDH = databaseHelper.placeOrderReturnId(maTK, hoTen, sdt, diaChi, phuongThucThanhToan, tongTienThanhToan, listCart);
        if (maDH != -1) {
            // Tự động lưu thông báo đặt hàng vào bảng ThongBao
            String tongTienStr = String.format("%,d", tongTienThanhToan).replace(',', '.') + " đ";
            databaseHelper.insertThongBao(
                    maTK,
                    "Đặt hàng thành công",
                    "Đơn hàng #" + maDH + " trị giá " + tongTienStr + " đã được tạo thành công và đang chờ xác nhận."
            );

            if (rbChuyenKhoan.isChecked()) {
                // Hiển thị Dialog mã VietQR động
                hienDialogVietQR(maDH, tongTienThanhToan);
            } else {
                Toast.makeText(this, "Đặt hàng thành công! Đơn hàng đang chờ xác nhận.", Toast.LENGTH_LONG).show();
                chuyenSangLichSu();
            }
        } else {
            Toast.makeText(this, "Đặt hàng thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void hienDialogVietQR(long maDH, long tongTienThanhToan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_vietqr_thanh_toan, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        ImageView imgVietQR = dialogView.findViewById(R.id.imgVietQR);
        TextView tvVietQRMaDH = dialogView.findViewById(R.id.tvVietQRMaDH);
        TextView tvVietQRSoTien = dialogView.findViewById(R.id.tvVietQRSoTien);
        TextView tvVietQRHuongDan = dialogView.findViewById(R.id.tvVietQRHuongDan);
        Button btnVietQRXacNhan = dialogView.findViewById(R.id.btnVietQRXacNhan);

        tvVietQRMaDH.setText("Mã đơn hàng: #" + maDH);
        String tienFormat = String.format("%,d", tongTienThanhToan).replace(',', '.') + " đ";
        tvVietQRSoTien.setText("Số tiền: " + tienFormat);

        String noiDungCK = "THANHTOAN_DH_" + maDH;
        tvVietQRHuongDan.setText("Ngân hàng: MB Bank\nSố tài khoản: 0987654321\nChủ tài khoản: TDMU MOBILE STORE\nNội dung chuyển khoản: " + noiDungCK);

        String qrUrl = "https://img.vietqr.io/image/MB-0987654321-compact2.png?amount=" + tongTienThanhToan + "&addInfo=" + noiDungCK;

        Glide.with(this)
                .load(qrUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imgVietQR);

        btnVietQRXacNhan.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(this, "Cảm ơn bạn đã thanh toán! Đơn hàng đang được xác nhận.", Toast.LENGTH_LONG).show();
            chuyenSangLichSu();
        });

        dialog.show();
    }

    private void chuyenSangLichSu() {
        Intent intent = new Intent(ThanhToanActivity.this, LichSuMuaHangActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
