package com.example.familyhealthhandbook.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.familyhealthhandbook.View.Covid.CovidFragment;
import com.example.familyhealthhandbook.View.Groups.GroupsFragment;
import com.example.familyhealthhandbook.View.Home.HomeFragment;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
    public MainViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 1:
                return new GroupsFragment();
            case 2:
                return new CovidFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
