package com.example.familyhealthhandbook.View.Personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familyhealthhandbook.Adapter.AutoCompleteTextViewSicknessAdapter;
import com.example.familyhealthhandbook.Adapter.RecyclerViewHealthRecordAdapter;
import com.example.familyhealthhandbook.Adapter.RecyclerViewHorizontalImageAdapter;
import com.example.familyhealthhandbook.Model.HealthRecord;
import com.example.familyhealthhandbook.Model.Sickness;
import com.example.familyhealthhandbook.Model.User;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;
import com.example.familyhealthhandbook.View.Login.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView avatar_iv;
    private TextView name_tv;
    private TextView gender_tv;
    private TextView year_tv;
    private TextView email_tv;
    private Button editInfo_btn;
    private RecyclerView recyclerView;
    private MaterialButton createHR_btn;

    public static ArrayList<Sickness> sicknesses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        mapping();
        initToolBar();
        initCreateHR_btn();
        getUserById();
        getMyHealthRecords();
        getSicknesses();
    }

    void mapping()
    {
        toolbar = findViewById(R.id.toolbar);
        avatar_iv = findViewById(R.id.avatar_ImageView);
        name_tv = findViewById(R.id.name_tv);
        gender_tv = findViewById(R.id.gender_tv);
        year_tv = findViewById(R.id.year_TextView);
        email_tv = findViewById(R.id.email_tv);
        editInfo_btn = findViewById(R.id.editInfo_btn);
        recyclerView = findViewById(R.id.health_record_rv);
        createHR_btn = findViewById(R.id.create_hr_btn);
    }

    private void initCreateHR_btn()
    {
        createHR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateDialog(Gravity.CENTER);
            }
        });
    }

    void getSicknesses()
    {
        Utils.getApi().getAllSickness().enqueue(new Callback<ArrayList<Sickness>>() {
            @Override
            public void onResponse(Call<ArrayList<Sickness>> call, Response<ArrayList<Sickness>> response) {
                if(response.isSuccessful() && response.body() != null)
                    sicknesses = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<Sickness>> call, Throwable t) {
                Log.e("Error connect to server", t.toString());
                t.printStackTrace();
            }
        });
    }

    private void initToolBar()
    {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
        {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Trang cá nhân");
        }
    }

    void setInfo(User user)
    {
        Picasso.with(this).load(user.getAvatar()).into(avatar_iv);
        name_tv.setText(user.getName());
        gender_tv.setText(user.getGender() == 1? "Nam": "Nữ");
        year_tv.setText(user.getYearOfBirth() + "");
        email_tv.setText(user.getEmail());
    }

    void setHealthRecords(List<HealthRecord> healthRecords)
    {
        RecyclerViewHealthRecordAdapter adapter = new RecyclerViewHealthRecordAdapter(this, healthRecords);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.setClipToPadding(true);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new RecyclerViewHealthRecordAdapter.ClickListener() {
            @Override
            public void OnClick(View v, int position) {
            }
        });
    }

    void getUserById()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString(LoginActivity.ID_USER, "");

        Utils.getApi().GetUserById(idUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body()!= null) {
                    setInfo(response.body());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Error connect to server", t.toString());
                t.printStackTrace();
            }
        });
    }

    void getMyHealthRecords()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(LoginActivity.TOKEN, "");
        Utils.getApi().GetMyHealthRecords(token).enqueue(new Callback<List<HealthRecord>>() {
            @Override
            public void onResponse(Call<List<HealthRecord>> call, Response<List<HealthRecord>> response) {
                if(response.isSuccessful() && response.body() != null)
                    setHealthRecords(response.body());
            }

            @Override
            public void onFailure(Call<List<HealthRecord>> call, Throwable t) {
                Log.e("Error connect to server", t.toString());
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    void showCreateDialog(int gravity)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_health_record);

        Window window = dialog.getWindow();
        if(window == null)
            return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);


        TextInputEditText location_edt = dialog.findViewById(R.id.location_edt);
        AppCompatAutoCompleteTextView sickness_tv = dialog.findViewById(R.id.sickness_tv);
        initDialogSicknessTv(sickness_tv);

        RecyclerView image_rv = dialog.findViewById(R.id.image_rv);
        RecyclerViewHorizontalImageAdapter adapter = new RecyclerViewHorizontalImageAdapter(this);
        image_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        image_rv.setHasFixedSize(true);
        image_rv.setAdapter(adapter);

        Button addImg_btn = dialog.findViewById(R.id.addImg_btn);
        addImg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission(adapter);
            }
        });

        MaterialButton submit_btn = dialog.findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sicknessId = "";
                if(sickness_tv.getTag() != null)
                {
                    int position = (int) sickness_tv.getTag();
                    if(position >=0) {
                        Sickness sickness = (Sickness) sickness_tv.getAdapter().getItem(position);
                        sicknessId = sickness.get_id();
                    }
                    else
                        sicknessId = "";
                }
                else
                    sicknessId = "";
                String token = getTokenFromSharedPreferences();
                String location = location_edt.getText().toString().toLowerCase().trim();
                List<Uri> images = ((RecyclerViewHorizontalImageAdapter)image_rv.getAdapter()).getData();

                if(checkCreateHRData(location,sicknessId, images)) {
                    createNewHealthRecord(token, location, sicknessId, images);
                    dialog.cancel();
                }

            }
        });
        dialog.show();
    }

    void initDialogSicknessTv(AppCompatAutoCompleteTextView sickness_tv)
    {
        if(sicknesses == null) {
            return;
        }
        AutoCompleteTextViewSicknessAdapter adapter = new AutoCompleteTextViewSicknessAdapter(getApplicationContext(), sicknesses);
        sickness_tv.setAdapter(adapter);
        sickness_tv.setTag(-1);
        sickness_tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sickness_tv.setTag(position);
            }
        });
        sickness_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sickness_tv.setTag(-1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void requestPermission(RecyclerViewHorizontalImageAdapter adapter) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                selectImageFromGallery(adapter);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(PersonalActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    private void selectImageFromGallery(RecyclerViewHorizontalImageAdapter adapter)
    {
        TedBottomPicker.with(this)
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No Select")
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        if(uriList != null && !uriList.isEmpty()){
                            adapter.setData(uriList);
                        }
                    }
                });
    }

    String getTokenFromSharedPreferences()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.TOKEN, "");
    }

    private boolean checkCreateHRData(String location, String sickness, List<Uri> images)
    {
        if(location.matches("")) {
            Toast.makeText(getApplicationContext(), "Chưa điền nơi khám bệnh", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(sickness.matches("")) {
            Toast.makeText(getApplicationContext(), "Chưa chọn loại bệnh", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(images == null || images.size() < 1) {
            Toast.makeText(getApplicationContext(), "Chưa có ảnh phiếu khám", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    void createNewHealthRecord(String token, String location, String sickness, List<Uri> images)
    {
        RequestBody requestBody_location = RequestBody.create(MultipartBody.FORM, location);
        RequestBody requestBody_sickness = RequestBody.create(MultipartBody.FORM, sickness);
        ArrayList<MultipartBody.Part> imagesBody = createImagesBodyFromListUri(images);

        Utils.getApi().CreateNewHealthRecord(token, requestBody_location,requestBody_sickness,imagesBody)
                .enqueue(new Callback<HealthRecord>() {
            @Override
            public void onResponse(Call<HealthRecord> call, Response<HealthRecord> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    Toast.makeText(getApplicationContext(), "Tạo phiếu khám thành công", Toast.LENGTH_SHORT).show();
                    RecyclerViewHealthRecordAdapter adapter = (RecyclerViewHealthRecordAdapter) recyclerView.getAdapter();
                    adapter.addItem(response.body());
                }
            }

            @Override
            public void onFailure(Call<HealthRecord> call, Throwable t) {
                Log.e("Error connect to server", t.toString());
                t.printStackTrace();
            }
        });
    }

    ArrayList<MultipartBody.Part> createImagesBodyFromListUri(List<Uri> uris)
    {
        ArrayList<MultipartBody.Part> imagesBody = new ArrayList<>();
        for(Uri item: uris)
        {
            File file = new File(item.getPath());
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("images", file.getName(), requestBody);
            imagesBody.add(part);
        }
        return  imagesBody;
    }
}