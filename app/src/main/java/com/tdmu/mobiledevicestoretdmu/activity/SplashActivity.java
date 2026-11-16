package com.tdmu.mobiledevicestoretdmu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;

public class SplashActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        databaseHelper = new DatabaseHelper(this);

        // Khởi chạy Database Seeding trong background nếu database trống
        new Thread(() -> {
            databaseHelper.seedDatabaseIfEmpty();
        }).start();

        // Chuyển màn hình sau 1.2 giây
        new Handler(Looper.getMainLooper()).postDelayed(this::dieuHuongManHinh, 1200);
    }

    private void dieuHuongManHinh() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int maTK = sharedPreferences.getInt("MaTK", -1);
        int quyen = sharedPreferences.getInt("Quyen", -1);

        Intent intent;
        if (maTK != -1) {
            if (quyen == 1) {
                intent = new Intent(SplashActivity.this, TrangChuAdminActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, TrangChuUserActivity.class);
            }
        } else {
            intent = new Intent(SplashActivity.this, DangNhapActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
