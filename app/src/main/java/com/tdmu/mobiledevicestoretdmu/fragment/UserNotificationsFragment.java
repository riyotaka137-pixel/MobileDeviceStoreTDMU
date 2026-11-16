package com.tdmu.mobiledevicestoretdmu.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.tdmu.mobiledevicestoretdmu.adapter.ThongBaoAdapter;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.ThongBao;

import java.util.List;

public class UserNotificationsFragment extends Fragment {

    private RecyclerView rvNotifications;
    private LinearLayout layoutEmptyNotifications;
    private DatabaseHelper databaseHelper;
    private ThongBaoAdapter adapter;
    private int maTK = -1;

    public UserNotificationsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_notifications, container, false);

        rvNotifications = view.findViewById(R.id.rvNotifications);
        layoutEmptyNotifications = view.findViewById(R.id.layoutEmptyNotifications);

        databaseHelper = new DatabaseHelper(requireContext());

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        maTK = sharedPreferences.getInt("MaTK", -1);

        taiDanhSachThongBao();

        return view;
    }

    public void taiDanhSachThongBao() {
        if (maTK == -1 || getContext() == null) return;

        List<ThongBao> list = databaseHelper.getThongBaoByMaTK(maTK);
        if (list == null || list.isEmpty()) {
            layoutEmptyNotifications.setVisibility(View.VISIBLE);
            rvNotifications.setVisibility(View.GONE);
        } else {
            layoutEmptyNotifications.setVisibility(View.GONE);
            rvNotifications.setVisibility(View.VISIBLE);

            adapter = new ThongBaoAdapter(requireContext(), list);
            rvNotifications.setLayoutManager(new LinearLayoutManager(requireContext()));
            rvNotifications.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        taiDanhSachThongBao();
    }
}
