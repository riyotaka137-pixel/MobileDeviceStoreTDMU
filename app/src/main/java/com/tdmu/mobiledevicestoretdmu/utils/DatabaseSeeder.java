package com.tdmu.mobiledevicestoretdmu.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DatabaseSeeder {

    private static final String TAG = "DatabaseSeeder";
    private static final String SEED_DATA_FILE = "seed_data.json";
    private static final String SEED_IMAGES_DIR = "seed_images/";

    /**
     * Hàm chính thực hiện seeding dữ liệu nếu SQLite đang trống.
     * Hoạt động an toàn, idempotent (chỉ chạy 1 lần duy nhất khi database rỗng).
     */
    public static synchronized boolean seedDatabase(Context context, DatabaseHelper dbHelper) {
        if (context == null || dbHelper == null) return false;

        // 1. Kiểm tra an toàn: Chỉ seed khi CẢ SanPham VÀ DanhMuc đều trống
        if (!isDatabaseEmpty(dbHelper)) {
            Log.d(TAG, "Cơ sở dữ liệu đã có dữ liệu, bỏ qua Database Seeding để bảo toàn dữ liệu người dùng.");
            return false;
        }

        Log.i(TAG, "Phát hiện database trống. Bắt đầu quá trình nạp dữ liệu mẫu (Database Seeding)...");

        // 2. Đọc file JSON từ assets
        String jsonString = loadJSONFromAsset(context, SEED_DATA_FILE);
        if (jsonString == null || jsonString.trim().isEmpty()) {
            Log.e(TAG, "Không thể đọc file " + SEED_DATA_FILE + " từ assets.");
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            JSONObject root = new JSONObject(jsonString);

            // 3. Nạp DanhMuc
            Map<String, Long> mapDanhMuc = new HashMap<>();
            JSONArray arrDanhMuc = root.optJSONArray("danh_muc");
            if (arrDanhMuc != null) {
                for (int i = 0; i < arrDanhMuc.length(); i++) {
                    JSONObject obj = arrDanhMuc.getJSONObject(i);
                    String tenDM = obj.optString("ten_danh_muc", "").trim();
                    String moTa = obj.optString("mo_ta", "").trim();
                    String hinhAnh = obj.optString("hinh_anh", "").trim();

                    String savedImagePath = copySeedImageIfAvailable(context, hinhAnh, "category_images");

                    ContentValues cv = new ContentValues();
                    cv.put("TenDanhMuc", tenDM);
                    cv.put("MoTa", moTa);
                    cv.put("HinhAnh", savedImagePath);
                    long id = db.insert("DanhMuc", null, cv);
                    if (id != -1 && !tenDM.isEmpty()) {
                        mapDanhMuc.put(tenDM.toLowerCase(), id);
                    }
                }
            }

            // 4. Nạp ThuongHieu
            JSONArray arrThuongHieu = root.optJSONArray("thuong_hieu");
            if (arrThuongHieu != null) {
                for (int i = 0; i < arrThuongHieu.length(); i++) {
                    JSONObject obj = arrThuongHieu.getJSONObject(i);
                    String tenTH = obj.optString("ten_thuong_hieu", "").trim();
                    String moTa = obj.optString("mo_ta", "").trim();
                    String hinhAnh = obj.optString("hinh_anh", "").trim();

                    String savedImagePath = copySeedImageIfAvailable(context, hinhAnh, "brand_images");

                    ContentValues cv = new ContentValues();
                    cv.put("TenThuongHieu", tenTH);
                    cv.put("MoTa", moTa);
                    cv.put("HinhAnh", savedImagePath);
                    db.insert("ThuongHieu", null, cv);
                }
            }

            // 5. Nạp SanPham
            JSONArray arrSanPham = root.optJSONArray("san_pham");
            if (arrSanPham != null) {
                for (int i = 0; i < arrSanPham.length(); i++) {
                    JSONObject obj = arrSanPham.getJSONObject(i);
                    String tenSP = obj.optString("ten_sp", "").trim();
                    String tenDanhMuc = obj.optString("danh_muc", "").trim();
                    String thuongHieu = obj.optString("thuong_hieu", "").trim();
                    int gia = obj.optInt("gia", 0);
                    int soLuong = obj.optInt("so_luong", 0);
                    String moTa = obj.optString("mo_ta", "").trim();
                    String hinhAnh = obj.optString("hinh_anh", "").trim();
                    int phanTramGiam = obj.optInt("phan_tram_giam", 0);

                    // Tìm MaDanhMuc tương ứng
                    long maDanhMuc = 1;
                    if (mapDanhMuc.containsKey(tenDanhMuc.toLowerCase())) {
                        maDanhMuc = mapDanhMuc.get(tenDanhMuc.toLowerCase());
                    }

                    String savedImagePath = copySeedImageIfAvailable(context, hinhAnh, "product_images");

                    ContentValues cv = new ContentValues();
                    cv.put("TenSP", tenSP);
                    cv.put("MaDanhMuc", (int) maDanhMuc);
                    cv.put("Gia", gia);
                    cv.put("SoLuong", soLuong);
                    cv.put("MoTa", moTa);
                    cv.put("HinhAnh", savedImagePath);
                    cv.put("ThuongHieu", thuongHieu);
                    cv.put("PhanTramGiam", phanTramGiam);
                    db.insert("SanPham", null, cv);
                }
            }

            // 6. Nạp Banner
            JSONArray arrBanner = root.optJSONArray("banner");
            if (arrBanner != null) {
                for (int i = 0; i < arrBanner.length(); i++) {
                    JSONObject obj = arrBanner.getJSONObject(i);
                    String tenBanner = obj.optString("ten_banner", "").trim();
                    String moTa = obj.optString("mo_ta", "").trim();
                    String hinhAnh = obj.optString("hinh_anh", "").trim();

                    String savedImagePath = copySeedImageIfAvailable(context, hinhAnh, "banner_images");

                    ContentValues cv = new ContentValues();
                    cv.put("TenBanner", tenBanner);
                    cv.put("HinhAnh", savedImagePath);
                    cv.put("MoTa", moTa);
                    db.insert("Banner", null, cv);
                }
            }

            // 7. Nạp GiamGia (Voucher)
            JSONArray arrGiamGia = root.optJSONArray("giam_gia");
            if (arrGiamGia != null) {
                for (int i = 0; i < arrGiamGia.length(); i++) {
                    JSONObject obj = arrGiamGia.getJSONObject(i);
                    String maCode = obj.optString("ma_code", "").trim().toUpperCase();
                    int phanTramGiam = obj.optInt("phan_tram_giam", 0);
                    int tienGiamToiDa = obj.optInt("tien_giam_toi_da", 0);
                    int donToiThieu = obj.optInt("don_toi_thieu", 0);
                    String ngayHetHan = obj.optString("ngay_het_han", "").trim();
                    String moTa = obj.optString("mo_ta", "").trim();
                    int trangThai = obj.optInt("trang_thai", 1);

                    ContentValues cv = new ContentValues();
                    cv.put("MaCode", maCode);
                    cv.put("PhanTramGiam", phanTramGiam);
                    cv.put("TienGiamToiDa", tienGiamToiDa);
                    cv.put("DonToiThieu", donToiThieu);
                    cv.put("NgayHetHan", ngayHetHan);
                    cv.put("MoTa", moTa);
                    cv.put("TrangThai", trangThai);
                    db.insert("GiamGia", null, cv);
                }
            }

            db.setTransactionSuccessful();
            Log.i(TAG, "Database Seeding hoàn tất thành công 100%!");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Lỗi xảy ra trong quá trình Database Seeding", e);
            return false;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Kiểm tra xem cả bảng SanPham và DanhMuc có đang trống không.
     */
    public static boolean isDatabaseEmpty(DatabaseHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int countSP = 0;
        int countDM = 0;

        try {
            Cursor cursorSP = db.rawQuery("SELECT COUNT(*) FROM SanPham", null);
            if (cursorSP.moveToFirst()) {
                countSP = cursorSP.getInt(0);
            }
            cursorSP.close();

            Cursor cursorDM = db.rawQuery("SELECT COUNT(*) FROM DanhMuc", null);
            if (cursorDM.moveToFirst()) {
                countDM = cursorDM.getInt(0);
            }
            cursorDM.close();
        } catch (Exception e) {
            Log.e(TAG, "Lỗi kiểm tra trạng thái bảng", e);
            return false;
        }

        return countSP == 0 && countDM == 0;
    }

    /**
     * Kiểm tra nếu file ảnh tồn tại trong assets/seed_images/, copy file sang Internal Storage
     * và trả về đường dẫn file thực tế. Nếu không có trong assets, trả về chuỗi rỗng "".
     */
    public static String copySeedImageIfAvailable(Context context, String imageName, String targetSubFolder) {
        if (imageName == null || imageName.trim().isEmpty()) {
            return "";
        }

        String fileName = imageName.trim();
        String assetPath = SEED_IMAGES_DIR + fileName;
        AssetManager assetManager = context.getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = assetManager.open(assetPath);

            File storageDir = new File(context.getFilesDir(), targetSubFolder);
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            String uniqueName = "img_" + System.currentTimeMillis() + "_" + fileName;
            File outputFile = new File(storageDir, uniqueName);

            outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();

            return outputFile.getAbsolutePath();
        } catch (IOException e) {
            // File ảnh chưa được đưa vào assets/seed_images/ -> Trả về chuỗi rỗng an toàn
            return "";
        } finally {
            if (outputStream != null) {
                try { outputStream.close(); } catch (IOException ignored) {}
            }
            if (inputStream != null) {
                try { inputStream.close(); } catch (IOException ignored) {}
            }
        }
    }

    /**
     * Nạp chuỗi JSON từ file asset.
     */
    public static String loadJSONFromAsset(Context context, String fileName) {
        try (InputStream is = context.getAssets().open(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception ex) {
            Log.e(TAG, "Không thể nạp asset: " + fileName, ex);
            return null;
        }
    }
}
