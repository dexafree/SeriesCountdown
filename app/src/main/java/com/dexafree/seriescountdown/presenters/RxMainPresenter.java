package com.dexafree.seriescountdown.presenters;

import android.util.Log;

import com.dexafree.seriescountdown.interactors.RxPopularSeriesInteractor;
import com.dexafree.seriescountdown.model.Serie;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Carlos on 2/9/15.
 */
public class RxMainPresenter implements Observer<Serie> {

    public interface Callback {
        void addItem(Serie serie);
        void showError();
    }

    private Callback callback;
    private Subscription subscription;
    private RxPopularSeriesInteractor interactor;

    public RxMainPresenter(Callback callback) {
        this.callback = callback;
        this.interactor = new RxPopularSeriesInteractor();
    }

    public void loadSeries(){
        if(subscription == null){
            subscription = interactor.loadSeries(this);
        }
    }

    @Override
    public void onCompleted() {
        Log.d("RXMAINPRESENTER", "ONCOMPLETED");
        subscription.unsubscribe();
        subscription = null;
    }

    @Override
    public void onError(Throwable e) {
        Log.d("RXMAINPRESENTER", "ONERROR");
        e.printStackTrace();
        callback.showError();
    }

    @Override
    public void onNext(Serie serie) {
        Log.d("RXMAINPRESENTER", "ONNEXT: "+serie.getName() );
        callback.addItem(serie);
    }
}
