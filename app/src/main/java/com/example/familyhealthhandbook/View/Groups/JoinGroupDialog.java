package com.example.familyhealthhandbook.View.Groups;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.familyhealthhandbook.Adapter.RecyclerViewGroupAdapter;
import com.example.familyhealthhandbook.Model.Group;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;
import com.example.familyhealthhandbook.View.Conversation.ConversationActivity;
import com.example.familyhealthhandbook.View.Login.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class JoinGroupDialog extends Dialog {

    private static final String MSG_GROUP_NOT_EXIST = "group not exist";
    private static final String MSG_USER_JOINED = "User joined";

    private FragmentActivity fragmentActivity;
    private GroupsFragment groupsFragment;

    private TextInputEditText code_edt;
    private MaterialButton submit_btn;

    public JoinGroupDialog(@NonNull Context context, FragmentActivity fragmentActivity, GroupsFragment groupsFragment) {
        super(context);
        this.fragmentActivity = fragmentActivity;
        this.groupsFragment = groupsFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_join_group);
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
        initSubmit_btn();
    }

    void mapping()
    {
        this.code_edt = findViewById(R.id.code_edt);
        this.submit_btn = findViewById(R.id.submit_btn);
    }

    void initSubmit_btn()
    {
        this.submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = getTokenFromSharedPreferences();
                String code = code_edt.getText().toString().trim();
                joinGroup(token, code);
            }
        });
    }

    void joinGroup(String token, String code)
    {
        if(code.matches(""))
        {
            Toast.makeText(getContext(), "Chưa nhập mã gia đình", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody requestBody_code = RequestBody.create(MultipartBody.FORM, code);
        Utils.getApi().JoinGroup(token, requestBody_code).enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                if(response.body() != null && response.isSuccessful())
                {
                    cancel();
                    Intent intent = new Intent(fragmentActivity, ConversationActivity.class);
                    intent.putExtra(GroupsFragment.EXTRA_GROUP_ID, response.body().getId());
                    intent.putExtra(GroupsFragment.EXTRA_GROUP_NAME, response.body().getName());
                    getContext().startActivity(intent);

                    RecyclerViewGroupAdapter adapter = (RecyclerViewGroupAdapter) groupsFragment.getRecyclerView().getAdapter();
                    adapter.addData(response.body());
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String msg = jsonObject.getString("msg");
                        if(msg.matches(JoinGroupDialog.MSG_USER_JOINED))
                            Toast.makeText(getContext(), "Bạn đã tham gia vào gia đình này!\nVui lòng nhập mã khác", Toast.LENGTH_SHORT).show();
                        else if(msg.matches(JoinGroupDialog.MSG_GROUP_NOT_EXIST))
                            Toast.makeText(getContext(), "Không tìm thấy nhóm này :/!", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Log.e("Error connect to server", t.toString());
                t.printStackTrace();
            }
        });
    }

    String getTokenFromSharedPreferences()
    {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.TOKEN, "");
    }
}
