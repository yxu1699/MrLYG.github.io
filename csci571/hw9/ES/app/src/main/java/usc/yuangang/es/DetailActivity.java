package usc.yuangang.es;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import usc.yuangang.es.adapter.DetailsVP2Adapter;
import usc.yuangang.es.model.Artist;
import usc.yuangang.es.model.Detail;
import usc.yuangang.es.model.Venue;
import usc.yuangang.es.utils.ScrollingTextView;
import usc.yuangang.es.viewmodel.DetailsViewModel;
import usc.yuangang.es.viewmodel.FavoriteViewModel;

public class DetailActivity extends AppCompatActivity {
    ViewPager2 dvp;
    private RequestQueue requestQueue;
    DetailsViewModel detailsViewModel;

    FavoriteViewModel favoriteViewModel;
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

        ImageView hrt = findViewById(R.id.favorite_image);
        Log.d("favoriteViewModel", favoriteViewModel.toString());
        System.out.println(favoriteViewModel.toString());
        List<String> favs = favoriteViewModel.eventids;
        System.out.println(favs);
        if (favs.contains(eventId)){
            hrt.setImageResource(R.drawable.heart_filled);
        }
        String finalEventId = eventId;
        hrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favs.contains(finalEventId)){
                    //remove
                    favoriteViewModel.eventids.removeIf(str -> str.equals(finalEventId));
//                    favoriteViewModel.events.removeIf(obj -> obj.getEventId() == finalEventId);
                    System.out.println(favoriteViewModel.eventids);
                    // outline heart
                    hrt.setImageResource(R.drawable.heart_outline);
                }else {
                    // add to application model
                    favoriteViewModel.eventids.add(finalEventId);
                    System.out.println(favoriteViewModel.eventids);
                    hrt.setImageResource(R.drawable.heart_filled);
                }
            }
        });
//        if (favs.contains(eventId)){
//            hrt.setImageResource(R.drawable.heart_filled);
//            // remove from favs
//            onItemClickListener.removeFav(position);
//        }else {
//            if (onItemClickListener != null) {
//                onItemClickListener.onFavClick(position);
//            }
//        }

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

                    ImageView twitter = findViewById(R.id.twitter_image);
                    String finalTwitterurl = twitterurl;
                    twitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalTwitterurl));
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        }
                    });

                    ImageView facebook = findViewById(R.id.facebook_image);
                    String finalFacebookurl = facebookurl;
