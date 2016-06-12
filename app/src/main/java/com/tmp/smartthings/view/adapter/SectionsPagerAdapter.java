package com.tmp.smartthings.view.adapter;

/**
 * Created by phapli on 08/06/2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.tmp.smartthings.view.fragment.ControlFragment;
import com.tmp.smartthings.view.fragment.LogFragment;
import com.tmp.smartthings.view.fragment.UserFragment;

/**
 * SectionsPagerAdapter {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public final static int CONTROL_SECTION = 0;
    public final static int USER_SECTION = 1;
    public final static int LOG_SECTION = 2;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private int size;

    public SectionsPagerAdapter(FragmentManager fm, int size) {
        super(fm);
        this.size = size;
    }

    public void update(int size) {
        this.size = size;
        this.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a ControlFragment (defined as a static inner class below).
        switch (position) {
            case CONTROL_SECTION:
                return ControlFragment.newInstance();
            case USER_SECTION:
                return UserFragment.newInstance();
            case LOG_SECTION:
                return LogFragment.newInstance();
        }
        return ControlFragment.newInstance();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return size;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "CONTROL";
            case 1:
                return "USERS";
            case 2:
                return "LOGS";
        }
        return null;
    }


}