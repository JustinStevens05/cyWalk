package com.example.androidexample;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.androidexample.fragment.friendsFragment;
import com.example.androidexample.fragment.globalFragment;
import com.example.androidexample.fragment.localFragment;

public class myViewPagerAdapter extends FragmentStateAdapter {

    public myViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new friendsFragment();
            case 1:
                return new localFragment();
            case 2:
                return new globalFragment();
            default:
                return new friendsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
