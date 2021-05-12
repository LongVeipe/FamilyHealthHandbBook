package com.example.familyhealthhandbook.View.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.familyhealthhandbook.MainActivity;
import com.example.familyhealthhandbook.Model.User;
import com.example.familyhealthhandbook.Model.responseLogin;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editText_Username;
    private EditText editText_Password;
    private Button login_Button;

    responseLogin rspLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mapping();
        setButtonLoginCLick();

    }

    private void setButtonLoginCLick() {
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
                if(rspLogin != null)
                    Toast.makeText(getApplicationContext(), rspLogin.getUser().getName(),  Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Email hoặc mật khẩu không đúng!",  Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mapping()
    {
        editText_Password = findViewById(R.id.password_EditText);
        editText_Username = findViewById(R.id.username_EditText);
        login_Button = findViewById(R.id.login_Button);
    }

    private void checkLogin()
    {
        String email = editText_Username.getText().toString().trim();
        String pass = editText_Password.getText().toString().trim();
        Utils.getApi().SingIn(email, pass).enqueue(new Callback<responseLogin>() {
            @Override
            public void onResponse(Call<responseLogin> call, Response<responseLogin> response) {

                if(response.isSuccessful() && response.body() != null)

                    rspLogin = response.body();
            }

            @Override
            public void onFailure(Call<responseLogin> call, Throwable t) {
                Log.e("Error connect to login", t.toString());
                t.printStackTrace();
            }
        });
    }

    private void login()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //intent.putExtra()
        startActivity(intent);
    }
}