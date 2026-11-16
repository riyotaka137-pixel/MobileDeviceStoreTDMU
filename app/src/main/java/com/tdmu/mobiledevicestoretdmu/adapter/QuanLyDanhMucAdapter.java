package com.tdmu.mobiledevicestoretdmu.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.bumptech.glide.Glide;
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
import com.tdmu.mobiledevicestoretdmu.activity.QuanLyDanhMucActivity;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.DanhMuc;

import java.io.File;
import java.util.List;

public class QuanLyDanhMucAdapter extends RecyclerView.Adapter<QuanLyDanhMucAdapter.ViewHolder> {

    private Context context;
    private List<DanhMuc> listDanhMuc;
    private DatabaseHelper databaseHelper;

    public QuanLyDanhMucAdapter(Context context, List<DanhMuc> listDanhMuc) {
        this.context = context;
        this.listDanhMuc = listDanhMuc;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quan_ly_danh_muc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DanhMuc danhMuc = listDanhMuc.get(position);

        holder.tvTenDanhMucQuanLy.setText(danhMuc.getTenDanhMuc());
        holder.tvMoTaDanhMucQuanLy.setText(danhMuc.getMoTa() != null && !danhMuc.getMoTa().isEmpty() ? danhMuc.getMoTa() : "Chưa có mô tả");

        loadHinhAnhCategory(context, danhMuc.getHinhAnh(), holder.imgLogoDanhMuc);

        holder.btnSuaDanhMuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienDialogSuaDanhMuc(danhMuc);
            }
        });

        holder.btnXoaDanhMuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa danh mục " + danhMuc.getTenDanhMuc() + " không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean isDeleted = databaseHelper.deleteDanhMuc(danhMuc.getMaDanhMuc());
                                if (isDeleted) {
                                    Toast.makeText(context, "Đã xóa danh mục!", Toast.LENGTH_SHORT).show();
                                    if (context instanceof QuanLyDanhMucActivity) {
                                        ((QuanLyDanhMucActivity) context).taiDanhSachDanhMuc();
                                    }
                                } else {
                                    Toast.makeText(context, "Xóa danh mục thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
    }

    private void hienDialogSuaDanhMuc(DanhMuc danhMuc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_quan_ly_danh_muc, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView tvTieuDeDialogDM = dialogView.findViewById(R.id.tvTieuDeDialogDM);
        EditText edtTenDMDialog = dialogView.findViewById(R.id.edtTenDMDialog);
        EditText edtMoTaDMDialog = dialogView.findViewById(R.id.edtMoTaDMDialog);
        ImageView imgPreviewDialogDM = dialogView.findViewById(R.id.imgPreviewDialogDM);
        Button btnChonAnhDialogDM = dialogView.findViewById(R.id.btnChonAnhDialogDM);

        Button btnHuyDialogDM = dialogView.findViewById(R.id.btnHuyDialogDM);
        Button btnLuuDialogDM = dialogView.findViewById(R.id.btnLuuDialogDM);

        tvTieuDeDialogDM.setText("SỬA DANH MỤC");
        edtTenDMDialog.setText(danhMuc.getTenDanhMuc());
        edtMoTaDMDialog.setText(danhMuc.getMoTa());

        loadHinhAnhCategory(context, danhMuc.getHinhAnh(), imgPreviewDialogDM);

        if (context instanceof QuanLyDanhMucActivity) {
            ((QuanLyDanhMucActivity) context).moGalleryChonAnhCategory(null, danhMuc.getHinhAnh());
        }

        btnChonAnhDialogDM.setOnClickListener(v -> {
            if (context instanceof QuanLyDanhMucActivity) {
                ((QuanLyDanhMucActivity) context).moGalleryChonAnhCategory(imgPreviewDialogDM, danhMuc.getHinhAnh());
            }
        });

        btnHuyDialogDM.setOnClickListener(v -> dialog.dismiss());

        btnLuuDialogDM.setOnClickListener(v -> {
            String tenMoi = edtTenDMDialog.getText().toString().trim();
            String moTaMoi = edtMoTaDMDialog.getText().toString().trim();
            String hinhAnhMoi = danhMuc.getHinhAnh();

            if (context instanceof QuanLyDanhMucActivity) {
                String selectedPath = ((QuanLyDanhMucActivity) context).getPathImageSelected();
                if (selectedPath != null && !selectedPath.isEmpty()) {
                    hinhAnhMoi = selectedPath;
                }
            }

            if (tenMoi.isEmpty()) {
                Toast.makeText(context, "Tên danh mục không được để trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = databaseHelper.updateDanhMuc(danhMuc.getMaDanhMuc(), tenMoi, moTaMoi, hinhAnhMoi);
            if (updated) {
                Toast.makeText(context, "Cập nhật danh mục thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                if (context instanceof QuanLyDanhMucActivity) {
                    ((QuanLyDanhMucActivity) context).taiDanhSachDanhMuc();
                }
            } else {
                Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    public static void loadHinhAnhCategory(Context context, String hinhAnh, ImageView imageView) {
        if (context == null || imageView == null) return;
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (hinhAnh != null && !hinhAnh.trim().isEmpty()) {
            File file = new File(hinhAnh);
            if (file.exists()) {
                Glide.with(context)
                        .load(file)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(imageView);
                return;
            }
            int imageResId = context.getResources().getIdentifier(hinhAnh, "drawable", context.getPackageName());
            if (imageResId != 0) {
                Glide.with(context)
                        .load(imageResId)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(imageView);
                return;
            }
        }
        Glide.with(context)
                .load(R.drawable.logo)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return listDanhMuc != null ? listDanhMuc.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenDanhMucQuanLy, tvMoTaDanhMucQuanLy;
        ImageView imgLogoDanhMuc;
        Button btnSuaDanhMuc, btnXoaDanhMuc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenDanhMucQuanLy = itemView.findViewById(R.id.tvTenDanhMucQuanLy);
            tvMoTaDanhMucQuanLy = itemView.findViewById(R.id.tvMoTaDanhMucQuanLy);
            imgLogoDanhMuc = itemView.findViewById(R.id.imgLogoDanhMuc);
            btnSuaDanhMuc = itemView.findViewById(R.id.btnSuaDanhMuc);
            btnXoaDanhMuc = itemView.findViewById(R.id.btnXoaDanhMuc);
        }
    }
}
