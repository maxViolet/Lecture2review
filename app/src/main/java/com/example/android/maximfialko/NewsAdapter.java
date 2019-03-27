package com.example.android.maximfialko;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.maximfialko.utils.DateUtils;
import com.example.android.maximfialko.data.NewsItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.android.maximfialko.utils.DateUtils.formatDateFromDb;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    @NonNull
    private List<NewsItem> news;
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
                .placeholder(R.drawable.nyt_logo_black)
                .fallback(R.drawable.nyt_logo_black)
                .centerCrop();
        this.imageLoader = Glide.with(context).applyDefaultRequestOptions(imageOption);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                inflater.inflate(R.layout.cardview_news_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(news.get(position));
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public int getFirstItemId() {
        return news.get(0).getId();
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
        private int id;
        private TextView categoryView;
        private TextView titleView;
        private TextView previewTextView;
        private ImageView imageUrlView;
        private TextView publishDateView;

//        public int returnID() {
//            return id;
//        }

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.onItemClick(news.get(position));
                }
            });
            categoryView = itemView.findViewById(R.id.tv_cd_category);
            titleView = itemView.findViewById(R.id.tv_cd_title);
            previewTextView = itemView.findViewById(R.id.tv_previewText);
            imageUrlView = itemView.findViewById(R.id.iv_imageUrl);
            publishDateView = itemView.findViewById(R.id.tv_publishDate);
        }

        void bind(NewsItem newsItem) {
            id = newsItem.getId();
            imageLoader.load(newsItem.getImageUrl()).into(imageUrlView);
            categoryView.setText(newsItem.getCategory());
            titleView.setText(newsItem.getTitle());
            previewTextView.setText(newsItem.getPreviewText());
            publishDateView.setText(DateUtils.formatDateTime(
                    itemView.getContext(),
                    formatDateFromDb(newsItem.getPublishDate())
            ));
        }
    }


}
