package com.tdmu.mobiledevicestoretdmu.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.adapter.LichSuDonHangAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.DonHang;

import java.util.List;

public class LichSuMuaHangActivity extends AppCompatActivity {

    private RecyclerView rvLichSuDonHang;
    private TextView tvEmptyLichSuDonHang;
    private ImageView btnBackHeader;
    private DatabaseHelper databaseHelper;
    private LichSuDonHangAdapter adapter;
    private int maTK = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_mua_hang);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        btnBackHeader.setOnClickListener(v -> finish());

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        maTK = sharedPreferences.getInt("MaTK", -1);

        taiLichSuDonHang();
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        rvLichSuDonHang = findViewById(R.id.rvLichSuDonHang);
        tvEmptyLichSuDonHang = findViewById(R.id.tvEmptyLichSuDonHang);
    }

    public void taiLichSuDonHang() {
        if (maTK == -1) return;

        List<DonHang> listDonHang = databaseHelper.getDonHangByMaTK(maTK);
        if (listDonHang == null || listDonHang.isEmpty()) {
            tvEmptyLichSuDonHang.setVisibility(View.VISIBLE);
            rvLichSuDonHang.setVisibility(View.GONE);
        } else {
            tvEmptyLichSuDonHang.setVisibility(View.GONE);
            rvLichSuDonHang.setVisibility(View.VISIBLE);

            adapter = new LichSuDonHangAdapter(this, listDonHang);
            rvLichSuDonHang.setLayoutManager(new LinearLayoutManager(this));
            rvLichSuDonHang.setAdapter(adapter);
        }
    }
}
