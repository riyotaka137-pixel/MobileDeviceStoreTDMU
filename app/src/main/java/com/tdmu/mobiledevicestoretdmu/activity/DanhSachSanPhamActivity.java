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

public class DanhSachSanPhamActivity extends AppCompatActivity {

    private TextView tvTieuDeDanhSachSanPham, tvEmptyDanhSachSanPham;
    private ImageView btnBackHeader;
    private RecyclerView rvDanhSachSanPham;
    private DatabaseHelper databaseHelper;
    private SanPhamAdapter sanPhamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_san_pham);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        btnBackHeader.setOnClickListener(v -> finish());

        int maDanhMuc = getIntent().getIntExtra("MaDanhMuc", -1);
        String tenDanhMuc = getIntent().getStringExtra("TenDanhMuc");

        if (tenDanhMuc != null && !tenDanhMuc.isEmpty()) {
            tvTieuDeDanhSachSanPham.setText(tenDanhMuc.toUpperCase());
        }

        taiSanPhamTheoDanhMuc(maDanhMuc);
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        tvTieuDeDanhSachSanPham = findViewById(R.id.tvTieuDeDanhSachSanPham);
        tvEmptyDanhSachSanPham = findViewById(R.id.tvEmptyDanhSachSanPham);
        rvDanhSachSanPham = findViewById(R.id.rvDanhSachSanPham);
    }

    private void taiSanPhamTheoDanhMuc(int maDanhMuc) {
        List<SanPham> listSanPham;
        if (maDanhMuc == -1) {
            listSanPham = databaseHelper.getAllSanPham();
        } else {
            listSanPham = databaseHelper.getSanPhamTheoDanhMuc(maDanhMuc);
        }

        if (listSanPham == null || listSanPham.isEmpty()) {
            tvEmptyDanhSachSanPham.setVisibility(View.VISIBLE);
            rvDanhSachSanPham.setVisibility(View.GONE);
        } else {
            tvEmptyDanhSachSanPham.setVisibility(View.GONE);
            rvDanhSachSanPham.setVisibility(View.VISIBLE);

            sanPhamAdapter = new SanPhamAdapter(this, listSanPham);
            rvDanhSachSanPham.setLayoutManager(new GridLayoutManager(this, 2));
            rvDanhSachSanPham.setAdapter(sanPhamAdapter);
        }
    }
}
