package com.example.familyhealthhandbook.View.Conversation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.familyhealthhandbook.Adapter.RecyclerViewConversationAdapter;
import com.example.familyhealthhandbook.Model.Post;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;
import com.example.familyhealthhandbook.View.GroupProfile.GroupProfileActivity;
import com.example.familyhealthhandbook.View.Groups.GroupsFragment;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  ConversationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        mapping();
        init();

        getPostsByIdGroup();
    }

    private void mapping()
    {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void init(){
        initToolBar();
    }

    private void initToolBar()
    {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
        {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            String title = getIntent().getStringExtra(GroupsFragment.EXTRA_GROUP_NAME);
            actionBar.setTitle(title);
        }
    }

    private void setPosts(List<Post> posts)
    {
        RecyclerViewConversationAdapter adapter = new RecyclerViewConversationAdapter(this, posts );

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.setClipToPadding(true);
        recyclerView.setAdapter(adapter);
        if(recyclerView.getAdapter().getItemCount() > 0)
            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() -1);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new RecyclerViewConversationAdapter.ClickListener() {
            @Override
            public void OnClick(View v, int position) {

            }
        });

    }


    void getPostsByIdGroup()
    {
        Intent intent = getIntent();
        String idGroup = intent.getStringExtra(GroupsFragment.EXTRA_GROUP_ID);
        Utils.getApi().GetPostsByIdGroup(idGroup).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful() && response.body()!= null)
                {
                    setPosts(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("Error getPostsByIdGroup", t.toString());
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
            case R.id.menu_info:
                openInfoGroup();
                break;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    void openInfoGroup()
    {
        String name = getIntent().getStringExtra(GroupsFragment.EXTRA_GROUP_NAME);
        String id = getIntent().getStringExtra(GroupsFragment.EXTRA_GROUP_ID);

        Intent intent = new Intent(getApplicationContext(), GroupProfileActivity.class);
        intent.putExtra(GroupsFragment.EXTRA_GROUP_ID, id);
        intent.putExtra(GroupsFragment.EXTRA_GROUP_NAME, name);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_conservation, menu);
        return true;
    }
}