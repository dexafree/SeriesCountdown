package com.dexafree.seriescountdown.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.dexafree.seriescountdown.R;
import com.dexafree.seriescountdown.model.Serie;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Carlos on 1/9/15.
 */
public class DetailActivity extends BaseActivity {

    public static final String EXTRA_IMAGE = "DetailActivity:serie_image";
    public final static String SERIE_EXTRA = "serie";

    @Bind(R.id.serie_image)
    ImageView serieImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        setToolbar();
        ButterKnife.bind(this);

        ViewCompat.setTransitionName(serieImage, EXTRA_IMAGE);

        Serie serie = getIntent().getExtras().getParcelable(SERIE_EXTRA);

        ((CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout)).setTitle(serie.getName());

        Picasso.with(this).load(serie.getImageUrl()).fit().centerInside().into(serieImage);
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
