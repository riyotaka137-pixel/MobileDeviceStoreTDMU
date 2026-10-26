package com.tdmu.mobiledevicestoretdmu.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;

public class DangKyActivity extends AppCompatActivity {

    private EditText edtHoTenDangKy, edtEmailDangKy, edtMatKhauDangKy, edtSoDienThoaiDangKy, edtDiaChiDangKy;
    private Button btnDangKy;
    private TextView tvChuyenSangDangNhap;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDangKy();
            }
        });

        tvChuyenSangDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhXa() {
        edtHoTenDangKy = findViewById(R.id.edtHoTenDangKy);
        edtEmailDangKy = findViewById(R.id.edtEmailDangKy);
        edtMatKhauDangKy = findViewById(R.id.edtMatKhauDangKy);
        edtSoDienThoaiDangKy = findViewById(R.id.edtSoDienThoaiDangKy);
        edtDiaChiDangKy = findViewById(R.id.edtDiaChiDangKy);
        btnDangKy = findViewById(R.id.btnDangKy);
        tvChuyenSangDangNhap = findViewById(R.id.tvChuyenSangDangNhap);
    }

    private void xuLyDangKy() {
        String hoTen = edtHoTenDangKy.getText().toString().trim();
        String email = edtEmailDangKy.getText().toString().trim();
        String matKhau = edtMatKhauDangKy.getText().toString().trim();
        String sdt = edtSoDienThoaiDangKy.getText().toString().trim();
        String diaChi = edtDiaChiDangKy.getText().toString().trim();

        if (hoTen.isEmpty() || email.isEmpty() || matKhau.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ các trường bắt buộc (*)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.checkEmailExist(email)) {
            Toast.makeText(this, "Email này đã được đăng ký! Vui lòng dùng Email khác.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean thanhCong = databaseHelper.insertTaiKhoan(hoTen, email, matKhau, sdt, diaChi);
        if (thanhCong) {
            Toast.makeText(this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }
}
