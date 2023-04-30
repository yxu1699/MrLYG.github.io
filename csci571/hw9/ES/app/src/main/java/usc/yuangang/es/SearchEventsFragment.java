package usc.yuangang.es;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationManager;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.GoogleApiAvailability;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import usc.yuangang.es.adapter.EventListAdapter;
import usc.yuangang.es.intf.OnItemClickListener;
import usc.yuangang.es.model.Event;
import usc.yuangang.es.viewmodel.FavoriteViewModel;
import usc.yuangang.es.viewmodel.SearchViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SearchEventsFragment extends Fragment  implements OnItemClickListener {
    private View mView;

    private SearchViewModel searchViewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;
    private RequestQueue requestQueue;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private List<Event> events;

    private FavoriteViewModel favoriteViewModel;

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("requestLocationPermissions", "un execute getCurrentLocation()");
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            Log.d("requestLocationPermissions", "execute getCurrentLocation()");
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(requireContext());
        return resultCode == com.google.android.gms.common.ConnectionResult.SUCCESS;
    }

    private boolean isLocationServiceEnabled() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            Log.e("LocationService", "Error checking GPS provider status.", e);
        }

        try {
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            Log.e("LocationService", "Error checking Network provider status.", e);
        }

        return gpsEnabled || networkEnabled;
    }

    private void getCurrentLocation() {
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        if (isLocationServiceEnabled()) {
            if (isGooglePlayServicesAvailable()) {
                System.out.println("isGooglePlayServicesAvailable true");
            }
            // 位置服务已启用
            Log.d("getCurrentLocation()", "location service is start");
            location = getLastKnownLocation();

            boolean isGPSEnabled = false;
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            System.out.println(isGPSEnabled);


            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                Log.d("getCurrentLocation()", "latitude " + String.valueOf(latitude));
                Toast.makeText(requireContext(), String.valueOf(latitude), Toast.LENGTH_SHORT).show();
            }
            System.out.println(location);
        } else {
            // 位置服务未启用
            Log.d("getCurrentLocation()", "location service is not start");
        }


    }

    private Location getLastKnownLocation(){
        Location bestLocation = null;
        Log.d("getCurrentLocation()", "enter in getCurrentLocation()");
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("getCurrentLocation()", "enter in getCurrentLocation() and check pass");
            bestLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return bestLocation;
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private boolean checkValue(Object value) {
        return value != null && !value.equals("Undefined") && !value.equals("");
    }


    private String convertDateFormat(String date){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date newDate = inputFormat.parse(date);
            String oDateStr = outputFormat.format(newDate);
            return oDateStr;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
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

    EventListAdapter adapterS = null;
    private void fecthDataAndShowData(String lat,String lng){
        //get data from backend
        Log.d("VolleyResponse", "fetch data start ");
        String urlGetTicketEvents = "https://nodejs-379321.uw.r.appspot.com/tickets?";
//        keyword=pink&distance=10&category=Default&latitude=34.0294&longitude=-118.2871
        urlGetTicketEvents += "keyword=" + searchViewModel.getKeyword() +
                "&distance=" + searchViewModel.getDistance() +
                "&category=" + (searchViewModel.getCategory() == "All" ? "Default" : searchViewModel.getCategory()) + "&latitude=" + lat + "&longitude=" + lng;
        System.out.println(urlGetTicketEvents);
        requestQueue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlGetTicketEvents, null, response -> {
                    // 处理成功的响应
                    Log.d("VolleyResponse", "Response: " + response.toString());

                    // totalElements > 0
                    try {
                        JSONObject pageObject = response.getJSONObject("page");
                        int totalElements = pageObject.getInt("totalElements");
                        Log.d("TotalElements", "Total Elements: " + totalElements);

                        if (totalElements <= 0) {
                            // TODO.
                            progressBar.setVisibility(View.GONE);
                            eventNotFound.setVisibility(View.VISIBLE);
                        } else {
                            // events array
                            JSONObject embeddedObject = response.getJSONObject("_embedded");
                            JSONArray eventsArray = embeddedObject.getJSONArray("events");

                            events = new ArrayList<>();
                            for (int i = 0; i < eventsArray.length(); i++) {
                                JSONObject eventObject = eventsArray.getJSONObject(i);

                                // 提取 JSON 对象中的数据
                                String eventName;
                                String eventId = eventObject.getString("id");
                                String date = "";
                                String time = "";
                                String icon = "";
                                String genre = "";
                                String venue = "";

                                if (checkValue(eventObject.optJSONObject("dates"))) {
                                    JSONObject dates = eventObject.getJSONObject("dates");
                                    if (checkValue(dates.optJSONObject("start"))) {
                                        JSONObject start = dates.getJSONObject("start");
                                        date = checkValue(start.optString("localDate")) ? start.getString("localDate") : "";
                                        time = start.optString("localTime", "");
                                    }
                                }

                                JSONArray imagesArray = eventObject.optJSONArray("images");
                                if (checkValue(imagesArray)) {
                                    icon = checkValue(imagesArray.optJSONObject(0)) ? imagesArray.getJSONObject(0).optString("url","") : "";
                                }

                                eventName = checkValue(eventObject.optString("name")) ? eventObject.getString("name") : "";

                                JSONArray classificationsArray = eventObject.optJSONArray("classifications");
                                if (checkValue(classificationsArray)) {
                                    JSONObject classification = classificationsArray.optJSONObject(0);
                                    if (checkValue(classification)) {
                                        JSONObject segment = classification.optJSONObject("segment");
                                        if (checkValue(segment)) {
                                            genre = checkValue(segment.optString("name","")) ? segment.getString("name") : "";
                                        }
                                    }
                                }
                                JSONObject embeddedObject2 = eventObject.optJSONObject("_embedded");
                                if (checkValue(embeddedObject2)) {
                                    JSONArray venuesArray = embeddedObject2.optJSONArray("venues");
                                    if (checkValue(venuesArray)) {
                                        JSONObject venueObject = venuesArray.optJSONObject(0);
                                        if (checkValue(venueObject)) {
                                            venue = checkValue(venueObject.optString("name","")) ? venueObject.getString("name") : "";
                                        }
                                    }
                                }

                                // handle format
                                if( !"".equals(date) ){
                                    date = this.convertDateFormat(date);
                                }
                                if( !"".equals(time) ){
                                    time = this.convertTimeFormat(time);
                                }



                                events.add(new Event(eventId,eventName,date,time,icon,venue,genre));
                                Log.d("Event", "Date: " + date + ", Time: " + time + ", Icon: " + icon + ", Event Name: " + eventName + ", Genre: " + genre + ", Venue: " + venue);

                            }
                            System.out.println(events);
                            RecyclerView recyclerView = mView.findViewById(R.id.re_events);
                            EventListAdapter adapter = new EventListAdapter(requireContext(), events,this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                            adapterS = adapter;
                            Handler updateHandler = new Handler();
                            Runnable updateRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.getAdapter().notifyDataSetChanged(); // 通知 RecyclerView 数据发生了变化
                                    updateHandler.postDelayed(this, 5000); // 重新调度 Runnable 在 1000 毫秒（1 秒）后运行
                                }
                            };
                            updateHandler.post(updateRunnable);
                            Log.d("eventlist", "ssssss");
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);


                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }, error -> {
                    Log.e("VolleyError", "Error: " + error.getMessage());
                });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onResume() {

        super.onResume();

        if (adapterS!=null){
            adapterS.notifyDataSetChanged();
        }
        Log.d("eventlist", "onResume");
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        eventNotFound.setVisibility(View.GONE);
        final String[] lat = {"34.0294"};
        final String[] lng = {"-118.2871"};
        // auto location ?
        if (searchViewModel.isAutoCheck()) {
            //get user address
            //TODO get current location
//            requestLocationPermissions();
            this.fecthDataAndShowData(lat[0],lng[0]);
        } else {
            // TODO use api get lat and lng
            String urlGetLocation = "https://nodejs-379321.uw.r.appspot.com/latlong?location=" + searchViewModel.getLocation();
            requestQueue = Volley.newRequestQueue(requireContext());

            requestQueue = Volley.newRequestQueue(requireContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, urlGetLocation, null, response -> {
                        // 处理成功的响应

                        try {
                            lat[0] = response.getString("lat");
                            lng[0] = response.getString("lng");

                            this.fecthDataAndShowData(lat[0],lng[0]);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }, error -> {
                        Log.e("VolleyError", "urlGetLocation Error: " + error.getMessage());
                    });
            requestQueue.add(jsonObjectRequest);


        }


    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && adapterS!= null) {
            adapterS.notifyDataSetChanged();
        }
    }


    ProgressBar progressBar;
    RecyclerView recyclerView;

    CardView eventNotFound;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_serachevents, container, false);
        mView = view;
        TextView searchEventBack = view.findViewById(R.id.search_event_back);
        progressBar = mView.findViewById(R.id.progress_bar);
        recyclerView = mView.findViewById(R.id.re_events);
        eventNotFound = mView.findViewById(R.id.event_no_found);

        searchEventBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).vp2.setCurrentItem(0, true);
                }
            }
        });



        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        MyApp myApp = (MyApp) requireActivity().getApplication();
        favoriteViewModel = myApp.getMyApplicationModel();



