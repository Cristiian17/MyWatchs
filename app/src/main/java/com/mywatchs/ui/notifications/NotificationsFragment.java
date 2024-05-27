package com.mywatchs.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mywatchs.adapter.ViewPagerMovieAdapter;
import com.mywatchs.adapter.ViewPagerSerieAdapter;
import com.mywatchs.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ViewPager2 viewPager = binding.serieViewPager;
        TabLayout tabLayout = binding.serieTabLayout;

        // Set up the ViewPager with the sections adapter.
        ViewPagerSerieAdapter viewPagerSerieAdapter = new ViewPagerSerieAdapter(this);
        viewPager.setAdapter(viewPagerSerieAdapter);

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