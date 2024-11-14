package com.example.androidexample;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.androidexample.fragment.friendsFragment;
import com.example.androidexample.fragment.globalFragment;
import com.example.androidexample.fragment.localFragment;

/**
 * page adapter used when switching tabs on the socials page
 * */
public class myViewPagerAdapter extends FragmentStateAdapter {

    /**
     * calls the fragment activity that is passed into it
     * */
    public myViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     *runs the necessary fragment for the leaderboards based on which tab the user has selected
     *
     * @return the fragment associated with the selected tab
     */
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

    /**
     *keeps the count of how many tabs there are
     *
     * @return the number of tabs in the tab bar
     */
    @Override
    public int getItemCount() {
        return 3;
    }
}
