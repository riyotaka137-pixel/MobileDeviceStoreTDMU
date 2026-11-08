package com.tdmu.mobiledevicestoretdmu.model;

public class ThuongHieu {
    private int MaThuongHieu;
    private String TenThuongHieu;
    private String MoTa;
    private String HinhAnh;

    public ThuongHieu() {
    }

    public ThuongHieu(int maThuongHieu, String tenThuongHieu, String moTa) {
        this(maThuongHieu, tenThuongHieu, moTa, "");
    }

    public ThuongHieu(int maThuongHieu, String tenThuongHieu, String moTa, String hinhAnh) {
        MaThuongHieu = maThuongHieu;
        TenThuongHieu = tenThuongHieu;
        MoTa = moTa;
        HinhAnh = hinhAnh;
    }

    public int getMaThuongHieu() { return MaThuongHieu; }
    public void setMaThuongHieu(int maThuongHieu) { MaThuongHieu = maThuongHieu; }

    public String getTenThuongHieu() { return TenThuongHieu; }
    public void setTenThuongHieu(String tenThuongHieu) { TenThuongHieu = tenThuongHieu; }

    public String getMoTa() { return MoTa; }
    public void setMoTa(String moTa) { MoTa = moTa; }

    public String getHinhAnh() { return HinhAnh; }
    public void setHinhAnh(String hinhAnh) { HinhAnh = hinhAnh; }

    @Override
    public String toString() {
        return TenThuongHieu;
    }
}
