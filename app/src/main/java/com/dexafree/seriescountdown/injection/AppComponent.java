package com.dexafree.seriescountdown.injection;

import com.dexafree.seriescountdown.interactors.FavoriteSeriesInteractor;
import com.dexafree.seriescountdown.interactors.GetPopularSeriesInteractor;
import com.dexafree.seriescountdown.interactors.SearchSeriesInteractor;
import com.dexafree.seriescountdown.interactors.SearchSuggestionsInteractor;
import com.dexafree.seriescountdown.interactors.service.ApiService;
import com.dexafree.seriescountdown.presenters.BaseSerieListPresenter;
import com.dexafree.seriescountdown.presenters.DetailPresenter;
import com.dexafree.seriescountdown.presenters.FavoriteSeriesPresenter;
import com.dexafree.seriescountdown.presenters.PopularSeriesPresenter;
import com.dexafree.seriescountdown.presenters.SearchPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(GetPopularSeriesInteractor interactor);
    void inject(SearchSeriesInteractor interactor);
    void inject(SearchSuggestionsInteractor interactor);
    void inject(FavoriteSeriesInteractor interactor);
    void inject(SearchPresenter presenter);

    void inject(FavoriteSeriesPresenter presenter);
    void inject(PopularSeriesPresenter presenter);
    void inject(DetailPresenter presenter);
}
