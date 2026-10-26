package com.tdmu.mobiledevicestoretdmu.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.fragment.AdminDashboardFragment;
import com.tdmu.mobiledevicestoretdmu.fragment.AdminManagementFragment;
import com.tdmu.mobiledevicestoretdmu.fragment.AdminProfileFragment;

public class TrangChuAdminActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationViewAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu_admin);

        bottomNavigationViewAdmin = findViewById(R.id.bottom_navigation_admin);

        // Load AdminDashboardFragment mac dinh ban dau
        if (savedInstanceState == null) {
            loadFragment(new AdminDashboardFragment());
        }

        bottomNavigationViewAdmin.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                return loadFragment(new AdminDashboardFragment());
            } else if (itemId == R.id.nav_management) {
                return loadFragment(new AdminManagementFragment());
            } else if (itemId == R.id.nav_profile) {
                return loadFragment(new AdminProfileFragment());
            }
            return false;
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container_admin, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
