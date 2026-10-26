package com.tdmu.mobiledevicestoretdmu.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.activity.QuanLyDanhMucActivity;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.DanhMuc;

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
        builder.setTitle("SỬA DANH MỤC");

        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(40, 20, 40, 20);

        final EditText edtTenDM = new EditText(context);
        edtTenDM.setHint("Tên danh mục");
        edtTenDM.setText(danhMuc.getTenDanhMuc());

        final EditText edtMoTaDM = new EditText(context);
        edtMoTaDM.setHint("Mô tả");
        edtMoTaDM.setText(danhMuc.getMoTa());

        container.addView(edtTenDM);
        container.addView(edtMoTaDM);

        builder.setView(container);

        builder.setPositiveButton("LƯU", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tenMoi = edtTenDM.getText().toString().trim();
                String moTaMoi = edtMoTaDM.getText().toString().trim();

                if (tenMoi.isEmpty()) {
                    Toast.makeText(context, "Tên danh mục không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean updated = databaseHelper.updateDanhMuc(danhMuc.getMaDanhMuc(), tenMoi, moTaMoi);
                if (updated) {
                    Toast.makeText(context, "Cập nhật danh mục thành công!", Toast.LENGTH_SHORT).show();
                    if (context instanceof QuanLyDanhMucActivity) {
                        ((QuanLyDanhMucActivity) context).taiDanhSachDanhMuc();
                    }
                } else {
                    Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("HỦY", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return listDanhMuc != null ? listDanhMuc.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenDanhMucQuanLy, tvMoTaDanhMucQuanLy;
        Button btnSuaDanhMuc, btnXoaDanhMuc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenDanhMucQuanLy = itemView.findViewById(R.id.tvTenDanhMucQuanLy);
            tvMoTaDanhMucQuanLy = itemView.findViewById(R.id.tvMoTaDanhMucQuanLy);
            btnSuaDanhMuc = itemView.findViewById(R.id.btnSuaDanhMuc);
            btnXoaDanhMuc = itemView.findViewById(R.id.btnXoaDanhMuc);
        }
    }
}
