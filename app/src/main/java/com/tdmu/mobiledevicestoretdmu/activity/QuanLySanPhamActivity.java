package com.tdmu.mobiledevicestoretdmu.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.adapter.QuanLySanPhamAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.DanhMuc;
import com.tdmu.mobiledevicestoretdmu.model.SanPham;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class QuanLySanPhamActivity extends AppCompatActivity {

    private RecyclerView rvQuanLySanPham;
    private TextView tvEmptyQuanLySanPham;
    private Button btnThemSanPham;
    private ImageView btnBackHeader;
    private DatabaseHelper databaseHelper;
    private QuanLySanPhamAdapter adapter;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ImageView imgCurrentPreview;
    private String pathImageSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);

        anhXa();
        databaseHelper = new DatabaseHelper(this);

        taiDanhSachSanPham();

        btnBackHeader.setOnClickListener(v -> finish());

        // Đăng ký ActivityResultLauncher để chọn ảnh từ Gallery
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        Uri imageUri = result.getData().getData();
                        String savedFilePath = copyImageToInternalStorage(imageUri);
                        if (savedFilePath != null) {
                            pathImageSelected = savedFilePath;
                            if (imgCurrentPreview != null) {
                                Bitmap bitmap = BitmapFactory.decodeFile(savedFilePath);
                                if (bitmap != null) {
                                    imgCurrentPreview.setImageBitmap(bitmap);
                                }
                            }
                            Toast.makeText(this, "Đã chọn ảnh từ thư viện!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Không thể lưu ảnh!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        btnThemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienDialogThemSanPham();
            }
        });
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        rvQuanLySanPham = findViewById(R.id.rvQuanLySanPham);
        tvEmptyQuanLySanPham = findViewById(R.id.tvEmptyQuanLySanPham);
        btnThemSanPham = findViewById(R.id.btnThemSanPham);
    }

    public void moGalleryChonAnh(ImageView imgPreview, String currentPath) {
        this.imgCurrentPreview = imgPreview;
        this.pathImageSelected = currentPath != null ? currentPath : "";

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    public String getPathImageSelected() {
        return pathImageSelected;
    }

    public String copyImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            File storageDir = new File(getFilesDir(), "product_images");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            String fileName = "img_" + System.currentTimeMillis() + ".jpg";
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

    public void taiDanhSachSanPham() {
        List<SanPham> listSanPham = databaseHelper.getAllSanPham();

        if (listSanPham == null || listSanPham.isEmpty()) {
            tvEmptyQuanLySanPham.setVisibility(View.VISIBLE);
            rvQuanLySanPham.setVisibility(View.GONE);
        } else {
            tvEmptyQuanLySanPham.setVisibility(View.GONE);
            rvQuanLySanPham.setVisibility(View.VISIBLE);

            adapter = new QuanLySanPhamAdapter(this, listSanPham);
            rvQuanLySanPham.setLayoutManager(new LinearLayoutManager(this));
            rvQuanLySanPham.setAdapter(adapter);
        }
    }

    private void hienDialogThemSanPham() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_quan_ly_san_pham, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView tvTieuDeDialogSP = dialogView.findViewById(R.id.tvTieuDeDialogSP);
        EditText edtTenSPDialog = dialogView.findViewById(R.id.edtTenSPDialog);
        Spinner spnDanhMucDialog = dialogView.findViewById(R.id.spnDanhMucDialog);
        EditText edtGiaSPDialog = dialogView.findViewById(R.id.edtGiaSPDialog);
        EditText edtSoLuongSPDialog = dialogView.findViewById(R.id.edtSoLuongSPDialog);
        EditText edtThuongHieuSPDialog = dialogView.findViewById(R.id.edtThuongHieuSPDialog);
        ImageView imgPreviewDialogSP = dialogView.findViewById(R.id.imgPreviewDialogSP);
        Button btnChonAnhDialogSP = dialogView.findViewById(R.id.btnChonAnhDialogSP);
        EditText edtMoTaSPDialog = dialogView.findViewById(R.id.edtMoTaSPDialog);

        Button btnHuyDialogSP = dialogView.findViewById(R.id.btnHuyDialogSP);
        Button btnLuuDialogSP = dialogView.findViewById(R.id.btnLuuDialogSP);

        tvTieuDeDialogSP.setText("THÊM SẢN PHẨM MỚI");
        pathImageSelected = "";

        List<DanhMuc> listDanhMuc = databaseHelper.getAllDanhMuc();
        ArrayAdapter<DanhMuc> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listDanhMuc);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDanhMucDialog.setAdapter(adapterSpinner);

        btnChonAnhDialogSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moGalleryChonAnh(imgPreviewDialogSP, "");
            }
        });

        btnHuyDialogSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuuDialogSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenSP = edtTenSPDialog.getText().toString().trim();
                String giaStr = edtGiaSPDialog.getText().toString().trim();
                String soLuongStr = edtSoLuongSPDialog.getText().toString().trim();
                String thuongHieu = edtThuongHieuSPDialog.getText().toString().trim();
                String hinhAnh = pathImageSelected;
                String moTa = edtMoTaSPDialog.getText().toString().trim();

                DanhMuc dmSelected = (DanhMuc) spnDanhMucDialog.getSelectedItem();

                if (tenSP.isEmpty() || giaStr.isEmpty() || soLuongStr.isEmpty() || dmSelected == null) {
                    Toast.makeText(QuanLySanPhamActivity.this, "Vui lòng nhập đầy đủ các trường bắt buộc (*)", Toast.LENGTH_SHORT).show();
                    return;
                }

                int gia = Integer.parseInt(giaStr);
                int soLuong = Integer.parseInt(soLuongStr);

                databaseHelper.insertSanPham(tenSP, dmSelected.getMaDanhMuc(), gia, soLuong, moTa, thuongHieu, hinhAnh);
                Toast.makeText(QuanLySanPhamActivity.this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                taiDanhSachSanPham();
            }
        });

        dialog.show();
    }
}
