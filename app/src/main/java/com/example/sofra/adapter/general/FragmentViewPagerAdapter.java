package com.example.sofra.adapter.general;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> listFragment  = new ArrayList<>();
    private final List<String> listFragmentTitles  = new ArrayList<>();

    public FragmentViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listFragmentTitles.get(position);
    }

    public void adFragment(Fragment fragment,String title){

        listFragment.add(fragment);
        listFragmentTitles.add(title);
    }
}
