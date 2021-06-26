package com.example.familyhealthhandbook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyhealthhandbook.Model.Post;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.View.Image.ImageActivity;
import com.example.familyhealthhandbook.View.Login.LoginActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int YOU = 1, ME = 2;
    public static final String EXTRA_IMAGES = "images";

    private Context context;
    private List<Post> posts;
    private String idUser;
    private static ClickListener clickListener;


    public RecyclerViewConversationAdapter(Context context, List<Post> posts)
    {
        this.context = context;
        this.posts =posts;
        idUser = getIdUserFromSharedPreferences();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType)
        {
            case ME:
                View vm = LayoutInflater.from(context).inflate(R.layout.item_recycler_holder_me, parent, false);
                return new RecyclerViewHolderMe(vm);
            default:
                View vy = LayoutInflater.from(context).inflate(R.layout.item_recycler_holder_you, parent, false);
                return new RecyclerViewHolderYou(vy);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == ME)
        {
            RecyclerViewHolderMe recyclerViewHolderMe = (RecyclerViewHolderMe) holder;
            configureViewHolderMe(recyclerViewHolderMe, position);
        }
        else
        {
            RecyclerViewHolderYou recyclerViewHolderYou = (RecyclerViewHolderYou) holder;
            configureViewHolderYou(recyclerViewHolderYou, position);
        }
    }

    @Override
    public int getItemCount() {
        if(posts == null)
            return 0;
        return posts.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(posts.get(position).getUser().get_id().matches(idUser))
            return ME;
        else
            return YOU;
    }

    String getIdUserFromSharedPreferences()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.ID_USER, "");
    }
    static class RecyclerViewHolderYou extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private CircleImageView avatar_iv;
        private TextView time_tv;
        private TextView location_tv;
        private TextView sickness_tv;
        private TextView date_tv;
        private ImageView firstImg_iv;

        public RecyclerViewHolderYou(@NonNull View itemView) {
            super(itemView);

            avatar_iv = itemView.findViewById(R.id.avatar_ImageView);
            time_tv = itemView.findViewById(R.id.tv_time);
            location_tv = itemView.findViewById(R.id.location_tv);
            sickness_tv = itemView.findViewById(R.id.sickness_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            firstImg_iv = itemView.findViewById(R.id.firstImg_iv);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            clickListener.OnClick(v,getAdapterPosition());
        }
    }

    static class RecyclerViewHolderMe extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView time_tv;
        private TextView location_tv;
        private TextView sickness_tv;
        private TextView date_tv;
        private ImageView firstImg_iv;

        public RecyclerViewHolderMe(@NonNull View itemView) {
            super(itemView);
            time_tv = itemView.findViewById(R.id.tv_time);
            location_tv = itemView.findViewById(R.id.location_tv);
            sickness_tv = itemView.findViewById(R.id.sickness_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            firstImg_iv = itemView.findViewById(R.id.firstImg_iv);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.OnClick(v,getAdapterPosition());
        }
    }

    public void setOnItemClickListener(ClickListener clickListener)
    {
        RecyclerViewConversationAdapter.clickListener = clickListener;
    }
    public interface ClickListener
    {
        void OnClick(View v, int position);
    }

    private void configureViewHolderMe(RecyclerViewHolderMe viewHolder, int position)
    {
        String avatar = posts.get(position).getUser().getAvatar();
        String time = posts.get(position).getCreateAt();
        String sickness = posts.get(position).getHealthRecord().getSickness().getName();
        String location = posts.get(position).getHealthRecord().getLocation();
        String date = getDate(posts.get(position).getHealthRecord().getCreateAt());
        String firstImg = posts.get(position).getHealthRecord().getImages().get(0);


        viewHolder.time_tv.setText(time);
        viewHolder.sickness_tv.setText(sickness);
        viewHolder.location_tv.setText(location);
        viewHolder.date_tv.setText(date);
        Picasso.with(context).load(firstImg).into(viewHolder.firstImg_iv);
        viewHolder.firstImg_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageClick(position);
            }
        });
    }
    private void configureViewHolderYou(RecyclerViewHolderYou viewHolder, int position)
    {
        String avatar = posts.get(position).getUser().getAvatar();
        String time = posts.get(position).getCreateAt();
        String sickness = posts.get(position).getHealthRecord().getSickness().getName();
        String location = posts.get(position).getHealthRecord().getLocation();
        String date = getDate(posts.get(position).getHealthRecord().getCreateAt());
        String firstImg = posts.get(position).getHealthRecord().getImages().get(0);

        Picasso.with(context).load(avatar).into(viewHolder.avatar_iv);
        viewHolder.time_tv.setText(time);
        viewHolder.sickness_tv.setText(sickness);
        viewHolder.location_tv.setText(location);
        viewHolder.date_tv.setText(date);
        Picasso.with(context).load(firstImg).into(viewHolder.firstImg_iv);
        viewHolder.firstImg_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageClick(position);
            }
        });
    }

    void imageClick(int position)
    {
        Intent intent = new Intent(context, ImageActivity.class);
        Gson gson = new Gson();
        String jsonImages = gson.toJson(posts.get(position).getHealthRecord().getImages());
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_IMAGES, jsonImages);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    String getDate(String date)
    {
        String[] arr = date.split("-");
        return arr[2].split("T")[0] + "/" + arr[1] + "/" + arr[0];
    }
}
