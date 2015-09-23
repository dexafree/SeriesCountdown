package com.dexafree.seriescountdown.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dexafree.seriescountdown.R;
import com.dexafree.seriescountdown.interfaces.DetailView;
import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.model.SerieInfo;
import com.dexafree.seriescountdown.presenters.DetailPresenter;
import com.dexafree.seriescountdown.ui.views.MaterialRow;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Bind(R.id.next_episode_date_row)
    MaterialRow dateRow;

    @Bind(R.id.next_episode_name_row)
    MaterialRow nameRow;

    @Bind(R.id.serie_start_row)
    MaterialRow serieStartRow;

    @Bind(R.id.serie_end_row)
    MaterialRow serieEndRow;

    @Bind(R.id.serie_genres_row)
    MaterialRow serieGenresRow;

    @Bind(R.id.detail_content)
    LinearLayout detailContent;

    @Bind(R.id.favorite_fab)
    FloatingActionButton favoriteFAB;

    @OnClick(R.id.favorite_fab)
    void onFabClicked(){
        presenter.onSaveSerieClicked();
    }

    @OnClick(R.id.serie_image)
    void onImageClicked(){
        ShowImageActivity.launch(this, serieImage, mSerie);
    }

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
        setExitTransition();


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
        dateRow.setRowContent(text);
        dateRow.setHintText("Emission date");
        dateRow.setImage(R.mipmap.ic_next_emission);
    }

    @Override
    public void showNextEpisodeInfo(String title, String subtitle) {

        nameRow.setRowContent(title);
        nameRow.setHintText(subtitle);
    }

    @Override
    public void showSerieStart(String text) {
        serieStartRow.setRowContent(text);
        serieStartRow.setHintText("Starting emission");
    }

    @Override
    public void showSerieEnd(String text) {
        serieEndRow.setRowContent(text);
        serieEndRow.setHintText("Final emission");
    }

    @Override
    public void showSerieGenres(String text) {
        serieGenresRow.setRowContent(text);
        serieGenresRow.setHintText("Genres");
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

        favoriteFAB.setImageResource(drawable);
    }

    private void loadFullSizeImage(){
        Picasso.with(this)
                .load(mSerie.getImageHDUrl())
                .noFade()
                .noPlaceholder()
                .into(serieImage);
    }

    private void loadThumbnail(){
        Picasso.with(this)
                .load(mSerie.getImageUrl())
                .noFade()
                .noPlaceholder()
                .into(serieImage);
    }

    private void fadeInContent(){
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        Animation moveUp = new TranslateAnimation(0, 0, 100, 0);
        moveUp.setInterpolator(new DecelerateInterpolator());
        moveUp.setDuration(1000);


        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(moveUp);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                detailContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        detailContent.startAnimation(animation);
    }

    private void fadeOutContent(){
        Animation fadeIn = new AlphaAnimation(1, 0);
        fadeIn.setInterpolator(new AccelerateInterpolator()); //add this
        fadeIn.setDuration(500);

        Animation moveUp = new TranslateAnimation(0, 0, 0, 100);
        moveUp.setInterpolator(new AccelerateInterpolator());
        moveUp.setDuration(500);


        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(moveUp);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                detailContent.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        detailContent.startAnimation(animation);
    }

    private void setExitTransition(){
        Transition transition = getWindow().getExitTransition();

        if(transition != null){
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    fadeOutContent();
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }
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
                    fadeInContent();
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

    public static void launch(Activity activity, View transitionView, Serie serie) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, EXTRA_IMAGE);


        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(SERIE_EXTRA, serie);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }
}
