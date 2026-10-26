package com.tdmu.mobiledevicestoretdmu.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.fragment.UserCartFragment;
import com.tdmu.mobiledevicestoretdmu.fragment.UserHomeFragment;
import com.tdmu.mobiledevicestoretdmu.fragment.UserProfileFragment;

public class TrangChuUserActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationViewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu_user);

        bottomNavigationViewUser = findViewById(R.id.bottom_navigation_user);

        // Load UserHomeFragment mac dinh ban dau
        if (savedInstanceState == null) {
            loadFragment(new UserHomeFragment());
        }

        bottomNavigationViewUser.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return loadFragment(new UserHomeFragment());
            } else if (itemId == R.id.nav_cart) {
                return loadFragment(new UserCartFragment());
            } else if (itemId == R.id.nav_profile) {
                return loadFragment(new UserProfileFragment());
            }
            return false;
        });
    }

    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container_user, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void chuyenTabGioHang() {
        if (bottomNavigationViewUser != null) {
            bottomNavigationViewUser.setSelectedItemId(R.id.nav_cart);
        }
    }

    public View getCartNavItemView() {
        return bottomNavigationViewUser != null ? bottomNavigationViewUser.findViewById(R.id.nav_cart) : null;
    }
}
