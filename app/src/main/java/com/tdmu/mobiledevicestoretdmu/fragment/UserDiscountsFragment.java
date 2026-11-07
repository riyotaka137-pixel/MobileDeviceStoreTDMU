package com.tdmu.mobiledevicestoretdmu.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.adapter.SanPhamAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.SanPham;

import java.util.List;

public class UserDiscountsFragment extends Fragment {

    private RecyclerView rvSanPhamKhuyenMai;
    private DatabaseHelper databaseHelper;

    public UserDiscountsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_discounts, container, false);
        
        rvSanPhamKhuyenMai = view.findViewById(R.id.rvSanPhamKhuyenMai);
        databaseHelper = new DatabaseHelper(requireContext());
        
        List<SanPham> danhSachGiamGia = databaseHelper.getSanPhamKhuyenMai();
        
        if (danhSachGiamGia != null && !danhSachGiamGia.isEmpty()) {
            SanPhamAdapter adapter = new SanPhamAdapter(requireContext(), danhSachGiamGia);
            rvSanPhamKhuyenMai.setLayoutManager(new LinearLayoutManager(requireContext()));
            rvSanPhamKhuyenMai.setAdapter(adapter);
        }

        return view;
    }
}
