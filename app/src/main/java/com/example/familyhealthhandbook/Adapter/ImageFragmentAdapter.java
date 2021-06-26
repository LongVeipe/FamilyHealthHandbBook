package com.example.familyhealthhandbook.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.familyhealthhandbook.View.Image.ImageFragment;

import java.util.List;

public class ImageFragmentAdapter extends FragmentStatePagerAdapter {

    public static final String EXTRA_IMAGE = "image";

    List<String> images;
    public ImageFragmentAdapter(@NonNull FragmentManager fm, List<String> images) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.images = images;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        ImageFragment fragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_IMAGE, images.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
