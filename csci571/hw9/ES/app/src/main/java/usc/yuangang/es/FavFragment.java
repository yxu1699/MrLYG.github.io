package usc.yuangang.es;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.CharArrayReader;
import java.util.List;

import usc.yuangang.es.adapter.EventListAdapter;
import usc.yuangang.es.adapter.FavListAdapter;
import usc.yuangang.es.intf.OnItemClickListener;
import usc.yuangang.es.model.Event;
import usc.yuangang.es.viewmodel.FavoriteViewModel;

public class FavFragment extends Fragment implements FavListAdapter.OnFavoriteRemovedListener {

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
            FavListAdapter adapter = new FavListAdapter(requireContext(), events, favoriteViewModel,this);
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

}
