package com.tdmu.mobiledevicestoretdmu.model;

public class SanPham {
    private int MaSP;
    private String TenSP;
    private int MaDanhMuc;
    private int Gia;
    private int SoLuong;
    private String MoTa;
    private String HinhAnh;
    private String ThuongHieu;
    private int PhanTramGiam;

    public SanPham() {
    }

    public SanPham(int maSP, String tenSP, int maDanhMuc, int gia, int soLuong, String moTa, String hinhAnh, String thuongHieu, int phanTramGiam) {
        MaSP = maSP;
        TenSP = tenSP;
        MaDanhMuc = maDanhMuc;
        Gia = gia;
        SoLuong = soLuong;
        MoTa = moTa;
        HinhAnh = hinhAnh;
        ThuongHieu = thuongHieu;
        PhanTramGiam = phanTramGiam;
    }

    public int getMaSP() { return MaSP; }
    public void setMaSP(int maSP) { MaSP = maSP; }

    public String getTenSP() { return TenSP; }
    public void setTenSP(String tenSP) { TenSP = tenSP; }

    public int getMaDanhMuc() { return MaDanhMuc; }
    public void setMaDanhMuc(int maDanhMuc) { MaDanhMuc = maDanhMuc; }

    public int getGia() { return Gia; }
    public void setGia(int gia) { Gia = gia; }

    public int getSoLuong() { return SoLuong; }
    public void setSoLuong(int soLuong) { SoLuong = soLuong; }

    public String getMoTa() { return MoTa; }
    public void setMoTa(String moTa) { MoTa = moTa; }

    public String getHinhAnh() { return HinhAnh; }
    public void setHinhAnh(String hinhAnh) { HinhAnh = hinhAnh; }

    public String getThuongHieu() { return ThuongHieu; }
    public void setThuongHieu(String thuongHieu) { ThuongHieu = thuongHieu; }

    public int getPhanTramGiam() { return PhanTramGiam; }
    public void setPhanTramGiam(int phanTramGiam) { PhanTramGiam = phanTramGiam; }

    public int getGiaKhuyenMai() {
        if (PhanTramGiam > 0 && PhanTramGiam <= 100) {
            return (int) (Gia - (Gia * (PhanTramGiam / 100.0f)));
        }
        return Gia;
    }
}
