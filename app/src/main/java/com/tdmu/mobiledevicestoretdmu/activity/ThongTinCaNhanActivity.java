package com.tdmu.mobiledevicestoretdmu.activity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.TaiKhoan;

public class ThongTinCaNhanActivity extends AppCompatActivity {

    private EditText edtEmailCaNhan, edtHoTenCaNhan, edtSoDienThoaiCaNhan, edtDiaChiCaNhan;
    private Button btnCapNhatThongTin, btnMoDialogDoiMatKhau;
    private ImageView btnBackHeader;
    private DatabaseHelper databaseHelper;
    private int maTK = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ca_nhan);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        btnBackHeader.setOnClickListener(v -> finish());

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        maTK = sharedPreferences.getInt("MaTK", -1);

        if (maTK != -1) {
            taiThongTinCaNhan();
        }

        btnCapNhatThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capNhatThongTin();
            }
        });

        btnMoDialogDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienDialogDoiMatKhau();
            }
        });
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        edtEmailCaNhan = findViewById(R.id.edtEmailCaNhan);
        edtHoTenCaNhan = findViewById(R.id.edtHoTenCaNhan);
        edtSoDienThoaiCaNhan = findViewById(R.id.edtSoDienThoaiCaNhan);
        edtDiaChiCaNhan = findViewById(R.id.edtDiaChiCaNhan);
        btnCapNhatThongTin = findViewById(R.id.btnCapNhatThongTin);
        btnMoDialogDoiMatKhau = findViewById(R.id.btnMoDialogDoiMatKhau);
    }

    private void taiThongTinCaNhan() {
        TaiKhoan taiKhoan = databaseHelper.getTaiKhoanById(maTK);
        if (taiKhoan != null) {
            edtEmailCaNhan.setText(taiKhoan.getEmail());
            edtHoTenCaNhan.setText(taiKhoan.getHoTen());
            edtSoDienThoaiCaNhan.setText(taiKhoan.getSoDienThoai());
            edtDiaChiCaNhan.setText(taiKhoan.getDiaChi());
        }
    }

    private void capNhatThongTin() {
        String hoTen = edtHoTenCaNhan.getText().toString().trim();
        String sdt = edtSoDienThoaiCaNhan.getText().toString().trim();
        String diaChi = edtDiaChiCaNhan.getText().toString().trim();

        if (hoTen.isEmpty()) {
            Toast.makeText(this, "Họ và tên không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean thanhCong = databaseHelper.updateThongTinCaNhan(maTK, hoTen, sdt, diaChi);
        if (thanhCong) {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("HoTen", hoTen);
            editor.apply();

            Toast.makeText(this, "Cập nhật thông tin cá nhân thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void hienDialogDoiMatKhau() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_doi_mat_khau, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        EditText edtMatKhauCu = view.findViewById(R.id.edtMatKhauCu);
        EditText edtMatKhauMoi = view.findViewById(R.id.edtMatKhauMoi);
        EditText edtXacNhanMatKhauMoi = view.findViewById(R.id.edtXacNhanMatKhauMoi);

        Button btnHuyDoiMatKhau = view.findViewById(R.id.btnHuyDoiMatKhau);
        Button btnLuuDoiMatKhau = view.findViewById(R.id.btnLuuDoiMatKhau);

        btnHuyDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuuDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passCu = edtMatKhauCu.getText().toString().trim();
                String passMoi = edtMatKhauMoi.getText().toString().trim();
                String xacNhan = edtXacNhanMatKhauMoi.getText().toString().trim();

                if (passCu.isEmpty() || passMoi.isEmpty() || xacNhan.isEmpty()) {
                    Toast.makeText(ThongTinCaNhanActivity.this, "Vui lòng nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!passMoi.equals(xacNhan)) {
                    Toast.makeText(ThongTinCaNhanActivity.this, "Mật khẩu mới không trùng khớp!", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean doiThanhCong = databaseHelper.doiMatKhau(maTK, passCu, passMoi);
                if (doiThanhCong) {
                    Toast.makeText(ThongTinCaNhanActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(ThongTinCaNhanActivity.this, "Mật khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
}
