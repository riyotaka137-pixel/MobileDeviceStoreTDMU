package com.tdmu.mobiledevicestoretdmu.model;

public class DanhMuc {
    private int MaDanhMuc;
    private String TenDanhMuc;
    private String MoTa;

    public DanhMuc() {
    }

    public DanhMuc(int maDanhMuc, String tenDanhMuc, String moTa) {
        MaDanhMuc = maDanhMuc;
        TenDanhMuc = tenDanhMuc;
        MoTa = moTa;
    }

    public int getMaDanhMuc() { return MaDanhMuc; }
    public void setMaDanhMuc(int maDanhMuc) { MaDanhMuc = maDanhMuc; }

    public String getTenDanhMuc() { return TenDanhMuc; }
    public void setTenDanhMuc(String tenDanhMuc) { TenDanhMuc = tenDanhMuc; }

    public String getMoTa() { return MoTa; }
    public void setMoTa(String moTa) { MoTa = moTa; }

    @Override
    public String toString() {
        return TenDanhMuc;
    }
}