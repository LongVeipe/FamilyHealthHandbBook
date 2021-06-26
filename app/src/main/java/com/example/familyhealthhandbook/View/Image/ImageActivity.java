package com.example.familyhealthhandbook.View.Image;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.familyhealthhandbook.Adapter.ImageFragmentAdapter;
import com.example.familyhealthhandbook.Adapter.RecyclerViewConversationAdapter;
import com.example.familyhealthhandbook.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    ViewPager image_vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        image_vp = findViewById(R.id.image_vp);
        String imagesJson = getIntent().getStringExtra(RecyclerViewConversationAdapter.EXTRA_IMAGES);
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> images = gson.fromJson(imagesJson,type);

        ImageFragmentAdapter adapter = new ImageFragmentAdapter(getSupportFragmentManager(), images);
        image_vp.setAdapter(adapter);
        image_vp.setCurrentItem(0);
    }
}