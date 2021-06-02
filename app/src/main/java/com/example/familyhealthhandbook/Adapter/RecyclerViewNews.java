package com.example.familyhealthhandbook.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyhealthhandbook.Model.News;
import com.example.familyhealthhandbook.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewNews extends RecyclerView.Adapter<RecyclerViewNews.RecyclerViewHolder> {

    private List<News> newsList;
    private Context context;
    private static ClickListener clickListener;

    public RecyclerViewNews(Context c, List<News> list)
    {
        this.context = c;
        this.newsList = list;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_news, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String title = newsList.get(position).getTitle();
        String image = newsList.get(position).getImage();
        String publishdate = newsList.get(position).getPublishdate();
        String link = newsList.get(position).getLink();
        String lead = newsList.get(position).getLead();

        Picasso.with(context).load(image).into(holder.thumb_Img);
        holder.title_TextView.setText(title);
        holder.des_TextView.setText(lead);
        holder.publish_TextView.setText(publishdate);

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView thumb_Img;
        private TextView title_TextView;
        private TextView des_TextView;
        private TextView publish_TextView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb_Img = (ImageView) itemView.findViewById(R.id.newsThumb_ImageView);
            des_TextView = itemView.findViewById(R.id.newsDes_TextView);
            title_TextView = itemView.findViewById(R.id.newsTitle_TextView);
            publish_TextView = itemView.findViewById(R.id.publishdate_TextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        RecyclerViewNews.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }
}
