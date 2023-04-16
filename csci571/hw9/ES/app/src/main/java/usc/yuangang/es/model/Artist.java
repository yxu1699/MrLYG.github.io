package usc.yuangang.es.model;

public class Artist {
    private String artistIcon;
    private String artistName;
    private String artistFollower;
    private String artistSpotifyUrl;
    private String artistPopularity;
    private String artistAlbums1ImgUrl;
    private String artistAlbums2ImgUrl;
    private String artistAlbums3ImgUrl;


    public Artist(String artistIcon, String artistName, String artistFollower, String artistSpotifyUrl, String artistPopularity, String artistAlbums1ImgUrl, String artistAlbums2ImgUrl, String artistAlbums3ImgUrl) {
        this.artistIcon = artistIcon;
        this.artistName = artistName;
        this.artistFollower = artistFollower;
        this.artistSpotifyUrl = artistSpotifyUrl;
        this.artistPopularity = artistPopularity;
        this.artistAlbums1ImgUrl = artistAlbums1ImgUrl;
        this.artistAlbums2ImgUrl = artistAlbums2ImgUrl;
        this.artistAlbums3ImgUrl = artistAlbums3ImgUrl;
    }


    public Artist() {
    }

    public String getArtistIcon() {
        return artistIcon;
    }

    public void setArtistIcon(String artistIcon) {
        this.artistIcon = artistIcon;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistFollower() {
        return artistFollower;
    }

    public void setArtistFollower(String artistFollower) {
        this.artistFollower = artistFollower;
    }

    public String getArtistSpotifyUrl() {
        return artistSpotifyUrl;
    }

    public void setArtistSpotifyUrl(String artistSpotifyUrl) {
        this.artistSpotifyUrl = artistSpotifyUrl;
    }

    public String getArtistPopularity() {
        return artistPopularity;
    }

    public void setArtistPopularity(String artistPopularity) {
        this.artistPopularity = artistPopularity;
    }

    public String getArtistAlbums1ImgUrl() {
        return artistAlbums1ImgUrl;
    }

    public void setArtistAlbums1ImgUrl(String artistAlbums1ImgUrl) {
        this.artistAlbums1ImgUrl = artistAlbums1ImgUrl;
    }

    public String getArtistAlbums2ImgUrl() {
        return artistAlbums2ImgUrl;
    }

    public void setArtistAlbums2ImgUrl(String artistAlbums2ImgUrl) {
        this.artistAlbums2ImgUrl = artistAlbums2ImgUrl;
    }

    public String getArtistAlbums3ImgUrl() {
        return artistAlbums3ImgUrl;
    }

    public void setArtistAlbums3ImgUrl(String artistAlbums3ImgUrl) {
        this.artistAlbums3ImgUrl = artistAlbums3ImgUrl;
    }
}
