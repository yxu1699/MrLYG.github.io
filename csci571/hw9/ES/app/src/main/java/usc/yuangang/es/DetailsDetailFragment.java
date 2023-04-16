package usc.yuangang.es;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import usc.yuangang.es.model.Detail;
import usc.yuangang.es.utils.ScrollingTextView;
import usc.yuangang.es.viewmodel.DetailsViewModel;

public class DetailsDetailFragment extends Fragment {
    View mView;
    DetailsViewModel detailsViewModel;




    @Override
    public void onResume() {
        super.onResume();
        Log.d("zhixing", "DetailsDetailFragment");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_detail, container, false);

        setAllViewScroll();
        detailsViewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        Log.d("DetailsDetailFragmentCheck", "start to observe");
        detailsViewModel.getDetail().observe(getViewLifecycleOwner(), new Observer<Detail>() {

            @Override
            public void onChanged(Detail detail) {
                // 更新 UI 以显示新的 Detail 数据
                Log.d("DetailsDetailFragmentCheck", detail.toString());
                if(detail != null){
                    ScrollingTextView stvArtist = mView.findViewById(R.id.detail_artist);
                    stvArtist.setText(detail.getArtist());

                    ScrollingTextView stvVenue = mView.findViewById(R.id.detail_venue);
                    stvVenue.setText(detail.getVenue());

                    ScrollingTextView stvDate = mView.findViewById(R.id.detail_date);
                    stvDate.setText(detail.getDate());

                    ScrollingTextView stvTime = mView.findViewById(R.id.detail_time);
                    stvTime.setText(detail.getTime());

                    ScrollingTextView stvGenres = mView.findViewById(R.id.detail_genre);
                    stvGenres.setText(detail.getGenre());

                    ScrollingTextView stvPR = mView.findViewById(R.id.detail_pricerange);
                    stvPR.setText(detail.getPriceRange());

                    ScrollingTextView stvTS = mView.findViewById(R.id.detail_ts);
                    stvTS.setText(detail.getTicketStatus());

                    ScrollingTextView stvBTA = mView.findViewById(R.id.detail_bta);
                    stvBTA.setText(detail.getBuyTicketAt());

                    ImageView stvMap = mView.findViewById(R.id.detail_map);

                    Glide.with(requireContext()).load(detail.getMapImageUrl()).into(stvMap);

                }
            }
        });
        return mView;
    }




    private  void setAllViewScroll(){
        ScrollingTextView stvArtist = mView.findViewById(R.id.detail_artist);
        stvArtist.setFocus(true);

        ScrollingTextView stvVenue = mView.findViewById(R.id.detail_venue);
        stvVenue.setFocus(true);

        ScrollingTextView stvDate = mView.findViewById(R.id.detail_date);
        stvDate.setFocus(true);

        ScrollingTextView stvTime = mView.findViewById(R.id.detail_time);
        stvTime.setFocus(true);

        ScrollingTextView stvGenres = mView.findViewById(R.id.detail_genre);
        stvGenres.setFocus(true);

        ScrollingTextView stvPR = mView.findViewById(R.id.detail_pricerange);
        stvPR.setFocus(true);

        ScrollingTextView stvTS = mView.findViewById(R.id.detail_ts);
        stvTS.setFocus(true);

        ScrollingTextView stvBTA = mView.findViewById(R.id.detail_bta);
        stvBTA.setFocus(true);
    }
}
