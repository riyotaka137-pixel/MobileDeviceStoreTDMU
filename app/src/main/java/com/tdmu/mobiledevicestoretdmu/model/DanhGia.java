package com.tdmu.mobiledevicestoretdmu.model;

public class DanhGia {
    private int maDG;
    private int maSP;
    private int maTK;
    private int soSao; // 1 -> 5 sao
    private String noiDung;
    private String ngayDanhGia;
    private String tenNguoiDung; // Phục vụ hiển thị sau khi JOIN với bảng TaiKhoan

    public DanhGia() {
    }

    public DanhGia(int maDG, int maSP, int maTK, int soSao, String noiDung, String ngayDanhGia, String tenNguoiDung) {
        this.maDG = maDG;
        this.maSP = maSP;
        this.maTK = maTK;
        this.soSao = soSao;
        this.noiDung = noiDung;
        this.ngayDanhGia = ngayDanhGia;
        this.tenNguoiDung = tenNguoiDung;
    }

    public DanhGia(int maSP, int maTK, int soSao, String noiDung, String ngayDanhGia) {
        this.maSP = maSP;
        this.maTK = maTK;
        this.soSao = soSao;
        this.noiDung = noiDung;
        this.ngayDanhGia = ngayDanhGia;
    }

    public int getMaDG() {
        return maDG;
    }

    public void setMaDG(int maDG) {
        this.maDG = maDG;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public int getMaTK() {
        return maTK;
    }

    public void setMaTK(int maTK) {
        this.maTK = maTK;
    }

    public int getSoSao() {
        return soSao;
    }

    public void setSoSao(int soSao) {
        this.soSao = soSao;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getNgayDanhGia() {
        return ngayDanhGia;
    }

    public void setNgayDanhGia(String ngayDanhGia) {
        this.ngayDanhGia = ngayDanhGia;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }
}
