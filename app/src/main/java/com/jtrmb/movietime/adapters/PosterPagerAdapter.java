package com.jtrmb.movietime.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jtrmb.movietime.ui.PosterDetailFragment;

import info.movito.themoviedbapi.model.MovieImages;

/**
 * Created by Guest on 4/6/16.
 */
public class PosterPagerAdapter extends FragmentPagerAdapter {
    private MovieImages mImages;

    public PosterPagerAdapter(FragmentManager fm, MovieImages images) {
        super(fm);
        mImages = images;
    }

    @Override
    public Fragment getItem(int position) {
        return PosterDetailFragment.newInstance(mImages.getPosters().get(position));
    }

    @Override
    public int getCount() {
        if (mImages.getPosters().size() > 18) {
            return 18;
        } else {
            return mImages.getPosters().size();
        }
    }
}
