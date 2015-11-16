package com.dexafree.seriescountdown.injection;

import com.dexafree.seriescountdown.interactors.FavoriteSeriesInteractor;
import com.dexafree.seriescountdown.interactors.GetPopularSeriesInteractor;
import com.dexafree.seriescountdown.interactors.SearchSeriesInteractor;
import com.dexafree.seriescountdown.interactors.SearchSuggestionsInteractor;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(GetPopularSeriesInteractor interactor);
    void inject(SearchSeriesInteractor interactor);
    void inject(SearchSuggestionsInteractor interactor);
    void inject(FavoriteSeriesInteractor interactor);
}
