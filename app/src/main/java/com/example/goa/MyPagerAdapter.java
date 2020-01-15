package com.example.goa;

import com.example.goa.fragments.Tab1;
import com.example.goa.fragments.Tab2;
import com.example.goa.fragments.Tab3;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    int number = 3;

    public MyPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public MyPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
        case 0:
        Tab1 tab1=new Tab1();
        return tab1;
        case 1:
        Tab2 tab2 = new Tab2();
        return (tab2);
        case 2:
        Tab3 tab3 = new Tab3();
        return (tab3);
        default:
        return null;}
    }

    @Override
    public int getCount() {
        return number;
    }
}