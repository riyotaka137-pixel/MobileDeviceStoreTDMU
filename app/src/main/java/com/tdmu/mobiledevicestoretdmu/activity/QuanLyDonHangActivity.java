package com.tdmu.mobiledevicestoretdmu.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.adapter.QuanLyDonHangAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.DonHang;

import java.util.List;

public class QuanLyDonHangActivity extends AppCompatActivity {

    private RecyclerView rvQuanLyDonHang;
    private TextView tvEmptyQuanLyDonHang;
    private ImageView btnBackHeader;
    private DatabaseHelper databaseHelper;
    private QuanLyDonHangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_don_hang);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        btnBackHeader.setOnClickListener(v -> finish());

        taiDanhSachDonHang();
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        rvQuanLyDonHang = findViewById(R.id.rvQuanLyDonHang);
        tvEmptyQuanLyDonHang = findViewById(R.id.tvEmptyQuanLyDonHang);
    }

    public void taiDanhSachDonHang() {
        List<DonHang> listDonHang = databaseHelper.getAllDonHang();

        if (listDonHang == null || listDonHang.isEmpty()) {
            tvEmptyQuanLyDonHang.setVisibility(View.VISIBLE);
            rvQuanLyDonHang.setVisibility(View.GONE);
        } else {
            tvEmptyQuanLyDonHang.setVisibility(View.GONE);
            rvQuanLyDonHang.setVisibility(View.VISIBLE);

            adapter = new QuanLyDonHangAdapter(this, listDonHang);
            rvQuanLyDonHang.setLayoutManager(new LinearLayoutManager(this));
            rvQuanLyDonHang.setAdapter(adapter);
        }
    }
}
