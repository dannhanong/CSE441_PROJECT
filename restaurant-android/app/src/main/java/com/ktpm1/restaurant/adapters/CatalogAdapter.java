package com.ktpm1.restaurant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ktpm1.restaurant.BuildConfig;
import com.ktpm1.restaurant.R;
import com.ktpm1.restaurant.models.Catalog;

import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder> {
    private List<Catalog> catalogList;

    public CatalogAdapter(List<Catalog> catalogList) {
        this.catalogList = catalogList;
    }

    public void setCatalogList(List<Catalog> catalogList) {
        this.catalogList = catalogList;
    }

    @NonNull
    @Override
    public CatalogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catalog, parent, false);
        return new CatalogAdapter.CatalogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogViewHolder holder, int position) {
        Catalog catalog = catalogList.get(position);
        String fileCode = catalog.getImage();
        String imageUrl = BuildConfig.BASE_URL + "/files/preview/" + fileCode;
        holder.tvCatalogName.setText(catalog.getName());
        holder.tvCatalogDescription.setText(catalog.getDescription());
        Glide.with(holder.itemView).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imgCatalog);
    }

    @Override
    public int getItemCount() {
        return catalogList.size();
    }

    public static class CatalogViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCatalog;
        TextView tvCatalogName, tvCatalogDescription;

        public CatalogViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCatalog = itemView.findViewById(R.id.img_catalog);
            tvCatalogName = itemView.findViewById(R.id.tv_catalog_name);
            tvCatalogDescription = itemView.findViewById(R.id.tv_catalog_description);
        }
    }
}
