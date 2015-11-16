package com.dexafree.seriescountdown.ui;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dexafree.seriescountdown.R;
import com.dexafree.seriescountdown.adapters.SeriesAdapter;
import com.dexafree.seriescountdown.interfaces.SearchView;
import com.dexafree.seriescountdown.model.Serie;
import com.dexafree.seriescountdown.presenters.SearchPresenter;
import com.dexafree.seriescountdown.utils.RecyclerClickListener;
import com.jakewharton.rxbinding.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

/**
 * Activity that will provide search capabilities
 */
public class SearchActivity extends BaseActivity implements SearchView {

    @Bind(R.id.content_layout)
    RelativeLayout contentLayout;

    @Bind(R.id.search_edittext)
    AutoCompleteTextView searchEditText;

    @Bind(R.id.series_recyclerview)
    RecyclerView seriesRecyclerView;

    @Bind(R.id.progress_view)
    ProgressBar progressBar;

    /**
     * Whenever the Search button is pressed, get the text and tell the presenter to search the text
     */
    @OnClick(R.id.search_button)
    void onSearchButtonClick(){
        String query = searchEditText.getText().toString();
        presenter.searchText(query);
    }

    // Hard reference to the presenter
    private SearchPresenter presenter;

    // Hard reference to the SeriesAdapter. It's needed for adding and removing series to the list
    private SeriesAdapter seriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setToolbar();
        ButterKnife.bind(this);

        // Instantiate the presenter
        presenter = new SearchPresenter(this);

        // Prepare the views
        prepareViews();
    }

    private void prepareViews(){

        // Prepare the lists
        prepareLists();

        // Make the "Enter" press act like the "Search" click
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                onSearchButtonClick();
            }
            return false;
        });


        // Show the "Go back" button at the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void prepareLists(){

        /*
            Observe the AutoCompleteEditText.
            If the text length is greater or equal to 3, wait 400 milliseconds.
            If the text has not changed during those 400 milliseconds, call the
            onTextChanged method from the presenter and pass it the text entered on the EditText
         */
        RxTextView
                .textChangeEvents(searchEditText)
                .filter(event -> event.text().length() >= 3)
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(event -> presenter.onTextChanged(event.text().toString()));


        /*
            Observe the clicks made to suggestions from the AutoCompleteEditText.
            If a suggestion is clicked, call the onSuggestionClicked method from the
            presenter and pass it the suggestion text
         */
        RxAutoCompleteTextView
                .itemClickEvents(searchEditText)
                .subscribe(event ->{
                    // From the TextView shown on the dropdown, get its text
                    String query = ((TextView)event.clickedView()).getText().toString();

                    // Pass the suggestion's text to the presenter
                    presenter.onSuggestionClicked(query);
                });

        // Initialize the adapter with an empty list
        seriesAdapter = new SeriesAdapter(new ArrayList<>());

        int numColumns = getResources().getInteger(R.integer.series_columns);

        // Create a 2 column GridLayoutManager
        seriesRecyclerView.setLayoutManager(new GridLayoutManager(this, numColumns) {

            // Override the method to be able to detect overscroll
            @Override
            public int scrollVerticallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
                int scrollRange = super.scrollVerticallyBy(dx, recycler, state);
                int overscroll = dx - scrollRange;
                if (overscroll > 20) {

                    // There has been overscroll
                    scrollFinished();
                }
                return scrollRange;
            }
        });

        // Prepare the list's insert and delete animations
        seriesRecyclerView.setItemAnimator(new FadeInUpAnimator());
        seriesRecyclerView.getItemAnimator().setAddDuration(400);
        seriesRecyclerView.getItemAnimator().setRemoveDuration(300);

        // Set the onClickListener
        seriesRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this,
                (view, position) -> presenter.onItemClicked(position)));

        // Set the adapter
        seriesRecyclerView.setAdapter(seriesAdapter);
    }

    /**
     * Method called whenever the List of series arrives to the end
     */
    private void scrollFinished(){
        searchText();
    }

    /**
     * Method that retrieves the AutoCompleteTextView's text and passes it to the presenter
     */
    private void searchText(){
        String query = searchEditText.getText().toString();
        presenter.onScrollFinished(query);
    }

    /**
     * Receives a list of Strings that contains suggestions related with the text currently
     * entered at the AutoCompleteTextView
     * @param suggestions List of suggestions
     */
    @Override
    public void showSuggestions(List<String> suggestions) {
        // Check if the list contains at least one element
        if(suggestions.size() > 0) {

            // Create a new ArrayAdapter with the received suggestions
            ArrayAdapter<String> suggestionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);

            // Set the adapter to the AutoCompleteTextView
            searchEditText.setAdapter(suggestionsAdapter);

            // Show the dropdown (in case it was previously hidden)
            searchEditText.showDropDown();
        } else {
            // If the list is empty, hide the dropdown
            searchEditText.dismissDropDown();
        }
    }

    /**
     * Method that clears the suggestions and closes the suggestions dropdown
     */
    @Override
    public void hideSuggestions() {
        searchEditText.setAdapter(null);
        searchEditText.dismissDropDown();
    }

    /**
     * Given a serie, it adds it to the List
     * @param serie Serie to be added to the list
     */
    @Override
    public void addSerie(Serie serie) {
        seriesAdapter.addItem(serie);
    }

    /**
     * Clears all the series shown in the List
     */
    @Override
    public void cleanList() {
        seriesAdapter.clearItems();
    }



    /**
     * Shows a search error
     */
    @Override
    public void showSearchError() {
        showToast("Search error");
    }


    @Override
    public void showNoResults() {
        showToast("There are no results");
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        seriesRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        seriesRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Given a serie, it returns if the serie is currently being shown in the List
     * @param serie Serie to be queried
     * @return true if the serie is being shown. Otherwise, false
     */
    @Override
    public boolean isShowingSerie(Serie serie) {
        return seriesAdapter.isShowingSerie(serie);
    }

    /**
     * Method that, given an item position, starts a DetailActivity of the Serie that is currently
     * placed on that position
     * @param position position of the serie to be passed to the DetailActivity
     */
    public void startDetailActivity(int position){

        // Get the ViewHolder for the View in the selected position
        View view = seriesRecyclerView.findViewHolderForAdapterPosition(position).itemView;

        // Get the Serie linked to the ViewHolder
        Serie serie = (Serie) view.getTag();

        /*
            Get the ImageView of the item (breaks encapsulation, but it's needed in order
            to be able to show the SharedElement transition
        */
        ImageView image = (ImageView) view.findViewById(R.id.serie_image);

        // Launch the DetailActivity
        DetailActivity.launch(this, image, serie);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ActivityCompat.finishAfterTransition(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
