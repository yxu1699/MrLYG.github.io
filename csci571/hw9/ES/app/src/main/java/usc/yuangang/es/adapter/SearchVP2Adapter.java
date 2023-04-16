package usc.yuangang.es.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import usc.yuangang.es.SearchEventsFragment;
import usc.yuangang.es.SearchFragment;

public class SearchVP2Adapter extends FragmentStateAdapter {

    public SearchVP2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new SearchFragment();
        } else {
            return new SearchEventsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}