package com.tdmu.mobiledevicestoretdmu.adapter;

import android.app.AlertDialog;
import android.content.Context;
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
import com.tdmu.mobiledevicestoretdmu.activity.QuanLyBannerActivity;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.Banner;

import java.io.File;
import java.util.List;

public class QuanLyBannerAdapter extends RecyclerView.Adapter<QuanLyBannerAdapter.ViewHolder> {

    private Context context;
    private List<Banner> listBanner;
    private DatabaseHelper databaseHelper;

    public QuanLyBannerAdapter(Context context, List<Banner> listBanner) {
        this.context = context;
        this.listBanner = listBanner;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quan_ly_banner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Banner banner = listBanner.get(position);

        holder.tvTenBannerQuanLy.setText(banner.getTenBanner() != null && !banner.getTenBanner().isEmpty() ? banner.getTenBanner() : "Banner #" + banner.getMaBanner());
        holder.tvMoTaBannerQuanLy.setText(banner.getMoTa() != null && !banner.getMoTa().isEmpty() ? banner.getMoTa() : "Chương trình khuyến mãi");

        loadHinhAnhBanner(context, banner.getHinhAnh(), holder.imgBannerQuanLy);

        holder.btnSuaBanner.setOnClickListener(v -> hienDialogSuaBanner(banner));

        holder.btnXoaBanner.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa banner này không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        boolean isDeleted = databaseHelper.deleteBanner(banner.getMaBanner());
                        if (isDeleted) {
                            Toast.makeText(context, "Đã xóa banner!", Toast.LENGTH_SHORT).show();
                            if (context instanceof QuanLyBannerActivity) {
                                ((QuanLyBannerActivity) context).taiDanhSachBanner();
                            }
                        } else {
                            Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void hienDialogSuaBanner(Banner banner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_quan_ly_banner, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView tvTieuDeDialogBN = dialogView.findViewById(R.id.tvTieuDeDialogBN);
        EditText edtTenBNDialog = dialogView.findViewById(R.id.edtTenBNDialog);
        EditText edtMoTaBNDialog = dialogView.findViewById(R.id.edtMoTaBNDialog);
        ImageView imgPreviewDialogBN = dialogView.findViewById(R.id.imgPreviewDialogBN);
        Button btnChonAnhDialogBN = dialogView.findViewById(R.id.btnChonAnhDialogBN);

        Button btnHuyDialogBN = dialogView.findViewById(R.id.btnHuyDialogBN);
        Button btnLuuDialogBN = dialogView.findViewById(R.id.btnLuuDialogBN);

        tvTieuDeDialogBN.setText("SỬA BANNER");
        edtTenBNDialog.setText(banner.getTenBanner());
        edtMoTaBNDialog.setText(banner.getMoTa());

        loadHinhAnhBanner(context, banner.getHinhAnh(), imgPreviewDialogBN);

        if (context instanceof QuanLyBannerActivity) {
            ((QuanLyBannerActivity) context).moGalleryChonAnhBanner(null, banner.getHinhAnh());
        }

        btnChonAnhDialogBN.setOnClickListener(v -> {
            if (context instanceof QuanLyBannerActivity) {
                ((QuanLyBannerActivity) context).moGalleryChonAnhBanner(imgPreviewDialogBN, banner.getHinhAnh());
            }
        });

        btnHuyDialogBN.setOnClickListener(v -> dialog.dismiss());

        btnLuuDialogBN.setOnClickListener(v -> {
            String tenMoi = edtTenBNDialog.getText().toString().trim();
            String moTaMoi = edtMoTaBNDialog.getText().toString().trim();
            String hinhAnhMoi = banner.getHinhAnh();

            if (context instanceof QuanLyBannerActivity) {
                String selectedPath = ((QuanLyBannerActivity) context).getPathImageSelected();
                if (selectedPath != null && !selectedPath.isEmpty()) {
                    hinhAnhMoi = selectedPath;
                }
            }

            boolean updated = databaseHelper.updateBanner(banner.getMaBanner(), tenMoi, hinhAnhMoi, moTaMoi);
            if (updated) {
                Toast.makeText(context, "Cập nhật banner thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                if (context instanceof QuanLyBannerActivity) {
                    ((QuanLyBannerActivity) context).taiDanhSachBanner();
                }
            } else {
                Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    public static void loadHinhAnhBanner(Context context, String hinhAnh, ImageView imageView) {
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
        return listBanner != null ? listBanner.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenBannerQuanLy, tvMoTaBannerQuanLy;
        ImageView imgBannerQuanLy;
        Button btnSuaBanner, btnXoaBanner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenBannerQuanLy = itemView.findViewById(R.id.tvTenBannerQuanLy);
            tvMoTaBannerQuanLy = itemView.findViewById(R.id.tvMoTaBannerQuanLy);
            imgBannerQuanLy = itemView.findViewById(R.id.imgBannerQuanLy);
            btnSuaBanner = itemView.findViewById(R.id.btnSuaBanner);
            btnXoaBanner = itemView.findViewById(R.id.btnXoaBanner);
        }
    }
}
