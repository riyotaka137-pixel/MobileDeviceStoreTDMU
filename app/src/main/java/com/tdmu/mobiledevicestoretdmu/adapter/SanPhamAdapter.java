package com.tdmu.mobiledevicestoretdmu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.activity.ChiTietSanPhamActivity;
import com.tdmu.mobiledevicestoretdmu.model.SanPham;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {

    private Context context;
    private List<SanPham> listSanPham;

    public SanPhamAdapter(Context context, List<SanPham> listSanPham) {
        this.context = context;
        this.listSanPham = listSanPham;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sanPham = listSanPham.get(position);

        holder.tvTenSanPham.setText(sanPham.getTenSP());
        holder.tvThuongHieuSanPham.setText(sanPham.getThuongHieu() != null ? sanPham.getThuongHieu() : "");

        String giaFormat = String.format("%,d", sanPham.getGia()).replace(',', '.');
        holder.tvGiaSanPham.setText(giaFormat + " đ");

        // Load hình ảnh (hỗ trợ cả đường dẫn Internal Storage và tên Drawable)
        QuanLySanPhamAdapter.loadHinhAnhSanPham(context, sanPham.getHinhAnh(), holder.imgSanPham);

        // Click sản phẩm với hiệu ứng chuyển cảnh mượt mà Shared Element Transition
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
                intent.putExtra("MaSP", sanPham.getMaSP());

                if (context instanceof Activity) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            (Activity) context,
                            holder.imgSanPham,
                            "product_img_trans"
                    );
                    context.startActivity(intent, options.toBundle());
                } else {
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSanPham != null ? listSanPham.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSanPham;
        TextView tvTenSanPham, tvThuongHieuSanPham, tvGiaSanPham;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvThuongHieuSanPham = itemView.findViewById(R.id.tvThuongHieuSanPham);
            tvGiaSanPham = itemView.findViewById(R.id.tvGiaSanPham);
        }
    }
}
