package com.dexafree.seriescountdown.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

    private MenuItem starItem;

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

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(mSerie.getName());
        collapsingToolbarLayout.setOnTouchListener((v, event) -> true);

        if(addTransitionListener()){
            loadThumbnail();
        }

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
    public void showNextEpisodeNumber(String text) {
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
    public void setFavoritable(boolean favoritable) {

        int drawable = favoritable ?  R.mipmap.ic_star : R.mipmap.ic_starred;

        if(starItem != null) {
            starItem.setIcon(drawable);
        } else {
            Log.d("DETAILACTIVITY", "STARITEM WAS NULL");
        }
    }

    private void loadFullSizeImage(){
        Picasso.with(this)
                .load(mSerie.getImageHDUrl())
                .noFade()
                .noPlaceholder()
                .fit().centerCrop()
                .into(serieImage);
    }

    private void loadThumbnail(){
        Picasso.with(this)
                .load(mSerie.getImageUrl())
                .noFade()
                .fit().centerCrop()
                .into(serieImage);
    }

    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            // There is an entering shared element transition so add a listener to it
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    // As the transition has ended, we can now load the full-size image
                    loadFullSizeImage();

                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // No-op
                }
            });
            return true;
        }

        // If we reach here then we have not added a listener
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        starItem = menu.getItem(0);
        Log.d("DETAILACTIVITY", "MENU SIZE: "+menu.size());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                ActivityCompat.finishAfterTransition(this);
                break;

            case R.id.action_favorite:
                presenter.onSaveSerieClicked();
                break;
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
