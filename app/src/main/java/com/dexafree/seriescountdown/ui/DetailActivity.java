package com.dexafree.seriescountdown.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.dexafree.seriescountdown.R;
import com.dexafree.seriescountdown.interfaces.DetailView;
import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.model.SerieInfo;
import com.dexafree.seriescountdown.presenters.DetailPresenter;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Carlos on 1/9/15.
 */
public class DetailActivity extends BaseActivity implements DetailView {

    public static final String EXTRA_IMAGE = "DetailActivity:serie_image";
    public final static String SERIE_EXTRA = "serie";

    @Bind(R.id.serie_image)
    ImageView serieImage;

    @Bind(R.id.time_remaining_textview)
    TextView timeRemainingTextView;

    @Bind(R.id.date_next_textview)
    TextView dateNextTextView;

    @Bind(R.id.next_episode_name_textview)
    TextView nextTitleTextView;

    private Serie mSerie;
    private DetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        setToolbar();
        ButterKnife.bind(this);
        presenter = new DetailPresenter(this);
        init();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.init();
    }

    private void init(){

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewCompat.setTransitionName(serieImage, EXTRA_IMAGE);

        mSerie = getIntent().getExtras().getParcelable(SERIE_EXTRA);

        ((CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout)).setTitle(mSerie.getName());

        Picasso.with(this)
                .load(mSerie.getImageHDUrl())
                .fit().centerCrop()
                .into(serieImage);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showTimeRemaining(String text) {
        timeRemainingTextView.setText(text);
    }

    @Override
    public void showNextEpisodeDate(String text) {
        dateNextTextView.setText(text);
    }

    @Override
    public void showNExtEpisodeNumber(String text) {
        nextTitleTextView.setText(text);
    }

    @Override
    public void showError() {
        showToast("ERROR");
    }

    @Override
    public Serie getSerie() {
        return mSerie;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ActivityCompat.finishAfterTransition(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public static void launch(BaseActivity activity, View transitionView, Serie serie) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, EXTRA_IMAGE);


        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(SERIE_EXTRA, serie);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }
}