//                    facebook.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Log.d("facebook_image", "facebook_image onClick:");
////                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalFacebookurl));
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
//                            System.out.println(intent.resolveActivity(getPackageManager()));
//                            if (intent.resolveActivity(getPackageManager()) != null) {
//                                Log.d("facebook_image", Uri.parse(finalFacebookurl).toString());
//                                startActivity(intent);
//                            }
//                        }
//                    });
                    facebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("facebook_image", "facebook_image onClick:");
                            String url = "https://www.google.com";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            PackageManager packageManager = getPackageManager();
                            if (intent.resolveActivity(packageManager) != null) {
                                Log.d("facebook_image", Uri.parse(url).toString());
                                startActivity(intent);
                            } else {
                                Log.d("facebook_image", "No suitable app found to handle the URL");
                            }
                        }
                    });

                    Detail detail = new Detail(artists, venue, date, time, genres, priceRanges + " " + priceUnit, ticketStatus, eventurl, imgurl);
                    Log.d("Volley", detail.toString());
                    Log.d("DetailsDetailFragmentCheck", "start to set data");
                    Log.d("zhixing", "DetailActivity start to set data");
                    detailsViewModel.setDetail(detail);


                    // handle artist data
                    parseEventDetails(event);
                    Log.d("zhixing", "finish parseEventDetails(event);");

                    // handle venue data
                    getVenueByName(venue);
                    Log.d("zhixing", "finish getVenueByName(venue);");

                }, error -> {
                    // 在这里处理错误的响应
                    Log.e("VolleyError", "Error: " + error.getMessage());
                });
        requestQueue.add(jsonObjectRequest);

    }



    // venue
    private void getVenueByName(String venueName) {
        String url = "https://nodejs-379321.uw.r.appspot.com/venue?name=" + venueName;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    processVenueResponse(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Error fetching venue data: " + error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    private void processVenueResponse(JSONObject venue) throws JSONException {
        Log.d("venue detail", venue.toString());
        boolean isContainVenueData;
        JSONObject venueData = null;

        if (venue.has("error") || venue.getJSONObject("page").getInt("totalElements") == 0) {
        } else {
            venueData = venueAbstract(venue.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0));
        }
//        venueData.getJSONObject()
//        detailsViewModel.setVenue();

        Venue venueObejct = new Venue();
        if (venueData!=null){
            venueObejct.setVenueName(venueData.has("name") ? venueData.getString("name") : "");
            venueObejct.setVenueAddress(venueData.has("address") ? venueData.getString("address") : "");
            venueObejct.setDetailCs(venueData.has("city") ? venueData.getString("city") : "");
            venueObejct.setDetailContact(venueData.has("phonenumber") ? venueData.getString("phonenumber") : "");
            venueObejct.setOhText(venueData.has("openhours") ? venueData.getString("openhours") : "");
            venueObejct.setGrText(venueData.has("generalrule") ? venueData.getString("generalrule") : "");
            venueObejct.setCrText(venueData.has("childrule") ? venueData.getString("childrule") : "");

            if (venueData.has("latitude") && venueData.has("longitude")) {
                double latitude = venueData.getDouble("latitude");
                double longitude = venueData.getDouble("longitude");
                // 如果需要，你可以将纬度和经度添加到 Venue 类，并使用相应的 getter 和 setter 方法
                venueObejct.setLatitude(latitude);
                venueObejct.setLongitude(longitude);
            }
            Log.d("venueData", venueData.toString());
        }
        detailsViewModel.setVenue(venueObejct);


//
//
//        venueData.get("name").toString();
//        venueData.get("address").toString();
//        venueData.get("city").toString();
//        venueData.get("phonenumber").toString();
//        venueData.get("openhours").toString();
//        venueData.get("generalrule").toString();
//        venueData.get("childrule").toString();
//        venueData.get("latitude").toString();
//        venueData.get("longitude").toString();

        /*
        * {
            "name": "Los Angeles Memorial Coliseum",
            "address": "3911 S. Figueroa St",
            "city": "Los Angeles, California",
            "phonenumber": "",
            "openhours": "",
            "generalrule": "",
            "childrule": "",
            "latitude": 34.014053,
            "longitude": -118.287865
        }*/
        // searchResultMessageService.detailCard = details;

    }

    public JSONObject venueAbstract(JSONObject venue) throws JSONException {
        String address = "";
        String city = "";
        String phoneNumber = "";
        String openHours = "";
        String generalRule = "";
        String childRule = "";
        String name = "";
        Double latitude = null;
        Double longitude = null;

        if (checkValue(venue.getString("name"))) {
            name = venue.getString("name");
        }

        if (checkValue(venue.getJSONObject("address")) && checkValue(venue.getJSONObject("address").getString("line1"))) {
            address = venue.getJSONObject("address").getString("line1");
        }

        String cityName = "";
        String stateName = "";
        if (checkValue(venue.getJSONObject("state")) && checkValue(venue.getJSONObject("state").getString("name"))) {
            stateName = venue.getJSONObject("state").getString("name");
        }
        if (checkValue(venue.getJSONObject("city")) && checkValue(venue.getJSONObject("city").getString("name"))) {
            cityName = venue.getJSONObject("city").getString("name");
        }
        if (!cityName.isEmpty() || !stateName.isEmpty()) {
            city = cityName + ", " + stateName;
        }

        // phoneNumber, openHours
        if (venue.has("boxOfficeInfo")) {
            JSONObject boxOfficeInfo = venue.getJSONObject("boxOfficeInfo");
            if (boxOfficeInfo.has("phoneNumberDetail")) {
                phoneNumber = boxOfficeInfo.getString("phoneNumberDetail");
            }
            if (boxOfficeInfo.has("openHoursDetail")) {
                openHours = boxOfficeInfo.getString("openHoursDetail");
            }
        }

        // generalRule, childRule
        if (venue.has("generalInfo")) {
            JSONObject generalInfo = venue.getJSONObject("generalInfo");
            if (generalInfo.has("childRule")) {
                childRule = generalInfo.getString("childRule");
            }
            if (generalInfo.has("generalRule")) {
                generalRule = generalInfo.getString("generalRule");
            }
        }

        if (venue.has("location")) {
            JSONObject location = venue.getJSONObject("location");
            if (location.has("latitude")) {
                latitude = location.getDouble("latitude");
            }
            if (location.has("longitude")) {
                longitude = location.getDouble("longitude");
            }
        }
        JSONObject venueAbstract = new JSONObject();
        venueAbstract.put("name", name);
        venueAbstract.put("address", address);
        venueAbstract.put("city", city);
        venueAbstract.put("phonenumber", phoneNumber);
        venueAbstract.put("openhours", openHours);
        venueAbstract.put("generalrule", generalRule);
        venueAbstract.put("childrule", childRule);
        venueAbstract.put("latitude", latitude);
        venueAbstract.put("longitude", longitude);

        return venueAbstract;
    }





    // artist

    ArrayList<Artist> artistInfos = new ArrayList<>();
    private void parseEventDetails(JSONObject event) {
        Log.d("zhixing", "start parseEventDetails(event);");
        JSONArray attractions = null;
        try {
            attractions = event.getJSONObject("_embedded").getJSONArray("attractions");

            List<String > artistsOrder = new ArrayList<>();
            for (int i = 0; i < attractions.length(); i++) {
                JSONObject attraction = attractions.getJSONObject(i);

                JSONObject cls = attraction.getJSONArray("classifications").getJSONObject(0);
                if (cls.getJSONObject("segment")
                        .getString("name").toLowerCase().equals("music")) {
                    String artistName = attraction.getString("name");
                    artistsOrder.add(artistName);
                }
            }
            detailsViewModel.artistsOrder = artistsOrder;
            for (int i = 0; i < attractions.length(); i++) {
                JSONObject attraction = attractions.getJSONObject(i);

                JSONObject cls = attraction.getJSONArray("classifications").getJSONObject(0);
                if (cls.getJSONObject("segment")
                        .getString("name").toLowerCase().equals("music")) {
                    String artistName = attraction.getString("name");
                    getArtistInfo(artistName);
                }
            }
            detailsViewModel.setArtists(artistInfos);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setAllViewScroll();
        setDetailViewPager();
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        MyApp myApp = (MyApp) getApplication();
        favoriteViewModel = myApp.getMyApplicationModel();
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


    public void getArtistInfo(String artistName) {
        Log.d("zhixing", "start getArtistInfo(String artistName, Callback callback)");
        String url = "https://nodejs-379321.uw.r.appspot.com/artistinfo?artist=" + artistName;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        try {
                            JSONArray items = response.getJSONObject("artists").getJSONArray("items");
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject item = items.getJSONObject(i);
                                if (artistName.toLowerCase().equals(item.getString("name").toLowerCase())) {
                                    JSONObject itemData = itemAbstract(item);
                                    itemData.put("artistName", artistName);
                                    Artist artist = new Artist();
                                    artist.setArtistName(artistName);
                                    artist.setArtistFollower(itemData.get("followers").toString());
                                    artist.setArtistPopularity(itemData.get("popularity").toString());
                                    artist.setArtistSpotifyUrl(itemData.get("spotifyLink").toString());
                                    artist.setArtistID(itemData.get("artistId").toString());

                                    JSONArray images = itemData.getJSONArray("images");
                                    artist.setArtistIcon(images.getJSONObject(0).get("url").toString());
                                    artistInfos.add(artist);
                                    break;
                                }
                            }
                            Log.d("zhixing", "all artistInfos");
                            Log.d("zhixing", artistInfos.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {@Override
                public void onErrorResponse(VolleyError error) {
                    // Handle the error
                }
                });
        requestQueue.add(jsonObjectRequest);
    }




    private JSONObject itemAbstract(JSONObject item) {
        Log.d("zhixing", "start itemAbstract(JSONObject item)");
        JSONObject result = new JSONObject();

        try {
            if (checkValue(item.getString("name"))) {
                result.put("name", item.getString("name"));
            }

            if (checkValue(item.getJSONObject("followers")) &&
                    checkValue(item.getJSONObject("followers").getInt("total"))) {

                int followers = item.getJSONObject("followers").getInt("total");
                String formattedNumber = NumberFormat.getInstance().format(followers);
                result.put("followers", formattedNumber);
            }

            if (checkValue(item.getInt("popularity"))) {
                result.put("popularity", item.getInt("popularity"));
            }

            if (checkValue(item.getJSONObject("external_urls"))) {
                result.put("spotifyLink", item.getJSONObject("external_urls").getString("spotify"));
            }

            if (checkValue(item.getJSONArray("images"))) {
                result.put("images", item.getJSONArray("images"));
            }

            if (checkValue(item.getString("id"))) {
                result.put("artistId", item.getString("id"));
//                JSONObject album;
//                album = getSpotifyArtistAlbum(item.getString("id"));
//                result.put("album", album);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("zhixing", "finish itemAbstract");
        Log.d("zhixing", result.toString());

        return result;
    }

//    public JSONObject getSpotifyArtistAlbum(String artistId) {
//        Log.d("zhixing", "start getSpotifyArtistAlbum(String artistId, Callback callback)");
//        String url = "https://nodejs-379321.uw.r.appspot.com/artistalbum?artistid=" + artistId;
//
//        final JSONObject[] r = {null};
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("zhixing", "onResponse getSpotifyArtistAlbum(String artistId, Callback callback)");
//                        r[0] = response;
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                    }
//                });
//
//        requestQueue.add(jsonObjectRequest);
//        Log.d("zhixing", "finish getSpotifyArtistAlbum(String artistId, Callback callback)");
//        return r[0];
//    }


//    public JSONObject getSpotifyArtistAlbum(String artistId) {
//        Log.d("zhixing", "start getSpotifyArtistAlbum(String artistId)");
//        String url = "https://nodejs-379321.uw.r.appspot.com/artistalbum?artistid=" + artistId;
//
//        RequestFuture<JSONObject> future = RequestFuture.newFuture();
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
//
//        requestQueue.add(jsonObjectRequest);
//
//        try {
//            JSONObject response = future.get(); // This line will block and wait for the response.
//            Log.d("zhixing", "finish getSpotifyArtistAlbum(String artistId)");
//            return response;
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        Log.d("zhixing", "Error in getSpotifyArtistAlbum(String artistId)");
//        return null;
//    }

}


