package com.dexafree.seriescountdown.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dexafree.seriescountdown.R;
import com.dexafree.seriescountdown.adapters.SeriesAdapter;
import com.dexafree.seriescountdown.interfaces.SeriesView;
import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.presenters.MainPresenter;
import com.dexafree.seriescountdown.utils.RecyclerClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainActivity extends BaseActivity implements SeriesView {


    @Bind(R.id.series_recycler_view)
    RecyclerView seriesRecyclerView;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this);
        setToolbar();
        ButterKnife.bind(this);


        prepareRecycler();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.init();
    }

    private void prepareRecycler(){
        seriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        seriesRecyclerView.setItemAnimator(new SlideInUpAnimator());
        seriesRecyclerView.getItemAnimator().setAddDuration(300);
        seriesRecyclerView.getItemAnimator().setRemoveDuration(300);
        seriesRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Serie serie = (Serie) view.getTag();
                ImageView image = (ImageView)view.findViewById(R.id.serie_image);

                startDetailActivity(serie, image);
            }
        }));
    }

    private void startDetailActivity(Serie serie, View view){
        DetailActivity.launch(this, view, serie);
    }

    @Override
    public void showSeries(List<Serie> series) {
        seriesRecyclerView.setAdapter(new SeriesAdapter(series));
    }

    @Override
    public void showError() {

    }
}
