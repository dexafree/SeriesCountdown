package com.dexafree.seriescountdown.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.dexafree.seriescountdown.R;
import com.dexafree.seriescountdown.model.Serie;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShowImageActivity extends BaseActivity {

    private final static String SERIE_EXTRA = "serie_extra";
    private final static String EXTRA_IMAGE = "ShowImageActivity:serie_image";

    @Bind(R.id.serie_image)
    ImageView serieImage;

    private Serie mSerie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        setToolbar();
        ButterKnife.bind(this);
        init();
    }

    private void init(){

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewCompat.setTransitionName(serieImage, EXTRA_IMAGE);

        mSerie = getIntent().getExtras().getParcelable(SERIE_EXTRA);

        getSupportActionBar().setTitle(mSerie.getName());

        if(addTransitionListener()){
            loadThumbnail();
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
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            ActivityCompat.finishAfterTransition(this);
        }

        return super.onOptionsItemSelected(item);
    }

    public static void launch(Activity activity, View transitionView, Serie serie) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, EXTRA_IMAGE);


        Intent intent = new Intent(activity, ShowImageActivity.class);
        intent.putExtra(SERIE_EXTRA, serie);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }
}
