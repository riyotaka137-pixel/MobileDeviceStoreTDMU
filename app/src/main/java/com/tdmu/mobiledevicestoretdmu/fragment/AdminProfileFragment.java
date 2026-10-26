package com.tdmu.mobiledevicestoretdmu.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.activity.DangNhapActivity;

public class AdminProfileFragment extends Fragment {

    private TextView tvProfileHoTen, tvProfileEmail;
    private Button btnProfileDangXuat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        tvProfileHoTen = view.findViewById(R.id.tvProfileHoTen);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        btnProfileDangXuat = view.findViewById(R.id.btnProfileDangXuat);

        hienThiThongTinAdmin();

        btnProfileDangXuat.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();

            Toast.makeText(requireContext(), "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(requireContext(), DangNhapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    private void hienThiThongTinAdmin() {
        if (getContext() == null) return;
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String hoTen = sharedPreferences.getString("HoTen", "Admin TDMU");
        String email = sharedPreferences.getString("Email", "admin@gmail.com");

        tvProfileHoTen.setText(hoTen);
        tvProfileEmail.setText(email);
    }
}
