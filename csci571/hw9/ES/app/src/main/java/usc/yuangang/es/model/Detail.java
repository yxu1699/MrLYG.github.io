package usc.yuangang.es.model;

public class Detail {
    private String artist;
    private String venue;
    private String date;
    private String time;
    private String genre;
    private String priceRange;
    private String ticketStatus;
    private String buyTicketAt;
    private String mapImageUrl;

    public Detail() {
    }

    @Override
    public String toString() {
        return "Detail{" +
                "artist='" + artist + '\'' +
                ", venue='" + venue + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", genre='" + genre + '\'' +
                ", priceRange='" + priceRange + '\'' +
                ", ticketStatus='" + ticketStatus + '\'' +
                ", buyTicketAt='" + buyTicketAt + '\'' +
                ", mapImageUrl='" + mapImageUrl + '\'' +
                '}';
    }

    public Detail(String artist, String venue, String date, String time, String genre, String priceRange, String ticketStatus, String buyTicketAt, String mapImageUrl) {
        this.artist = artist;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.genre = genre;
        this.priceRange = priceRange;
        this.ticketStatus = ticketStatus;
        this.buyTicketAt = buyTicketAt;
        this.mapImageUrl = mapImageUrl;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getBuyTicketAt() {
        return buyTicketAt;
    }

    public void setBuyTicketAt(String buyTicketAt) {
        this.buyTicketAt = buyTicketAt;
    }

    public String getMapImageUrl() {
        return mapImageUrl;
    }

    public void setMapImageUrl(String mapImageUrl) {
        this.mapImageUrl = mapImageUrl;
    }
}
