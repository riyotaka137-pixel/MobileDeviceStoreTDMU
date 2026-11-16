package com.tdmu.mobiledevicestoretdmu.activity;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.adapter.DanhGiaAdapter;
import com.tdmu.mobiledevicestoretdmu.adapter.QuanLySanPhamAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.DanhGia;
import com.tdmu.mobiledevicestoretdmu.model.SanPham;

import java.util.List;
import java.util.Locale;

public class ChiTietSanPhamActivity extends AppCompatActivity {

    private ImageView imgChiTietSP, btnBackHeader;
    private TextView tvTenChiTietSP, tvThuongHieuChiTietSP, tvGiaChiTietSP, tvGiaGocChiTietSP, tvBadgeChiTietSP, tvKhoChiTietSP, tvMoTaChiTietSP;
    private Button btnThemVaoGioHang;

    // Các View đánh giá sản phẩm
    private TextView tvDiemTrungBinhSao, tvSoLuotDanhGia, tvChuaCoDanhGia;
    private RatingBar rbTrungBinhSao;
    private AppCompatButton btnVietDanhGia;
    private RecyclerView rvDanhSachDanhGia;
    private DanhGiaAdapter danhGiaAdapter;

    private DatabaseHelper databaseHelper;
    private SanPham sanPham;
    private int maSP = -1;
    private int maTK = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        databaseHelper = new DatabaseHelper(this);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        maTK = sharedPreferences.getInt("MaTK", -1);

        anhXa();

        btnBackHeader.setOnClickListener(v -> finish());

        maSP = getIntent().getIntExtra("MaSP", -1);
        if (maSP != -1) {
            taiThongTinSanPham();
            taiDanhSachDanhGia();
            capNhatNutVietDanhGia();
        }

        btnThemVaoGioHang.setOnClickListener(v -> themVaoGioHang());

