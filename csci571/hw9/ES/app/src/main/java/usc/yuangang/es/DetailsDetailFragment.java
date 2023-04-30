package usc.yuangang.es;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

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


    ProgressBar detailProgressBar;
    CardView detailCardView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_detail, container, false);

        detailProgressBar = mView.findViewById(R.id.detail_progress_bar);
        detailCardView = mView.findViewById(R.id.detail_card_view);

        detailProgressBar.setVisibility(View.VISIBLE);
        detailCardView.setVisibility(View.GONE);
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

                    TextView stvTS = mView.findViewById(R.id.detail_ts);
                    stvTS.setText(detail.getTicketStatus());

                    String ts = detail.getTicketStatus();
                    GradientDrawable backgroundDrawable = (GradientDrawable) stvTS.getBackground();

                    if (ts.contains("onsale")) {
                        backgroundDrawable.setColor(Color.parseColor("#4CAF50")); // Green
                    } else if (ts.contains("offsale")) {
                        backgroundDrawable.setColor(Color.parseColor("#FF0000")); // Red
                    } else if (ts.contains("cancel")) {
                        backgroundDrawable.setColor(Color.parseColor("#000000")); // Black
                    } else if (ts.contains("postpone")) {
                        backgroundDrawable.setColor(Color.parseColor("#FFA500")); // Orange
                    } else if (ts.contains("reschedul")) {
                        backgroundDrawable.setColor(Color.parseColor("#FFA500")); // Orange
                    }

                    ScrollingTextView stvBTA = mView.findViewById(R.id.detail_bta);
                    SpannableString content = new SpannableString(detail.getBuyTicketAt());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    stvBTA.setText(content);
                    stvBTA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(detail.getBuyTicketAt()));
                            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        }
                    });

                    ImageView stvMap = mView.findViewById(R.id.detail_map);

                    Glide.with(requireContext()).load(detail.getMapImageUrl()).into(stvMap);

                }
            }
        });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        detailProgressBar.setVisibility(View.GONE);
        detailCardView.setVisibility(View.VISIBLE);
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


        ScrollingTextView stvBTA = mView.findViewById(R.id.detail_bta);
        stvBTA.setFocus(true);
    }
}
