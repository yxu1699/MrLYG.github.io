package usc.yuangang.es.model;

import java.util.Iterator;

public class Event implements Comparable<Event> {

    String EventId;
    String EventName;
    String Date;
    String Time;
    String iconUrl;
    String venue;
    String genre;

    boolean isFav = false;

    public Event() {
    }

    public Event(String eventId, String eventName, String date, String time, String iconUrl, String venue, String genre) {
        EventId = eventId;
        EventName = eventName;
        Date = date;
        Time = time;
        this.iconUrl = iconUrl;
        this.venue = venue;
        this.genre = genre;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Event{" +
                "EventId='" + EventId + '\'' +
                ", EventName='" + EventName + '\'' +
                ", Date='" + Date + '\'' +
                ", Time='" + Time + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", venue='" + venue + '\'' +
                ", genre='" + genre + '\'' +
                ", isFav=" + isFav +
                '}';
    }

    @Override
    public int compareTo(Event event) {
        return this.Date.compareTo(event.Date);
    }
}
