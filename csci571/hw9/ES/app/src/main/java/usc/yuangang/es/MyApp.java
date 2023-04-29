package usc.yuangang.es;

import android.app.Application;

import usc.yuangang.es.viewmodel.FavoriteViewModel;

public class MyApp extends Application {
    private FavoriteViewModel favoriteViewModel;

    @Override
    public void onCreate() {
        super.onCreate();
        favoriteViewModel = new FavoriteViewModel();
    }

    public FavoriteViewModel getMyApplicationModel() {
        return favoriteViewModel;
    }
}
