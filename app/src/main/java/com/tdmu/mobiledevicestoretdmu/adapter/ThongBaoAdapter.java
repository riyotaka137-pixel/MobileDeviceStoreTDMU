package com.tdmu.mobiledevicestoretdmu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.database.DatabaseHelper;
import com.tdmu.mobiledevicestoretdmu.model.ThongBao;

import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHolder> {

    private Context context;
    private List<ThongBao> listThongBao;
    private DatabaseHelper databaseHelper;

    public ThongBaoAdapter(Context context, List<ThongBao> listThongBao) {
        this.context = context;
        this.listThongBao = listThongBao;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thong_bao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThongBao tb = listThongBao.get(position);

        holder.tvTieuDeThongBao.setText(tb.getTieuDe());
        holder.tvNoiDungThongBao.setText(tb.getNoiDung());
        holder.tvNgayTaoThongBao.setText(tb.getNgayTao() != null ? tb.getNgayTao() : "");

        // Đảm bảo màu chữ luôn rõ ràng, tương phản cao trên nền xám sáng của thẻ
        holder.tvTieuDeThongBao.setTextColor(Color.parseColor("#000000"));
        holder.tvNoiDungThongBao.setTextColor(Color.parseColor("#222222"));
        holder.tvNgayTaoThongBao.setTextColor(Color.parseColor("#555555"));

        if (tb.getTrangThai() == 0) {
            holder.viewUnreadDot.setVisibility(View.VISIBLE);
            holder.cardThongBao.setCardBackgroundColor(Color.parseColor("#F0F0F0"));
        } else {
            holder.viewUnreadDot.setVisibility(View.GONE);
            holder.cardThongBao.setCardBackgroundColor(Color.parseColor("#EAEAEA"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (tb.getTrangThai() == 0) {
                databaseHelper.danhDauDaDocThongBao(tb.getMaTB());
                tb.setTrangThai(1);
                holder.viewUnreadDot.setVisibility(View.GONE);
                holder.cardThongBao.setCardBackgroundColor(Color.parseColor("#EAEAEA"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listThongBao != null ? listThongBao.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardThongBao;
        TextView tvTieuDeThongBao, tvNoiDungThongBao, tvNgayTaoThongBao;
        View viewUnreadDot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardThongBao = itemView.findViewById(R.id.cardThongBao);
            tvTieuDeThongBao = itemView.findViewById(R.id.tvTieuDeThongBao);
            tvNoiDungThongBao = itemView.findViewById(R.id.tvNoiDungThongBao);
            tvNgayTaoThongBao = itemView.findViewById(R.id.tvNgayTaoThongBao);
            viewUnreadDot = itemView.findViewById(R.id.viewUnreadDot);
        }
    }
}
