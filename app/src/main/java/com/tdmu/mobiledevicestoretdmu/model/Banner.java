package com.tdmu.mobiledevicestoretdmu.model;

public class Banner {
    private int MaBanner;
    private String TenBanner;
    private String HinhAnh;
    private String MoTa;

    public Banner() {
    }

    public Banner(int maBanner, String tenBanner, String hinhAnh, String moTa) {
        MaBanner = maBanner;
        TenBanner = tenBanner;
        HinhAnh = hinhAnh;
        MoTa = moTa;
    }

    public int getMaBanner() { return MaBanner; }
    public void setMaBanner(int maBanner) { MaBanner = maBanner; }

    public String getTenBanner() { return TenBanner; }
    public void setTenBanner(String tenBanner) { TenBanner = tenBanner; }

    public String getHinhAnh() { return HinhAnh; }
    public void setHinhAnh(String hinhAnh) { HinhAnh = hinhAnh; }

    public String getMoTa() { return MoTa; }
    public void setMoTa(String moTa) { MoTa = moTa; }
}
