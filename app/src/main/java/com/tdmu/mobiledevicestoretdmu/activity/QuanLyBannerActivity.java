package com.tdmu.mobiledevicestoretdmu.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.tdmu.mobiledevicestoretdmu.adapter.QuanLyBannerAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.Banner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class QuanLyBannerActivity extends AppCompatActivity {

    private RecyclerView rvQuanLyBanner;
    private TextView tvEmptyQuanLyBanner;
    private FloatingActionButton btnThemBanner;
    private ImageView btnBackHeader;
    private DatabaseHelper databaseHelper;
    private QuanLyBannerAdapter adapter;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ImageView imgCurrentPreview;
    private String pathImageSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_banner);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        taiDanhSachBanner();

        btnBackHeader.setOnClickListener(v -> finish());

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        Uri imageUri = result.getData().getData();
                        String savedFilePath = copyBannerImageToInternalStorage(imageUri);
                        if (savedFilePath != null) {
                            pathImageSelected = savedFilePath;
                            if (imgCurrentPreview != null) {
                                Bitmap bitmap = BitmapFactory.decodeFile(savedFilePath);
                                if (bitmap != null) {
                                    imgCurrentPreview.setImageBitmap(bitmap);
                                }
                            }
                            Toast.makeText(this, "Đã chọn ảnh banner!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Không thể lưu ảnh!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        btnThemBanner.setOnClickListener(v -> hienDialogThemBanner());
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        rvQuanLyBanner = findViewById(R.id.rvQuanLyBanner);
        tvEmptyQuanLyBanner = findViewById(R.id.tvEmptyQuanLyBanner);
        btnThemBanner = findViewById(R.id.btnThemBanner);
    }

    public void moGalleryChonAnhBanner(ImageView imgPreview, String currentPath) {
        this.imgCurrentPreview = imgPreview;
        this.pathImageSelected = currentPath != null ? currentPath : "";

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    public String getPathImageSelected() {
        return pathImageSelected;
    }

    public String copyBannerImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            File storageDir = new File(getFilesDir(), "banner_images");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            String fileName = "img_banner_" + System.currentTimeMillis() + ".jpg";
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

    public void taiDanhSachBanner() {
        List<Banner> listBanner = databaseHelper.getAllBanner();

        if (listBanner == null || listBanner.isEmpty()) {
            tvEmptyQuanLyBanner.setVisibility(View.VISIBLE);
            rvQuanLyBanner.setVisibility(View.GONE);
        } else {
            tvEmptyQuanLyBanner.setVisibility(View.GONE);
            rvQuanLyBanner.setVisibility(View.VISIBLE);

            adapter = new QuanLyBannerAdapter(this, listBanner);
            rvQuanLyBanner.setLayoutManager(new LinearLayoutManager(this));
            rvQuanLyBanner.setAdapter(adapter);
        }
    }

    private void hienDialogThemBanner() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_quan_ly_banner, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView tvTieuDeDialogBN = dialogView.findViewById(R.id.tvTieuDeDialogBN);
        EditText edtTenBNDialog = dialogView.findViewById(R.id.edtTenBNDialog);
        EditText edtMoTaBNDialog = dialogView.findViewById(R.id.edtMoTaBNDialog);
        ImageView imgPreviewDialogBN = dialogView.findViewById(R.id.imgPreviewDialogBN);
        Button btnChonAnhDialogBN = dialogView.findViewById(R.id.btnChonAnhDialogBN);

        Button btnHuyDialogBN = dialogView.findViewById(R.id.btnHuyDialogBN);
        Button btnLuuDialogBN = dialogView.findViewById(R.id.btnLuuDialogBN);

        tvTieuDeDialogBN.setText("THÊM BANNER MỚI");
        pathImageSelected = "";

        btnChonAnhDialogBN.setOnClickListener(v -> moGalleryChonAnhBanner(imgPreviewDialogBN, ""));

        btnHuyDialogBN.setOnClickListener(v -> dialog.dismiss());

        btnLuuDialogBN.setOnClickListener(v -> {
            String tenBN = edtTenBNDialog.getText().toString().trim();
            String moTa = edtMoTaBNDialog.getText().toString().trim();
            String hinhAnh = pathImageSelected;

            if (hinhAnh.isEmpty()) {
                Toast.makeText(QuanLyBannerActivity.this, "Vui lòng chọn hình ảnh cho banner!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean thanhCong = databaseHelper.insertBanner(tenBN, hinhAnh, moTa);
            if (thanhCong) {
                Toast.makeText(QuanLyBannerActivity.this, "Thêm banner thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                taiDanhSachBanner();
            } else {
                Toast.makeText(QuanLyBannerActivity.this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
