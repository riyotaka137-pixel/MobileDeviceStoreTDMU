package com.tdmu.mobiledevicestoretdmu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdmu.mobiledevicestoretdmu.R;
import com.tdmu.mobiledevicestoretdmu.activity.TrangChuUserActivity;
import com.tdmu.mobiledevicestoretdmu.model.Banner;

import java.util.List;

public class BannerUserAdapter extends RecyclerView.Adapter<BannerUserAdapter.ViewHolder> {

    private Context context;
    private List<Banner> listBanner;

    public BannerUserAdapter(Context context, List<Banner> listBanner) {
        this.context = context;
        this.listBanner = listBanner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Banner banner = listBanner.get(position);

        QuanLyBannerAdapter.loadHinhAnhBanner(context, banner.getHinhAnh(), holder.imgBannerUser);

        // Khi User bấm vào Banner -> Chuyển thẳng sang thẻ Khuyến Mãi dưới Bottom Navigation (giống như click vào Giỏ hàng hay Trang chủ)
        holder.itemView.setOnClickListener(v -> {
            if (context instanceof TrangChuUserActivity) {
                ((TrangChuUserActivity) context).chuyenTabKhuyenMai();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBanner != null ? listBanner.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBannerUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBannerUser = itemView.findViewById(R.id.imgBannerUser);
        }
    }
}
