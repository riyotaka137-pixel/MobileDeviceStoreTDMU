package com.tdmu.mobiledevicestoretdmu.model;

public class DanhMuc {
    private int MaDanhMuc;
    private String TenDanhMuc;
    private String MoTa;
    private String HinhAnh;

    public DanhMuc() {
    }

    public DanhMuc(int maDanhMuc, String tenDanhMuc, String moTa) {
        this(maDanhMuc, tenDanhMuc, moTa, "");
    }

    public DanhMuc(int maDanhMuc, String tenDanhMuc, String moTa, String hinhAnh) {
        MaDanhMuc = maDanhMuc;
        TenDanhMuc = tenDanhMuc;
        MoTa = moTa;
        HinhAnh = hinhAnh;
    }

    public int getMaDanhMuc() { return MaDanhMuc; }
    public void setMaDanhMuc(int maDanhMuc) { MaDanhMuc = maDanhMuc; }

    public String getTenDanhMuc() { return TenDanhMuc; }
    public void setTenDanhMuc(String tenDanhMuc) { TenDanhMuc = tenDanhMuc; }

    public String getMoTa() { return MoTa; }
    public void setMoTa(String moTa) { MoTa = moTa; }

    public String getHinhAnh() { return HinhAnh; }
    public void setHinhAnh(String hinhAnh) { HinhAnh = hinhAnh; }

    @Override
    public String toString() {
        return TenDanhMuc;
    }
}