//        Toast.makeText(getActivity(), "searchViewModel"+searchViewModel.toString(),
//                Toast.LENGTH_SHORT).show();





        return view;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        Event event = events.get(position);
        intent.putExtra("eventId", event.getEventId());
        startActivity(intent);

    }

    public void executeMethod() {
        // 在这里执行你想要的操作
    }
    @Override
    public void onFavClick(int position) {
        favoriteViewModel.eventids.add(events.get(position).getEventId());
        favoriteViewModel.addEvent(events.get(position));
        System.out.println(favoriteViewModel.eventids);
    }

    @Override
    public List<Event> getAllFav() {
        return favoriteViewModel.events;
    }

    @Override
    public List<String> getAllFavStr() {
        return favoriteViewModel.eventids;
    }

    @Override
    public void removeFav(int position) {
        String eventId = events.get(position).getEventId();
        favoriteViewModel.eventids.removeIf(str -> str.equals(eventId));
        favoriteViewModel.events.removeIf(obj -> obj.getEventId() == eventId);
        Log.d("favoriteViewModel", favoriteViewModel.eventids.toString());
        System.out.println(favoriteViewModel.eventids);
    }

    @Override
    public void showAddFav(String name) {
        LinearLayout linearLayout = mView.findViewById(R.id.serach_event_snackbar_layout);
        Snackbar snackbar = Snackbar.make(linearLayout, name + " added to favorites", Snackbar.LENGTH_LONG);
        // 获取 Snackbar 的 TextView
        TextView snackbarTextView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);

        // 设置 Snackbar 文本大小
        float textSizeInSp = 14; // 设置文本大小，单位为 sp
        snackbarTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp);
        // 设置 Snackbar 文本颜色
        int textColorB = getResources().getColor(R.color.black);
        int colorGray = getResources().getColor(R.color.grey);
        snackbar.setTextColor(textColorB);
        snackbar.setBackgroundTint(colorGray);
        snackbar.setActionTextColor(textColorB);
        snackbar.show();
    }

    @Override
    public void showRemoveFav(String name) {
        LinearLayout linearLayout = mView.findViewById(R.id.serach_event_snackbar_layout);
        Snackbar snackbar = Snackbar.make(linearLayout, name + " removed to favorites", Snackbar.LENGTH_LONG);
        // 获取 Snackbar 的 TextView
        TextView snackbarTextView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);

        // 设置 Snackbar 文本大小
        float textSizeInSp = 14; // 设置文本大小，单位为 sp
        snackbarTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp);
        int textColorB = getResources().getColor(R.color.black);
        int colorGray = getResources().getColor(R.color.grey);
        snackbar.setTextColor(textColorB);
        snackbar.setBackgroundTint(colorGray);
        snackbar.setActionTextColor(textColorB);
        snackbar.show();
    }

}
