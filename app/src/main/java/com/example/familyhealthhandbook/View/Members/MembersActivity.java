package com.example.familyhealthhandbook.View.Members;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familyhealthhandbook.Adapter.RecyclerViewGroupAdapter;
import com.example.familyhealthhandbook.Adapter.RecyclerViewMembersAdapter;
import com.example.familyhealthhandbook.Model.Group;
import com.example.familyhealthhandbook.Model.User;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;
import com.example.familyhealthhandbook.View.Conversation.ConversationActivity;
import com.example.familyhealthhandbook.View.GroupProfile.GroupProfileActivity;
import com.example.familyhealthhandbook.View.Groups.GroupsFragment;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MembersActivity extends AppCompatActivity {
    public static final String RESULT_GROUP = "result_group";


    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private Group currentGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        mapping();
        initIntent();
        initToolbar();
        getMembers();
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.members_rv);
    }

    private void initIntent()
    {
        currentGroup = (Group) getIntent().getSerializableExtra(GroupProfileActivity.EXTRA_CURRENT_GROUP);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
        {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            setTitle("Thành viên");
        }
    }

    void getMembers()
    {
        Utils.getApi().GetMembers(currentGroup.getId()).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    initRecyclerView(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("Error connect to server", t.toString());
                t.printStackTrace();
            }
        });
    }

    void initRecyclerView(List<User> members)
    {
        RecyclerViewMembersAdapter adapter = new RecyclerViewMembersAdapter(MembersActivity.this, members, MembersActivity.this);
        adapter.setCurrentGroup(currentGroup);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setClipToPadding(false);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new RecyclerViewMembersAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
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
}