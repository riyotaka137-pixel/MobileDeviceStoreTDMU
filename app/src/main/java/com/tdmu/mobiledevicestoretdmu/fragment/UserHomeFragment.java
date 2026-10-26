package com.tdmu.mobiledevicestoretdmu.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.activity.TrangChuUserActivity;
import com.tdmu.mobiledevicestoretdmu.activity.TimKiemSanPhamActivity;
import com.tdmu.mobiledevicestoretdmu.adapter.DanhMucAdapter;
import com.tdmu.mobiledevicestoretdmu.adapter.SanPhamAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.DanhMuc;
import com.tdmu.mobiledevicestoretdmu.model.SanPham;

import java.util.List;

public class UserHomeFragment extends Fragment {

    private TextView tvXinChaoUser;
    private EditText edtTimKiem;
    private Button btnGioHangHeader, btnTimKiem;
    private RecyclerView rvDanhMuc, rvSanPham;

    private DatabaseHelper databaseHelper;
    private DanhMucAdapter danhMucAdapter;
    private SanPhamAdapter sanPhamAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        tvXinChaoUser = view.findViewById(R.id.tvXinChaoUser);
        edtTimKiem = view.findViewById(R.id.edtTimKiem);
        btnGioHangHeader = view.findViewById(R.id.btnGioHangHeader);
        btnTimKiem = view.findViewById(R.id.btnTimKiem);
        rvDanhMuc = view.findViewById(R.id.rvDanhMuc);
        rvSanPham = view.findViewById(R.id.rvSanPham);

        databaseHelper = new DatabaseHelper(requireContext());

        hienThiThongTinUser();
        taiDanhSachDanhMuc();
        taiDanhSachSanPham();
        batSuKien();

        return view;
    }

    private void hienThiThongTinUser() {
        if (getContext() == null) return;
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String hoTen = sharedPreferences.getString("HoTen", "Khách hàng");
        tvXinChaoUser.setText("Xin chào, " + hoTen);
    }

    private void taiDanhSachDanhMuc() {
        List<DanhMuc> listDanhMuc = databaseHelper.getAllDanhMuc();
        danhMucAdapter = new DanhMucAdapter(requireContext(), listDanhMuc);
        rvDanhMuc.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDanhMuc.setAdapter(danhMucAdapter);
    }

    private void taiDanhSachSanPham() {
        List<SanPham> listSanPham = databaseHelper.getAllSanPham();
        sanPhamAdapter = new SanPhamAdapter(requireContext(), listSanPham);
        rvSanPham.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvSanPham.setAdapter(sanPhamAdapter);
    }

    private void batSuKien() {
        btnGioHangHeader.setOnClickListener(v -> {
            if (getActivity() instanceof TrangChuUserActivity) {
                ((TrangChuUserActivity) getActivity()).chuyenTabGioHang();
            }
        });

        btnTimKiem.setOnClickListener(v -> {
            String tuKhoa = edtTimKiem.getText().toString().trim();
            if (!tuKhoa.isEmpty()) {
                Intent intent = new Intent(requireContext(), TimKiemSanPhamActivity.class);
                intent.putExtra("TuKhoa", tuKhoa);
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Vui lòng nhập từ khóa tìm kiếm!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Hiệu ứng "Bay" Parabola thêm vào giỏ hàng
     * Lấy tọa độ ảnh sản phẩm & icon giỏ hàng trên Bottom Nav để vẽ đường cong Bezier Parabola
     */
    public void animateAddToCart(ImageView imgProduct, View targetCartView) {
        if (imgProduct == null || targetCartView == null || getActivity() == null) return;

        // 1. TÍNH TỌA ĐỘ BẮT ĐẦU VÀ KẾT THÚC TRÊN MÀN HÌNH (getLocationOnScreen)
        int[] startLoc = new int[2];
        imgProduct.getLocationOnScreen(startLoc);

        int[] endLoc = new int[2];
        targetCartView.getLocationOnScreen(endLoc);

        // 2. TẠO BẢN SAO HOẠT HÌNH FLYING VIEW BÊN TRÊN
        ViewGroup rootLayout = (ViewGroup) getActivity().getWindow().getDecorView();

        ImageView flyingView = new ImageView(requireContext());
        flyingView.setImageDrawable(imgProduct.getDrawable());

        int width = imgProduct.getWidth() > 0 ? imgProduct.getWidth() : 120;
        int height = imgProduct.getHeight() > 0 ? imgProduct.getHeight() : 120;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.leftMargin = startLoc[0];
        params.topMargin = startLoc[1];
        flyingView.setLayoutParams(params);

        rootLayout.addView(flyingView);

        // 3. XÂY DỰNG ĐƯỜNG CONG PARABOLA (BEZIER QUADRATIC CURVE)
        float startX = startLoc[0];
        float startY = startLoc[1];
        float endX = endLoc[0] + (targetCartView.getWidth() / 2f) - (width / 2f);
        float endY = endLoc[1] + (targetCartView.getHeight() / 2f) - (height / 2f);

        Path path = new Path();
        path.moveTo(startX, startY);
        float controlX = (startX + endX) / 2f;
        float controlY = startY - 220f; // Nhô cao lên trên tạo hiệu ứng rơi hình vòm Parabola
        path.quadTo(controlX, controlY, endX, endY);

        // 4. THỰC HIỆN ANIMATION CONG VÀ THU NHỎ DẦN
        ObjectAnimator pathAnimator = ObjectAnimator.ofFloat(flyingView, View.X, View.Y, path);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(flyingView, View.SCALE_X, 1.0f, 0.15f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(flyingView, View.SCALE_Y, 1.0f, 0.15f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(flyingView, View.ALPHA, 1.0f, 0.4f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(pathAnimator, scaleXAnimator, scaleYAnimator, alphaAnimator);
        animatorSet.setDuration(700);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rootLayout.removeView(flyingView);

                // 5. HIỆU ỨNG NHÚNG BÁO HIỆU (SCALE BOUNCE) TRÊN ICON GIỎ HÀNG THUỘC BOTTOM NAV
                ObjectAnimator bounceX = ObjectAnimator.ofFloat(targetCartView, View.SCALE_X, 1.0f, 1.3f, 1.0f);
                ObjectAnimator bounceY = ObjectAnimator.ofFloat(targetCartView, View.SCALE_Y, 1.0f, 1.3f, 1.0f);
                AnimatorSet bounceSet = new AnimatorSet();
                bounceSet.playTogether(bounceX, bounceY);
                bounceSet.setDuration(300);
                bounceSet.start();
            }
        });

        animatorSet.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        hienThiThongTinUser();
        taiDanhSachSanPham();
    }
}
