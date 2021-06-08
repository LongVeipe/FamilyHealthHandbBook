package com.example.familyhealthhandbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyhealthhandbook.Model.Group;
import com.example.familyhealthhandbook.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewGroupAdapter extends RecyclerView.Adapter<RecyclerViewGroupAdapter.RecyclerViewHolder> {

    private List<Group> groupList;
    private Context context;
    private static ClickListener clickListener;

    public RecyclerViewGroupAdapter(Context context, List<Group> groupList)
    {
        this.context = context;
        this.groupList = groupList;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_group, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String name = groupList.get(position).getName();
        String avatar = groupList.get(position).getAvatar();

        Picasso.with(context).load(avatar).into(holder.imageView);
        holder.textView.setText(name);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private CircleImageView imageView;
        private TextView textView;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.groupAvatar_iv);
            textView = itemView.findViewById(R.id.groupName_TextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.OnClick(v, getAdapterPosition());
        }
    }

    public void setOnItemClickListener(RecyclerViewGroupAdapter.ClickListener clickListener) {
        RecyclerViewGroupAdapter.clickListener = clickListener;
    }
    public interface ClickListener{
        void OnClick(View v, int position);
    }
}
