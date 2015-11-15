package com.dexafree.seriescountdown.injection;

import com.dexafree.seriescountdown.interactors.GetPopularSeriesInteractor;
import com.dexafree.seriescountdown.interactors.SearchSeriesInteractor;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(GetPopularSeriesInteractor interactor);
    void inject(SearchSeriesInteractor interactor);

}
