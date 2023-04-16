package usc.yuangang.es.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import usc.yuangang.es.ArtistsFragment;
import usc.yuangang.es.DetailsDetailFragment;
import usc.yuangang.es.SearchEventsFragment;
import usc.yuangang.es.SearchFragment;
import usc.yuangang.es.VenueFragment;

public class DetailsVP2Adapter extends FragmentStateAdapter {

    public DetailsVP2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new DetailsDetailFragment();
        } else if (position == 1) {
            return new ArtistsFragment();
        }else {
            return new VenueFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}