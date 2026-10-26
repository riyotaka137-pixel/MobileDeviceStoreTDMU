package com.tdmu.mobiledevicestoretdmu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.activity.QuanLyDanhMucActivity;
import com.tdmu.mobiledevicestoretdmu.activity.QuanLyDonHangActivity;
import com.tdmu.mobiledevicestoretdmu.activity.QuanLySanPhamActivity;
import com.tdmu.mobiledevicestoretdmu.activity.QuanLyTaiKhoanActivity;

public class AdminManagementFragment extends Fragment {

    private MaterialCardView cardQuanLyDanhMuc, cardQuanLySanPham, cardQuanLyDonHang, cardQuanLyTaiKhoan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_management, container, false);

        cardQuanLyDanhMuc = view.findViewById(R.id.cardQuanLyDanhMuc);
        cardQuanLySanPham = view.findViewById(R.id.cardQuanLySanPham);
        cardQuanLyDonHang = view.findViewById(R.id.cardQuanLyDonHang);
        cardQuanLyTaiKhoan = view.findViewById(R.id.cardQuanLyTaiKhoan);

        cardQuanLyDanhMuc.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), QuanLyDanhMucActivity.class);
            startActivity(intent);
        });

        cardQuanLySanPham.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), QuanLySanPhamActivity.class);
            startActivity(intent);
        });

        cardQuanLyDonHang.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), QuanLyDonHangActivity.class);
            startActivity(intent);
        });

        cardQuanLyTaiKhoan.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), QuanLyTaiKhoanActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
