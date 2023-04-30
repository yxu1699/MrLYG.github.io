package usc.yuangang.es;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import usc.yuangang.es.model.Venue;
import usc.yuangang.es.utils.ScrollingTextView;
import usc.yuangang.es.viewmodel.DetailsViewModel;

public class VenueFragment extends Fragment implements OnMapReadyCallback {

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
        // 获取 SupportMapFragment 并注册地图准备回调
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 添加一个标记在场地位置并移动相机
        detailsViewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        Venue venue = detailsViewModel.getVenue();
        if (venue != null){
            LatLng venueLocation = new LatLng(venue.getLatitude(), venue.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(venueLocation).title(detailsViewModel.getVenue().getVenueName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venueLocation, 15));
        }

    }

}