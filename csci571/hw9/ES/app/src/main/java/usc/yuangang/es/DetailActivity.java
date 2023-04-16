package usc.yuangang.es;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import usc.yuangang.es.adapter.DetailsVP2Adapter;
import usc.yuangang.es.adapter.EventListAdapter;
import usc.yuangang.es.adapter.SearchFavViewAdapter;
import usc.yuangang.es.model.Detail;
import usc.yuangang.es.model.Event;
import usc.yuangang.es.utils.ScrollingTextView;
import usc.yuangang.es.viewmodel.DetailsViewModel;
import usc.yuangang.es.viewmodel.SearchViewModel;

public class DetailActivity extends AppCompatActivity {
    ViewPager2 dvp;

    private RequestQueue requestQueue;


    DetailsViewModel detailsViewModel;
    private  void setAllViewScroll(){
        ScrollingTextView stv = findViewById(R.id.title);
        stv.setFocus(true);

    }



    @Override
    protected void onResume() {
        super.onResume();

        Log.d("zhixing", "DetailActivity");
        Intent intent = getIntent();
        String eventId = "";
        if (intent != null) {
            // Extract the eventId from the Intent
            eventId = intent.getStringExtra("eventId");
        }
        Toast.makeText(this, eventId, Toast.LENGTH_SHORT).show();



        fetchData(eventId);


    }
    public static String convertDateFormat(String inDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

        Date date;
        String oDate = "";

        try {
            date = inputFormat.parse(inDate);
            oDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return oDate;
    }
    public String convertTimeFormat(String time) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a");

        try {
            Date newTime = inputFormat.parse(time);
            String oTimeStr = outputFormat.format(newTime);
            return oTimeStr;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkValue(Object value) {
        return value != null && !value.equals("Undefined") && !value.equals("");
    }
    public void fetchData(String eventId) {
        requestQueue = Volley.newRequestQueue(this);

        String urlGetDetail = "https://nodejs-379321.uw.r.appspot.com/event?id="+eventId;
        Log.d("VolleyResponse", "fetch event detail data start "+ urlGetDetail);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlGetDetail, null, event -> {
                    // 处理成功的响应
                    Log.d("VolleyResponse", "Response: " + event.toString());

                    String eventname = "";
                    String date = "";
                    String time = "";
                    String artists = "";
                    String venue = "";
                    String genres = "";
                    String priceRanges = "";
                    String priceUnit = "";
                    String ticketStatus = "";
                    String imgurl = "";
                    String eventurl = "";
                    String twitterurl = "";
                    String facebookurl = "";

                    if (checkValue(event.optString("url"))) {
                        eventurl = event.optString("url");
                    }

                    if (checkValue(event.optString("name"))) {
                        eventname = event.optString("name");
                    }

                    JSONObject dates = event.optJSONObject("dates");
                    if (dates != null) {
                        JSONObject start = dates.optJSONObject("start");
                        if (start != null) {
                            if (checkValue(start.optString("localDate"))) {
                                date = start.optString("localDate");
                            }
                            if (checkValue(start.optString("localTime"))) {
                                time = start.optString("localTime");
                            }
                        }

                        JSONObject status = dates.optJSONObject("status");
                        if (status != null) {
                            if (checkValue(status.optString("code"))) {
                                ticketStatus = status.optString("code");
                            }
                        }
                    }

                    JSONObject seatmap = event.optJSONObject("seatmap");
                    if (seatmap != null) {
                        if (checkValue(seatmap.optString("staticUrl"))) {
                            imgurl = seatmap.optString("staticUrl");
                        }
                    }

                    JSONObject embedded = event.optJSONObject("_embedded");
                    if (embedded != null) {
                        JSONArray attractions = embedded.optJSONArray("attractions");
                        if (attractions != null) {
                            StringBuilder sbArtists = new StringBuilder();
                            for (int i = 0; i < attractions.length(); i++) {
                                JSONObject attraction = attractions.optJSONObject(i);
                                if (attraction != null) {
                                    if (checkValue(attraction.optString("name"))) {
                                        sbArtists.append(attraction.optString("name")).append(" | ");
                                    }
                                }
                            }
                            if (sbArtists.length() > 0) {
                                sbArtists.setLength(sbArtists.length() - 3); // 移除最后的 " | "
                                artists = sbArtists.toString();
                            }
                        }

                        JSONArray venues = embedded.optJSONArray("venues");
                        if (venues != null) {
                            JSONObject firstVenue = venues.optJSONObject(0);
                            if (firstVenue != null) {
                                if (checkValue(firstVenue.optString("name"))) {
                                    venue = firstVenue.optString("name");
                                }
                            }
                        }
                    }
                    JSONArray classifications = event.optJSONArray("classifications");
                    if (classifications != null) {
                        StringBuilder sbGenres = new StringBuilder();
                        JSONObject firstClassification = classifications.optJSONObject(0);
                        if (firstClassification != null) {
                            JSONObject segment = firstClassification.optJSONObject("segment");
                            JSONObject genre = firstClassification.optJSONObject("genre");
                            JSONObject subGenre = firstClassification.optJSONObject("subGenre");
                            JSONObject type = firstClassification.optJSONObject("type");
                            JSONObject subType = firstClassification.optJSONObject("subType");

                            if (segment != null && checkValue(segment.optString("name"))) {
                                sbGenres.append(segment.optString("name")).append(" | ");
                            }
                            if (genre != null && checkValue(genre.optString("name"))) {
                                sbGenres.append(genre.optString("name")).append(" | ");
                            }
                            if (subGenre != null && checkValue(subGenre.optString("name"))) {
                                sbGenres.append(subGenre.optString("name")).append(" | ");
                            }
                            if (type != null && checkValue(type.optString("name"))) {
                                sbGenres.append(type.optString("name")).append(" | ");
                            }
                            if (subType != null && checkValue(subType.optString("name"))) {
                                sbGenres.append(subType.optString("name")).append(" | ");
                            }

                            if (sbGenres.length() > 0) {
                                sbGenres.setLength(sbGenres.length() - 3); // 移除最后的 " | "
                                genres = sbGenres.toString();
                            }
                        }
                    }

                    JSONArray priceRangesArray = event.optJSONArray("priceRanges");
                    if (priceRangesArray != null) {
                        JSONObject firstPriceRange = priceRangesArray.optJSONObject(0);
                        if (firstPriceRange != null) {
                            if (checkValue(firstPriceRange.optString("min")) && checkValue(firstPriceRange.optString("max"))) {
                                priceRanges = firstPriceRange.optString("min") + " - " + firstPriceRange.optString("max");
                            }
                            if (checkValue(firstPriceRange.optString("currency"))) {
                                priceUnit = firstPriceRange.optString("currency");
                            }
                        }
                    }

                    // 生成 Twitter 和 Facebook URL
                    try {
                        twitterurl = "https://twitter.com/intent/tweet?url=" + eventurl + "&text=" + URLEncoder.encode(eventname + " on Ticketmaster.", "UTF-8");
                        facebookurl = "https://www.facebook.com/dialog/share?app_id=your_app_id&display=popup&href=" + URLEncoder.encode(eventurl, "UTF-8") + "&redirect_uri=" + URLEncoder.encode("https://www.facebook.com", "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }


                    // format date and time
                    // handle format
                    if( !"".equals(date) ){
                        date = this.convertDateFormat(date);
                    }
                    if( !"".equals(time) ){
                        time = this.convertTimeFormat(time);
                    }

                    TextView title = findViewById(R.id.title);
                    title.setText(eventname);
                    Detail detail = new Detail(artists, venue, date, time, genres, priceRanges + " " + priceUnit, ticketStatus, eventurl, imgurl);
                    Log.d("Volley", detail.toString());
                    Log.d("DetailsDetailFragmentCheck", "start to set data");
                    Log.d("zhixing", "DetailActivity start to set data");
                    detailsViewModel.setDetail(detail);

                }, error -> {
                    // 在这里处理错误的响应
                    Log.e("VolleyError", "Error: " + error.getMessage());
                });
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setAllViewScroll();
        setDetailViewPager();
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        ImageView backButton = findViewById(R.id.detail_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setDetailViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailsVP2Adapter detailsVP2Adapter = new DetailsVP2Adapter(fragmentManager, getLifecycle());
        dvp = findViewById(R.id.pagesSearchFav);
        dvp.setAdapter(detailsVP2Adapter);

        TabLayout tabLayout = findViewById(R.id.tabSearchFav);
        tabLayout.addTab(tabLayout.newTab().setText("DETAILS"));
        tabLayout.addTab(tabLayout.newTab().setText("ARTIST(S)"));
        tabLayout.addTab(tabLayout.newTab().setText("VENUE"));



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                dvp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        dvp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        // 隐藏默认的 ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
}