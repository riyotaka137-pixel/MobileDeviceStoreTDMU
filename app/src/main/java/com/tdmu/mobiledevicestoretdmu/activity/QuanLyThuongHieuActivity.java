package com.tdmu.mobiledevicestoretdmu.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import com.bumptech.glide.Glide;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.adapter.QuanLyThuongHieuAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.ThuongHieu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class QuanLyThuongHieuActivity extends AppCompatActivity {

    private RecyclerView rvQuanLyThuongHieu;
    private TextView tvEmptyQuanLyThuongHieu;
    private FloatingActionButton btnThemThuongHieu;
    private ImageView btnBackHeader;
    private DatabaseHelper databaseHelper;
    private QuanLyThuongHieuAdapter adapter;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ImageView imgCurrentPreview;
    private String pathImageSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_thuong_hieu);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        taiDanhSachThuongHieu();

        btnBackHeader.setOnClickListener(v -> finish());

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        Uri imageUri = result.getData().getData();
                        String savedFilePath = copyBrandImageToInternalStorage(imageUri);
                        if (savedFilePath != null) {
                            pathImageSelected = savedFilePath;
                            if (imgCurrentPreview != null) {
                                Glide.with(this).load(new File(savedFilePath)).into(imgCurrentPreview);
                            }
                            Toast.makeText(this, "Đã chọn ảnh thương hiệu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Không thể lưu ảnh!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        btnThemThuongHieu.setOnClickListener(v -> hienDialogThemThuongHieu());
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        rvQuanLyThuongHieu = findViewById(R.id.rvQuanLyThuongHieu);
        tvEmptyQuanLyThuongHieu = findViewById(R.id.tvEmptyQuanLyThuongHieu);
        btnThemThuongHieu = findViewById(R.id.btnThemThuongHieu);
    }

    public void moGalleryChonAnhBrand(ImageView imgPreview, String currentPath) {
        this.imgCurrentPreview = imgPreview;
        this.pathImageSelected = currentPath != null ? currentPath : "";

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    public String getPathImageSelected() {
        return pathImageSelected;
    }

    public String copyBrandImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            File storageDir = new File(getFilesDir(), "brand_images");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            String fileName = "img_brand_" + System.currentTimeMillis() + ".jpg";
            File outputFile = new File(storageDir, fileName);

            OutputStream outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            return outputFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void taiDanhSachThuongHieu() {
        List<ThuongHieu> listThuongHieu = databaseHelper.getAllThuongHieu();

        if (listThuongHieu == null || listThuongHieu.isEmpty()) {
            tvEmptyQuanLyThuongHieu.setVisibility(View.VISIBLE);
            rvQuanLyThuongHieu.setVisibility(View.GONE);
        } else {
            tvEmptyQuanLyThuongHieu.setVisibility(View.GONE);
            rvQuanLyThuongHieu.setVisibility(View.VISIBLE);

            adapter = new QuanLyThuongHieuAdapter(this, listThuongHieu);
            rvQuanLyThuongHieu.setLayoutManager(new LinearLayoutManager(this));
            rvQuanLyThuongHieu.setAdapter(adapter);
        }
    }

    private void hienDialogThemThuongHieu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_quan_ly_thuong_hieu, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView tvTieuDeDialogTH = dialogView.findViewById(R.id.tvTieuDeDialogTH);
        EditText edtTenTHDialog = dialogView.findViewById(R.id.edtTenTHDialog);
        EditText edtMoTaTHDialog = dialogView.findViewById(R.id.edtMoTaTHDialog);
        ImageView imgPreviewDialogTH = dialogView.findViewById(R.id.imgPreviewDialogTH);
        Button btnChonAnhDialogTH = dialogView.findViewById(R.id.btnChonAnhDialogTH);

        Button btnHuyDialogTH = dialogView.findViewById(R.id.btnHuyDialogTH);
        Button btnLuuDialogTH = dialogView.findViewById(R.id.btnLuuDialogTH);

        tvTieuDeDialogTH.setText("THÊM THƯƠNG HIỆU MỚI");
        pathImageSelected = "";

        btnChonAnhDialogTH.setOnClickListener(v -> moGalleryChonAnhBrand(imgPreviewDialogTH, ""));

        btnHuyDialogTH.setOnClickListener(v -> dialog.dismiss());

        btnLuuDialogTH.setOnClickListener(v -> {
            String tenTH = edtTenTHDialog.getText().toString().trim();
            String moTa = edtMoTaTHDialog.getText().toString().trim();
            String hinhAnh = pathImageSelected;

            if (tenTH.isEmpty()) {
                Toast.makeText(QuanLyThuongHieuActivity.this, "Tên thương hiệu không được để trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean thanhCong = databaseHelper.insertThuongHieu(tenTH, moTa, hinhAnh);
            if (thanhCong) {
                Toast.makeText(QuanLyThuongHieuActivity.this, "Thêm thương hiệu thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                taiDanhSachThuongHieu();
            } else {
                Toast.makeText(QuanLyThuongHieuActivity.this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
