package com.mywatchs.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mywatchs.ui.seriesViewPager.CompletedSeriesFragment;
import com.mywatchs.ui.seriesViewPager.DiscardedSeriesFragment;
import com.mywatchs.ui.seriesViewPager.ToWatchSeriesFragment;

public class ViewPagerSerieAdapter extends FragmentStateAdapter {
    public ViewPagerSerieAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CompletedSeriesFragment();
            case 1:
                return new ToWatchSeriesFragment();
            case 2:
                return new DiscardedSeriesFragment();
            default:
                return new CompletedSeriesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
