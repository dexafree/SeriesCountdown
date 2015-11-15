package com.dexafree.seriescountdown.injection;

import com.dexafree.seriescountdown.interactors.service.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@Module
public class AppModule {

    private final static String ENDPOINT = "https://www.episodate.com/api/";


    @Provides
    ApiService provideApiService(){
        Gson gson = new GsonBuilder()
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .build();

        return restAdapter.create(ApiService.class);
    }

}
