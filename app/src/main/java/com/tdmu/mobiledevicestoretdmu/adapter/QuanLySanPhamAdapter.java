package com.tdmu.mobiledevicestoretdmu.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.activity.QuanLySanPhamActivity;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.DanhMuc;
import com.tdmu.mobiledevicestoretdmu.model.SanPham;

import java.io.File;
import java.util.List;

public class QuanLySanPhamAdapter extends RecyclerView.Adapter<QuanLySanPhamAdapter.ViewHolder> {

    private Context context;
    private List<SanPham> listSanPham;
    private DatabaseHelper databaseHelper;

    public QuanLySanPhamAdapter(Context context, List<SanPham> listSanPham) {
        this.context = context;
        this.listSanPham = listSanPham;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quan_ly_san_pham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sanPham = listSanPham.get(position);

        holder.tvTenSPQuanLy.setText(sanPham.getTenSP());

        String giaFormat = String.format("%,d", sanPham.getGia()).replace(',', '.');
        holder.tvGiaSPQuanLy.setText("Giá: " + giaFormat + " đ");
        holder.tvKhoSPQuanLy.setText("Kho: " + sanPham.getSoLuong());

        loadHinhAnhSanPham(context, sanPham.getHinhAnh(), holder.imgQuanLySP);

        holder.btnSuaSPQuanLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienDialogSuaSanPham(sanPham);
            }
        });

        holder.btnXoaSPQuanLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa sản phẩm " + sanPham.getTenSP() + " không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean isDeleted = databaseHelper.deleteSanPham(sanPham.getMaSP());
                                if (isDeleted) {
                                    Toast.makeText(context, "Đã xóa sản phẩm!", Toast.LENGTH_SHORT).show();
                                    if (context instanceof QuanLySanPhamActivity) {
                                        ((QuanLySanPhamActivity) context).taiDanhSachSanPham();
                                    }
                                } else {
                                    Toast.makeText(context, "Xóa sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
    }

    private void hienDialogSuaSanPham(SanPham sanPham) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_quan_ly_san_pham, null);
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

        tvTieuDeDialogSP.setText("SỬA SẢN PHẨM");
        edtTenSPDialog.setText(sanPham.getTenSP());
        edtGiaSPDialog.setText(String.valueOf(sanPham.getGia()));
        edtSoLuongSPDialog.setText(String.valueOf(sanPham.getSoLuong()));
        edtThuongHieuSPDialog.setText(sanPham.getThuongHieu());
        edtMoTaSPDialog.setText(sanPham.getMoTa());

        loadHinhAnhSanPham(context, sanPham.getHinhAnh(), imgPreviewDialogSP);

        if (context instanceof QuanLySanPhamActivity) {
            ((QuanLySanPhamActivity) context).moGalleryChonAnh(null, sanPham.getHinhAnh());
        }

        btnChonAnhDialogSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof QuanLySanPhamActivity) {
                    ((QuanLySanPhamActivity) context).moGalleryChonAnh(imgPreviewDialogSP, sanPham.getHinhAnh());
                }
            }
        });

        List<DanhMuc> listDanhMuc = databaseHelper.getAllDanhMuc();
        ArrayAdapter<DanhMuc> adapterSpinner = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listDanhMuc);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDanhMucDialog.setAdapter(adapterSpinner);

        for (int i = 0; i < listDanhMuc.size(); i++) {
            if (listDanhMuc.get(i).getMaDanhMuc() == sanPham.getMaDanhMuc()) {
                spnDanhMucDialog.setSelection(i);
                break;
            }
        }

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
                String moTa = edtMoTaSPDialog.getText().toString().trim();

                String hinhAnh = sanPham.getHinhAnh();
                if (context instanceof QuanLySanPhamActivity) {
                    String selectedPath = ((QuanLySanPhamActivity) context).getPathImageSelected();
                    if (selectedPath != null && !selectedPath.isEmpty()) {
                        hinhAnh = selectedPath;
                    }
                }

                DanhMuc dmSelected = (DanhMuc) spnDanhMucDialog.getSelectedItem();

                if (tenSP.isEmpty() || giaStr.isEmpty() || soLuongStr.isEmpty() || dmSelected == null) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ các trường bắt buộc (*)", Toast.LENGTH_SHORT).show();
                    return;
                }

                int gia = Integer.parseInt(giaStr);
                int soLuong = Integer.parseInt(soLuongStr);

                boolean isUpdated = databaseHelper.updateSanPham(sanPham.getMaSP(), tenSP, dmSelected.getMaDanhMuc(), gia, soLuong, moTa, thuongHieu, hinhAnh);
                if (isUpdated) {
                    Toast.makeText(context, "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    if (context instanceof QuanLySanPhamActivity) {
                        ((QuanLySanPhamActivity) context).taiDanhSachSanPham();
                    }
                } else {
                    Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    public static void loadHinhAnhSanPham(Context context, String hinhAnh, ImageView imageView) {
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
        imageView.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return listSanPham != null ? listSanPham.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgQuanLySP;
        TextView tvTenSPQuanLy, tvGiaSPQuanLy, tvKhoSPQuanLy;
        Button btnSuaSPQuanLy, btnXoaSPQuanLy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgQuanLySP = itemView.findViewById(R.id.imgQuanLySP);
            tvTenSPQuanLy = itemView.findViewById(R.id.tvTenSPQuanLy);
            tvGiaSPQuanLy = itemView.findViewById(R.id.tvGiaSPQuanLy);
            tvKhoSPQuanLy = itemView.findViewById(R.id.tvKhoSPQuanLy);
            btnSuaSPQuanLy = itemView.findViewById(R.id.btnSuaSPQuanLy);
            btnXoaSPQuanLy = itemView.findViewById(R.id.btnXoaSPQuanLy);
        }
    }
}
