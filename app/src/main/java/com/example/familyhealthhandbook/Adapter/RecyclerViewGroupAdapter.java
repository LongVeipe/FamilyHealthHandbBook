package com.example.familyhealthhandbook.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyhealthhandbook.Model.Group;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;
import com.example.familyhealthhandbook.View.Groups.GroupsFragment;
import com.example.familyhealthhandbook.View.Login.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.familyhealthhandbook.Adapter.RecyclerViewHealthRecordAdapter.ID_HR;
import static com.example.familyhealthhandbook.Adapter.RecyclerViewHealthRecordAdapter.SHARED_PREFERENCES_ID_HEALTH_RECORD_TO_SHARE;

public class RecyclerViewGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public static final int TYPE_CONVERSATION = 1, TYPE_SHARE = 2;

    private List<Group> groupList;
    private ArrayList<Group> allGroups;
    private Context context;
    private static ClickListener clickListener;
    private int type;

    public RecyclerViewGroupAdapter(Context context, List<Group> groupList, int type)
    {
        this.context = context;
        this.groupList = groupList;
        this.allGroups = new ArrayList<>(groupList);
        this.type = type;
    }

    public void addData(Group group)
    {
        this.groupList.add(group);
        notifyDataSetChanged();
    }

    public void removeItemById(String idGroup)
    {
        for(Group item: groupList)
        {
            if(item.getId().matches(idGroup))
            {
                groupList.remove(item);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public List<Group> getData()
    {
        return groupList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == TYPE_CONVERSATION) {
            view = LayoutInflater.from(context).inflate(R.layout.item_recycler_group, parent, false);
            return new RecyclerViewHolderConversation(view);
        }
        else
        {
            view = LayoutInflater.from(context).inflate(R.layout.item_recycler_share_group, parent, false);
            return new RecyclerViewHolderShare(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == TYPE_CONVERSATION)
        {
            RecyclerViewHolderConversation holderConversation = (RecyclerViewHolderConversation) holder;
            configureViewHolderConversation(holderConversation, position);
        }
        else
        {
            RecyclerViewHolderShare holderShare = (RecyclerViewHolderShare) holder;
            configureViewHolderShare(holderShare, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(type == TYPE_CONVERSATION)
            return TYPE_CONVERSATION;
        else
            return TYPE_SHARE;
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString().toLowerCase().trim();
                if(strSearch.isEmpty())
                    groupList = allGroups;
                else
                {
                    List<Group> list = new ArrayList<>();
                    for(Group item: allGroups)
                    {
                        if(item.getName().toLowerCase().contains(strSearch))
                            list.add(item);
                    }
                    groupList = list;
                }

                FilterResults results = new FilterResults();
                results.values = groupList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                groupList = (List<Group>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class RecyclerViewHolderConversation extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private CircleImageView imageView;
        private TextView textView;
        public RecyclerViewHolderConversation(@NonNull View itemView) {
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

    static class RecyclerViewHolderShare extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private CircleImageView imageView;
        private TextView textView;
        private MaterialButton send_btn;
        public RecyclerViewHolderShare(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.groupAvatar_iv);
            textView = itemView.findViewById(R.id.groupName_TextView);
            send_btn = itemView.findViewById(R.id.send_btn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) { clickListener.OnClick(v, getAdapterPosition());
        }
    }

    public void setOnItemClickListener(RecyclerViewGroupAdapter.ClickListener clickListener) {
        RecyclerViewGroupAdapter.clickListener = clickListener;
    }
    public interface ClickListener{
        void OnClick(View v, int position);
    }

    private void configureViewHolderConversation(RecyclerViewHolderConversation holder, int position)
    {
        String name = groupList.get(position).getName();
        String avatar = groupList.get(position).getAvatar();

        Picasso.with(context).load(avatar).into(holder.imageView);
        holder.textView.setText(name);
    }

    private void configureViewHolderShare(RecyclerViewHolderShare holder, int position)
    {
        String name = groupList.get(position).getName();
        String avatar = groupList.get(position).getAvatar();

        Picasso.with(context).load(avatar).into(holder.imageView);
        holder.textView.setText(name);
        holder.send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = getTokenFromSharedPreferences();
                String idGroup = groupList.get(position).getId();
                String idHR = getIdHealthRecordFromSharedPreferences();

                createPost(token, idGroup, idHR);
            }
        });
    }

    void createPost(String token, String idGroup, String idHealthRecord)
    {
        RequestBody requestBody_idGroup = RequestBody.create(MultipartBody.FORM, idGroup);
        RequestBody requestBody_idHR = RequestBody.create(MultipartBody.FORM, idHealthRecord);
        Utils.getApi().CreatePost(token, requestBody_idGroup, requestBody_idHR).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "Đã chia sẻ phiếu khám", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("HR Creation Error", t.toString());
                t.printStackTrace();
            }
        });
    }

    String getTokenFromSharedPreferences()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.TOKEN, "");
    }

    private String getIdHealthRecordFromSharedPreferences()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_ID_HEALTH_RECORD_TO_SHARE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ID_HR, "");
    }
}
