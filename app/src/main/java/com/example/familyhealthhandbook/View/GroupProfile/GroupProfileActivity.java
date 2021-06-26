package com.example.familyhealthhandbook.View.GroupProfile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familyhealthhandbook.Model.Group;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;
import com.example.familyhealthhandbook.View.Conversation.ConversationActivity;
import com.example.familyhealthhandbook.View.Groups.GroupsFragment;
import com.example.familyhealthhandbook.View.Login.LoginActivity;
import com.example.familyhealthhandbook.View.Main.MainActivity;
import com.example.familyhealthhandbook.View.Members.MembersActivity;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupProfileActivity extends AppCompatActivity {

    public final static String EXTRA_GROUP_MASTER = "group_master";
    public final static String EXTRA_CURRENT_GROUP = "current_group";
    public final static String EXTRA_ID_GROUP = "idGroup";
    public final static int REQUEST_CODE_GROUP = 1;

    private Group currentGroup;

    private Toolbar toolbar;
    private CardView members_cv;
    private CardView out_cv;


    public TextView getName_tv() {
        return name_tv;
    }

    private TextView name_tv;
    private TextView code_tv;
    private CircleImageView avatar_iv;
    private MaterialButton editName_btn;
    private MaterialButton copyCode_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);

        mapping();
        init();
        getGroupInfo();
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar);
        members_cv = findViewById(R.id.members_cv);
        out_cv = findViewById(R.id.out_cv);
        name_tv = findViewById(R.id.name_tv);
        code_tv = findViewById(R.id.code_tv);
        avatar_iv = findViewById(R.id.avatar_ImageView);
        editName_btn = findViewById(R.id.editName_btn);
        copyCode_btn = findViewById(R.id.copyCode_btn);
    }

    void init()
    {
        initToolbar();
        initCardViews();
        initEditEditName_btn();
        initCopyCode_btn();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
        {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            setTitle("");
        }
    }

    void initEditEditName_btn()
    {
        editName_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditGroupNameDialog dialog = new EditGroupNameDialog(GroupProfileActivity.this, GroupProfileActivity.this);
                dialog.show();
            }
        });
    }

    void initCopyCode_btn()
    {
        copyCode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "đã sao chép mã mời", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void initCardViews()
    {
        members_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentGroup == null)
                    return;
                Intent intent = new Intent(getApplicationContext(), MembersActivity.class);
                intent.putExtra(GroupProfileActivity.EXTRA_CURRENT_GROUP, currentGroup);
                startActivityForResult(intent, REQUEST_CODE_GROUP);
            }
        });

        out_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(GroupProfileActivity.this)
                        .setTitle("Rời khỏi gia đình?")
                        .setMessage("Bạn có chắc muốn rời khỏi cuộc trò chuyện này không? Bạn sẽ không nhận được tin nhắn mới nữa.")
                        .setPositiveButton("Rời đi", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                outGroup();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    void outGroup()
    {
        if(getIdUserFromSharedPreferences().matches(currentGroup.getMaster()) && currentGroup.getMembers().size() > 1)
        {
            Toast.makeText(getApplicationContext(),
                    "Bạn phải chuyển quyền quản trị cho thành viên khác trước khi rời khỏi nhóm", Toast.LENGTH_SHORT).show();
        }
        else
        {
        String token = getTokenFromSharedPreferences();
        String idGroup = currentGroup.getId();
        RequestBody responseBody_idGroup = RequestBody.create(MultipartBody.FORM, idGroup);
        Utils.getApi().OutGroup(token, responseBody_idGroup).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Đã rời gia đình " + currentGroup.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

        Intent intent = new Intent(GroupProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(MainActivity.EXTRA_VIEWPAGER_ITEM, MainActivity.VIEWPAGER_ITEM_GROUPS);
        intent.putExtra(EXTRA_ID_GROUP, currentGroup.getId());
        startActivity(intent);
        finish();
        }
    }


    void setDataToView(Group group)
    {
        this.name_tv.setText(group.getName());
        this.code_tv.setText(group.getInviteCode());
        Picasso.with(getApplicationContext()).load(group.getAvatar()).into(avatar_iv);
    }
    void getGroupInfo()
    {
        String id = getIntent().getStringExtra(GroupsFragment.EXTRA_GROUP_ID);
        Utils.getApi().GetGroupById(id).enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                if(response.body() != null && response.isSuccessful())
                {
                    setDataToView(response.body());
                    currentGroup = response.body();
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Log.e("Error connect to server", t.toString());
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GROUP)
        {
            if(resultCode == RESULT_OK)
            {
                Group group = (Group) data.getSerializableExtra(MembersActivity.RESULT_GROUP);
                if(group != null)
                    currentGroup = group;
            }
        }
    }

    String getTokenFromSharedPreferences()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.TOKEN, "");
    }

    String getIdUserFromSharedPreferences()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.ID_USER, "");
    }
}