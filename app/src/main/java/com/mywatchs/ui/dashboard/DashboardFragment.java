package com.mywatchs.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mywatchs.adapter.ViewPagerMovieAdapter;
import com.mywatchs.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewPager2 viewPager = binding.movieViewPager;
        TabLayout tabLayout = binding.movieTabLayout;

        // Set up the ViewPager with the sections adapter.
        ViewPagerMovieAdapter viewPagerMovieAdapter = new ViewPagerMovieAdapter(this);
        viewPager.setAdapter(viewPagerMovieAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Completadas");
                    break;
                case 1:
                    tab.setText("Ver mas tarde");
                    break;
                case 2:
                    tab.setText("Abandonadas");
                    break;
            }
        }).attach();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}