package com.example.familyhealthhandbook.View.Groups;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.ULocale;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyhealthhandbook.Adapter.RecyclerViewGroupAdapter;
import com.example.familyhealthhandbook.Adapter.RecyclerViewHorizontalImageAdapter;
import com.example.familyhealthhandbook.Model.Group;
import com.example.familyhealthhandbook.Model.Sickness;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;
import com.example.familyhealthhandbook.View.Login.LoginActivity;
import com.example.familyhealthhandbook.View.Personal.PersonalActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroupDialog extends Dialog {

    private TextInputEditText name_edt;
    private TextInputEditText des_edt;
    private MaterialButton addAvatar_btn;
    private ImageView avatar_iv;
    private MaterialButton submit_btn;

    private FragmentActivity fragmentActivity;
    private GroupsFragment groupsFragment;

    public CreateGroupDialog(@NonNull Context context, FragmentActivity fragmentActivity, GroupsFragment groupsFragment) {
        super(context);
        this.fragmentActivity = fragmentActivity;
        this.groupsFragment = groupsFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_create_group);
        Window window = this.getWindow();
        if(window == null)
            return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        this.setCancelable(true);

        mapping();
        initAddAvatar_btn();
        initSubmit_btn();
    }

    private void mapping()
    {
        this.name_edt = this.findViewById(R.id.name_edt);
        this.des_edt = this.findViewById(R.id.description_edt);
        this.addAvatar_btn = this.findViewById(R.id.addAvatar_btn);
        this.avatar_iv = this.findViewById(R.id.avatar_iv);
        this.submit_btn = this.findViewById(R.id.submit_btn);
    }

    private void initAddAvatar_btn()
    {
        this.addAvatar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
    }

    private void initSubmit_btn()
    {
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = getTokenFromSharedPreferences();
                String name = name_edt.getText().toString().toLowerCase().trim();
                String des = des_edt.getText().toString().toLowerCase().trim();
                String avatarPath = (String)avatar_iv.getTag();
                createGroup(token, name, des, avatarPath);
                cancel();
            }
        });
    }

    void createGroup(String token, String name, String des, String avatarPath)
    {
        RequestBody requestBody_name = RequestBody.create(MultipartBody.FORM, name);
        RequestBody requestBody_des = RequestBody.create(MultipartBody.FORM, des);
        File file = new File(avatarPath);
        RequestBody requestBody_avatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part avatar = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody_avatar);

        Utils.getApi().CreateGroup(token, requestBody_name, requestBody_des, avatar).enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                if(response.body() != null && response.isSuccessful()) {
                    Toast.makeText(getContext(), "Gia đình " + name + " đã được tạo!", Toast.LENGTH_SHORT).show();
                    RecyclerViewGroupAdapter adapter = (RecyclerViewGroupAdapter) groupsFragment.getRecyclerView().getAdapter();
                    adapter.addData(response.body());
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Log.e("Error connect to server", t.toString());
                t.printStackTrace();
            }
        });
    }

    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                selectImageFromGallery();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(getContext())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    private void selectImageFromGallery()
    {
        TedBottomPicker.with(fragmentActivity)
                .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        Picasso.with(getContext()).load(uri).into(avatar_iv);
                        avatar_iv.setTag(uri.getPath());
                    }
                });
    }

    String getTokenFromSharedPreferences()
    {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.TOKEN, "");
    }
}
