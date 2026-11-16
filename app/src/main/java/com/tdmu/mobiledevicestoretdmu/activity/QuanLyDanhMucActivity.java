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
import com.tdmu.mobiledevicestoretdmu.adapter.QuanLyDanhMucAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.DanhMuc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class QuanLyDanhMucActivity extends AppCompatActivity {

    private RecyclerView rvQuanLyDanhMuc;
    private TextView tvEmptyQuanLyDanhMuc;
    private FloatingActionButton btnThemDanhMuc;
    private ImageView btnBackHeader;
    private DatabaseHelper databaseHelper;
    private QuanLyDanhMucAdapter adapter;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ImageView imgCurrentPreview;
    private String pathImageSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_danh_muc);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        taiDanhSachDanhMuc();

        btnBackHeader.setOnClickListener(v -> finish());

        // Đăng ký ActivityResultLauncher để chọn ảnh từ Gallery cho Danh Mục
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        Uri imageUri = result.getData().getData();
                        String savedFilePath = copyCategoryImageToInternalStorage(imageUri);
                        if (savedFilePath != null) {
                            pathImageSelected = savedFilePath;
                            if (imgCurrentPreview != null) {
                                Glide.with(this).load(new File(savedFilePath)).into(imgCurrentPreview);
                            }
                            Toast.makeText(this, "Đã chọn ảnh danh mục từ thư viện!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Không thể lưu ảnh!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        btnThemDanhMuc.setOnClickListener(v -> hienDialogThemDanhMuc());
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        rvQuanLyDanhMuc = findViewById(R.id.rvQuanLyDanhMuc);
        tvEmptyQuanLyDanhMuc = findViewById(R.id.tvEmptyQuanLyDanhMuc);
        btnThemDanhMuc = findViewById(R.id.btnThemDanhMuc);
    }

    public void moGalleryChonAnhCategory(ImageView imgPreview, String currentPath) {
        this.imgCurrentPreview = imgPreview;
        this.pathImageSelected = currentPath != null ? currentPath : "";

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    public String getPathImageSelected() {
        return pathImageSelected;
    }

    public String copyCategoryImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            File storageDir = new File(getFilesDir(), "category_images");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            String fileName = "img_cat_" + System.currentTimeMillis() + ".jpg";
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
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_quan_ly_danh_muc, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView tvTieuDeDialogDM = dialogView.findViewById(R.id.tvTieuDeDialogDM);
        EditText edtTenDMDialog = dialogView.findViewById(R.id.edtTenDMDialog);
        EditText edtMoTaDMDialog = dialogView.findViewById(R.id.edtMoTaDMDialog);
        ImageView imgPreviewDialogDM = dialogView.findViewById(R.id.imgPreviewDialogDM);
        Button btnChonAnhDialogDM = dialogView.findViewById(R.id.btnChonAnhDialogDM);

        Button btnHuyDialogDM = dialogView.findViewById(R.id.btnHuyDialogDM);
        Button btnLuuDialogDM = dialogView.findViewById(R.id.btnLuuDialogDM);

        tvTieuDeDialogDM.setText("THÊM DANH MỤC MỚI");
        pathImageSelected = "";

        btnChonAnhDialogDM.setOnClickListener(v -> moGalleryChonAnhCategory(imgPreviewDialogDM, ""));

        btnHuyDialogDM.setOnClickListener(v -> dialog.dismiss());

        btnLuuDialogDM.setOnClickListener(v -> {
            String tenDM = edtTenDMDialog.getText().toString().trim();
            String moTa = edtMoTaDMDialog.getText().toString().trim();
            String hinhAnh = pathImageSelected;

            if (tenDM.isEmpty()) {
                Toast.makeText(QuanLyDanhMucActivity.this, "Tên danh mục không được để trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            databaseHelper.insertDanhMuc(tenDM, moTa, hinhAnh);
            Toast.makeText(QuanLyDanhMucActivity.this, "Thêm danh mục thành công!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            taiDanhSachDanhMuc();
        });

        dialog.show();
    }
}
