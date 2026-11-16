package com.tdmu.mobiledevicestoretdmu.model;

public class DonHang {
    private int MaDH;
    private int MaTK;
    private String TenNguoiNhan;
    private String NgayDat;
    private long TongTien;
    private String DiaChiNhan;
    private String SoDienThoai;
    private int TrangThai; // 0 = ChoXacNhan, 1 = DangGiao, 2 = DaGiao, 3 = DaHuy
    private String PhuongThucThanhToan; // "COD" hoặc "Chuyển khoản ngân hàng"

    public DonHang() {
        this.PhuongThucThanhToan = "COD";
    }

    public DonHang(int maDH, int maTK, String tenNguoiNhan, String ngayDat, long tongTien, String diaChiNhan, String soDienThoai, int trangThai, String phuongThucThanhToan) {
        MaDH = maDH;
        MaTK = maTK;
        TenNguoiNhan = tenNguoiNhan;
        NgayDat = ngayDat;
        TongTien = tongTien;
        DiaChiNhan = diaChiNhan;
        SoDienThoai = soDienThoai;
        TrangThai = trangThai;
        PhuongThucThanhToan = phuongThucThanhToan;
    }

    public DonHang(int maDH, int maTK, String ngayDat, long tongTien, String diaChiNhan, String soDienThoai, int trangThai) {
        MaDH = maDH;
        MaTK = maTK;
        NgayDat = ngayDat;
        TongTien = tongTien;
        DiaChiNhan = diaChiNhan;
        SoDienThoai = soDienThoai;
        TrangThai = trangThai;
        PhuongThucThanhToan = "COD";
    }

    public int getMaDH() { return MaDH; }
    public void setMaDH(int maDH) { MaDH = maDH; }

    public int getMaTK() { return MaTK; }
    public void setMaTK(int maTK) { MaTK = maTK; }

    public String getTenNguoiNhan() { return TenNguoiNhan; }
    public void setTenNguoiNhan(String tenNguoiNhan) { TenNguoiNhan = tenNguoiNhan; }

    public String getNgayDat() { return NgayDat; }
    public void setNgayDat(String ngayDat) { NgayDat = ngayDat; }

    public long getTongTien() { return TongTien; }
    public void setTongTien(long tongTien) { TongTien = tongTien; }
    public void setTongTien(int tongTien) { TongTien = tongTien; }

    public String getDiaChiNhan() { return DiaChiNhan; }
    public void setDiaChiNhan(String diaChiNhan) { DiaChiNhan = diaChiNhan; }

    public String getSoDienThoai() { return SoDienThoai; }
    public void setSoDienThoai(String soDienThoai) { SoDienThoai = soDienThoai; }

    public int getTrangThai() { return TrangThai; }
    public void setTrangThai(int trangThai) { TrangThai = trangThai; }

    public String getPhuongThucThanhToan() { return PhuongThucThanhToan; }
    public void setPhuongThucThanhToan(String phuongThucThanhToan) { PhuongThucThanhToan = phuongThucThanhToan; }
}
