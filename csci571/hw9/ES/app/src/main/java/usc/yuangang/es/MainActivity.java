package usc.yuangang.es;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import usc.yuangang.es.adapter.SearchFavViewAdapter;
import usc.yuangang.es.intf.ViewModelInteractor;

public class MainActivity extends AppCompatActivity  implements ViewModelInteractor {
    ViewPager2 vp;
    ViewPager2 vp2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        SearchFavViewAdapter searchFavViewAdapter = new SearchFavViewAdapter(fragmentManager, getLifecycle());
        vp = findViewById(R.id.pagesSearchFav);
        vp.setAdapter(searchFavViewAdapter);

        TabLayout tabLayout = findViewById(R.id.tabSearchFav);
        tabLayout.addTab(tabLayout.newTab().setText("SEARCH"));
        tabLayout.addTab(tabLayout.newTab().setText("FAVORITES"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });



    }


    @Override
    public void onItemClicked(int position) {

    }
}
