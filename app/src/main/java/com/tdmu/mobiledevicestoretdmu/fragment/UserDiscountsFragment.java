package com.tdmu.mobiledevicestoretdmu.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;

public class UserDiscountsFragment extends Fragment {

    private RecyclerView rvSanPhamKhuyenMai;

    public UserDiscountsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_discounts, container, false);
        
        rvSanPhamKhuyenMai = view.findViewById(R.id.rvSanPhamKhuyenMai);

        // Thiết lập Adapter cho danh sách sản phẩm giảm giá tại đây
        // ...

        return view;
    }
}
