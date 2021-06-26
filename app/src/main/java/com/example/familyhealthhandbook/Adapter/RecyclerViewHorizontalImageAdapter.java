package com.example.familyhealthhandbook.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyhealthhandbook.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewHorizontalImageAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalImageAdapter.RecyclerViewHolder> {

    private Context context;
    List<Uri> images;

    public RecyclerViewHorizontalImageAdapter(Context context)
    {
        this.context = context;
    }

    public void setData(List<Uri> list)
    {
        this.images = list;
        notifyDataSetChanged();
    }

    public List<Uri> getData()
    {
        return this.images;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_horizontal_image, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        if(images == null)
            return;;
        Picasso.with(context).load(images.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(images == null)
            return 0;
        return images.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.health_record_iv);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
