package com.example.familyhealthhandbook.View.Groups;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyhealthhandbook.Adapter.RecyclerViewGroupAdapter;
import com.example.familyhealthhandbook.Model.Group;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;
import com.example.familyhealthhandbook.View.Conversation.ConversationActivity;
import com.example.familyhealthhandbook.View.GroupProfile.EditGroupNameDialog;
import com.example.familyhealthhandbook.View.GroupProfile.GroupProfileActivity;
import com.example.familyhealthhandbook.View.Login.LoginActivity;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupsFragment extends Fragment {

    public static final String EXTRA_GROUP_NAME = "groupName";
    public static final String EXTRA_GROUP_ID = "groupId";


    private RecyclerView recyclerView;
    private MaterialButton create_btn;
    private MaterialButton join_btn;
    private androidx.appcompat.widget.SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        recyclerView = view.findViewById(R.id.groups_RecyclerView);
        searchView = view.findViewById(R.id.searchView);
        initCreateBtn(view);
        initJoin_btn(view);
        initIntent();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getGroupsList();
    }

    void initIntent()
    {
       String idGroupLeaf = getActivity().getIntent().getStringExtra(GroupProfileActivity.EXTRA_ID_GROUP);
       if(idGroupLeaf != null && recyclerView.getAdapter() != null)
       {
           RecyclerViewGroupAdapter adapter = (RecyclerViewGroupAdapter) recyclerView.getAdapter();
           adapter.removeItemById(idGroupLeaf);
       }
    }
    private void initCreateBtn(View view)
    {
        create_btn = view.findViewById(R.id.createGroup_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateGroupDialog dialog = new CreateGroupDialog(getContext(), getActivity(), GroupsFragment.this);
                dialog.show();
            }
        });
    }

    private void initJoin_btn(View view)
    {
        join_btn = view.findViewById(R.id.joinGroup_btn);
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinGroupDialog dialog = new JoinGroupDialog(getContext(), getActivity(), GroupsFragment.this);
                dialog.show();
            }
        });
    }

    void initSearchView(RecyclerViewGroupAdapter adapter)
    {
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    void initRVGroupAdapter(List<Group> groupsList)
    {
        RecyclerViewGroupAdapter adapter = new RecyclerViewGroupAdapter(getActivity(), groupsList, RecyclerViewGroupAdapter.TYPE_CONVERSATION);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setClipToPadding(false);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new RecyclerViewGroupAdapter.ClickListener() {
            @Override
            public void OnClick(View v, int position) {
                Intent intent = new Intent(getActivity(), ConversationActivity.class);
                intent.putExtra(EXTRA_GROUP_ID, groupsList.get(position).getId());
                intent.putExtra(EXTRA_GROUP_NAME, groupsList.get(position).getName());
                startActivity(intent);
            }
        });
        initIntent();
        initSearchView(adapter);
    }

    void getGroupsList()
    {
        String access_token = getTokenFromSharedPreferences();
        Utils.getApi().GetJoinedGroups(access_token).enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    initRVGroupAdapter(response.body());
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginActivity.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LoginActivity.TOKEN, "");
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
}
