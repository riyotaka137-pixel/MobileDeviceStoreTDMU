package com.tdmu.mobiledevicestoretdmu.activity;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.adapter.QuanLySanPhamAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.SanPham;

public class ChiTietSanPhamActivity extends AppCompatActivity {

    private ImageView imgChiTietSP, btnBackHeader;
    private TextView tvTenChiTietSP, tvThuongHieuChiTietSP, tvGiaChiTietSP, tvKhoChiTietSP, tvMoTaChiTietSP;
    private Button btnThemVaoGioHang;

    private DatabaseHelper databaseHelper;
    private SanPham sanPham;
    private int maSP = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        btnBackHeader.setOnClickListener(v -> finish());

        maSP = getIntent().getIntExtra("MaSP", -1);
        if (maSP != -1) {
            taiThongTinSanPham();
        }

        btnThemVaoGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themVaoGioHang();
            }
        });
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        imgChiTietSP = findViewById(R.id.imgChiTietSP);
        tvTenChiTietSP = findViewById(R.id.tvTenChiTietSP);
        tvThuongHieuChiTietSP = findViewById(R.id.tvThuongHieuChiTietSP);
        tvGiaChiTietSP = findViewById(R.id.tvGiaChiTietSP);
        tvKhoChiTietSP = findViewById(R.id.tvKhoChiTietSP);
        tvMoTaChiTietSP = findViewById(R.id.tvMoTaChiTietSP);
        btnThemVaoGioHang = findViewById(R.id.btnThemVaoGioHang);
    }

    private void taiThongTinSanPham() {
        sanPham = databaseHelper.getSanPhamById(maSP);
        if (sanPham != null) {
            tvTenChiTietSP.setText(sanPham.getTenSP());
            tvThuongHieuChiTietSP.setText("Thương hiệu: " + (sanPham.getThuongHieu() != null ? sanPham.getThuongHieu() : "Chưa cập nhật"));

            String giaFormat = String.format("%,d", sanPham.getGia()).replace(',', '.');
            tvGiaChiTietSP.setText(giaFormat + " đ");

            // Load hình ảnh (hỗ trợ cả đường dẫn Internal Storage và tên Drawable)
            QuanLySanPhamAdapter.loadHinhAnhSanPham(this, sanPham.getHinhAnh(), imgChiTietSP);

            if (sanPham.getSoLuong() <= 0) {
                tvKhoChiTietSP.setText("Số lượng tồn kho: HẾT HÀNG");
                tvKhoChiTietSP.setTextColor(Color.parseColor("#D32F2F"));
                btnThemVaoGioHang.setText("HẾT HÀNG");
                btnThemVaoGioHang.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#757575")));
                btnThemVaoGioHang.setEnabled(false);
            } else {
                tvKhoChiTietSP.setText("Số lượng tồn kho: " + sanPham.getSoLuong());
                tvKhoChiTietSP.setTextColor(Color.parseColor("#388E3C"));
                btnThemVaoGioHang.setText("THÊM VÀO GIỎ HÀNG");
                btnThemVaoGioHang.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#111111")));
                btnThemVaoGioHang.setEnabled(true);
            }

            tvMoTaChiTietSP.setText(sanPham.getMoTa() != null && !sanPham.getMoTa().isEmpty() ? sanPham.getMoTa() : "Chưa có mô tả cho sản phẩm này.");
        }
    }

    private void themVaoGioHang() {
        if (sanPham == null) return;

        if (sanPham.getSoLuong() <= 0) {
            Toast.makeText(this, "Sản phẩm này hiện đã hết hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int maTK = sharedPreferences.getInt("MaTK", -1);

        if (maTK == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean thanhCong = databaseHelper.themVaoGioHang(maTK, sanPham.getMaSP(), sanPham.getGia());
        if (thanhCong) {
            Toast.makeText(this, "Đã thêm " + sanPham.getTenSP() + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            taiThongTinSanPham();
        } else {
            Toast.makeText(this, "Thêm vào giỏ hàng thất bại (có thể do vượt quá tồn kho)!", Toast.LENGTH_SHORT).show();
        }
    }
}
