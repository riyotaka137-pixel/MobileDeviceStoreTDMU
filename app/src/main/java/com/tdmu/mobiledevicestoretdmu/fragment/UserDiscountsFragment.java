package com.tdmu.mobiledevicestoretdmu.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.adapter.SanPhamAdapter;
import com.tdmu.mobiledevicestoretdmu.adapter.VoucherUserAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.GiamGia;
import com.tdmu.mobiledevicestoretdmu.model.SanPham;

import java.util.List;

public class UserDiscountsFragment extends Fragment {

    private LinearLayout layoutVoucherKhuyenMai;
    private RecyclerView rvVouchersUser, rvSanPhamKhuyenMai;
    private DatabaseHelper databaseHelper;

    public UserDiscountsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_discounts, container, false);

        layoutVoucherKhuyenMai = view.findViewById(R.id.layoutVoucherKhuyenMai);
        rvVouchersUser = view.findViewById(R.id.rvVouchersUser);
        rvSanPhamKhuyenMai = view.findViewById(R.id.rvSanPhamKhuyenMai);
        databaseHelper = new DatabaseHelper(requireContext());

        taiDuLieuKhuyenMai();

        return view;
    }

    private void taiDuLieuKhuyenMai() {
        if (getContext() == null || databaseHelper == null) return;

        // 1. Tải danh sách Voucher còn hiệu lực
        List<GiamGia> activeVouchers = databaseHelper.getActiveVouchers();
        if (activeVouchers != null && !activeVouchers.isEmpty()) {
            layoutVoucherKhuyenMai.setVisibility(View.VISIBLE);
            VoucherUserAdapter voucherAdapter = new VoucherUserAdapter(requireContext(), activeVouchers);
            rvVouchersUser.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            rvVouchersUser.setAdapter(voucherAdapter);
        } else {
            layoutVoucherKhuyenMai.setVisibility(View.GONE);
        }

        // 2. Tải danh sách Sản phẩm giảm giá
        List<SanPham> danhSachGiamGia = databaseHelper.getSanPhamKhuyenMai();
        if (danhSachGiamGia != null && !danhSachGiamGia.isEmpty()) {
            SanPhamAdapter spAdapter = new SanPhamAdapter(requireContext(), danhSachGiamGia);
            rvSanPhamKhuyenMai.setLayoutManager(new LinearLayoutManager(requireContext()));
            rvSanPhamKhuyenMai.setAdapter(spAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        taiDuLieuKhuyenMai();
    }
}
