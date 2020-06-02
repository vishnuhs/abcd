package com.parkinncharge.parkinncharge.ui.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.parkinncharge.parkinncharge.R;
import com.parkinncharge.parkinncharge.referpageadapter;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class ShareFragment extends Fragment {

    private ShareViewModel shareviewmodel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareviewmodel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.sharefragment, container, false);
        TabLayout tabLayout=(TabLayout)root.findViewById(R.id.tabs);
        ViewPager pager=(ViewPager)root.findViewById(R.id.viewpager);
        referpageadapter TabPageAdapter=new referpageadapter(getChildFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pager.setAdapter(TabPageAdapter);
        tabLayout.setupWithViewPager(pager);

        return root;
    }
}