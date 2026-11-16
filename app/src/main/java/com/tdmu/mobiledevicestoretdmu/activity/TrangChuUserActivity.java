package com.tdmu.mobiledevicestoretdmu.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.fragment.UserCartFragment;
import com.tdmu.mobiledevicestoretdmu.fragment.UserDiscountsFragment;
import com.tdmu.mobiledevicestoretdmu.fragment.UserHomeFragment;
import com.tdmu.mobiledevicestoretdmu.fragment.UserNotificationsFragment;
import com.tdmu.mobiledevicestoretdmu.fragment.UserProfileFragment;

public class TrangChuUserActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationViewUser;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu_user);

        databaseHelper = new DatabaseHelper(this);
        bottomNavigationViewUser = findViewById(R.id.bottom_navigation_user);

        // Load UserHomeFragment mac dinh ban dau
        if (savedInstanceState == null) {
            loadFragment(new UserHomeFragment());
        }

        bottomNavigationViewUser.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return loadFragment(new UserHomeFragment());
            } else if (itemId == R.id.nav_discount) {
                return loadFragment(new UserDiscountsFragment());
            } else if (itemId == R.id.nav_notification) {
                return loadFragment(new UserNotificationsFragment());
            } else if (itemId == R.id.nav_cart) {
                return loadFragment(new UserCartFragment());
            } else if (itemId == R.id.nav_profile) {
                return loadFragment(new UserProfileFragment());
            }
            return false;
        });

        updateNotificationBadge();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNotificationBadge();
    }

    public void updateNotificationBadge() {
        if (bottomNavigationViewUser == null) return;

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int maTK = sharedPreferences.getInt("MaTK", -1);

        if (maTK > 0) {
            if (databaseHelper == null) {
                databaseHelper = new DatabaseHelper(this);
            }
            int unreadCount = databaseHelper.getUnreadThongBaoCount(maTK);
            if (unreadCount > 0) {
                BadgeDrawable badge = bottomNavigationViewUser.getOrCreateBadge(R.id.nav_notification);
                badge.setBackgroundColor(Color.parseColor("#D32F2F"));
                badge.setBadgeTextColor(Color.WHITE);
                badge.setNumber(unreadCount);
                badge.setVisible(true);
            } else {
                bottomNavigationViewUser.removeBadge(R.id.nav_notification);
            }
        } else {
            bottomNavigationViewUser.removeBadge(R.id.nav_notification);
        }
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

    // Chuyển sang Tab Khuyến mãi khi bấm vào Banner (không cần trang trung gian)
    public void chuyenTabKhuyenMai() {
        if (bottomNavigationViewUser != null) {
            bottomNavigationViewUser.setSelectedItemId(R.id.nav_discount);
        }
    }

    public View getCartNavItemView() {
        return bottomNavigationViewUser != null ? bottomNavigationViewUser.findViewById(R.id.nav_cart) : null;
    }
}
