package com.example.android.maximfialko.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.maximfialko.R;
import com.example.android.maximfialko.Utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    @NonNull
    private List<NewsItem> news = new ArrayList<>();
    private final LayoutInflater inflater;
    @NonNull
    private final newsItemClickListener clickListener;
    @NonNull
    private final RequestManager imageLoader;

    public NewsAdapter(@NonNull Context context,
                       @NonNull List<NewsItem> news,
                       @NonNull newsItemClickListener clickListener) {
        this.news = news;
        this.inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;

        RequestOptions imageOption = new RequestOptions()
                .placeholder(R.drawable.avatar_placeholder)
                .fallback(R.drawable.avatar_placeholder)
                .centerCrop();
        this.imageLoader = Glide.with(context).applyDefaultRequestOptions(imageOption);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                inflater.inflate(R.layout.cardview_layout, parent, false)
        );
    }

    @Override //указывает содержимое каждого элемента RecyclerView
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get our custom object from our dataset at this position
        holder.bind(news.get(position));
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public void replaceItems(@NonNull List<NewsItem> newItems) {
        news.clear();
        news.addAll(newItems);
        notifyDataSetChanged();
    }

    public void add(List<NewsItem> newsItems) {
        this.news.addAll(newsItems);
    }

    public interface newsItemClickListener {
        void onItemClick(NewsItem item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryView;
        private TextView titleView;
        private TextView previewTextView;
        private ImageView imageUrlView;
        private TextView publishDateView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.onItemClick(news.get(position));
                }
            });
            categoryView = (TextView) itemView.findViewById(R.id.tv_cd_category);
            titleView = (TextView) itemView.findViewById(R.id.tv_cd_title);
            previewTextView = (TextView) itemView.findViewById(R.id.tv_previewText);
            imageUrlView = (ImageView) itemView.findViewById(R.id.iv_imageUrl);
            publishDateView = (TextView) itemView.findViewById(R.id.tv_publishDate);
        }

        void bind(NewsItem newsItem) {
            imageLoader.load(newsItem.getImageUrl()).into(imageUrlView);
            categoryView.setText(newsItem.getCategory());
            titleView.setText(newsItem.getTitle());
            previewTextView.setText(newsItem.getPreviewText());
//            publishDateView.setText(newsItem.getPublishDate().toString());
            publishDateView.setText(DateUtils.formatDateTime(itemView.getContext(),newsItem.getPublishDate()));
        }
    }
}
