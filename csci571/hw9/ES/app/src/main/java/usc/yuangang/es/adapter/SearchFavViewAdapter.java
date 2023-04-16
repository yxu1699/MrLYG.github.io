package usc.yuangang.es.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import usc.yuangang.es.FavFragment;
import usc.yuangang.es.SearchVP2Fragment;

public class SearchFavViewAdapter extends FragmentStateAdapter {

    public SearchFavViewAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new SearchVP2Fragment();
        }else{
            return new FavFragment();
        }
    }

    @Override
    public int getItemCount() {
        // Hardcoded, use lists
        return 2;
    }
}