        btnVietDanhGia.setOnClickListener(v -> hienThiDialogVietDanhGia());
    }

    private void anhXa() {
        btnBackHeader = findViewById(R.id.btnBackHeader);
        imgChiTietSP = findViewById(R.id.imgChiTietSP);
        tvTenChiTietSP = findViewById(R.id.tvTenChiTietSP);
        tvThuongHieuChiTietSP = findViewById(R.id.tvThuongHieuChiTietSP);
        tvGiaChiTietSP = findViewById(R.id.tvGiaChiTietSP);
        tvGiaGocChiTietSP = findViewById(R.id.tvGiaGocChiTietSP);
        tvBadgeChiTietSP = findViewById(R.id.tvBadgeChiTietSP);
        tvKhoChiTietSP = findViewById(R.id.tvKhoChiTietSP);
        tvMoTaChiTietSP = findViewById(R.id.tvMoTaChiTietSP);
        btnThemVaoGioHang = findViewById(R.id.btnThemVaoGioHang);

        // Ánh xạ thành phần đánh giá
        tvDiemTrungBinhSao = findViewById(R.id.tvDiemTrungBinhSao);
        rbTrungBinhSao = findViewById(R.id.rbTrungBinhSao);
        tvSoLuotDanhGia = findViewById(R.id.tvSoLuotDanhGia);
        btnVietDanhGia = findViewById(R.id.btnVietDanhGia);
        tvChuaCoDanhGia = findViewById(R.id.tvChuaCoDanhGia);
        rvDanhSachDanhGia = findViewById(R.id.rvDanhSachDanhGia);
    }

    private void taiThongTinSanPham() {
        sanPham = databaseHelper.getSanPhamById(maSP);
        if (sanPham != null) {
            tvTenChiTietSP.setText(sanPham.getTenSP());
            tvThuongHieuChiTietSP.setText("Thương hiệu: " + (sanPham.getThuongHieu() != null ? sanPham.getThuongHieu() : "Chưa cập nhật"));

            int gia = sanPham.getGia();
            int phanTramGiam = sanPham.getPhanTramGiam();
            if (phanTramGiam > 0) {
                int giaSauGiam = (int) ((long) gia * (100 - phanTramGiam) / 100);
                String giaSauGiamFormat = String.format("%,d", giaSauGiam).replace(',', '.') + " đ";
                tvGiaChiTietSP.setText(giaSauGiamFormat);

                String giaGocFormat = String.format("%,d", gia).replace(',', '.') + " đ";
                tvGiaGocChiTietSP.setText(giaGocFormat);
                tvGiaGocChiTietSP.setPaintFlags(tvGiaGocChiTietSP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvGiaGocChiTietSP.setVisibility(View.VISIBLE);

                if (tvBadgeChiTietSP != null) {
                    tvBadgeChiTietSP.setText("-" + phanTramGiam + "%");
                    tvBadgeChiTietSP.setVisibility(View.VISIBLE);
                }
            } else {
                String giaFormat = String.format("%,d", gia).replace(',', '.') + " đ";
                tvGiaChiTietSP.setText(giaFormat);
                tvGiaGocChiTietSP.setVisibility(View.GONE);
                if (tvBadgeChiTietSP != null) {
                    tvBadgeChiTietSP.setVisibility(View.GONE);
                }
            }

            // Load hình ảnh (hỗ trợ cả đường dẫn Internal Storage và tên Drawable)
            QuanLySanPhamAdapter.loadHinhAnhSanPham(this, sanPham.getHinhAnh(), imgChiTietSP);

            if (sanPham.getSoLuong() <= 0) {
                tvKhoChiTietSP.setText("Số lượng tồn kho: HẾT HÀNG");
                tvKhoChiTietSP.setTextColor(Color.parseColor("#D32F2F"));
                btnThemVaoGioHang.setText("HẾT HÀNG");
                btnThemVaoGioHang.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#757575")));
                btnThemVaoGioHang.setEnabled(false);
            } else {
                tvKhoChiTietSP.setText("Số lượng tồn kho: " + sanPham.getSoLuong());
                tvKhoChiTietSP.setTextColor(Color.parseColor("#388E3C"));
                btnThemVaoGioHang.setText("THÊM VÀO GIỎ HÀNG");
                btnThemVaoGioHang.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#111111")));
                btnThemVaoGioHang.setEnabled(true);
            }

            tvMoTaChiTietSP.setText(sanPham.getMoTa() != null && !sanPham.getMoTa().isEmpty() ? sanPham.getMoTa() : "Chưa có mô tả cho sản phẩm này.");
        }
    }

    private void taiDanhSachDanhGia() {
        if (maSP == -1) return;
        List<DanhGia> list = databaseHelper.getDanhGiaByMaSP(maSP);
        float avgRating = databaseHelper.getAverageRating(maSP);

        if (list != null && !list.isEmpty()) {
            tvDiemTrungBinhSao.setText(String.format(Locale.getDefault(), "%.1f", avgRating));
            rbTrungBinhSao.setRating(avgRating);
            tvSoLuotDanhGia.setText("(" + list.size() + " đánh giá)");

            tvChuaCoDanhGia.setVisibility(View.GONE);
            rvDanhSachDanhGia.setVisibility(View.VISIBLE);

            if (danhGiaAdapter == null) {
                danhGiaAdapter = new DanhGiaAdapter(this, list);
                rvDanhSachDanhGia.setLayoutManager(new LinearLayoutManager(this));
                rvDanhSachDanhGia.setAdapter(danhGiaAdapter);
            } else {
                danhGiaAdapter.updateData(list);
            }
        } else {
            tvDiemTrungBinhSao.setText("0.0");
            rbTrungBinhSao.setRating(0f);
            tvSoLuotDanhGia.setText("(Chưa có đánh giá)");

            tvChuaCoDanhGia.setVisibility(View.VISIBLE);
            rvDanhSachDanhGia.setVisibility(View.GONE);
        }
    }

    private void capNhatNutVietDanhGia() {
        if (maTK > 0 && maSP > 0 && databaseHelper.canUserReviewProduct(maTK, maSP)) {
            btnVietDanhGia.setVisibility(View.VISIBLE);
        } else {
            btnVietDanhGia.setVisibility(View.GONE);
        }
    }

    private void hienThiDialogVietDanhGia() {
        if (maTK <= 0) {
            Toast.makeText(this, "Vui lòng đăng nhập để viết đánh giá!", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_viet_danh_gia, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        RatingBar rbDialogDanhGia = dialogView.findViewById(R.id.rbDialogDanhGia);
        EditText edtDialogNoiDungDanhGia = dialogView.findViewById(R.id.edtDialogNoiDungDanhGia);
        Button btnHuyDanhGia = dialogView.findViewById(R.id.btnHuyDanhGia);
        Button btnGuiDanhGia = dialogView.findViewById(R.id.btnGuiDanhGia);

        btnHuyDanhGia.setOnClickListener(v -> dialog.dismiss());

        btnGuiDanhGia.setOnClickListener(v -> {
            int soSao = (int) rbDialogDanhGia.getRating();
            if (soSao < 1) {
                Toast.makeText(this, "Vui lòng chọn số sao đánh giá (ít nhất 1 sao)!", Toast.LENGTH_SHORT).show();
                return;
            }

            String noiDung = edtDialogNoiDungDanhGia.getText().toString().trim();
            if (noiDung.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập nội dung đánh giá!", Toast.LENGTH_SHORT).show();
                edtDialogNoiDungDanhGia.requestFocus();
                return;
            }

            DanhGia dg = new DanhGia(maSP, maTK, soSao, noiDung, null);
            boolean thanhCong = databaseHelper.insertDanhGia(dg);
            if (thanhCong) {
                Toast.makeText(this, "Cảm ơn bạn đã gửi đánh giá!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                taiDanhSachDanhGia();
                capNhatNutVietDanhGia();
            } else {
                Toast.makeText(this, "Gửi đánh giá thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void themVaoGioHang() {
        if (sanPham == null) return;

        if (sanPham.getSoLuong() <= 0) {
            Toast.makeText(this, "Sản phẩm này hiện đã hết hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (maTK == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        int gia = sanPham.getGia();
        int phanTramGiam = sanPham.getPhanTramGiam();
        int donGiaVaoGio = (phanTramGiam > 0) ? (int) ((long) gia * (100 - phanTramGiam) / 100) : gia;

        boolean thanhCong = databaseHelper.themVaoGioHang(maTK, sanPham.getMaSP(), donGiaVaoGio);
        if (thanhCong) {
            Toast.makeText(this, "Đã thêm " + sanPham.getTenSP() + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            taiThongTinSanPham();
        } else {
            Toast.makeText(this, "Thêm vào giỏ hàng thất bại (có thể do vượt quá tồn kho)!", Toast.LENGTH_SHORT).show();
        }
    }
}
