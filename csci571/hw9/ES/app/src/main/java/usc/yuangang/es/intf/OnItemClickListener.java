package usc.yuangang.es.intf;

import java.util.List;

import usc.yuangang.es.model.Event;

public interface OnItemClickListener {
    void onItemClick(int position);

    void onFavClick(int position);

    List<Event> getAllFav();
    List<String> getAllFavStr();

    void removeFav(int position);
}
