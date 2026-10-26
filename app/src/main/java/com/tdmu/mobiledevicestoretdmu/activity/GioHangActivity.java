package com.tdmu.mobiledevicestoretdmu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.adapter.GioHangAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.ChiTietGioHang;

import java.util.List;

public class GioHangActivity extends AppCompatActivity {

    private RecyclerView rvGioHang;
    private TextView tvEmptyGioHang, tvTongTienGioHang;
    private LinearLayout layoutBottomGioHang;
    private Button btnThanhToanGioHang;
    private ImageView btnBackHeader;

    private DatabaseHelper databaseHelper;
    private GioHangAdapter gioHangAdapter;
    private List<ChiTietGioHang> listChiTiet;
    private int maTK = -1;
    private long tongTien = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        btnBackHeader.setOnClickListener(v -> finish());

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        maTK = sharedPreferences.getInt("MaTK", -1);

        taiDanhSachGioHang();

        btnThanhToanGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listChiTiet != null && !listChiTiet.isEmpty()) {
                    Intent intent = new Intent(GioHangActivity.this, ThanhToanActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(GioHangActivity.this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        rvGioHang = findViewById(R.id.rvGioHang);
        tvEmptyGioHang = findViewById(R.id.tvEmptyGioHang);
        tvTongTienGioHang = findViewById(R.id.tvTongTienGioHang);
        layoutBottomGioHang = findViewById(R.id.layoutBottomGioHang);
        btnThanhToanGioHang = findViewById(R.id.btnThanhToanGioHang);
    }

    public void taiDanhSachGioHang() {
        if (maTK == -1) return;

        listChiTiet = databaseHelper.getListCartItem(maTK);

        if (listChiTiet == null || listChiTiet.isEmpty()) {
            tvEmptyGioHang.setVisibility(View.VISIBLE);
            rvGioHang.setVisibility(View.GONE);
            layoutBottomGioHang.setVisibility(View.GONE);
        } else {
            tvEmptyGioHang.setVisibility(View.GONE);
            rvGioHang.setVisibility(View.VISIBLE);
            layoutBottomGioHang.setVisibility(View.VISIBLE);

            gioHangAdapter = new GioHangAdapter(this, listChiTiet);
            rvGioHang.setLayoutManager(new LinearLayoutManager(this));
            rvGioHang.setAdapter(gioHangAdapter);

            capNhatTongTien();
        }
    }

    public void capNhatTongTien() {
        tongTien = 0;
        if (listChiTiet != null) {
            for (ChiTietGioHang ctgh : listChiTiet) {
                tongTien += (long) ctgh.getDonGia() * ctgh.getSoLuong();
            }
        }
        String giaFormat = String.format("%,d", tongTien).replace(',', '.');
        tvTongTienGioHang.setText(giaFormat + " đ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        taiDanhSachGioHang();
    }
}
