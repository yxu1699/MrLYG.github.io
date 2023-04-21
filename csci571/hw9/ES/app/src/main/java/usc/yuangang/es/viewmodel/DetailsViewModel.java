package usc.yuangang.es.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
    Venue venue;

    public List<String> artistsOrder;

    public DetailsViewModel() {
        detail = new MutableLiveData<>(); // 初始化 MutableLiveData 对象
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

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }
}
