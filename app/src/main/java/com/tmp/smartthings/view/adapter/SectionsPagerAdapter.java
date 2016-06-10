package com.tmp.smartthings.view.adapter;

/**
 * Created by phapli on 08/06/2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tmp.smartthings.view.fragment.ControlFragment;
import com.tmp.smartthings.view.fragment.LogFragment;
import com.tmp.smartthings.view.fragment.UserFragment;

/**
 * SectionsPagerAdapter {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {


    private final int size;

    public SectionsPagerAdapter(FragmentManager fm, int size) {
        super(fm);
        this.size = size;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a ControlFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return ControlFragment.newInstance(position + 1);
            case 1:
                return UserFragment.newInstance(position + 1);
            case 2:
                return LogFragment.newInstance(position + 1);
        }
        return ControlFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return size;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(size>1) {
            switch (position) {
                case 0:
                    return "CONTROL";
                case 1:
                    return "USERS";
                case 2:
                    return "LOGS";
            }
        }
        return null;
    }


}