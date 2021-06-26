package com.example.familyhealthhandbook.View.Image;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.familyhealthhandbook.Adapter.ImageFragmentAdapter;
import com.example.familyhealthhandbook.R;
import com.squareup.picasso.Picasso;

public class ImageFragment extends Fragment {


    public ImageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        ImageView imageView = view.findViewById(R.id.imagefrm_iv);
        String image = getArguments().getString(ImageFragmentAdapter.EXTRA_IMAGE, "");
        Picasso.with(getContext()).load(image).into(imageView);

        return view;
    }
}