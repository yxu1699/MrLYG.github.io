package usc.yuangang.es.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import usc.yuangang.es.model.Artist;
import usc.yuangang.es.model.Event;

public class FavoriteViewModel{
    public List<Event> events = new ArrayList<>();
    public List<String> eventids = new ArrayList<>();
    public void addEvent(Event e){
        events.add(e);
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public FavoriteViewModel() {
    }

    @Override
    public String toString() {
        return "FavoriteViewModel{" +
                "events=" + events.toString() +
                ", eventids=" + eventids.toString() +
                '}';
    }
}
