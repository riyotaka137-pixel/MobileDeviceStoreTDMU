package com.tdmu.mobiledevicestoretdmu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.TaiKhoan;

public class DangNhapActivity extends AppCompatActivity {

    private EditText edtEmailDangNhap, edtMatKhauDangNhap;
    private Button btnDangNhap;
    private TextView tvChuyenSangDangKy;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDangNhap();
            }
        });

        tvChuyenSangDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void anhXa() {
        edtEmailDangNhap = findViewById(R.id.edtEmailDangNhap);
        edtMatKhauDangNhap = findViewById(R.id.edtMatKhauDangNhap);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        tvChuyenSangDangKy = findViewById(R.id.tvChuyenSangDangKy);
    }

    private void xuLyDangNhap() {
        String email = edtEmailDangNhap.getText().toString().trim();
        String matKhau = edtMatKhauDangNhap.getText().toString().trim();

        if (email.isEmpty() || matKhau.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Email và Mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        TaiKhoan taiKhoan = databaseHelper.checkLogin(email, matKhau);
        if (taiKhoan != null) {
            if (taiKhoan.getTrangThai() == 0) {
                Toast.makeText(this, "Tài khoản của bạn đã bị khóa! Vui lòng liên hệ Admin.", Toast.LENGTH_LONG).show();
                return;
            }

            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("MaTK", taiKhoan.getMaTK());
            editor.putString("HoTen", taiKhoan.getHoTen());
            editor.putInt("Quyen", taiKhoan.getQuyen());
            editor.apply();

            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

            if (taiKhoan.getQuyen() == 1) {
                Intent intent = new Intent(DangNhapActivity.this, TrangChuAdminActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(DangNhapActivity.this, TrangChuUserActivity.class);
                startActivity(intent);
            }
            finish();
        } else {
            Toast.makeText(this, "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
        }
    }
}
