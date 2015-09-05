package com.dexafree.seriescountdown.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.dexafree.seriescountdown.R;
import com.dexafree.seriescountdown.interfaces.ISearchView;
import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.presenters.SearchPresenter;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Carlos on 5/9/15.
 */
public class SearchActivity extends BaseActivity implements ISearchView {

    private final static String EXTRA_IMAGE = "SearchActivity:fabimage";
    private final static long ANIM_DURATION = 1000;

    @Bind(R.id.content_layout)
    LinearLayout contentLayout;

    @Bind(R.id.search_edittext)
    AutoCompleteTextView searchEditText;

    @Bind(R.id.series_recyclerview)
    RecyclerView seriesRecyclerView;

    private SearchPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setToolbar();
        ButterKnife.bind(this);
        presenter = new SearchPresenter(this);
        prepareLists();
    }

    private void prepareLists(){

        RxTextView
                .textChangeEvents(searchEditText)
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(event -> presenter.onTextChanged(event.text().toString()));
    }

    @Override
    public void showSuggestions(List<String> suggestions) {
        if(suggestions.size() > 0) {
            ArrayAdapter<String> suggestionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
            searchEditText.setAdapter(suggestionsAdapter);
            searchEditText.showDropDown();
        } else {
            searchEditText.dismissDropDown();
        }
    }

    @Override
    public void showSeries(List<Serie> series) {

    }
}
