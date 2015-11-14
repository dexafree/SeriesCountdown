package com.dexafree.seriescountdown.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dexafree.seriescountdown.R;
import com.dexafree.seriescountdown.adapters.SeriesAdapter;
import com.dexafree.seriescountdown.interfaces.SeriesView;
import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.presenters.BaseSerieListPresenter;
import com.dexafree.seriescountdown.ui.DetailActivity;
import com.dexafree.seriescountdown.utils.RecyclerClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

public abstract class BaseSerieListFragment<T extends BaseSerieListPresenter> extends BaseFragment implements SeriesView {

    @Bind(R.id.series_recyclerview)
    RecyclerView seriesRecyclerView;

    protected T presenter;
    protected SeriesAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, v);
        presenter = getPresenter();
        mAdapter = new SeriesAdapter(new ArrayList<>());
        prepareViews();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.init();
    }

    private void prepareViews(){

        int numColumns = getResources().getInteger(R.integer.series_columns);

        seriesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns) {
            @Override
            public int scrollVerticallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
                int scrollRange = super.scrollVerticallyBy(dx, recycler, state);
                int overscroll = dx - scrollRange;
                if (overscroll > 20) {
                    listScrollFinished();
                }
                return scrollRange;
            }
        });
        seriesRecyclerView.setItemAnimator(new FadeInUpAnimator());
        seriesRecyclerView.getItemAnimator().setAddDuration(800);
        seriesRecyclerView.getItemAnimator().setRemoveDuration(300);
        seriesRecyclerView.addOnItemTouchListener(new RecyclerClickListener(getContext(),
                (view, position) -> presenter.onItemClicked(position)));

        seriesRecyclerView.setAdapter(mAdapter);
    }

    public void startDetailActivity(int position){

        View view = seriesRecyclerView.findViewHolderForAdapterPosition(position).itemView;

        Serie serie = (Serie) view.getTag();
        ImageView image = (ImageView) view.findViewById(R.id.serie_image);

        DetailActivity.launch(getActivity(), image, serie);
    }

    protected void listScrollFinished(){

    }

    @Override
    public void updateSeries(List<Serie> series) {
        mAdapter.setList(series);
    }

    @Override
    public void clearList(){
        mAdapter.clearItems();
    }

    @Override
    public void addItem(Serie serie) {
        mAdapter.addItem(serie);
    }

    @Override
    public void showError() {
        showToast("There has been an error");
    }

    protected abstract T getPresenter();
}
