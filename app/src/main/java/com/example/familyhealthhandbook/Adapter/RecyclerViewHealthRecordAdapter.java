package com.example.familyhealthhandbook.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyhealthhandbook.Model.Group;
import com.example.familyhealthhandbook.Model.HealthRecord;
import com.example.familyhealthhandbook.Model.responseLogin;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;
import com.example.familyhealthhandbook.View.Image.ImageActivity;
import com.example.familyhealthhandbook.View.Login.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewHealthRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String EXTRA_IMAGES = "images";
    public static final String SHARED_PREFERENCES_ID_HEALTH_RECORD_TO_SHARE = "id_health_record";
    public static final String ID_HR = "id";
    private final int CONVERSATION = 1, PERSONAL = 2;

    private Context context;
    private List<HealthRecord> healthRecords;
    private static RecyclerViewHealthRecordAdapter.ClickListener clickListener;

    public RecyclerViewHealthRecordAdapter(Context context, List<HealthRecord> healthRecords) {
        this.context = context;
        this.healthRecords = healthRecords;
    }

    void setData(List<HealthRecord> list)
    {
        this.healthRecords = list;
        notifyDataSetChanged();
    }

    public void addItem(HealthRecord item)
    {
        this.healthRecords.add(item);
        notifyDataSetChanged();
    }

    void removeItemAtPosition(int position)
    {
        this.healthRecords.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == PERSONAL) {
            view = LayoutInflater.from(context).inflate(R.layout.item_recycler_personal_health_record, parent, false);
            return new RecyclerViewHolderPersonal(view);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.item_recycler_health_record, parent, false);
            return new RecyclerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == PERSONAL)
        {
            RecyclerViewHolderPersonal viewHolderPersonal = (RecyclerViewHolderPersonal) holder;
            configureViewHolderPersonal(viewHolderPersonal, position);
        }
        else
        {
            RecyclerViewHolder viewHolder = (RecyclerViewHolder) holder;
            configureViewHolder(viewHolder, position);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(context.getClass().getSimpleName().matches("PersonalActivity"))
            return PERSONAL;
        else return CONVERSATION;
    }

    @Override
    public int getItemCount() {
        return healthRecords.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView location_tv;
        private TextView sickness_tv;
        private TextView date_tv;
        private ImageView firstImg_iv;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            location_tv = itemView.findViewById(R.id.location_tv);
            sickness_tv = itemView.findViewById(R.id.sickness_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            firstImg_iv = itemView.findViewById(R.id.firstImg_iv);
        }

        @Override
        public void onClick(View v) {
            clickListener.OnClick(v, getAdapterPosition());
        }
    }
    static class RecyclerViewHolderPersonal extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView location_tv;
        private TextView sickness_tv;
        private TextView date_tv;
        private ImageView firstImg_iv;
        private MaterialButton button;
        private MaterialButton popupMenu_btn;

        public RecyclerViewHolderPersonal(@NonNull View itemView) {
            super(itemView);
            location_tv = itemView.findViewById(R.id.location_tv);
            sickness_tv = itemView.findViewById(R.id.sickness_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            firstImg_iv = itemView.findViewById(R.id.firstImg_iv);
            button = itemView.findViewById(R.id.share_btn);
            popupMenu_btn = itemView.findViewById(R.id.popupMenu_btn);
        }

        @Override
        public void onClick(View v) {
            clickListener.OnClick(v, getAdapterPosition());
        }
    }
    void imageClick(int position)
    {
        Intent intent = new Intent(context, ImageActivity.class);
        Gson gson = new Gson();
        String jsonImages = gson.toJson(healthRecords.get(position).getImages());
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

    public void setOnItemClickListener(RecyclerViewHealthRecordAdapter.ClickListener clickListener)
    {
        RecyclerViewHealthRecordAdapter.clickListener = clickListener;
    }
    public interface ClickListener
    {
        void OnClick(View v, int position);
    }
    void configureViewHolder(RecyclerViewHolder holder, int position)
    {
        String location = healthRecords.get(position).getLocation();
        String date = getDate(healthRecords.get(position).getCreateAt());
        String sickness = healthRecords.get(position).getSickness().getName();
        String firstImg = healthRecords.get(position).getImages().get(0);


        holder.location_tv.setText(location);
        holder.sickness_tv.setText(sickness);
        holder.date_tv.setText(date);
        Picasso.with(context).load(firstImg).into(holder.firstImg_iv);
        holder.firstImg_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageClick(position);
            }
        });
    }

    void configureViewHolderPersonal(RecyclerViewHolderPersonal holder, int position)
    {
        String location = healthRecords.get(position).getLocation();
        String date = getDate(healthRecords.get(position).getCreateAt());
        String sickness = healthRecords.get(position).getSickness().getName();
        String firstImg = healthRecords.get(position).getImages().get(0);


        holder.location_tv.setText(location);
        holder.sickness_tv.setText(sickness);
        holder.date_tv.setText(date);
        Picasso.with(context).load(firstImg).into(holder.firstImg_iv);

        holder.firstImg_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageClick(position);
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIdHR(position);
                showShareDialog(Gravity.CENTER, v);
            }
        });
        PopupMenu menu = new PopupMenu(context, holder.popupMenu_btn);
        menu.inflate(R.menu.menu_personal_health_record);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.option_edit:
                        return true;
                    case R.id.option_delete:
                        deleteHealthRecord(position);
                        return true;
                    default:
                        return false;

                }
            }
        });
        holder.popupMenu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.show();
            }
        });
    }

    private void deleteHealthRecord(int position)
    {
        new AlertDialog.Builder(context)
                .setTitle("Chắc chắn muốn xóa?")
                .setMessage("phiếu khám sẽ bị khỏi hệ thống và trên các gia đình bạn đã chia sẻ!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.getApi().DeleteHealthRecord(healthRecords.get(position).getId(), getTokenFromSharedPreferences())
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e("Deletion error", t.toString());
                                        t.printStackTrace();
                                    }
                                });
                        removeItemAtPosition(position);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        //Toast.makeText(context, "Xóa phiếu khám thành công", Toast.LENGTH_SHORT).show();
    }

    void showShareDialog(int gravity, View v)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.dialog_share_health_record, null);
        dialog.setContentView(R.layout.dialog_share_health_record);

        Window window = dialog.getWindow();
        if(window == null)
            return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);
        getGroups(dialog);
        dialog.show();
    }

    void initShareGroupRecyclerView(Dialog dialog, List<Group> groups)
    {
        RecyclerView recyclerView = dialog.findViewById(R.id.shareGroup_rv);
        RecyclerViewGroupAdapter adapter = new RecyclerViewGroupAdapter(dialog.getContext(), groups, RecyclerViewGroupAdapter.TYPE_SHARE);
        recyclerView.setLayoutManager(new GridLayoutManager(dialog.getContext(), 1));
        recyclerView.setClipToPadding(false);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    void getGroups(Dialog dialog)
    {
        String access_token = getTokenFromSharedPreferences();
        //RequestBody requestBody_token = RequestBody.create(MultipartBody.FORM, access_token);
        Utils.getApi().GetJoinedGroups(access_token).enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    initShareGroupRecyclerView(dialog, response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Log.e("Error connect to server", t.toString());
                t.printStackTrace();
            }
        });
    }

    String getTokenFromSharedPreferences()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.TOKEN, "");
    }

    private void saveIdHR(int position)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_ID_HEALTH_RECORD_TO_SHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ID_HR,healthRecords.get(position).getId());
        editor.apply();
    }
}
