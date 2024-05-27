package com.mywatchs.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mywatchs.ui.moviesViewPager.ToWatchMoviesFragment;
import com.mywatchs.ui.moviesViewPager.CompletedMoviesFragment;
import com.mywatchs.ui.moviesViewPager.DiscardedMoviesFragment;

public class ViewPagerMovieAdapter extends FragmentStateAdapter {

    public ViewPagerMovieAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CompletedMoviesFragment();
            case 1:
                return new ToWatchMoviesFragment();
            case 2:
                return new DiscardedMoviesFragment();
            default:
                return new CompletedMoviesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
