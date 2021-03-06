package com.example.familyhealthhandbook.View.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.familyhealthhandbook.View.Main.MainActivity;
import com.example.familyhealthhandbook.Model.responseLogin;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;
import com.example.familyhealthhandbook.View.Register.RegisterActivity;
import com.google.firebase.FirebaseApp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES_LOGIN = "shared_preferences_login";
    public static final String TOKEN = "token";
    public static final String ID_USER = "idUser";
    public static final String AVATAR = "avatar";

    private EditText editText_Username;
    private EditText editText_Password;
    private Button login_Button;
    private Button register_Button;

    responseLogin rspLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mapping();
        init();

    }

    private void init()
    {
        initLogin_Button();
        initRegister_Button();
    }

    private void initLogin_Button() {
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
    }

    private void initRegister_Button()
    {
        register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void mapping()
    {
        editText_Password = findViewById(R.id.password_EditText);
        editText_Username = findViewById(R.id.username_EditText);
        login_Button = findViewById(R.id.login_Button);
        register_Button = findViewById(R.id.register_Button);
    }

    private void checkLogin()
    {
        String email = editText_Username.getText().toString().trim();
        String pass = editText_Password.getText().toString().trim();
        Utils.getApi().SingIn(email, pass).enqueue(new Callback<responseLogin>() {
            @Override
            public void onResponse(Call<responseLogin> call, Response<responseLogin> response) {
                rspLogin = null;
                if(response.isSuccessful() && response.body() != null) {
                    rspLogin = response.body();
                    Toast.makeText(getApplicationContext(), "????ng nh???p th??nh c??ng\n" + rspLogin.getUser().getName(), Toast.LENGTH_SHORT).show();
                    saveUser(rspLogin);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    //intent.putExtra()
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(), "Email ho???c m???t kh???u kh??ng ????ng!",  Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<responseLogin> call, Throwable t) {
                Log.e("Error connect to login", t.toString());
                t.printStackTrace();
            }
        });
    }

    void saveUser(responseLogin rspLogin)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN,rspLogin.getToken());
        editor.putString(ID_USER, rspLogin.getUser().get_id());
        editor.putString(AVATAR, rspLogin.getUser().getAvatar());
        editor.apply();
    }
}