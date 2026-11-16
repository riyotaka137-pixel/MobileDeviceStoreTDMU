package com.tdmu.mobiledevicestoretdmu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tdmu.mobiledevicestoretdmu.model.Banner;
import com.tdmu.mobiledevicestoretdmu.model.ChiTietGioHang;
import com.tdmu.mobiledevicestoretdmu.model.DanhMuc;
import com.tdmu.mobiledevicestoretdmu.model.DonHang;
import com.tdmu.mobiledevicestoretdmu.model.GiamGia;
import com.tdmu.mobiledevicestoretdmu.model.SanPham;
import com.tdmu.mobiledevicestoretdmu.model.TaiKhoan;
import com.tdmu.mobiledevicestoretdmu.model.ThongBao;
import com.tdmu.mobiledevicestoretdmu.model.ThuongHieu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Tên cơ sở dữ liệu và phiên bản Production (Nâng lên V14 để thêm bảng ThongBao và GiamGia)
    private static final String DATABASE_NAME = "BanThietBiTDMU.db";
    private static final int DATABASE_VERSION = 14;

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

        // 2. Tạo bảng DanhMuc (Bao gồm cột HinhAnh)
        String createTableDanhMuc = "CREATE TABLE DanhMuc (" +
                "MaDanhMuc INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TenDanhMuc TEXT NOT NULL, " +
                "MoTa TEXT, " +
                "HinhAnh TEXT)";
        db.execSQL(createTableDanhMuc);

        // 3. Tạo bảng ThuongHieu mới
        String createTableThuongHieu = "CREATE TABLE ThuongHieu (" +
                "MaThuongHieu INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TenThuongHieu TEXT NOT NULL, " +
                "MoTa TEXT, " +
                "HinhAnh TEXT)";
        db.execSQL(createTableThuongHieu);

        // 4. Tạo bảng Banner mới cho Slide quảng cáo
        String createTableBanner = "CREATE TABLE Banner (" +
                "MaBanner INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TenBanner TEXT, " +
                "HinhAnh TEXT NOT NULL, " +
                "MoTa TEXT)";
        db.execSQL(createTableBanner);

        // 5. Tạo bảng SanPham (Thêm cột PhanTramGiam cho Khuyến mãi)
        String createTableSanPham = "CREATE TABLE SanPham (" +
                "MaSP INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TenSP TEXT NOT NULL, " +
                "MaDanhMuc INTEGER, " +
                "Gia INTEGER, " +
                "SoLuong INTEGER, " +
                "MoTa TEXT, " +
                "HinhAnh TEXT, " +
                "ThuongHieu TEXT, " +
                "PhanTramGiam INTEGER DEFAULT 0, " +
                "FOREIGN KEY(MaDanhMuc) REFERENCES DanhMuc(MaDanhMuc))";
        db.execSQL(createTableSanPham);

        // 6. Tạo bảng GioHang
        String createTableGioHang = "CREATE TABLE GioHang (" +
                "MaGioHang INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MaTK INTEGER, " +
                "NgayTao TEXT, " +
                "FOREIGN KEY(MaTK) REFERENCES TaiKhoan(MaTK))";
        db.execSQL(createTableGioHang);

        // 7. Tạo bảng ChiTietGioHang
        String createTableChiTietGioHang = "CREATE TABLE ChiTietGioHang (" +
                "MaCTGH INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MaGioHang INTEGER, " +
                "MaSP INTEGER, " +
                "SoLuong INTEGER, " +
                "DonGia INTEGER, " +
                "FOREIGN KEY(MaGioHang) REFERENCES GioHang(MaGioHang), " +
                "FOREIGN KEY(MaSP) REFERENCES SanPham(MaSP))";
        db.execSQL(createTableChiTietGioHang);

        // 8. Tạo bảng DonHang (Bao gồm TenNguoiNhan, DiaChiNhan, SoDienThoai chốt cứng)
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

        // 9. Tạo bảng ChiTietDonHang
        String createTableChiTietDonHang = "CREATE TABLE ChiTietDonHang (" +
                "MaCTDH INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MaDH INTEGER, " +
                "MaSP INTEGER, " +
                "SoLuong INTEGER, " +
                "DonGia INTEGER, " +
                "FOREIGN KEY(MaDH) REFERENCES DonHang(MaDH), " +
                "FOREIGN KEY(MaSP) REFERENCES SanPham(MaSP))";
        db.execSQL(createTableChiTietDonHang);

        // 10. Tạo bảng ThongBao
        String createTableThongBao = "CREATE TABLE IF NOT EXISTS ThongBao (" +
                "MaTB INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MaTK INTEGER, " +
                "TieuDe TEXT NOT NULL, " +
                "NoiDung TEXT NOT NULL, " +
                "NgayTao TEXT, " +
                "TrangThai INTEGER DEFAULT 0, " +
                "FOREIGN KEY(MaTK) REFERENCES TaiKhoan(MaTK))";
        db.execSQL(createTableThongBao);

        // 11. Tạo bảng GiamGia (Mã voucher động)
        String createTableGiamGia = "CREATE TABLE IF NOT EXISTS GiamGia (" +
                "MaGG INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MaCode TEXT UNIQUE NOT NULL, " +
                "PhanTramGiam INTEGER DEFAULT 0, " +
                "TienGiamToiDa INTEGER DEFAULT 0, " +
                "DonToiThieu INTEGER DEFAULT 0, " +
                "NgayHetHan TEXT, " +
                "MoTa TEXT, " +
                "TrangThai INTEGER DEFAULT 1)";
        db.execSQL(createTableGiamGia);

        // --- KHỞI TẠO SẴN TÀI KHOẢN HỆ THỐNG ---
        String insertAdmin = "INSERT INTO TaiKhoan (HoTen, Email, MatKhau, Quyen, TrangThai) " +
                "VALUES ('Admin TDMU', 'admin@gmail.com', 'admin', 1, 1)";
        db.execSQL(insertAdmin);

        String insertUser = "INSERT INTO TaiKhoan (HoTen, Email, MatKhau, SoDienThoai, DiaChi, Quyen, TrangThai) " +
                "VALUES ('Khách Hàng Mẫu', 'user@gmail.com', 'user', '0912345678', 'Thủ Dầu Một, Bình Dương', 0, 1)";
        db.execSQL(insertUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Bảo toàn 100% dữ liệu cũ: Chỉ tạo các bảng mới nếu chưa tồn tại, tuyệt đối không dùng DROP TABLE
        if (oldVersion < 14) {
            String createTableThongBao = "CREATE TABLE IF NOT EXISTS ThongBao (" +
                    "MaTB INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "MaTK INTEGER, " +
                    "TieuDe TEXT NOT NULL, " +
                    "NoiDung TEXT NOT NULL, " +
                    "NgayTao TEXT, " +
                    "TrangThai INTEGER DEFAULT 0, " +
                    "FOREIGN KEY(MaTK) REFERENCES TaiKhoan(MaTK))";
            db.execSQL(createTableThongBao);

            String createTableGiamGia = "CREATE TABLE IF NOT EXISTS GiamGia (" +
                    "MaGG INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "MaCode TEXT UNIQUE NOT NULL, " +
                    "PhanTramGiam INTEGER DEFAULT 0, " +
                    "TienGiamToiDa INTEGER DEFAULT 0, " +
                    "DonToiThieu INTEGER DEFAULT 0, " +
                    "NgayHetHan TEXT, " +
                    "MoTa TEXT, " +
                    "TrangThai INTEGER DEFAULT 1)";
            db.execSQL(createTableGiamGia);
        }
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

    // Lấy danh sách sản phẩm đang có giảm giá (PhanTramGiam > 0)
    public List<SanPham> getSanPhamKhuyenMai() {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SanPham WHERE PhanTramGiam > 0 ORDER BY PhanTramGiam DESC", null);
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
                sp.setPhanTramGiam(cursor.getColumnCount() > 8 ? cursor.getInt(8) : 0);
                list.add(sp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
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

    // Lấy doanh thu 7 ngày gần nhất (Theo Tuần)
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

    // Lấy doanh thu 30 ngày gần nhất (Theo Tháng)
    public Map<String, Long> getDoanhThu30NgayQua() {
        Map<String, Long> mapDoanhThu = new LinkedHashMap<>();
        SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -29);

        Map<String, String> mapFullToDisplay = new LinkedHashMap<>();
        for (int i = 0; i < 30; i++) {
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

    // Lấy doanh thu 12 tháng gần nhất (Theo Năm)
    public Map<String, Long> getDoanhThu12ThangQua() {
        Map<String, Long> mapDoanhThu = new LinkedHashMap<>();
        SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        SimpleDateFormat sdfDisplay = new SimpleDateFormat("MM/yy", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -11);

        Map<String, String> mapFullToDisplay = new LinkedHashMap<>();
        for (int i = 0; i < 12; i++) {
            String fullDate = sdfFull.format(calendar.getTime());
            String displayDate = sdfDisplay.format(calendar.getTime());
            mapFullToDisplay.put(fullDate, displayDate);
            mapDoanhThu.put(displayDate, 0L);
            calendar.add(Calendar.MONTH, 1);
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT substr(NgayDat, 1, 7) AS Thang, SUM(TongTien) AS DoanhThu " +
                "FROM DonHang WHERE TrangThai != 3 AND substr(NgayDat, 1, 7) >= ? " +
                "GROUP BY substr(NgayDat, 1, 7)";

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

    // --- CÁC HÀM QUẢN LÝ BANNER ---
    public List<Banner> getAllBanner() {
        List<Banner> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Banner", null);
        if (cursor.moveToFirst()) {
            int idxId = cursor.getColumnIndex("MaBanner");
            int idxTen = cursor.getColumnIndex("TenBanner");
            int idxHinhAnh = cursor.getColumnIndex("HinhAnh");
            int idxMoTa = cursor.getColumnIndex("MoTa");

            do {
                Banner bn = new Banner();
                if (idxId != -1) bn.setMaBanner(cursor.getInt(idxId));
                if (idxTen != -1) bn.setTenBanner(cursor.getString(idxTen));
                if (idxHinhAnh != -1 && !cursor.isNull(idxHinhAnh)) {
                    bn.setHinhAnh(cursor.getString(idxHinhAnh));
                } else {
                    bn.setHinhAnh("");
                }
                if (idxMoTa != -1) bn.setMoTa(cursor.getString(idxMoTa));
                list.add(bn);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean insertBanner(String tenBanner, String hinhAnh, String moTa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenBanner", tenBanner);
        values.put("HinhAnh", hinhAnh != null ? hinhAnh : "");
        values.put("MoTa", moTa);
        long result = db.insert("Banner", null, values);
        return result != -1;
    }

    public boolean updateBanner(int maBanner, String tenBanner, String hinhAnh, String moTa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenBanner", tenBanner);
        values.put("HinhAnh", hinhAnh != null ? hinhAnh : "");
        values.put("MoTa", moTa);
        int rows = db.update("Banner", values, "MaBanner = ?", new String[]{String.valueOf(maBanner)});
        return rows > 0;
    }

    public boolean deleteBanner(int maBanner) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("Banner", "MaBanner = ?", new String[]{String.valueOf(maBanner)});
        return rows > 0;
    }

    // --- CÁC HÀM QUẢN LÝ THƯƠNG HIỆU ---
    public List<ThuongHieu> getAllThuongHieu() {
        List<ThuongHieu> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ThuongHieu", null);
        if (cursor.moveToFirst()) {
            int idxId = cursor.getColumnIndex("MaThuongHieu");
            int idxTen = cursor.getColumnIndex("TenThuongHieu");
            int idxMoTa = cursor.getColumnIndex("MoTa");
            int idxHinhAnh = cursor.getColumnIndex("HinhAnh");

            do {
                ThuongHieu th = new ThuongHieu();
                if (idxId != -1) th.setMaThuongHieu(cursor.getInt(idxId));
                if (idxTen != -1) th.setTenThuongHieu(cursor.getString(idxTen));
                if (idxMoTa != -1) th.setMoTa(cursor.getString(idxMoTa));
                if (idxHinhAnh != -1 && !cursor.isNull(idxHinhAnh)) {
                    th.setHinhAnh(cursor.getString(idxHinhAnh));
                } else {
                    th.setHinhAnh("");
                }
                list.add(th);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean insertThuongHieu(String tenThuongHieu, String moTa, String hinhAnh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenThuongHieu", tenThuongHieu);
        values.put("MoTa", moTa);
        values.put("HinhAnh", hinhAnh != null ? hinhAnh : "");
        long result = db.insert("ThuongHieu", null, values);
        return result != -1;
    }

    public boolean insertThuongHieu(String tenThuongHieu, String moTa) {
        return insertThuongHieu(tenThuongHieu, moTa, "");
    }

    public boolean updateThuongHieu(int maTH, String tenTH, String moTa, String hinhAnh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenThuongHieu", tenTH);
        values.put("MoTa", moTa);
        values.put("HinhAnh", hinhAnh != null ? hinhAnh : "");
        int rows = db.update("ThuongHieu", values, "MaThuongHieu = ?", new String[]{String.valueOf(maTH)});
        return rows > 0;
    }

    public boolean updateThuongHieu(int maTH, String tenTH, String moTa) {
        return updateThuongHieu(maTH, tenTH, moTa, "");
    }

    public boolean deleteThuongHieu(int maTH) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("ThuongHieu", "MaThuongHieu = ?", new String[]{String.valueOf(maTH)});
        return rows > 0;
    }

    // Lấy toàn bộ danh mục (Hỗ trợ HinhAnh an toàn theo Tên Cột)
    public List<DanhMuc> getAllDanhMuc() {
        List<DanhMuc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DanhMuc", null);
        if (cursor.moveToFirst()) {
            int idxId = cursor.getColumnIndex("MaDanhMuc");
            int idxTen = cursor.getColumnIndex("TenDanhMuc");
            int idxMoTa = cursor.getColumnIndex("MoTa");
            int idxHinhAnh = cursor.getColumnIndex("HinhAnh");

            do {
                DanhMuc dm = new DanhMuc();
                if (idxId != -1) dm.setMaDanhMuc(cursor.getInt(idxId));
                if (idxTen != -1) dm.setTenDanhMuc(cursor.getString(idxTen));
                if (idxMoTa != -1) dm.setMoTa(cursor.getString(idxMoTa));
                if (idxHinhAnh != -1 && !cursor.isNull(idxHinhAnh)) {
                    dm.setHinhAnh(cursor.getString(idxHinhAnh));
                } else {
                    dm.setHinhAnh("");
                }
                list.add(dm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Thêm danh mục mới (Trả về boolean xác nhận thành công)
    public boolean insertDanhMuc(String tenDanhMuc, String moTa, String hinhAnh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenDanhMuc", tenDanhMuc);
        values.put("MoTa", moTa);
        values.put("HinhAnh", hinhAnh != null ? hinhAnh : "");
        long result = db.insert("DanhMuc", null, values);
        return result != -1;
    }

    public boolean insertDanhMuc(String tenDanhMuc, String moTa) {
        return insertDanhMuc(tenDanhMuc, moTa, "");
    }

    // Thêm sản phẩm (Hỗ trợ PhanTramGiam)
    public void insertSanPham(String tenSP, int maDanhMuc, int gia, int soLuong, String moTa, String thuongHieu, String hinhAnh, int phanTramGiam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenSP", tenSP);
        values.put("MaDanhMuc", maDanhMuc);
        values.put("Gia", gia);
        values.put("SoLuong", soLuong);
        values.put("MoTa", moTa);
        values.put("ThuongHieu", thuongHieu);
        values.put("PhanTramGiam", phanTramGiam);
        values.put("HinhAnh", hinhAnh != null ? hinhAnh : "");
        db.insert("SanPham", null, values);
    }

    public void insertSanPham(String tenSP, int maDanhMuc, int gia, int soLuong, String moTa, String thuongHieu, String hinhAnh) {
        insertSanPham(tenSP, maDanhMuc, gia, soLuong, moTa, thuongHieu, hinhAnh, 0);
    }

    public void insertSanPham(String tenSP, int maDanhMuc, int gia, int soLuong, String moTa, String thuongHieu) {
        insertSanPham(tenSP, maDanhMuc, gia, soLuong, moTa, thuongHieu, "", 0);
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
                sp.setPhanTramGiam(cursor.getColumnCount() > 8 ? cursor.getInt(8) : 0);
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
                sp.setPhanTramGiam(cursor.getColumnCount() > 8 ? cursor.getInt(8) : 0);
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
            sp.setPhanTramGiam(cursor.getColumnCount() > 8 ? cursor.getInt(8) : 0);
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
                sp.setPhanTramGiam(cursor.getColumnCount() > 8 ? cursor.getInt(8) : 0);
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

    // Đặt hàng (Thêm đơn hàng với TenNguoiNhan chốt cứng, lưu chi tiết, trừ kho LẬP TỨC và xóa giỏ, trả về MaDH vừa tạo)
    public long placeOrderReturnId(int maTK, String tenNguoiNhan, String soDienThoai, String diaChiNhan, String phuongThucThanhToan, int tongTien, List<ChiTietGioHang> listCart) {
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
            if (maDH == -1) return -1;

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
            return maDH;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            db.endTransaction();
        }
    }

    public boolean placeOrder(int maTK, String tenNguoiNhan, String soDienThoai, String diaChiNhan, String phuongThucThanhToan, int tongTien, List<ChiTietGioHang> listCart) {
        return placeOrderReturnId(maTK, tenNguoiNhan, soDienThoai, diaChiNhan, phuongThucThanhToan, tongTien, listCart) != -1;
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

    // Sửa danh mục (Hỗ trợ HinhAnh)
    public boolean updateDanhMuc(int maDM, String tenDM, String moTa, String hinhAnh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenDanhMuc", tenDM);
        values.put("MoTa", moTa);
        values.put("HinhAnh", hinhAnh != null ? hinhAnh : "");
        int rows = db.update("DanhMuc", values, "MaDanhMuc = ?", new String[]{String.valueOf(maDM)});
        return rows > 0;
    }

    public boolean updateDanhMuc(int maDM, String tenDM, String moTa) {
        return updateDanhMuc(maDM, tenDM, moTa, "");
    }

    // Xóa danh mục
    public boolean deleteDanhMuc(int maDM) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("DanhMuc", "MaDanhMuc = ?", new String[]{String.valueOf(maDM)});
        return rows > 0;
    }

    // Cập nhật sản phẩm (Hỗ trợ PhanTramGiam)
    public boolean updateSanPham(int maSP, String tenSP, int maDanhMuc, int gia, int soLuong, String moTa, String thuongHieu, String hinhAnh, int phanTramGiam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenSP", tenSP);
        values.put("MaDanhMuc", maDanhMuc);
        values.put("Gia", gia);
        values.put("SoLuong", soLuong);
        values.put("MoTa", moTa);
        values.put("ThuongHieu", thuongHieu);
        values.put("PhanTramGiam", phanTramGiam);
        values.put("HinhAnh", hinhAnh != null ? hinhAnh : "");
        int rows = db.update("SanPham", values, "MaSP = ?", new String[]{String.valueOf(maSP)});
        return rows > 0;
    }

    public boolean updateSanPham(int maSP, String tenSP, int maDanhMuc, int gia, int soLuong, String moTa, String thuongHieu, String hinhAnh) {
        return updateSanPham(maSP, tenSP, maDanhMuc, gia, soLuong, moTa, thuongHieu, hinhAnh, 0);
    }

    public boolean updateSanPham(int maSP, String tenSP, int maDanhMuc, int gia, int soLuong, String moTa, String thuongHieu) {
        return updateSanPham(maSP, tenSP, maDanhMuc, gia, soLuong, moTa, thuongHieu, "", 0);
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

    // ==========================================
    // CÁC HÀM XỬ LÝ THÔNG BÁO (ThongBao)
    // ==========================================
    public boolean insertThongBao(int maTK, String tieuDe, String noiDung) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaTK", maTK);
        values.put("TieuDe", tieuDe);
        values.put("NoiDung", noiDung);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        values.put("NgayTao", sdf.format(new Date()));
        values.put("TrangThai", 0); // 0: Chưa đọc
        long result = db.insert("ThongBao", null, values);
        return result != -1;
    }

    public List<ThongBao> getThongBaoByMaTK(int maTK) {
        List<ThongBao> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ThongBao WHERE MaTK = ? ORDER BY MaTB DESC", new String[]{String.valueOf(maTK)});
        if (cursor.moveToFirst()) {
            do {
                ThongBao tb = new ThongBao();
                tb.setMaTB(cursor.getInt(0));
                tb.setMaTK(cursor.getInt(1));
                tb.setTieuDe(cursor.getString(2));
                tb.setNoiDung(cursor.getString(3));
                tb.setNgayTao(cursor.getString(4));
                tb.setTrangThai(cursor.getInt(5));
                list.add(tb);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean danhDauDaDocThongBao(int maTB) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TrangThai", 1);
        int rows = db.update("ThongBao", values, "MaTB = ?", new String[]{String.valueOf(maTB)});
        return rows > 0;
    }

    // ==========================================
    // CÁC HÀM XỬ LÝ MÃ GIẢM GIÁ (GiamGia / Voucher)
    // ==========================================
    public List<GiamGia> getAllGiamGia() {
        List<GiamGia> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GiamGia ORDER BY MaGG DESC", null);
        if (cursor.moveToFirst()) {
            do {
                GiamGia gg = new GiamGia();
                gg.setMaGG(cursor.getInt(0));
                gg.setMaCode(cursor.getString(1));
                gg.setPhanTramGiam(cursor.getInt(2));
                gg.setTienGiamToiDa(cursor.getInt(3));
                gg.setDonToiThieu(cursor.getInt(4));
                gg.setNgayHetHan(cursor.getString(5));
                gg.setMoTa(cursor.getString(6));
                gg.setTrangThai(cursor.getColumnCount() > 7 ? cursor.getInt(7) : 1);
                list.add(gg);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public GiamGia getGiamGiaByCode(String code) {
        if (code == null || code.trim().isEmpty()) return null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GiamGia WHERE MaCode = ? COLLATE NOCASE", new String[]{code.trim()});
        GiamGia gg = null;
        if (cursor.moveToFirst()) {
            gg = new GiamGia();
            gg.setMaGG(cursor.getInt(0));
            gg.setMaCode(cursor.getString(1));
            gg.setPhanTramGiam(cursor.getInt(2));
            gg.setTienGiamToiDa(cursor.getInt(3));
            gg.setDonToiThieu(cursor.getInt(4));
            gg.setNgayHetHan(cursor.getString(5));
            gg.setMoTa(cursor.getString(6));
            gg.setTrangThai(cursor.getColumnCount() > 7 ? cursor.getInt(7) : 1);
        }
        cursor.close();
        return gg;
    }

    public boolean insertGiamGia(String code, int phanTramGiam, int tienGiamToiDa, int donToiThieu, String ngayHetHan, String moTa, int trangThai) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaCode", code != null ? code.trim().toUpperCase() : "");
        values.put("PhanTramGiam", phanTramGiam);
        values.put("TienGiamToiDa", tienGiamToiDa);
        values.put("DonToiThieu", donToiThieu);
        values.put("NgayHetHan", ngayHetHan != null ? ngayHetHan.trim() : "");
        values.put("MoTa", moTa != null ? moTa.trim() : "");
        values.put("TrangThai", trangThai);
        long result = db.insert("GiamGia", null, values);
        return result != -1;
    }

    public boolean updateGiamGia(int maGG, String code, int phanTramGiam, int tienGiamToiDa, int donToiThieu, String ngayHetHan, String moTa, int trangThai) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaCode", code != null ? code.trim().toUpperCase() : "");
        values.put("PhanTramGiam", phanTramGiam);
        values.put("TienGiamToiDa", tienGiamToiDa);
        values.put("DonToiThieu", donToiThieu);
        values.put("NgayHetHan", ngayHetHan != null ? ngayHetHan.trim() : "");
        values.put("MoTa", moTa != null ? moTa.trim() : "");
        values.put("TrangThai", trangThai);
        int rows = db.update("GiamGia", values, "MaGG = ?", new String[]{String.valueOf(maGG)});
        return rows > 0;
    }

    public boolean deleteGiamGia(int maGG) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("GiamGia", "MaGG = ?", new String[]{String.valueOf(maGG)});
        return rows > 0;
    }
}
