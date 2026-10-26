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

public class TimKiemSanPhamActivity extends AppCompatActivity {

    private TextView tvTieuDeTimKiem, tvEmptyTimKiem;
    private ImageView btnBackHeader;
    private RecyclerView rvTimKiemSanPham;
    private DatabaseHelper databaseHelper;
    private SanPhamAdapter sanPhamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem_san_pham);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        btnBackHeader.setOnClickListener(v -> finish());

        String tuKhoa = getIntent().getStringExtra("TuKhoa");
        if (tuKhoa != null && !tuKhoa.isEmpty()) {
            tvTieuDeTimKiem.setText("KẾT QUẢ TÌM KIẾM: " + tuKhoa.toUpperCase());
            timKiemSanPham(tuKhoa);
        }
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        tvTieuDeTimKiem = findViewById(R.id.tvTieuDeTimKiem);
        tvEmptyTimKiem = findViewById(R.id.tvEmptyTimKiem);
        rvTimKiemSanPham = findViewById(R.id.rvTimKiemSanPham);
    }

    private void timKiemSanPham(String tuKhoa) {
        List<SanPham> listSanPham = databaseHelper.searchSanPham(tuKhoa);

        if (listSanPham == null || listSanPham.isEmpty()) {
            tvEmptyTimKiem.setVisibility(View.VISIBLE);
            rvTimKiemSanPham.setVisibility(View.GONE);
        } else {
            tvEmptyTimKiem.setVisibility(View.GONE);
            rvTimKiemSanPham.setVisibility(View.VISIBLE);

            sanPhamAdapter = new SanPhamAdapter(this, listSanPham);
            rvTimKiemSanPham.setLayoutManager(new GridLayoutManager(this, 2));
            rvTimKiemSanPham.setAdapter(sanPhamAdapter);
        }
    }
}
