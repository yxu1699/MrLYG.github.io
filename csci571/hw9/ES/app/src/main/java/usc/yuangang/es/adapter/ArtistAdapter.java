package usc.yuangang.es.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import usc.yuangang.es.R;

import usc.yuangang.es.model.Artist;
import usc.yuangang.es.utils.ScrollingTextView;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private List<Artist> artists;
    private Context context;

    public ArtistAdapter(Context context, List<Artist> artists) {
        this.context = context;
        this.artists = artists;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_item, parent, false);
        return new ArtistViewHolder(view);
    }



    private static String formatNumber(int number) {
        if (number >= 1000000) {
            double numberInMillions = (double) number / 1000000;
            return String.format("%.1fM", numberInMillions);
        } else if (number >= 1000) {
            double numberInThousands = (double) number / 1000;
            return String.format("%.1fK", numberInThousands);
        } else {
            return Integer.toString(number);
        }
    }
    private RequestQueue requestQueue;

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = artists.get(position);

        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        Volley.newRequestQueue(context.getApplicationContext());
        String url = "https://nodejs-379321.uw.r.appspot.com/artistalbum?artistid=" + artist.getArtistID();

        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        holder.artistName.setText(artist.getArtistName());
                        String fs =formatNumber(Integer.parseInt(artist.getArtistFollower().replace(",", "")));
                        holder.artistFollower.setText(fs+" Followers");
//                        holder.artistSpotifyUrl.setText(artist.getArtistSpotifyUrl());


                        Glide.with(context).load(artist.getArtistIcon()).into(holder.artistIcon);

                        List<String> imagesUrl = new ArrayList<>();
                        try {
                            JSONArray items = response.optJSONArray("items");
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject item = items.getJSONObject(i);
                                JSONArray images = item.getJSONArray("images");

                                if (images.length() > 0) {
                                    JSONObject image = images.getJSONObject(0);
                                    String imageUrl = image.getString("url");
                                    imagesUrl.add(imageUrl);
                                }
                            }
                            if (imagesUrl.size() >= 3) {
                                Glide.with(context).load(imagesUrl.get(0)).into(holder.artistAlbums1Img);
                                Glide.with(context).load(imagesUrl.get(1)).into(holder.artistAlbums2Img);
                                Glide.with(context).load(imagesUrl.get(2)).into(holder.artistAlbums3Img);
                            }
                            if (imagesUrl.size() >= 2) {
                                Glide.with(context).load(imagesUrl.get(0)).into(holder.artistAlbums1Img);
                                Glide.with(context).load(imagesUrl.get(1)).into(holder.artistAlbums2Img);
                            }
                            if (imagesUrl.size() >= 1) {
                                Glide.with(context).load(imagesUrl.get(0)).into(holder.artistAlbums1Img);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        requestQueue.add(jsonObjectRequest);



        // TODO 请求albums api
        // Example with Glide: Glide.with(context).load(artist.getArtistAlbums1ImgUrl()).into(holder.artistAlbums1Img);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {

        ImageView artistIcon, artistAlbums1Img, artistAlbums2Img, artistAlbums3Img,artistPopularity;
        ScrollingTextView artistName,artistFollower, artistSpotifyUrl;
        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            artistIcon = itemView.findViewById(R.id.artist_icon);
            artistName = itemView.findViewById(R.id.artist_name);
            artistFollower = itemView.findViewById(R.id.artist_follower);
            artistSpotifyUrl = itemView.findViewById(R.id.artist_spotify);
            artistPopularity = itemView.findViewById(R.id.artist_popularity_img);
            artistAlbums1Img = itemView.findViewById(R.id.artist_albums_1);
            artistAlbums2Img = itemView.findViewById(R.id.artist_albums_2);
            artistAlbums3Img = itemView.findViewById(R.id.artist_albums_3);
        }
    }
}
