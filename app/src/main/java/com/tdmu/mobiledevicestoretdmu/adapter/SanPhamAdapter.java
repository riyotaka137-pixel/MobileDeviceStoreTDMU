package com.tdmu.mobiledevicestoretdmu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.activity.ChiTietSanPhamActivity;
import com.tdmu.mobiledevicestoretdmu.activity.ThanhToanActivity;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.SanPham;

import java.util.List;
import java.util.Locale;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {

    private Context context;
    private List<SanPham> listSanPham;
    private DatabaseHelper databaseHelper;
    private boolean showBuyNow = false;

    public SanPhamAdapter(Context context, List<SanPham> listSanPham) {
        this(context, listSanPham, false);
    }

    public SanPhamAdapter(Context context, List<SanPham> listSanPham, boolean showBuyNow) {
        this.context = context;
        this.listSanPham = listSanPham;
        this.showBuyNow = showBuyNow;
        this.databaseHelper = new DatabaseHelper(context);
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

        // Xử lý logic hiển thị phần trăm giảm giá & giá khuyến mãi
        if (sanPham.getPhanTramGiam() > 0) {
            holder.tvBadgeGiamGia.setVisibility(View.VISIBLE);
            holder.tvGiaGocSanPham.setVisibility(View.VISIBLE);
            
            holder.tvBadgeGiamGia.setText("-" + sanPham.getPhanTramGiam() + "%");
            
            String giaGocFormat = String.format("%,d", sanPham.getGia()).replace(',', '.');
            holder.tvGiaGocSanPham.setText(giaGocFormat + " đ");
            holder.tvGiaGocSanPham.setPaintFlags(holder.tvGiaGocSanPham.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            int gia = sanPham.getGia();
            int phanTramGiam = sanPham.getPhanTramGiam();
            int giaSauGiam = (int) ((long) gia * (100 - phanTramGiam) / 100);
            String giaMoiFormat = String.format("%,d", giaSauGiam).replace(',', '.');
            holder.tvGiaSanPham.setText(giaMoiFormat + " đ");
        } else {
            holder.tvBadgeGiamGia.setVisibility(View.GONE);
            holder.tvGiaGocSanPham.setVisibility(View.GONE);
            
            String giaFormat = String.format("%,d", sanPham.getGia()).replace(',', '.');
            holder.tvGiaSanPham.setText(giaFormat + " đ");
        }

        // Đánh giá sao trung bình & Số lượng đã bán
        float avgRating = databaseHelper.getAverageRating(sanPham.getMaSP());
        holder.tvSaoSanPham.setText(String.format(Locale.getDefault(), "★ %.1f", avgRating));

        int soLuongDaBan = databaseHelper.getSoLuongDaBan(sanPham.getMaSP());
        holder.tvDaBanSanPham.setText("• Đã bán " + soLuongDaBan);

        // Số lượng tồn kho
        int tonKho = sanPham.getSoLuongTon();
        if (tonKho <= 0) {
            holder.tvTonKhoSanPham.setText("Hết hàng");
            holder.tvTonKhoSanPham.setTextColor(Color.parseColor("#D32F2F"));
        } else {
            holder.tvTonKhoSanPham.setText("Tồn kho: " + tonKho);
            holder.tvTonKhoSanPham.setTextColor(Color.parseColor("#2E7D32"));
        }

        // Xử lý nút Mua ngay (Chỉ hiển thị khi bật cờ showBuyNow ở Tab Khuyến mãi)
        if (showBuyNow) {
            holder.btnMuaNgaySanPham.setVisibility(View.VISIBLE);
            holder.btnMuaNgaySanPham.setOnClickListener(v -> {
                // 1. Kiểm tra tồn kho
                if (sanPham.getSoLuongTon() <= 0) {
                    Toast.makeText(context, "Sản phẩm đã hết hàng!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 2. Kiểm tra đăng nhập (MaTK trong UserPrefs)
                SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                int maTK = sharedPreferences.getInt("MaTK", -1);
                if (maTK == -1) {
                    Toast.makeText(context, "Vui lòng đăng nhập để mua hàng!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 3. Tính đơn giá sau giảm an toàn chống tràn số
                int donGia = (int) ((long) sanPham.getGia() * (100 - sanPham.getPhanTramGiam()) / 100);

                // 4. Thêm sản phẩm vào giỏ hàng
                boolean thanhCong = databaseHelper.themVaoGioHang(maTK, sanPham.getMaSP(), donGia);
                if (thanhCong) {
                    // 5. Mở thẳng sang màn hình Thanh toán
                    Intent intent = new Intent(context, ThanhToanActivity.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Không thể thêm vào giỏ hàng (có thể do vượt quá số lượng tồn kho)!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.btnMuaNgaySanPham.setVisibility(View.GONE);
        }

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
        TextView tvTenSanPham, tvThuongHieuSanPham, tvGiaSanPham, tvGiaGocSanPham, tvBadgeGiamGia;
        TextView tvSaoSanPham, tvDaBanSanPham, tvTonKhoSanPham;
        Button btnMuaNgaySanPham;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvThuongHieuSanPham = itemView.findViewById(R.id.tvThuongHieuSanPham);
            tvGiaSanPham = itemView.findViewById(R.id.tvGiaSanPham);
            tvGiaGocSanPham = itemView.findViewById(R.id.tvGiaGocSanPham);
            tvBadgeGiamGia = itemView.findViewById(R.id.tvBadgeGiamGia);
            tvSaoSanPham = itemView.findViewById(R.id.tvSaoSanPham);
            tvDaBanSanPham = itemView.findViewById(R.id.tvDaBanSanPham);
            tvTonKhoSanPham = itemView.findViewById(R.id.tvTonKhoSanPham);
            btnMuaNgaySanPham = itemView.findViewById(R.id.btnMuaNgaySanPham);
        }
    }
}
