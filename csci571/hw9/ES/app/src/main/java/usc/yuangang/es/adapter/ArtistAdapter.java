package usc.yuangang.es.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = artists.get(position);

        // Set artist data
        // You should use an image loading library like Glide or Picasso for loading images from URLs
        // Example with Glide: Glide.with(context).load(artist.getArtistIcon()).into(holder.artistIcon);
        holder.artistName.setText(artist.getArtistName());
        holder.artistFollower.setText(artist.getArtistFollower());
        holder.artistSpotifyUrl.setText(artist.getArtistSpotifyUrl());
//        holder.artistPopularity.setText(artist.getArtistPopularity());

        // Set album images
        Glide.with(context).load(artist.getArtistIcon()).into(holder.artistIcon);


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
