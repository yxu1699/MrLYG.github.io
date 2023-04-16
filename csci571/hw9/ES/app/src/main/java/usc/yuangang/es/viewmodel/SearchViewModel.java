package usc.yuangang.es.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.io.Closeable;

public class SearchViewModel extends ViewModel {

    private String keyword;
    private String distance;
    private String category;
    private String location;

    private boolean autoCheck;

    public boolean isAutoCheck() {
        return autoCheck;
    }

    public void setAutoCheck(boolean autoCheck) {
        this.autoCheck = autoCheck;
    }

    @Override
    public String toString() {
        return "SearchViewModel{" +
                "keyword='" + keyword + '\'' +
                ", distance='" + distance + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", autoCheck=" + autoCheck +
                '}';
    }

    public SearchViewModel(String keyword, String distance, String category, String location) {
        this.keyword = keyword;
        this.distance = distance;
        this.category = category;
        this.location = location;
    }


    public SearchViewModel() {
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
