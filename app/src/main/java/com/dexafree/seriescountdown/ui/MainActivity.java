package com.dexafree.seriescountdown.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.dexafree.seriescountdown.R;
import com.dexafree.seriescountdown.adapters.ViewPagerAdapter;
import com.dexafree.seriescountdown.ui.fragments.FavoriteSeriesFragment;
import com.dexafree.seriescountdown.ui.fragments.PopularSeriesFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Launching Activity.
 * It will have ViewPager with two Fragments
 */
public class MainActivity extends BaseActivity {


    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.tablayout)
    TabLayout tabLayout;

    @OnClick(R.id.search_fab)
    void searchFabClicked(){
        startActivity(new Intent(this, SearchActivity.class));
    }

    /*
        Hard reference to the FavoriteSeriesFragment.
        It's needed to update the favorited series
     */
    private FavoriteSeriesFragment favoriteSeriesFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        ButterKnife.bind(this);
        prepareViews();
    }


    private void prepareViews(){

        // Prepare the ViewPager
        setupViewPager();

        // Link the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(){

        this.favoriteSeriesFragment = new FavoriteSeriesFragment();

        // Create the ViewPagerAdapter and populate it
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new PopularSeriesFragment(), "POPULAR");
        adapter.addFrag(favoriteSeriesFragment, "FAVORITES");

        // Set the adapter to the ViewPager
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (favoriteSeriesFragment != null){

            /*
                If the Activity comes back from another Activity (i.e. SearchActivity or
                DetailActivity), reload the series shown in the FavoriteSeriesFragment
             */
            favoriteSeriesFragment.reloadSeries();
        } else {
            // It will be null when the Activity first launches
            Log.d("MAINACTIVITY", "FRAGMENT IS NULL");
        }
    }
}
