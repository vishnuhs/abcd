package com.parkinncharge.parkinncharge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.parkinncharge.parkinncharge.ui.share.ShareFragment;

public class referpageadapter extends FragmentStatePagerAdapter {


    String[] tabArray=new String[]{"REFER","EARN"};

    public referpageadapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabArray[position];
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                Refer refer=new Refer();
                return refer;

            case 1:
                earn Earn=new earn();
                return Earn;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
