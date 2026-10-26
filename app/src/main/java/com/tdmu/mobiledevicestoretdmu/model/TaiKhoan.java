package com.tdmu.mobiledevicestoretdmu.model;

public class TaiKhoan {
    private int MaTK;
    private String HoTen;
    private String Email;
    private String MatKhau;
    private String SoDienThoai;
    private String DiaChi;
    private int Quyen; // 0 = Khách hàng, 1 = Admin
    private int TrangThai; // 1 = Hoạt động, 0 = Bị khóa

    public TaiKhoan() {
        this.TrangThai = 1; // Mặc định là Hoạt động
    }

    public TaiKhoan(int maTK, String hoTen, String email, String matKhau, String soDienThoai, String diaChi, int quyen, int trangThai) {
        MaTK = maTK;
        HoTen = hoTen;
        Email = email;
        MatKhau = matKhau;
        SoDienThoai = soDienThoai;
        DiaChi = diaChi;
        Quyen = quyen;
        TrangThai = trangThai;
    }

    public TaiKhoan(int maTK, String hoTen, String email, String matKhau, String soDienThoai, String diaChi, int quyen) {
        MaTK = maTK;
        HoTen = hoTen;
        Email = email;
        MatKhau = matKhau;
        SoDienThoai = soDienThoai;
        DiaChi = diaChi;
        Quyen = quyen;
        TrangThai = 1;
    }

    // Getters and Setters
    public int getMaTK() { return MaTK; }
    public void setMaTK(int maTK) { MaTK = maTK; }

    public String getHoTen() { return HoTen; }
    public void setHoTen(String hoTen) { HoTen = hoTen; }

    public String getEmail() { return Email; }
    public void setEmail(String email) { Email = email; }

    public String getMatKhau() { return MatKhau; }
    public void setMatKhau(String matKhau) { MatKhau = matKhau; }

    public String getSoDienThoai() { return SoDienThoai; }
    public void setSoDienThoai(String soDienThoai) { SoDienThoai = soDienThoai; }

    public String getDiaChi() { return DiaChi; }
    public void setDiaChi(String diaChi) { DiaChi = diaChi; }

    public int getQuyen() { return Quyen; }
    public void setQuyen(int quyen) { Quyen = quyen; }

    public int getTrangThai() { return TrangThai; }
    public void setTrangThai(int trangThai) { TrangThai = trangThai; }
}
