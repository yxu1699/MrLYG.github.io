package usc.yuangang.es;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager2.widget.ViewPager2;

import usc.yuangang.es.adapter.SearchVP2Adapter;

public class SearchVP2Fragment  extends Fragment {
    public ViewPager2 SearchVP2;
    public SearchVP2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_vp2, container, false);

        SearchVP2 = view.findViewById(R.id.search_vp2);

        SearchVP2Adapter SearchVP2Adapter = new SearchVP2Adapter(requireActivity());
        SearchVP2.setAdapter(SearchVP2Adapter);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).vp2 = SearchVP2;
        }
        return view;
    }
    public void switchSearchVP2Fragment(int position) {
        if (SearchVP2 != null) {
            SearchVP2.setCurrentItem(position, true);
            System.out.println("setCurrentItem---------------1");
        }
    }
}