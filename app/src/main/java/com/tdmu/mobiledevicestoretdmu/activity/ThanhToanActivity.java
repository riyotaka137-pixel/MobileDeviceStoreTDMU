package com.tdmu.mobiledevicestoretdmu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.ChiTietGioHang;
import com.tdmu.mobiledevicestoretdmu.model.TaiKhoan;

import java.util.List;

public class ThanhToanActivity extends AppCompatActivity {

    private EditText edtHoTenThanhToan, edtSoDienThoaiThanhToan, edtDiaChiThanhToan;
    private TextView tvTongTienThanhToan;
    private RadioGroup rgPhuongThucThanhToan;
    private RadioButton rbCOD, rbChuyenKhoan;
    private Button btnXacNhanDatHang;
    private ImageView btnBackHeader;

    private DatabaseHelper databaseHelper;
    private List<ChiTietGioHang> listCart;
    private int maTK = -1;
    private long tongTien = 0;

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
        tongTien = 0;
        if (listCart != null) {
            for (ChiTietGioHang ctgh : listCart) {
                tongTien += (long) ctgh.getDonGia() * ctgh.getSoLuong();
            }
        }
        String giaFormat = String.format("%,d", tongTien).replace(',', '.');
        tvTongTienThanhToan.setText(giaFormat + " đ");
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

        boolean thanhCong = databaseHelper.placeOrder(maTK, hoTen, sdt, diaChi, phuongThucThanhToan, (int) tongTien, listCart);
        if (thanhCong) {
            Toast.makeText(this, "Đặt hàng thành công! Đơn hàng đang chờ xác nhận.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ThanhToanActivity.this, LichSuMuaHangActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Đặt hàng thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }
}
