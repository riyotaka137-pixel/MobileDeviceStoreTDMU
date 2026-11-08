package com.tdmu.mobiledevicestoretdmu.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.activity.QuanLyThuongHieuActivity;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.ThuongHieu;

import java.io.File;
import java.util.List;

public class QuanLyThuongHieuAdapter extends RecyclerView.Adapter<QuanLyThuongHieuAdapter.ViewHolder> {

    private Context context;
    private List<ThuongHieu> listThuongHieu;
    private DatabaseHelper databaseHelper;

    public QuanLyThuongHieuAdapter(Context context, List<ThuongHieu> listThuongHieu) {
        this.context = context;
        this.listThuongHieu = listThuongHieu;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quan_ly_thuong_hieu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThuongHieu thuongHieu = listThuongHieu.get(position);

        holder.tvTenThuongHieuQuanLy.setText(thuongHieu.getTenThuongHieu());
        holder.tvMoTaThuongHieuQuanLy.setText(thuongHieu.getMoTa() != null && !thuongHieu.getMoTa().isEmpty() ? thuongHieu.getMoTa() : "Chưa có mô tả");

        loadHinhAnhBrand(context, thuongHieu.getHinhAnh(), holder.imgLogoThuongHieu);

        holder.btnSuaThuongHieu.setOnClickListener(v -> hienDialogSuaThuongHieu(thuongHieu));

        holder.btnXoaThuongHieu.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa thương hiệu " + thuongHieu.getTenThuongHieu() + " không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        boolean isDeleted = databaseHelper.deleteThuongHieu(thuongHieu.getMaThuongHieu());
                        if (isDeleted) {
                            Toast.makeText(context, "Đã xóa thương hiệu!", Toast.LENGTH_SHORT).show();
                            if (context instanceof QuanLyThuongHieuActivity) {
                                ((QuanLyThuongHieuActivity) context).taiDanhSachThuongHieu();
                            }
                        } else {
                            Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void hienDialogSuaThuongHieu(ThuongHieu thuongHieu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_quan_ly_thuong_hieu, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView tvTieuDeDialogTH = dialogView.findViewById(R.id.tvTieuDeDialogTH);
        EditText edtTenTHDialog = dialogView.findViewById(R.id.edtTenTHDialog);
        EditText edtMoTaTHDialog = dialogView.findViewById(R.id.edtMoTaTHDialog);
        ImageView imgPreviewDialogTH = dialogView.findViewById(R.id.imgPreviewDialogTH);
        Button btnChonAnhDialogTH = dialogView.findViewById(R.id.btnChonAnhDialogTH);

        Button btnHuyDialogTH = dialogView.findViewById(R.id.btnHuyDialogTH);
        Button btnLuuDialogTH = dialogView.findViewById(R.id.btnLuuDialogTH);

        tvTieuDeDialogTH.setText("SỬA THƯƠNG HIỆU");
        edtTenTHDialog.setText(thuongHieu.getTenThuongHieu());
        edtMoTaTHDialog.setText(thuongHieu.getMoTa());

        loadHinhAnhBrand(context, thuongHieu.getHinhAnh(), imgPreviewDialogTH);

        if (context instanceof QuanLyThuongHieuActivity) {
            ((QuanLyThuongHieuActivity) context).moGalleryChonAnhBrand(null, thuongHieu.getHinhAnh());
        }

        btnChonAnhDialogTH.setOnClickListener(v -> {
            if (context instanceof QuanLyThuongHieuActivity) {
                ((QuanLyThuongHieuActivity) context).moGalleryChonAnhBrand(imgPreviewDialogTH, thuongHieu.getHinhAnh());
            }
        });

        btnHuyDialogTH.setOnClickListener(v -> dialog.dismiss());

        btnLuuDialogTH.setOnClickListener(v -> {
            String tenMoi = edtTenTHDialog.getText().toString().trim();
            String moTaMoi = edtMoTaTHDialog.getText().toString().trim();
            String hinhAnhMoi = thuongHieu.getHinhAnh();

            if (context instanceof QuanLyThuongHieuActivity) {
                String selectedPath = ((QuanLyThuongHieuActivity) context).getPathImageSelected();
                if (selectedPath != null && !selectedPath.isEmpty()) {
                    hinhAnhMoi = selectedPath;
                }
            }

            if (tenMoi.isEmpty()) {
                Toast.makeText(context, "Tên thương hiệu không được để trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = databaseHelper.updateThuongHieu(thuongHieu.getMaThuongHieu(), tenMoi, moTaMoi, hinhAnhMoi);
            if (updated) {
                Toast.makeText(context, "Cập nhật thương hiệu thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                if (context instanceof QuanLyThuongHieuActivity) {
                    ((QuanLyThuongHieuActivity) context).taiDanhSachThuongHieu();
                }
            } else {
                Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    public static void loadHinhAnhBrand(Context context, String hinhAnh, ImageView imageView) {
        if (imageView == null) return;
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (hinhAnh != null && !hinhAnh.trim().isEmpty()) {
            File file = new File(hinhAnh);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    return;
                }
            }
            int imageResId = context.getResources().getIdentifier(hinhAnh, "drawable", context.getPackageName());
            if (imageResId != 0) {
                imageView.setImageResource(imageResId);
                return;
            }
        }
        imageView.setImageResource(R.drawable.logo);
    }

    @Override
    public int getItemCount() {
        return listThuongHieu != null ? listThuongHieu.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenThuongHieuQuanLy, tvMoTaThuongHieuQuanLy;
        ImageView imgLogoThuongHieu;
        Button btnSuaThuongHieu, btnXoaThuongHieu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenThuongHieuQuanLy = itemView.findViewById(R.id.tvTenThuongHieuQuanLy);
            tvMoTaThuongHieuQuanLy = itemView.findViewById(R.id.tvMoTaThuongHieuQuanLy);
            imgLogoThuongHieu = itemView.findViewById(R.id.imgLogoThuongHieu);
            btnSuaThuongHieu = itemView.findViewById(R.id.btnSuaThuongHieu);
            btnXoaThuongHieu = itemView.findViewById(R.id.btnXoaThuongHieu);
        }
    }
}
