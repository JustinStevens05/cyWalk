package com.example.androidexample.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidexample.R;

/**
 * global fragment for the social leaderbaord
 * */
public class globalFragment extends Fragment {

    /**
     * creates the leaderboard sections for the global leaderboard
     *
     * @return global leaderboard view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_global, container, false);
    }
}