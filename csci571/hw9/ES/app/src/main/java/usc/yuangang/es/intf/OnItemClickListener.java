package usc.yuangang.es.intf;

import java.util.List;

import usc.yuangang.es.model.Event;

public interface OnItemClickListener {
    void onItemClick(int position);
    void onItemClickE(Event event);

    void onFavClick(int position);

    List<Event> getAllFav();
    List<String> getAllFavStr();

    void removeFav(int position);

    void showAddFav(String name);

    void showRemoveFav(String name);
}
