package com.tdmu.mobiledevicestoretdmu.model;

public class ThongBao {
    private int maTB;
    private int maTK;
    private String tieuDe;
    private String noiDung;
    private String ngayTao;
    private int trangThai; // 0: Chưa đọc, 1: Đã đọc

    public ThongBao() {
    }

    public ThongBao(int maTB, int maTK, String tieuDe, String noiDung, String ngayTao, int trangThai) {
        this.maTB = maTB;
        this.maTK = maTK;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
    }

    public int getMaTB() { return maTB; }
    public void setMaTB(int maTB) { this.maTB = maTB; }

    public int getMaTK() { return maTK; }
    public void setMaTK(int maTK) { this.maTK = maTK; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getNgayTao() { return ngayTao; }
    public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }
}
