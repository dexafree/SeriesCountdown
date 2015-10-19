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

public class MainActivity extends BaseActivity {


    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.tablayout)
    TabLayout tabLayout;

    @OnClick(R.id.search_fab)
    void searchFabClicked(){
        startActivity(new Intent(this, SearchActivity.class));
    }

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
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(){

        this.favoriteSeriesFragment = new FavoriteSeriesFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new PopularSeriesFragment(), "POPULAR");
        adapter.addFrag(favoriteSeriesFragment, "FAVORITES");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (favoriteSeriesFragment != null){
            favoriteSeriesFragment.reloadSeries();
        } else {
            Log.d("MAINACTIVITY", "FRAGMENT IS NULL");
        }
    }
}
