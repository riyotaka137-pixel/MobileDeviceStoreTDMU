package com.tdmu.mobiledevicestoretdmu.model;

public class ChiTietGioHang {
    private int MaCTGH;
    private int MaGioHang;
    private int MaSP;
    private int SoLuong;
    private int DonGia;

    public ChiTietGioHang() {
    }

    public ChiTietGioHang(int maCTGH, int maGioHang, int maSP, int soLuong, int donGia) {
        MaCTGH = maCTGH;
        MaGioHang = maGioHang;
        MaSP = maSP;
        SoLuong = soLuong;
        DonGia = donGia;
    }

    public int getMaCTGH() { return MaCTGH; }
    public void setMaCTGH(int maCTGH) { MaCTGH = maCTGH; }

    public int getMaGioHang() { return MaGioHang; }
    public void setMaGioHang(int maGioHang) { MaGioHang = maGioHang; }

    public int getMaSP() { return MaSP; }
    public void setMaSP(int maSP) { MaSP = maSP; }

    public int getSoLuong() { return SoLuong; }
    public void setSoLuong(int soLuong) { SoLuong = soLuong; }

    public int getDonGia() { return DonGia; }
    public void setDonGia(int donGia) { DonGia = donGia; }
}
