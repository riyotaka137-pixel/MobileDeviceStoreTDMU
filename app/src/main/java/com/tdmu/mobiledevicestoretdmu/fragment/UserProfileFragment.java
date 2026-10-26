package com.tdmu.mobiledevicestoretdmu.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.activity.DangNhapActivity;
import com.tdmu.mobiledevicestoretdmu.activity.LichSuMuaHangActivity;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.TaiKhoan;

public class UserProfileFragment extends Fragment {

    private TextInputEditText edtEmailCaNhan, edtHoTenCaNhan, edtSoDienThoaiCaNhan, edtDiaChiCaNhan;
    private Button btnCapNhatThongTin, btnMoDialogDoiMatKhau, btnXemLichSuDonHang, btnDangXuatUser;
    private ImageView btnToggleEditMode;

    private DatabaseHelper databaseHelper;
    private int maTK = -1;
    private boolean isEditing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        btnToggleEditMode = view.findViewById(R.id.btnToggleEditMode);
        edtEmailCaNhan = view.findViewById(R.id.edtEmailCaNhan);
        edtHoTenCaNhan = view.findViewById(R.id.edtHoTenCaNhan);
        edtSoDienThoaiCaNhan = view.findViewById(R.id.edtSoDienThoaiCaNhan);
        edtDiaChiCaNhan = view.findViewById(R.id.edtDiaChiCaNhan);

        btnCapNhatThongTin = view.findViewById(R.id.btnCapNhatThongTin);
        btnMoDialogDoiMatKhau = view.findViewById(R.id.btnMoDialogDoiMatKhau);
        btnXemLichSuDonHang = view.findViewById(R.id.btnXemLichSuDonHang);
        btnDangXuatUser = view.findViewById(R.id.btnDangXuatUser);

        databaseHelper = new DatabaseHelper(requireContext());

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        maTK = sharedPreferences.getInt("MaTK", -1);

        if (maTK != -1) {
            taiThongTinCaNhan();
        }

        // Bắt sự kiện bật/tắt chế độ khóa/sửa bằng nút cây bút
        btnToggleEditMode.setOnClickListener(v -> toggleEditMode(!isEditing));

        btnCapNhatThongTin.setOnClickListener(v -> capNhatThongTin());
        btnMoDialogDoiMatKhau.setOnClickListener(v -> hienDialogDoiMatKhau());

        btnXemLichSuDonHang.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LichSuMuaHangActivity.class);
            startActivity(intent);
        });

        btnDangXuatUser.setOnClickListener(v -> {
            SharedPreferences sp = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            sp.edit().clear().apply();

            Toast.makeText(requireContext(), "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(requireContext(), DangNhapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    /**
     * Bật hoặc Tắt chế độ chỉnh sửa thông tin cá nhân
     */
    private void toggleEditMode(boolean enable) {
        this.isEditing = enable;

        // Bật/Khóa trạng thái cho các trường nhập liệu (Email giữ nguyên khóa)
        edtHoTenCaNhan.setEnabled(enable);
        edtSoDienThoaiCaNhan.setEnabled(enable);
        edtDiaChiCaNhan.setEnabled(enable);

        if (enable) {
            // Đang trong chế độ Sửa -> Hiện nút LƯU, chuyển icon sang nút X hủy bỏ
            btnCapNhatThongTin.setVisibility(View.VISIBLE);
            btnToggleEditMode.setImageResource(R.drawable.ic_close);

            edtHoTenCaNhan.requestFocus();
            if (getContext() != null) {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(edtHoTenCaNhan, InputMethodManager.SHOW_IMPLICIT);
                }
            }
            Toast.makeText(requireContext(), "Đã bật chế độ chỉnh sửa thông tin", Toast.LENGTH_SHORT).show();
        } else {
            // Đang trong chế độ Khóa -> Ẩn nút LƯU, trả icon Cây Bút, load lại dữ liệu cũ
            btnCapNhatThongTin.setVisibility(View.GONE);
            btnToggleEditMode.setImageResource(R.drawable.ic_edit);
            taiThongTinCaNhan();

            if (getContext() != null) {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null && getView() != null) {
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                }
            }
            Toast.makeText(requireContext(), "Đã hủy chỉnh sửa", Toast.LENGTH_SHORT).show();
        }
    }

    private void taiThongTinCaNhan() {
        if (databaseHelper == null) return;
        TaiKhoan taiKhoan = databaseHelper.getTaiKhoanById(maTK);
        if (taiKhoan != null) {
            edtEmailCaNhan.setText(taiKhoan.getEmail());
            edtHoTenCaNhan.setText(taiKhoan.getHoTen());
            edtSoDienThoaiCaNhan.setText(taiKhoan.getSoDienThoai());
            edtDiaChiCaNhan.setText(taiKhoan.getDiaChi());
        }
    }

    private void capNhatThongTin() {
        if (edtHoTenCaNhan.getText() == null) return;
        String hoTen = edtHoTenCaNhan.getText().toString().trim();
        String sdt = edtSoDienThoaiCaNhan.getText() != null ? edtSoDienThoaiCaNhan.getText().toString().trim() : "";
        String diaChi = edtDiaChiCaNhan.getText() != null ? edtDiaChiCaNhan.getText().toString().trim() : "";

        if (hoTen.isEmpty()) {
            Toast.makeText(requireContext(), "Họ và tên không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean thanhCong = databaseHelper.updateThongTinCaNhan(maTK, hoTen, sdt, diaChi);
        if (thanhCong) {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("HoTen", hoTen);
            editor.apply();

            Toast.makeText(requireContext(), "Cập nhật thông tin cá nhân thành công!", Toast.LENGTH_SHORT).show();

            // Khóa lại các ô sau khi lưu thành công
            toggleEditMode(false);
        } else {
            Toast.makeText(requireContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void hienDialogDoiMatKhau() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_doi_mat_khau, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        EditText edtMatKhauCu = view.findViewById(R.id.edtMatKhauCu);
        EditText edtMatKhauMoi = view.findViewById(R.id.edtMatKhauMoi);
        EditText edtXacNhanMatKhauMoi = view.findViewById(R.id.edtXacNhanMatKhauMoi);

        Button btnHuyDoiMatKhau = view.findViewById(R.id.btnHuyDoiMatKhau);
        Button btnLuuDoiMatKhau = view.findViewById(R.id.btnLuuDoiMatKhau);

        btnHuyDoiMatKhau.setOnClickListener(v -> dialog.dismiss());

        btnLuuDoiMatKhau.setOnClickListener(v -> {
            String passCu = edtMatKhauCu.getText().toString().trim();
            String passMoi = edtMatKhauMoi.getText().toString().trim();
            String xacNhan = edtXacNhanMatKhauMoi.getText().toString().trim();

            if (passCu.isEmpty() || passMoi.isEmpty() || xacNhan.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!passMoi.equals(xacNhan)) {
                Toast.makeText(requireContext(), "Mật khẩu mới không trùng khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean doiThanhCong = databaseHelper.doiMatKhau(maTK, passCu, passMoi);
            if (doiThanhCong) {
                Toast.makeText(requireContext(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(requireContext(), "Mật khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
