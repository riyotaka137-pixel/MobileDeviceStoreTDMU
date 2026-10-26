package com.tdmu.mobiledevicestoretdmu.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.activity.ThanhToanActivity;
import com.tdmu.mobiledevicestoretdmu.adapter.GioHangAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.ChiTietGioHang;

import java.util.List;

public class UserCartFragment extends Fragment {

    private RecyclerView rvGioHang;
    private TextView tvTongTienGioHang;
    private LinearLayout layoutEmptyCart, layoutBottomGioHang;
    private ImageView imgEmptyCartShake;
    private Button btnThanhToanGioHang;

    private DatabaseHelper databaseHelper;
    private GioHangAdapter gioHangAdapter;
    private List<ChiTietGioHang> listChiTiet;
    private int maTK = -1;
    private long tongTien = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_cart, container, false);

        rvGioHang = view.findViewById(R.id.rvGioHang);
        layoutEmptyCart = view.findViewById(R.id.layoutEmptyCart);
        imgEmptyCartShake = view.findViewById(R.id.imgEmptyCartShake);
        tvTongTienGioHang = view.findViewById(R.id.tvTongTienGioHang);
        layoutBottomGioHang = view.findViewById(R.id.layoutBottomGioHang);
        btnThanhToanGioHang = view.findViewById(R.id.btnThanhToanGioHang);

        databaseHelper = new DatabaseHelper(requireContext());

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        maTK = sharedPreferences.getInt("MaTK", -1);

        taiDanhSachGioHang();

        btnThanhToanGioHang.setOnClickListener(v -> {
            if (listChiTiet != null && !listChiTiet.isEmpty()) {
                Intent intent = new Intent(requireContext(), ThanhToanActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void taiDanhSachGioHang() {
        if (maTK == -1 || getContext() == null) return;

        listChiTiet = databaseHelper.getListCartItem(maTK);

        if (listChiTiet == null || listChiTiet.isEmpty()) {
            layoutEmptyCart.setVisibility(View.VISIBLE);
            rvGioHang.setVisibility(View.GONE);
            layoutBottomGioHang.setVisibility(View.GONE);

            // Kích hoạt hiệu ứng thuần XML Animated Vector Drawable lắc giỏ hàng trống
            imgEmptyCartShake.setImageResource(R.drawable.avd_cart_shake);
            Drawable drawable = imgEmptyCartShake.getDrawable();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
        } else {
            layoutEmptyCart.setVisibility(View.GONE);
            rvGioHang.setVisibility(View.VISIBLE);
            layoutBottomGioHang.setVisibility(View.VISIBLE);

            gioHangAdapter = new GioHangAdapter(requireContext(), listChiTiet);
            rvGioHang.setLayoutManager(new LinearLayoutManager(requireContext()));
            rvGioHang.setAdapter(gioHangAdapter);

            capNhatTongTien();
        }
    }

    public void capNhatTongTien() {
        tongTien = 0;
        if (listChiTiet != null) {
            for (ChiTietGioHang ctgh : listChiTiet) {
                tongTien += (long) ctgh.getDonGia() * ctgh.getSoLuong();
            }
        }
        String giaFormat = String.format("%,d", tongTien).replace(',', '.');
        tvTongTienGioHang.setText(giaFormat + " đ");
    }

    @Override
    public void onResume() {
        super.onResume();
        taiDanhSachGioHang();
    }
}
