package com.example.familyhealthhandbook.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyhealthhandbook.Model.Group;
import com.example.familyhealthhandbook.Model.User;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;
import com.example.familyhealthhandbook.View.Login.LoginActivity;
import com.example.familyhealthhandbook.View.Members.MembersActivity;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewMembersAdapter extends RecyclerView.Adapter<RecyclerViewMembersAdapter.RecyclerViewHolder> {


    private List<User> members;
    private Context context;
    private MembersActivity activity;
    private Group currentGroup;
    private static ClickListener clickListener;

    public RecyclerViewMembersAdapter(Context context, List<User> members, MembersActivity activity)
    {
        this.context = context;
        this.members = members;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_member, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        if(members == null)
            return;

        String avatar = members.get(position).getAvatar();
        String name = members.get(position).getName();

        Picasso.with(context).load(avatar).into(holder.imageView);
        holder.name_tv.setText(name);
        if(members.get(position).get_id().matches(currentGroup.getMaster()))
            holder.per_tv.setText("Quản trị viên");
        else
            holder.per_tv.setText("Thành viên");

        if(!getIdUserFromSharedPreferences().matches(currentGroup.getMaster()))
        {
            holder.button.setVisibility(View.GONE);
            return;
        }

        PopupMenu menu = new PopupMenu(context, holder.button);
        menu.inflate(R.menu.menu_member);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.option_transferPer:
                        if(members.get(position).get_id().matches(currentGroup.getMaster())) {
                            Toast.makeText(context, "Bạn đang là quản trị viên!", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        transferPermission(position);
                        return true;
                    case R.id.option_kick:
                        if(members.get(position).get_id().matches(currentGroup.getMaster())) {
                            showAlertYouAreMaster();
                        }
                        else
                        {
                            kickMember(position);
                        }
                        return true;
                    default:
                        return false;

                }
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.show();
            }
        });
    }

    void showAlertYouAreMaster()
    {
        new AlertDialog.Builder(context)
                .setTitle("Thông báo")
                .setMessage("Bạn đang là quản trị viên\nHãy chuyển quyền quản trị cho thành viên khác trước khi rời nhóm")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    void kickMember(int position)
    {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa thành viên")
                .setMessage("Bạn có chắc muốn xóa " + members.get(position).getName() + " khỏi gia đình?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String token = getTokenFromSharedPreferences();
                        String idUser = members.get(position).get_id();
                        String idGroup = currentGroup.getId();

                        RequestBody requestBody_idUser = RequestBody.create(MultipartBody.FORM, idUser);
                        RequestBody requestBody_idGroup = RequestBody.create(MultipartBody.FORM, idGroup);
                        Utils.getApi().KickMember(token, requestBody_idGroup, requestBody_idUser).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful())
                                {
                                    Toast.makeText(context, "Đã xóa " +members.get(position).getName() + " khỏi gia đình", Toast.LENGTH_SHORT).show();
                                    members.remove(position);
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }
                })
                .setNegativeButton("Hủy", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
    void transferPermission(int position)
    {
        String token = getTokenFromSharedPreferences();
        String idUser = members.get(position).get_id();

        RequestBody requestBody_idGroup = RequestBody.create(MultipartBody.FORM, currentGroup.getId());
        RequestBody requestBody_idUser = RequestBody.create(MultipartBody.FORM, idUser);

        Utils.getApi().TransferPermission(token, requestBody_idGroup, requestBody_idUser).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "Đã chuyển quyền quản trị", Toast.LENGTH_SHORT).show();
                setMaster(idUser);
                Intent intent = new Intent();
                intent.putExtra(MembersActivity.RESULT_GROUP, currentGroup);
                activity.setResult(Activity.RESULT_OK, intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Error connect to server", t.toString());
                t.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(members== null)
            return 0;

        return members.size();
    }

    public void setMaster(String master) {
        currentGroup.setMaster(master);
        notifyDataSetChanged();
    }

    public void setCurrentGroup(Group group)
    {
        currentGroup = group;
        notifyDataSetChanged();
    }
    public void setIdGroup(String idGroup) {
        currentGroup.setId(idGroup);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private CircleImageView imageView;
        private TextView name_tv;
        private TextView per_tv;
        private MaterialButton button;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.avatar_iv);
            name_tv = itemView.findViewById(R.id.name_tv);
            per_tv = itemView.findViewById(R.id.permission_tv);
            button = itemView.findViewById(R.id.menu_btn);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }


    public void setOnItemClickListener(RecyclerViewMembersAdapter.ClickListener clickListener) {
        RecyclerViewMembersAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }


    String getTokenFromSharedPreferences()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.TOKEN, "");
    }

    String getIdUserFromSharedPreferences()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.ID_USER, "");
    }
}
