package com.tdmu.mobiledevicestoretdmu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tdmu.mobiledevicestoretdmu.model.ChiTietGioHang;
import com.tdmu.mobiledevicestoretdmu.model.DanhMuc;
import com.tdmu.mobiledevicestoretdmu.model.DonHang;
import com.tdmu.mobiledevicestoretdmu.model.SanPham;
import com.tdmu.mobiledevicestoretdmu.model.TaiKhoan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Tên cơ sở dữ liệu và phiên bản Production
    private static final String DATABASE_NAME = "BanThietBiTDMU.db";
    private static final int DATABASE_VERSION = 8;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Tạo bảng TaiKhoan
        String createTableTaiKhoan = "CREATE TABLE TaiKhoan (" +
                "MaTK INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "HoTen TEXT NOT NULL, " +
                "Email TEXT NOT NULL UNIQUE, " +
                "MatKhau TEXT NOT NULL, " +
                "SoDienThoai TEXT, " +
                "DiaChi TEXT, " +
                "Quyen INTEGER, " +
                "TrangThai INTEGER DEFAULT 1)";
        db.execSQL(createTableTaiKhoan);

        // 2. Tạo bảng DanhMuc
        String createTableDanhMuc = "CREATE TABLE DanhMuc (" +
                "MaDanhMuc INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TenDanhMuc TEXT NOT NULL, " +
                "MoTa TEXT)";
        db.execSQL(createTableDanhMuc);

        // 3. Tạo bảng SanPham
        String createTableSanPham = "CREATE TABLE SanPham (" +
                "MaSP INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TenSP TEXT NOT NULL, " +
                "MaDanhMuc INTEGER, " +
                "Gia INTEGER, " +
                "SoLuong INTEGER, " +
                "MoTa TEXT, " +
                "HinhAnh TEXT, " +
                "ThuongHieu TEXT, " +
                "FOREIGN KEY(MaDanhMuc) REFERENCES DanhMuc(MaDanhMuc))";
        db.execSQL(createTableSanPham);

        // 4. Tạo bảng GioHang
        String createTableGioHang = "CREATE TABLE GioHang (" +
                "MaGioHang INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MaTK INTEGER, " +
                "NgayTao TEXT, " +
                "FOREIGN KEY(MaTK) REFERENCES TaiKhoan(MaTK))";
        db.execSQL(createTableGioHang);

        // 5. Tạo bảng ChiTietGioHang
        String createTableChiTietGioHang = "CREATE TABLE ChiTietGioHang (" +
                "MaCTGH INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MaGioHang INTEGER, " +
                "MaSP INTEGER, " +
                "SoLuong INTEGER, " +
                "DonGia INTEGER, " +
                "FOREIGN KEY(MaGioHang) REFERENCES GioHang(MaGioHang), " +
                "FOREIGN KEY(MaSP) REFERENCES SanPham(MaSP))";
        db.execSQL(createTableChiTietGioHang);

        // 6. Tạo bảng DonHang (Bao gồm TenNguoiNhan, DiaChiNhan, SoDienThoai chốt cứng)
        String createTableDonHang = "CREATE TABLE DonHang (" +
                "MaDH INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MaTK INTEGER, " +
                "TenNguoiNhan TEXT, " +
                "NgayDat TEXT, " +
                "TongTien INTEGER, " +
                "DiaChiNhan TEXT, " +
                "SoDienThoai TEXT, " +
                "TrangThai INTEGER, " +
                "PhuongThucThanhToan TEXT DEFAULT 'COD', " +
                "FOREIGN KEY(MaTK) REFERENCES TaiKhoan(MaTK))";
        db.execSQL(createTableDonHang);

        // 7. Tạo bảng ChiTietDonHang
        String createTableChiTietDonHang = "CREATE TABLE ChiTietDonHang (" +
                "MaCTDH INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MaDH INTEGER, " +
                "MaSP INTEGER, " +
                "SoLuong INTEGER, " +
                "DonGia INTEGER, " +
                "FOREIGN KEY(MaDH) REFERENCES DonHang(MaDH), " +
                "FOREIGN KEY(MaSP) REFERENCES SanPham(MaSP))";
        db.execSQL(createTableChiTietDonHang);

        // --- KHỞI TẠO SẴN TÀI KHOẢN HỆ THỐNG ---
        // 1. Tài khoản Admin mặc định
        String insertAdmin = "INSERT INTO TaiKhoan (HoTen, Email, MatKhau, Quyen, TrangThai) " +
                "VALUES ('Admin TDMU', 'admin@gmail.com', 'admin', 1, 1)";
        db.execSQL(insertAdmin);

        // 2. Tài khoản Khách hàng mẫu mặc định
        String insertUser = "INSERT INTO TaiKhoan (HoTen, Email, MatKhau, SoDienThoai, DiaChi, Quyen, TrangThai) " +
                "VALUES ('Khách Hàng Mẫu', 'user@gmail.com', 'user', '0912345678', 'Thủ Dầu Một, Bình Dương', 0, 1)";
        db.execSQL(insertUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ChiTietDonHang");
        db.execSQL("DROP TABLE IF EXISTS DonHang");
        db.execSQL("DROP TABLE IF EXISTS ChiTietGioHang");
        db.execSQL("DROP TABLE IF EXISTS GioHang");
        db.execSQL("DROP TABLE IF EXISTS SanPham");
        db.execSQL("DROP TABLE IF EXISTS DanhMuc");
        db.execSQL("DROP TABLE IF EXISTS TaiKhoan");

        onCreate(db);
    }

    // Kiểm tra xem Email đã tồn tại chưa
    public boolean checkEmailExist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TaiKhoan WHERE Email = ?", new String[]{email});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Thêm tài khoản mới
    public boolean insertTaiKhoan(String hoTen, String email, String matKhau, String sdt, String diaChi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HoTen", hoTen);
        values.put("Email", email);
        values.put("MatKhau", matKhau);
        values.put("SoDienThoai", sdt);
        values.put("DiaChi", diaChi);
        values.put("Quyen", 0);
        values.put("TrangThai", 1);

        long result = db.insert("TaiKhoan", null, values);
        return result != -1;
    }

    // Kiểm tra đăng nhập
    public TaiKhoan checkLogin(String email, String matKhau) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TaiKhoan WHERE Email = ? AND MatKhau = ?", new String[]{email, matKhau});

        TaiKhoan tk = null;
        if (cursor.moveToFirst()) {
            tk = new TaiKhoan();
            tk.setMaTK(cursor.getInt(0));
            tk.setHoTen(cursor.getString(1));
            tk.setEmail(cursor.getString(2));
            tk.setMatKhau(cursor.getString(3));
            tk.setSoDienThoai(cursor.getString(4));
            tk.setDiaChi(cursor.getString(5));
            tk.setQuyen(cursor.getInt(6));
            if (cursor.getColumnCount() > 7) {
                tk.setTrangThai(cursor.getInt(7));
            } else {
                tk.setTrangThai(1);
            }
        }
        cursor.close();
        return tk;
    }

    // Lấy thông tin tài khoản theo MaTK
    public TaiKhoan getTaiKhoanById(int maTK) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TaiKhoan WHERE MaTK = ?", new String[]{String.valueOf(maTK)});
        TaiKhoan tk = null;
        if (cursor.moveToFirst()) {
            tk = new TaiKhoan();
            tk.setMaTK(cursor.getInt(0));
            tk.setHoTen(cursor.getString(1));
            tk.setEmail(cursor.getString(2));
            tk.setMatKhau(cursor.getString(3));
            tk.setSoDienThoai(cursor.getString(4));
            tk.setDiaChi(cursor.getString(5));
            tk.setQuyen(cursor.getInt(6));
            if (cursor.getColumnCount() > 7) {
                tk.setTrangThai(cursor.getInt(7));
            } else {
                tk.setTrangThai(1);
            }
        }
        cursor.close();
        return tk;
    }

    // Cập nhật thông tin cá nhân khách hàng
    public boolean updateThongTinCaNhan(int maTK, String hoTen, String sdt, String diaChi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HoTen", hoTen);
        values.put("SoDienThoai", sdt);
        values.put("DiaChi", diaChi);
        int rows = db.update("TaiKhoan", values, "MaTK = ?", new String[]{String.valueOf(maTK)});
        return rows > 0;
    }

    // Đổi mật khẩu
    public boolean doiMatKhau(int maTK, String matKhauCu, String matKhauMoi) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT MatKhau FROM TaiKhoan WHERE MaTK = ?", new String[]{String.valueOf(maTK)});
        if (cursor.moveToFirst()) {
            String currentPass = cursor.getString(0);
            cursor.close();
            if (currentPass.equals(matKhauCu)) {
                ContentValues values = new ContentValues();
                values.put("MatKhau", matKhauMoi);
                int rows = db.update("TaiKhoan", values, "MaTK = ?", new String[]{String.valueOf(maTK)});
                return rows > 0;
            }
        } else {
            cursor.close();
        }
        return false;
    }

    // Lấy danh sách tất cả Khách hàng (Quyen = 0)
    public List<TaiKhoan> getAllKhachHang() {
        List<TaiKhoan> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TaiKhoan WHERE Quyen = 0 ORDER BY MaTK DESC", null);
        if (cursor.moveToFirst()) {
            do {
                TaiKhoan tk = new TaiKhoan();
                tk.setMaTK(cursor.getInt(0));
                tk.setHoTen(cursor.getString(1));
                tk.setEmail(cursor.getString(2));
                tk.setMatKhau(cursor.getString(3));
                tk.setSoDienThoai(cursor.getString(4));
                tk.setDiaChi(cursor.getString(5));
                tk.setQuyen(cursor.getInt(6));
                if (cursor.getColumnCount() > 7) {
                    tk.setTrangThai(cursor.getInt(7));
                } else {
                    tk.setTrangThai(1);
                }
                list.add(tk);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Cập nhật trạng thái tài khoản
    public boolean updateTrangThaiTaiKhoan(int maTK, int trangThai) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TrangThai", trangThai);
        int rows = db.update("TaiKhoan", values, "MaTK = ?", new String[]{String.valueOf(maTK)});
        return rows > 0;
    }

    // Xóa tài khoản
    public boolean deleteTaiKhoan(int maTK) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("TaiKhoan", "MaTK = ?", new String[]{String.valueOf(maTK)});
        return rows > 0;
    }

    // Lấy chuỗi danh sách tên sản phẩm trong Đơn Hàng (JOIN giữa ChiTietDonHang và SanPham)
    public String getChiTietDonHangString(int maDonHang) {
        StringBuilder builder = new StringBuilder();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SanPham.TenSP, ChiTietDonHang.SoLuong " +
                "FROM ChiTietDonHang " +
                "INNER JOIN SanPham ON ChiTietDonHang.MaSP = SanPham.MaSP " +
                "WHERE ChiTietDonHang.MaDH = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(maDonHang)});
        if (cursor.moveToFirst()) {
            do {
                String tenSP = cursor.getString(0);
                int soLuong = cursor.getInt(1);
                if (builder.length() > 0) {
                    builder.append("\n");
                }
                builder.append("• ").append(tenSP).append(" (x").append(soLuong).append(")");
            } while (cursor.moveToNext());
        }
        cursor.close();
        return builder.length() > 0 ? builder.toString() : "Chưa có thông tin sản phẩm";
    }

    // Lấy danh sách đơn hàng của một User (Đọc thông tin chốt cứng từ DonHang)
    public List<DonHang> getDonHangByMaTK(int maTK) {
        List<DonHang> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DonHang WHERE MaTK = ? ORDER BY MaDH DESC", new String[]{String.valueOf(maTK)});
        if (cursor.moveToFirst()) {
            do {
                DonHang dh = new DonHang();
                dh.setMaDH(cursor.getInt(0));
                dh.setMaTK(cursor.getInt(1));
                dh.setTenNguoiNhan(cursor.getColumnCount() > 2 ? cursor.getString(2) : "Khách hàng");
                dh.setNgayDat(cursor.getColumnCount() > 3 ? cursor.getString(3) : "");
                dh.setTongTien(cursor.getColumnCount() > 4 ? cursor.getInt(4) : 0);
                dh.setDiaChiNhan(cursor.getColumnCount() > 5 ? cursor.getString(5) : "");
                dh.setSoDienThoai(cursor.getColumnCount() > 6 ? cursor.getString(6) : "");
                dh.setTrangThai(cursor.getColumnCount() > 7 ? cursor.getInt(7) : 0);
                dh.setPhuongThucThanhToan(cursor.getColumnCount() > 8 && !cursor.isNull(8) ? cursor.getString(8) : "COD");
                list.add(dh);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // --- CÁC HÀM THỐNG KÊ DASHBOARD ADMIN ---
    public long getTongDoanhThu() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(TongTien) FROM DonHang WHERE TrangThai != 3", null);
        long tongDoanhThu = 0;
        if (cursor.moveToFirst()) {
            tongDoanhThu = cursor.getLong(0);
        }
        cursor.close();
        return tongDoanhThu;
    }

    public int getSoDonHangChoDuyet() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM DonHang WHERE TrangThai = 0", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getTongSoKhachHang() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM TaiKhoan WHERE Quyen = 0", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // Lấy doanh thu 7 ngày gần nhất (Map từ chuỗi Ngày 'dd/MM' sang Doanh thu Long)
    public Map<String, Long> getDoanhThu7NgayQua() {
        Map<String, Long> mapDoanhThu = new LinkedHashMap<>();

        SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -6);

        Map<String, String> mapFullToDisplay = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            String fullDate = sdfFull.format(calendar.getTime());
            String displayDate = sdfDisplay.format(calendar.getTime());
            mapFullToDisplay.put(fullDate, displayDate);
            mapDoanhThu.put(displayDate, 0L);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT substr(NgayDat, 1, 10) AS Ngay, SUM(TongTien) AS DoanhThu " +
                "FROM DonHang WHERE TrangThai != 3 AND substr(NgayDat, 1, 10) >= ? " +
                "GROUP BY substr(NgayDat, 1, 10)";

        String minDate = mapFullToDisplay.keySet().iterator().next();
        Cursor cursor = db.rawQuery(query, new String[]{minDate});

        if (cursor.moveToFirst()) {
            do {
                String fullDate = cursor.getString(0);
                long doanhThu = cursor.getLong(1);
                String displayDate = mapFullToDisplay.get(fullDate);
                if (displayDate != null) {
                    mapDoanhThu.put(displayDate, doanhThu);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return mapDoanhThu;
    }

    // Lấy tỷ trọng sản phẩm/doanh thu theo Danh Mục (Map từ Tên danh mục sang Giá trị Float)
    public Map<String, Float> getTyTrongDanhMuc() {
        Map<String, Float> mapTyTrong = new LinkedHashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String querySales = "SELECT DanhMuc.TenDanhMuc, SUM(ChiTietDonHang.SoLuong * ChiTietDonHang.DonGia) " +
                "FROM ChiTietDonHang " +
                "INNER JOIN DonHang ON ChiTietDonHang.MaDH = DonHang.MaDH " +
                "INNER JOIN SanPham ON ChiTietDonHang.MaSP = SanPham.MaSP " +
                "INNER JOIN DanhMuc ON SanPham.MaDanhMuc = DanhMuc.MaDanhMuc " +
                "WHERE DonHang.TrangThai != 3 " +
                "GROUP BY DanhMuc.MaDanhMuc, DanhMuc.TenDanhMuc";

        Cursor cursor = db.rawQuery(querySales, null);
        if (cursor.moveToFirst()) {
            do {
                String tenDM = cursor.getString(0);
                float tongTien = cursor.getFloat(1);
                mapTyTrong.put(tenDM, tongTien);
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (mapTyTrong.isEmpty()) {
            String queryProducts = "SELECT DanhMuc.TenDanhMuc, COUNT(SanPham.MaSP) " +
                    "FROM DanhMuc " +
                    "LEFT JOIN SanPham ON DanhMuc.MaDanhMuc = SanPham.MaDanhMuc " +
                    "GROUP BY DanhMuc.MaDanhMuc, DanhMuc.TenDanhMuc";
            Cursor cursorCount = db.rawQuery(queryProducts, null);
            if (cursorCount.moveToFirst()) {
                do {
                    String tenDM = cursorCount.getString(0);
                    float count = cursorCount.getFloat(1);
                    if (count > 0) {
                        mapTyTrong.put(tenDM, count);
                    }
                } while (cursorCount.moveToNext());
            }
            cursorCount.close();
        }

        return mapTyTrong;
    }

    // Lấy toàn bộ danh mục
    public List<DanhMuc> getAllDanhMuc() {
        List<DanhMuc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DanhMuc", null);
        if (cursor.moveToFirst()) {
            do {
                DanhMuc dm = new DanhMuc();
                dm.setMaDanhMuc(cursor.getInt(0));
                dm.setTenDanhMuc(cursor.getString(1));
                dm.setMoTa(cursor.getString(2));
                list.add(dm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Thêm danh mục mới
    public void insertDanhMuc(String tenDanhMuc, String moTa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenDanhMuc", tenDanhMuc);
        values.put("MoTa", moTa);
        db.insert("DanhMuc", null, values);
    }

    // Thêm sản phẩm
    public void insertSanPham(String tenSP, int maDanhMuc, int gia, int soLuong, String moTa, String thuongHieu, String hinhAnh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenSP", tenSP);
        values.put("MaDanhMuc", maDanhMuc);
        values.put("Gia", gia);
        values.put("SoLuong", soLuong);
        values.put("MoTa", moTa);
        values.put("ThuongHieu", thuongHieu);
        values.put("HinhAnh", hinhAnh != null ? hinhAnh : "");
        db.insert("SanPham", null, values);
    }

    public void insertSanPham(String tenSP, int maDanhMuc, int gia, int soLuong, String moTa, String thuongHieu) {
        insertSanPham(tenSP, maDanhMuc, gia, soLuong, moTa, thuongHieu, "");
    }

    // Lấy tất cả sản phẩm
    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SanPham", null);
        if (cursor.moveToFirst()) {
            do {
                SanPham sp = new SanPham();
                sp.setMaSP(cursor.getInt(0));
                sp.setTenSP(cursor.getString(1));
                sp.setMaDanhMuc(cursor.getInt(2));
                sp.setGia(cursor.getInt(3));
                sp.setSoLuong(cursor.getInt(4));
                sp.setMoTa(cursor.getString(5));
                sp.setHinhAnh(cursor.getString(6));
                sp.setThuongHieu(cursor.getString(7));
                list.add(sp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Lấy sản phẩm theo Danh Mục
    public List<SanPham> getSanPhamTheoDanhMuc(int maDanhMuc) {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SanPham WHERE MaDanhMuc = ?", new String[]{String.valueOf(maDanhMuc)});
        if (cursor.moveToFirst()) {
            do {
                SanPham sp = new SanPham();
                sp.setMaSP(cursor.getInt(0));
                sp.setTenSP(cursor.getString(1));
                sp.setMaDanhMuc(cursor.getInt(2));
                sp.setGia(cursor.getInt(3));
                sp.setSoLuong(cursor.getInt(4));
                sp.setMoTa(cursor.getString(5));
                sp.setHinhAnh(cursor.getString(6));
                sp.setThuongHieu(cursor.getString(7));
                list.add(sp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Lấy thông tin 1 sản phẩm
    public SanPham getSanPhamById(int maSP) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SanPham WHERE MaSP = ?", new String[]{String.valueOf(maSP)});
        SanPham sp = null;
        if (cursor.moveToFirst()) {
            sp = new SanPham();
            sp.setMaSP(cursor.getInt(0));
            sp.setTenSP(cursor.getString(1));
            sp.setMaDanhMuc(cursor.getInt(2));
            sp.setGia(cursor.getInt(3));
            sp.setSoLuong(cursor.getInt(4));
            sp.setMoTa(cursor.getString(5));
            sp.setHinhAnh(cursor.getString(6));
            sp.setThuongHieu(cursor.getString(7));
        }
        cursor.close();
        return sp;
    }

    // Lấy hoặc tạo mới Giỏ hàng cho User
    private int getOrCreateGioHang(int maTK) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaGioHang FROM GioHang WHERE MaTK = ?", new String[]{String.valueOf(maTK)});
        if (cursor.moveToFirst()) {
            int maGH = cursor.getInt(0);
            cursor.close();
            return maGH;
        } else {
            cursor.close();
            ContentValues values = new ContentValues();
            values.put("MaTK", maTK);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            values.put("NgayTao", sdf.format(new Date()));
            return (int) db.insert("GioHang", null, values);
        }
    }

    // Thêm vào giỏ hàng (GỘP SẢN PHẨM TRÙNG LẶP & KIỂM TRA TỒN KHO)
    public boolean themVaoGioHang(int maTK, int maSP, int donGia) {
        int maGioHang = getOrCreateGioHang(maTK);
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra tồn kho của sản phẩm trong bảng SanPham
        int soLuongTon = 0;
        Cursor cursorSP = db.rawQuery("SELECT SoLuong FROM SanPham WHERE MaSP = ?", new String[]{String.valueOf(maSP)});
        if (cursorSP.moveToFirst()) {
            soLuongTon = cursorSP.getInt(0);
        }
        cursorSP.close();

        // Nếu sản phẩm đã hết hàng -> Không cho thêm vào giỏ
        if (soLuongTon < 1) {
            return false;
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Cursor cursorGH = db.rawQuery("SELECT MaCTGH, SoLuong FROM ChiTietGioHang WHERE MaGioHang = ? AND MaSP = ?",
                new String[]{String.valueOf(maGioHang), String.valueOf(maSP)});

        if (cursorGH.moveToFirst()) {
            int maCTGH = cursorGH.getInt(0);
            int soLuongCu = cursorGH.getInt(1);
            cursorGH.close();

            // Nếu số lượng cộng dồn vượt quá tồn kho -> Không cho thêm
            if (soLuongCu + 1 > soLuongTon) {
                return false;
            }

            // Gộp trùng: UPDATE cộng dồn thêm 1
            ContentValues values = new ContentValues();
            values.put("SoLuong", soLuongCu + 1);
            int rows = db.update("ChiTietGioHang", values, "MaCTGH = ?", new String[]{String.valueOf(maCTGH)});
            return rows > 0;
        } else {
            cursorGH.close();
            // Chưa có -> INSERT sản phẩm mới vào giỏ
            ContentValues values = new ContentValues();
            values.put("MaGioHang", maGioHang);
            values.put("MaSP", maSP);
            values.put("SoLuong", 1);
            values.put("DonGia", donGia);
            long result = db.insert("ChiTietGioHang", null, values);
            return result != -1;
        }
    }

    // Tìm kiếm sản phẩm theo tên
    public List<SanPham> searchSanPham(String keyword) {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM SanPham WHERE TenSP LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + keyword + "%"});

        if (cursor.moveToFirst()) {
            do {
                SanPham sp = new SanPham();
                sp.setMaSP(cursor.getInt(0));
                sp.setTenSP(cursor.getString(1));
                sp.setMaDanhMuc(cursor.getInt(2));
                sp.setGia(cursor.getInt(3));
                sp.setSoLuong(cursor.getInt(4));
                sp.setMoTa(cursor.getString(5));
                sp.setHinhAnh(cursor.getString(6));
                sp.setThuongHieu(cursor.getString(7));
                list.add(sp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Lấy danh sách giỏ hàng
    public List<ChiTietGioHang> getListCartItem(int maTK) {
        List<ChiTietGioHang> list = new ArrayList<>();
        int maGioHang = getOrCreateGioHang(maTK);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM ChiTietGioHang WHERE MaGioHang = ?", new String[]{String.valueOf(maGioHang)});
        if (cursor.moveToFirst()) {
            do {
                ChiTietGioHang ct = new ChiTietGioHang();
                ct.setMaCTGH(cursor.getInt(0));
                ct.setMaGioHang(cursor.getInt(1));
                ct.setMaSP(cursor.getInt(2));
                ct.setSoLuong(cursor.getInt(3));
                ct.setDonGia(cursor.getInt(4));
                list.add(ct);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Cập nhật số lượng sản phẩm trong giỏ
    public void updateSoLuongGioHang(int maCTGH, int soLuongMoi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SoLuong", soLuongMoi);
        db.update("ChiTietGioHang", values, "MaCTGH = ?", new String[]{String.valueOf(maCTGH)});
    }

    // Xóa sản phẩm khỏi giỏ
    public void deleteChiTietGioHang(int maCTGH) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ChiTietGioHang", "MaCTGH = ?", new String[]{String.valueOf(maCTGH)});
    }

    // Đặt hàng (Thêm đơn hàng với TenNguoiNhan chốt cứng, lưu chi tiết, trừ kho LẬP TỨC và xóa giỏ)
    public boolean placeOrder(int maTK, String tenNguoiNhan, String soDienThoai, String diaChiNhan, String phuongThucThanhToan, int tongTien, List<ChiTietGioHang> listCart) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues dhValues = new ContentValues();
            dhValues.put("MaTK", maTK);
            dhValues.put("TenNguoiNhan", tenNguoiNhan != null && !tenNguoiNhan.trim().isEmpty() ? tenNguoiNhan : "Khách hàng");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            dhValues.put("NgayDat", sdf.format(new Date()));
            dhValues.put("TongTien", tongTien);
            dhValues.put("DiaChiNhan", diaChiNhan);
            dhValues.put("SoDienThoai", soDienThoai);
            dhValues.put("TrangThai", 0);
            dhValues.put("PhuongThucThanhToan", phuongThucThanhToan != null ? phuongThucThanhToan : "COD");

            long maDH = db.insert("DonHang", null, dhValues);
            if (maDH == -1) return false;

            for (ChiTietGioHang ctgh : listCart) {
                ContentValues ctdhValues = new ContentValues();
                ctdhValues.put("MaDH", (int) maDH);
                ctdhValues.put("MaSP", ctgh.getMaSP());
                ctdhValues.put("SoLuong", ctgh.getSoLuong());
                ctdhValues.put("DonGia", ctgh.getDonGia());
                db.insert("ChiTietDonHang", null, ctdhValues);

                // TRỪ KHO LẬP TỨC KHI ĐẶT HÀNG THÀNH CÔNG
                db.execSQL("UPDATE SanPham SET SoLuong = SoLuong - ? WHERE MaSP = ?", new Object[]{ctgh.getSoLuong(), ctgh.getMaSP()});
            }

            if (!listCart.isEmpty()) {
                int maGioHang = listCart.get(0).getMaGioHang();
                db.delete("ChiTietGioHang", "MaGioHang = ?", new String[]{String.valueOf(maGioHang)});
            }

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }

    // Hủy đơn hàng và HOÀN TRẢ LẠI SỐ LƯỢNG TỒN KHO
    public boolean huyDonHang(int maDH) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            Cursor cursorDH = db.rawQuery("SELECT TrangThai FROM DonHang WHERE MaDH = ?", new String[]{String.valueOf(maDH)});
            if (cursorDH.moveToFirst()) {
                int trangThaiCu = cursorDH.getInt(0);
                cursorDH.close();

                if (trangThaiCu != 3) {
                    Cursor cursorCT = db.rawQuery("SELECT MaSP, SoLuong FROM ChiTietDonHang WHERE MaDH = ?", new String[]{String.valueOf(maDH)});
                    if (cursorCT.moveToFirst()) {
                        do {
                            int maSP = cursorCT.getInt(0);
                            int soLuong = cursorCT.getInt(1);
                            db.execSQL("UPDATE SanPham SET SoLuong = SoLuong + ? WHERE MaSP = ?", new Object[]{soLuong, maSP});
                        } while (cursorCT.moveToNext());
                    }
                    cursorCT.close();

                    ContentValues values = new ContentValues();
                    values.put("TrangThai", 3);
                    db.update("DonHang", values, "MaDH = ?", new String[]{String.valueOf(maDH)});
                }
            } else {
                cursorDH.close();
            }

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }

    // Sửa danh mục
    public boolean updateDanhMuc(int maDM, String tenDM, String moTa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenDanhMuc", tenDM);
        values.put("MoTa", moTa);
        int rows = db.update("DanhMuc", values, "MaDanhMuc = ?", new String[]{String.valueOf(maDM)});
        return rows > 0;
    }

    // Xóa danh mục
    public boolean deleteDanhMuc(int maDM) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("DanhMuc", "MaDanhMuc = ?", new String[]{String.valueOf(maDM)});
        return rows > 0;
    }

    // Cập nhật sản phẩm (Hỗ trợ tham số HinhAnh)
    public boolean updateSanPham(int maSP, String tenSP, int maDanhMuc, int gia, int soLuong, String moTa, String thuongHieu, String hinhAnh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenSP", tenSP);
        values.put("MaDanhMuc", maDanhMuc);
        values.put("Gia", gia);
        values.put("SoLuong", soLuong);
        values.put("MoTa", moTa);
        values.put("ThuongHieu", thuongHieu);
        values.put("HinhAnh", hinhAnh != null ? hinhAnh : "");
        int rows = db.update("SanPham", values, "MaSP = ?", new String[]{String.valueOf(maSP)});
        return rows > 0;
    }

    public boolean updateSanPham(int maSP, String tenSP, int maDanhMuc, int gia, int soLuong, String moTa, String thuongHieu) {
        return updateSanPham(maSP, tenSP, maDanhMuc, gia, soLuong, moTa, thuongHieu, "");
    }

    // Xóa sản phẩm
    public boolean deleteSanPham(int maSP) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("SanPham", "MaSP = ?", new String[]{String.valueOf(maSP)});
        return rows > 0;
    }

    // Lấy tất cả đơn hàng (Đọc thông tin chốt cứng từ DonHang)
    public List<DonHang> getAllDonHang() {
        List<DonHang> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DonHang ORDER BY MaDH DESC", null);
        if (cursor.moveToFirst()) {
            do {
                DonHang dh = new DonHang();
                dh.setMaDH(cursor.getInt(0));
                dh.setMaTK(cursor.getInt(1));
                dh.setTenNguoiNhan(cursor.getColumnCount() > 2 ? cursor.getString(2) : "Khách hàng");
                dh.setNgayDat(cursor.getColumnCount() > 3 ? cursor.getString(3) : "");
                dh.setTongTien(cursor.getColumnCount() > 4 ? cursor.getInt(4) : 0);
                dh.setDiaChiNhan(cursor.getColumnCount() > 5 ? cursor.getString(5) : "");
                dh.setSoDienThoai(cursor.getColumnCount() > 6 ? cursor.getString(6) : "");
                dh.setTrangThai(cursor.getColumnCount() > 7 ? cursor.getInt(7) : 0);
                dh.setPhuongThucThanhToan(cursor.getColumnCount() > 8 && !cursor.isNull(8) ? cursor.getString(8) : "COD");
                list.add(dh);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Cập nhật trạng thái đơn hàng (Nếu chuyển sang 3 - Đã hủy thì tự động hoàn kho)
    public boolean updateTrangThaiDonHang(int maDH, int trangThaiMoi) {
        if (trangThaiMoi == 3) {
            return huyDonHang(maDH);
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TrangThai", trangThaiMoi);
        int rows = db.update("DonHang", values, "MaDH = ?", new String[]{String.valueOf(maDH)});
        return rows > 0;
    }
}
