package com.example.familyhealthhandbook.View.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familyhealthhandbook.Adapter.RecyclerViewNews;
import com.example.familyhealthhandbook.Model.News;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.news_RecyclerView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getNewsList();
    }

    void setNewsList(List<News> newsList)
    {
        RecyclerViewNews adapter = new RecyclerViewNews(getActivity(), newsList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setClipToPadding(false);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener((view, position) -> {

            Intent intentSource = new Intent(Intent.ACTION_VIEW);
            intentSource.setData(Uri.parse(newsList.get(position).getLink()));
            startActivity(intentSource);
        });
    }

    void getNewsList()
    {
        Utils.getApi().GetNews().enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    setNewsList(response.body());
                }
                else
                    Toast.makeText(getContext(), "Không có tin tức",  Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.e("Error connect to server", t.toString());
                t.printStackTrace();
            }
        });
    }
}