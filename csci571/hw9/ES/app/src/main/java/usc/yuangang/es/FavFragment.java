package usc.yuangang.es;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.CharArrayReader;
import java.util.List;

import usc.yuangang.es.adapter.EventListAdapter;
import usc.yuangang.es.adapter.FavListAdapter;
import usc.yuangang.es.intf.OnItemClickListener;
import usc.yuangang.es.model.Event;
import usc.yuangang.es.viewmodel.FavoriteViewModel;

public class FavFragment extends Fragment implements FavListAdapter.OnFavoriteRemovedListener, OnItemClickListener {

    View mView;

    private FavoriteViewModel favoriteViewModel;

    public FavFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView =  inflater.inflate(R.layout.fragment_fav, container, false);

        MyApp myApp = (MyApp) requireActivity().getApplication();
        favoriteViewModel = myApp.getMyApplicationModel();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        CardView nf = mView.findViewById(R.id.fav_no_found);
        RecyclerView rv = mView.findViewById(R.id.fav_events);
        if (favoriteViewModel.eventids.size() == 0){
            nf.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }else {
            nf.setVisibility(View.GONE);


            List<Event> events = favoriteViewModel.events;
            FavListAdapter adapter = new FavListAdapter(requireContext(), events, favoriteViewModel,this,this);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(requireContext()));
            rv.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            // 当前 Fragment 变为可见且已恢复
            onFragmentVisible();
        }
    }

    private void onFragmentVisible() {
        // 在此处执行操作，例如重新加载数据或更新 UI
        onResume();
    }

    @Override
    public void onFavoriteRemoved() {
        CardView nf = mView.findViewById(R.id.fav_no_found);
        RecyclerView rv = mView.findViewById(R.id.fav_events);
        if (favoriteViewModel.eventids.size() == 0){
            nf.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }else {
            nf.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onItemClick(int position) {

    }
    @Override
    public void onItemClickE(Event event) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("eventId", event.getEventId());
        startActivity(intent);
    }

    @Override
    public void onFavClick(int position) {

    }

    @Override
    public List<Event> getAllFav() {
        return null;
    }

    @Override
    public List<String> getAllFavStr() {
        return null;
    }

    @Override
    public void removeFav(int position) {

    }

    @Override
    public void showAddFav(String name) {

    }

    @Override
    public void showRemoveFav(String name) {
        FrameLayout frameLayout = mView.findViewById(R.id.fav_snackbar_layout);
        Snackbar snackbar = Snackbar.make(frameLayout, name + " removed to favorites", Snackbar.LENGTH_LONG);
        // 获取 Snackbar 的 TextView
        TextView snackbarTextView = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);

        // 设置 Snackbar 文本大小
        float textSizeInSp = 14; // 设置文本大小，单位为 sp
        snackbarTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeInSp);
        int textColorB = getResources().getColor(R.color.black);
        int colorGray = getResources().getColor(R.color.grey);
        snackbar.setTextColor(textColorB);
        snackbar.setBackgroundTint(colorGray);
        snackbar.setActionTextColor(textColorB);
        snackbar.show();
    }
}
