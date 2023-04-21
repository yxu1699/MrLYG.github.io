package usc.yuangang.es;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import usc.yuangang.es.model.Venue;
import usc.yuangang.es.utils.ScrollingTextView;
import usc.yuangang.es.viewmodel.DetailsViewModel;

public class VenueFragment extends Fragment {

    DetailsViewModel detailsViewModel;

    View mView;
    public VenueFragment() {
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
        mView =  inflater.inflate(R.layout.fragment_venue, container, false);
        return mView;
    }


    @Override
    public void onResume() {
        super.onResume();
        detailsViewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);

        Venue venue = detailsViewModel.getVenue();
        Log.d("VenueFragment", venue.toString());


        ScrollingTextView venueName = mView.findViewById(R.id.veneu_name);
        venueName.setFocus(true);
        ScrollingTextView venueAddress = mView.findViewById(R.id.venue_address);
        venueAddress.setFocus(true);
        ScrollingTextView detailCs = mView.findViewById(R.id.detail_cs);
        detailCs.setFocus(true);
        ScrollingTextView detailContact = mView.findViewById(R.id.detail_contact);
        detailContact.setFocus(true);

        TextView ohText = mView.findViewById(R.id.long_text_view);
        TextView grText = mView.findViewById(R.id.gr_text);
        TextView crText = mView.findViewById(R.id.cr_text);

        venueName.setText(venue.getVenueName());
        venueAddress.setText(venue.getVenueAddress());
        detailCs.setText(venue.getDetailCs());
        detailContact.setText(venue.getDetailContact());
        ohText.setText(venue.getOhText());
        grText.setText(venue.getGrText());
        crText.setText(venue.getCrText());
    }
}