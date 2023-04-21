package usc.yuangang.es;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import usc.yuangang.es.adapter.ArtistAdapter;
import usc.yuangang.es.adapter.EventListAdapter;
import usc.yuangang.es.model.Artist;
import usc.yuangang.es.viewmodel.DetailsViewModel;


public class ArtistsFragment extends Fragment {
    DetailsViewModel detailsViewModel;

    View mView;
    public ArtistsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_artists, container, false);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        detailsViewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        List<Artist> artists = detailsViewModel.getArtists();
        Log.d("ArtistsFragment", artists.toString());
        Log.d("ArtistsFragment", detailsViewModel.artistsOrder.toString());


        List<Artist> orderedArtists = new ArrayList<>();
        List<String> artistsOrder = detailsViewModel.artistsOrder;
        for (int i = 0; i < artistsOrder.size(); i++) {
            for (int j = 0; j < artists.size(); j++) {
                String artistName = artists.get(j).getArtistName();
                if (artistName.equals(artistsOrder.get(i))){
                    orderedArtists.add(artists.get(j));
                }
            }
        }

        RecyclerView recyclerView = mView.findViewById(R.id.re_artists);
        ArtistAdapter adapter = new ArtistAdapter(requireContext(), orderedArtists);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}