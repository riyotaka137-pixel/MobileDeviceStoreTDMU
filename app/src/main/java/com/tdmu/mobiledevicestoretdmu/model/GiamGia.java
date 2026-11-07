package com.tdmu.mobiledevicestoretdmu.model;

public class GiamGia {
    private int maGG;
    private String maCode;
    private int phanTramGiam;
    private int tienGiamToiDa;
    private int donToiThieu;
    private String ngayHetHan;
    private String moTa;

    public GiamGia() {
    }

    public GiamGia(int maGG, String maCode, int phanTramGiam, int tienGiamToiDa, int donToiThieu, String ngayHetHan, String moTa) {
        this.maGG = maGG;
        this.maCode = maCode;
        this.phanTramGiam = phanTramGiam;
        this.tienGiamToiDa = tienGiamToiDa;
        this.donToiThieu = donToiThieu;
        this.ngayHetHan = ngayHetHan;
        this.moTa = moTa;
    }

    public int getMaGG() { return maGG; }
    public void setMaGG(int maGG) { this.maGG = maGG; }

    public String getMaCode() { return maCode; }
    public void setMaCode(String maCode) { this.maCode = maCode; }

    public int getPhanTramGiam() { return phanTramGiam; }
    public void setPhanTramGiam(int phanTramGiam) { this.phanTramGiam = phanTramGiam; }

    public int getTienGiamToiDa() { return tienGiamToiDa; }
    public void setTienGiamToiDa(int tienGiamToiDa) { this.tienGiamToiDa = tienGiamToiDa; }

    public int getDonToiThieu() { return donToiThieu; }
    public void setDonToiThieu(int donToiThieu) { this.donToiThieu = donToiThieu; }

    public String getNgayHetHan() { return ngayHetHan; }
    public void setNgayHetHan(String ngayHetHan) { this.ngayHetHan = ngayHetHan; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}
