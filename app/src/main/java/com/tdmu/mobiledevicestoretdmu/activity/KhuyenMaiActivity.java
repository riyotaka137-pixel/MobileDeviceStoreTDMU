package com.tdmu.mobiledevicestoretdmu.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.adapter.SanPhamAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.SanPham;

import java.util.List;

public class KhuyenMaiActivity extends AppCompatActivity {

    private TextView tvTieuDeKhuyenMai, tvEmptyKhuyenMai;
    private ImageView btnBackHeader;
    private RecyclerView rvKhuyenMaiSanPham;
    private DatabaseHelper databaseHelper;
    private SanPhamAdapter sanPhamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khuyen_mai);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        btnBackHeader.setOnClickListener(v -> finish());

        String tenBanner = getIntent().getStringExtra("TenBanner");
        if (tenBanner != null && !tenBanner.trim().isEmpty()) {
            tvTieuDeKhuyenMai.setText(tenBanner.toUpperCase());
        }

        taiSanPhamKhuyenMai();
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        tvTieuDeKhuyenMai = findViewById(R.id.tvTieuDeKhuyenMai);
        tvEmptyKhuyenMai = findViewById(R.id.tvEmptyKhuyenMai);
        rvKhuyenMaiSanPham = findViewById(R.id.rvKhuyenMaiSanPham);
    }

    private void taiSanPhamKhuyenMai() {
        List<SanPham> listSanPham = databaseHelper.getSanPhamKhuyenMai();

        // Nếu chưa có SP có PhanTramGiam > 0 -> Lấy tất cả SP
        if (listSanPham == null || listSanPham.isEmpty()) {
            listSanPham = databaseHelper.getAllSanPham();
        }

        if (listSanPham == null || listSanPham.isEmpty()) {
            tvEmptyKhuyenMai.setVisibility(View.VISIBLE);
            rvKhuyenMaiSanPham.setVisibility(View.GONE);
        } else {
            tvEmptyKhuyenMai.setVisibility(View.GONE);
            rvKhuyenMaiSanPham.setVisibility(View.VISIBLE);

            sanPhamAdapter = new SanPhamAdapter(this, listSanPham);
            rvKhuyenMaiSanPham.setLayoutManager(new GridLayoutManager(this, 2));
            rvKhuyenMaiSanPham.setAdapter(sanPhamAdapter);
        }
    }
}
