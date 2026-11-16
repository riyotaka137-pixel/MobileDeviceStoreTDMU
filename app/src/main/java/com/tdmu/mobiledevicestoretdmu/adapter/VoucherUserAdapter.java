package com.tdmu.mobiledevicestoretdmu.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.model.GiamGia;

import java.util.List;

public class VoucherUserAdapter extends RecyclerView.Adapter<VoucherUserAdapter.ViewHolder> {

    private Context context;
    private List<GiamGia> listVoucher;

    public VoucherUserAdapter(Context context, List<GiamGia> listVoucher) {
        this.context = context;
        this.listVoucher = listVoucher;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voucher_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GiamGia gg = listVoucher.get(position);

        holder.tvMaCodeVoucherUser.setText(gg.getMaCode());
        holder.tvMucGiamVoucherUser.setText("Giảm " + gg.getPhanTramGiam() + "%");

        if (gg.getTienGiamToiDa() > 0) {
            String toiDaStr = String.format("%,d", gg.getTienGiamToiDa()).replace(',', '.');
            holder.tvTienGiamToiDaUser.setText("Giảm tối đa: " + toiDaStr + " đ");
            holder.tvTienGiamToiDaUser.setVisibility(View.VISIBLE);
        } else {
            holder.tvTienGiamToiDaUser.setVisibility(View.GONE);
        }

        if (gg.getDonToiThieu() > 0) {
            String donToiThieuStr = String.format("%,d", gg.getDonToiThieu()).replace(',', '.');
            holder.tvDonToiThieuUser.setText("Đơn tối thiểu: " + donToiThieuStr + " đ");
        } else {
            holder.tvDonToiThieuUser.setText("Đơn tối thiểu: Không yêu cầu");
        }

        if (gg.getNgayHetHan() != null && !gg.getNgayHetHan().trim().isEmpty()) {
            holder.tvHSDVoucherUser.setText("HSD: " + gg.getNgayHetHan());
        } else {
            holder.tvHSDVoucherUser.setText("HSD: Vô thời hạn");
        }

        holder.btnSaoChepMa.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("VoucherCode", gg.getMaCode());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Đã sao chép mã: " + gg.getMaCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listVoucher != null ? listVoucher.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaCodeVoucherUser, tvMucGiamVoucherUser, tvTienGiamToiDaUser, tvDonToiThieuUser, tvHSDVoucherUser;
        Button btnSaoChepMa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaCodeVoucherUser = itemView.findViewById(R.id.tvMaCodeVoucherUser);
            tvMucGiamVoucherUser = itemView.findViewById(R.id.tvMucGiamVoucherUser);
            tvTienGiamToiDaUser = itemView.findViewById(R.id.tvTienGiamToiDaUser);
            tvDonToiThieuUser = itemView.findViewById(R.id.tvDonToiThieuUser);
            tvHSDVoucherUser = itemView.findViewById(R.id.tvHSDVoucherUser);
            btnSaoChepMa = itemView.findViewById(R.id.btnSaoChepMa);
        }
    }
}
