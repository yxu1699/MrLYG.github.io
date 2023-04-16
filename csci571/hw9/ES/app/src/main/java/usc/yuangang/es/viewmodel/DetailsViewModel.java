package usc.yuangang.es.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Closeable;
import java.util.List;

import usc.yuangang.es.model.Artist;
import usc.yuangang.es.model.Detail;
import usc.yuangang.es.model.Venue;

public class DetailsViewModel  extends ViewModel {

    // details model
    MutableLiveData<Detail> detail;

    // artists
    List<Artist> artists;

    // venue
    MutableLiveData<Venue> venue;

    public DetailsViewModel() {
        detail = new MutableLiveData<>(); // 初始化 MutableLiveData 对象
    }

    public DetailsViewModel(MutableLiveData<Detail> detail, List<Artist> artists, MutableLiveData<Venue> venue, @NonNull Closeable... closeables) {
        super(closeables);
        this.detail = detail;
        this.artists = artists;
        this.venue = venue;
    }

    public MutableLiveData<Detail> getDetail() {
        return detail;
    }

    public void setDetail(Detail  detail) {
        this.detail.setValue(detail);
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public MutableLiveData<Venue> getVenue() {
        return venue;
    }

    public void setVenue(MutableLiveData<Venue> venue) {
        this.venue = venue;
    }
}
