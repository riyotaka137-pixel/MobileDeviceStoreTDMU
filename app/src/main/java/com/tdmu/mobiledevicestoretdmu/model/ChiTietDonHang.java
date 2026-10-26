package com.tdmu.mobiledevicestoretdmu.model;

public class ChiTietDonHang {
    private int MaCTDH;
    private int MaDH;
    private int MaSP;
    private int SoLuong;
    private int DonGia;

    public ChiTietDonHang() {
    }

    public ChiTietDonHang(int maCTDH, int maDH, int maSP, int soLuong, int donGia) {
        MaCTDH = maCTDH;
        MaDH = maDH;
        MaSP = maSP;
        SoLuong = soLuong;
        DonGia = donGia;
    }

    public int getMaCTDH() { return MaCTDH; }
    public void setMaCTDH(int maCTDH) { MaCTDH = maCTDH; }

    public int getMaDH() { return MaDH; }
    public void setMaDH(int maDH) { MaDH = maDH; }

    public int getMaSP() { return MaSP; }
    public void setMaSP(int maSP) { MaSP = maSP; }

    public int getSoLuong() { return SoLuong; }
    public void setSoLuong(int soLuong) { SoLuong = soLuong; }

    public int getDonGia() { return DonGia; }
    public void setDonGia(int donGia) { DonGia = donGia; }
}