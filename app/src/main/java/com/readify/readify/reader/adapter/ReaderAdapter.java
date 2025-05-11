package com.readify.readify.reader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.bumptech.glide.Glide;
import com.readify.readify.R;

public class ReaderAdapter extends RecyclerView.Adapter<ReaderAdapter.PageViewHolder> {

    private List<String> pages;
    private float fontSize;
    private boolean nightMode;

    public ReaderAdapter(List<String> pages, float fontSize, boolean nightMode) {
        this.pages = pages;
        this.fontSize = fontSize;
        this.nightMode = nightMode;
    }

    public void setFontSize(float newSize) {
        this.fontSize = newSize;
        notifyDataSetChanged();
    }

    public void setNightMode(boolean nightMode) {
        this.nightMode = nightMode;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reader_page_item, parent, false);
        return new PageViewHolder(view);
    }

    @Override
//    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
//        holder.tvPage.setText(pages.get(position));
//        holder.tvPage.setTextSize(fontSize);
//        holder.tvPage.setTextColor(nightMode ? 0xFFFFFFFF : 0xFF000000);
//        holder.itemView.setBackgroundColor(nightMode ? 0xFF1E1E1E : 0xFFFFFFFF);
//    }

    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        String pageContent = pages.get(position);

        // Nếu là trang đầu tiên và là URL ảnh thì hiển thị ảnh bìa
        if (position == 0 && (pageContent.contains("http://") || pageContent.contains("https://"))) {
            holder.tvPage.setVisibility(View.GONE);
            holder.imgCover.setVisibility(View.VISIBLE);

            Glide.with(holder.itemView.getContext())
                    .load(pageContent)
                    .into(holder.imgCover);
        } else {
            holder.imgCover.setVisibility(View.GONE);
            holder.tvPage.setVisibility(View.VISIBLE);

            holder.tvPage.setText(pageContent);
            holder.tvPage.setTextSize(fontSize);
            holder.tvPage.setTextColor(nightMode ? 0xFFFFFFFF : 0xFF000000);
            holder.itemView.setBackgroundColor(nightMode ? 0xFF1E1E1E : 0xFFFFFFFF);
        }
    }


    @Override
    public int getItemCount() {
        return pages.size();
    }

    static class PageViewHolder extends RecyclerView.ViewHolder {
        TextView tvPage;
        ImageView imgCover;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPage = itemView.findViewById(R.id.tvPage);
            imgCover = itemView.findViewById(R.id.imgCover);
        }
    }

}
