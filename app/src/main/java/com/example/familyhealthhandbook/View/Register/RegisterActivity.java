package com.example.familyhealthhandbook.View.Register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.audiofx.DynamicsProcessing;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familyhealthhandbook.Model.responseLogin;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    private Button button_PickDay;
    private TextInputEditText birth_EditText;
    private AppCompatAutoCompleteTextView gender_TextView;
    private ImageView avatar_ImageView;
    private Button addAvatar_Button;
    private Toolbar toolbar;
    private Button create_Button;
    private TextInputEditText name_EditText;
    private TextInputEditText email_EditText;
    private TextInputEditText password_EditText;
    private File avatarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mapping();
        init();
    }

    private void mapping()
    {
        button_PickDay = findViewById(R.id.pickDay_Button);
        birth_EditText = findViewById(R.id.birth_EditText);
        gender_TextView = findViewById(R.id.gender_TextView);
        avatar_ImageView = findViewById(R.id.avatar_ImageView);
        addAvatar_Button = findViewById(R.id.addAvatar_Button);
        toolbar = findViewById(R.id.toolbar);
        create_Button = findViewById(R.id.create_Button);
        name_EditText = findViewById(R.id.name_EditText);
        password_EditText = findViewById(R.id.password_EditText);
        email_EditText = findViewById(R.id.email_EditText);
    }

    private void  init()
    {
        initButton_PickDay();
        initGender_TextView();
        initAddAvatar_Button();
        initToolbar();
        initCreate_Button();
    }

    private void initButton_PickDay()
    {
        button_PickDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker materialDatePicker = setupDatePicker();
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });
    }

    private void initGender_TextView()
    {
        final String[] listGender = new String[] {"Nam", "Nữ"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listGender);

        gender_TextView.setAdapter(arrayAdapter);
    }

    private void initAddAvatar_Button()
    {
        addAvatar_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                }
                else
                {
                    imagePicker();
                }
            }
        });
    }

    private void initToolbar()
    {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initCreate_Button()
    {
        create_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_EditText.getText().toString().trim();
                String email = email_EditText.getText().toString().trim();
                String pass = password_EditText.getText().toString().trim();
                String gender = gender_TextView.getText().toString().trim();
                String[] date = birth_EditText.getText().toString().split(",");


                Boolean isEmpty = checkInputError(name, email, pass, gender, date, avatarImage);
                if(!isEmpty)
                {
                    createAccount(name, email, pass, gender, date);
                }
            }
        });
    }

    private Boolean checkInputError(String name, String email, String pass, String gender, String[] date, File img)
    {
        if(name.matches(""))
        {
            Toast.makeText(getApplicationContext(), "Chưa nhập Tên" , Toast.LENGTH_LONG).show();
            return true;
        }
        else if(email.matches(""))
        {
            Toast.makeText(getApplicationContext(), "Chưa nhập email" , Toast.LENGTH_LONG).show();
            return true;
        }
        else if(pass.matches(""))
        {
            Toast.makeText(getApplicationContext(), "Chưa nhập mật khẩu" , Toast.LENGTH_LONG).show();
            return true;
        }
        else if(gender.matches("") )
        {
            Toast.makeText(getApplicationContext(), "Chưa chọn giới tính" , Toast.LENGTH_LONG).show();
            return true;
        }
        else if(date.length < 2)
        {
            Toast.makeText(getApplicationContext(), "Chưa chọn ngày sinh" , Toast.LENGTH_LONG).show();
            return true;
        }
        else if(img == null)
        {
            Toast.makeText(getApplicationContext(), "Chưa chọn ảnh đại diện" , Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private void createAccount(String name, String email, String pass, String gender, String[] date)
    {
        int year = Integer.parseInt( date[1].trim());
        int bool_gender;
        if(gender.matches("Nam"))
            bool_gender = 1;
        else
            bool_gender = 0;
        RequestBody requestBody_email = RequestBody.create(MultipartBody.FORM, email);
        RequestBody requestBody_name = RequestBody.create(MultipartBody.FORM, name);
        RequestBody requestBody_pass = RequestBody.create(MultipartBody.FORM, pass);
        RequestBody requestBody_img = RequestBody.create(MediaType.parse("multipart/form-data"), avatarImage);
        MultipartBody.Part part_avatar = MultipartBody.Part.createFormData("avatar", avatarImage.getName(), requestBody_img);

        Utils.getApi().Register(requestBody_email, requestBody_pass, requestBody_name, bool_gender, year, part_avatar)
                .enqueue(new Callback<responseLogin>() {
            @Override
            public void onResponse(Call<responseLogin> call, Response<responseLogin> response) {
                if(response.isSuccessful() && response.body() != null)
                    Toast.makeText(getApplicationContext(), "Đăng ký thành công\n" + response.body().getUser().getName(),  Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Đăng ký không thành công",  Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<responseLogin> call, Throwable t) {
                Log.e("Error connect to login", t.toString());
                t.printStackTrace();
            }
        });
    }

    private  void imagePicker()
    {
        Intent intent = new Intent(RegisterActivity.this, FilePickerActivity.class);

        //Put extra
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
        .setCheckPermission(true)
        .setShowImages(true)
        .setShowVideos(false)
        .enableImageCapture(true)
        .setMaxSelection(1)
        .setSkipZeroSizeFiles(true)
        .build());

        //start
        startActivityForResult(intent, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check condition
        if((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
        {
            //when permission granted
            //Check condition
            if(requestCode == 1)
            {
                //When request code í 1
                //Call image picker method
                imagePicker();
            }
        }
        else
        {
            //When permission denied
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check condition
        if(resultCode == RESULT_OK && data !=  null)
        {
            //When result code is ok and data is not equal to null
            //Initialize array list
            ArrayList<MediaFile> mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);

            //Get string path
            String path = mediaFiles.get(0).getPath();
            //Check condition
            if(requestCode == 101)
            {
                //avatar_ImageView.setImageURI(Uri.parse(path));
                //avatar_ImageView.setTag(path);

                avatarImage = new  File(path);

                if(avatarImage.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(avatarImage.getAbsolutePath());
                    avatar_ImageView.setImageBitmap(myBitmap);
                }
            }

        }
    }


    @NotNull
    private MaterialDatePicker setupDatePicker()
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        final long today = MaterialDatePicker.todayInUtcMilliseconds();

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setSelection(today);
        final MaterialDatePicker materialDatePicker = builder.build();

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                birth_EditText.setText(materialDatePicker.getHeaderText());
            }
        });
        return materialDatePicker;
    }
}