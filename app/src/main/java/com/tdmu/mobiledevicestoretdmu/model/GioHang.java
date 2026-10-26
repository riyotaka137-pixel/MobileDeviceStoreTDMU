package com.tdmu.mobiledevicestoretdmu.model;

public class GioHang {
    private int MaGioHang;
    private int MaTK;
    private String NgayTao;

    public GioHang() {
    }

    public GioHang(int maGioHang, int maTK, String ngayTao) {
        MaGioHang = maGioHang;
        MaTK = maTK;
        NgayTao = ngayTao;
    }

    public int getMaGioHang() { return MaGioHang; }
    public void setMaGioHang(int maGioHang) { MaGioHang = maGioHang; }

    public int getMaTK() { return MaTK; }
    public void setMaTK(int maTK) { MaTK = maTK; }

    public String getNgayTao() { return NgayTao; }
    public void setNgayTao(String ngayTao) { NgayTao = ngayTao; }
}