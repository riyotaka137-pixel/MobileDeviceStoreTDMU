package com.tdmu.mobiledevicestoretdmu.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.adapter.QuanLyDanhMucAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.DanhMuc;

import java.util.List;

public class QuanLyDanhMucActivity extends AppCompatActivity {

    private RecyclerView rvQuanLyDanhMuc;
    private TextView tvEmptyQuanLyDanhMuc;
    private Button btnThemDanhMuc;
    private ImageView btnBackHeader;
    private DatabaseHelper databaseHelper;
    private QuanLyDanhMucAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_danh_muc);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        taiDanhSachDanhMuc();

        btnBackHeader.setOnClickListener(v -> finish());

        btnThemDanhMuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienDialogThemDanhMuc();
            }
        });
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        rvQuanLyDanhMuc = findViewById(R.id.rvQuanLyDanhMuc);
        tvEmptyQuanLyDanhMuc = findViewById(R.id.tvEmptyQuanLyDanhMuc);
        btnThemDanhMuc = findViewById(R.id.btnThemDanhMuc);
    }

    public void taiDanhSachDanhMuc() {
        List<DanhMuc> listDanhMuc = databaseHelper.getAllDanhMuc();

        if (listDanhMuc == null || listDanhMuc.isEmpty()) {
            tvEmptyQuanLyDanhMuc.setVisibility(View.VISIBLE);
            rvQuanLyDanhMuc.setVisibility(View.GONE);
        } else {
            tvEmptyQuanLyDanhMuc.setVisibility(View.GONE);
            rvQuanLyDanhMuc.setVisibility(View.VISIBLE);

            adapter = new QuanLyDanhMucAdapter(this, listDanhMuc);
            rvQuanLyDanhMuc.setLayoutManager(new LinearLayoutManager(this));
            rvQuanLyDanhMuc.setAdapter(adapter);
        }
    }

    private void hienDialogThemDanhMuc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("THÊM DANH MỤC MỚI");

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(40, 20, 40, 20);

        final EditText edtTenDM = new EditText(this);
        edtTenDM.setHint("Tên danh mục *");

        final EditText edtMoTaDM = new EditText(this);
        edtMoTaDM.setHint("Mô tả danh mục");

        container.addView(edtTenDM);
        container.addView(edtMoTaDM);

        builder.setView(container);

        builder.setPositiveButton("THÊM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tenDM = edtTenDM.getText().toString().trim();
                String moTa = edtMoTaDM.getText().toString().trim();

                if (tenDM.isEmpty()) {
                    Toast.makeText(QuanLyDanhMucActivity.this, "Tên danh mục không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                databaseHelper.insertDanhMuc(tenDM, moTa);
                Toast.makeText(QuanLyDanhMucActivity.this, "Thêm danh mục thành công!", Toast.LENGTH_SHORT).show();
                taiDanhSachDanhMuc();
            }
        });

        builder.setNegativeButton("HỦY", null);
        builder.show();
    }
}
