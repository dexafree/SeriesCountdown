package com.dexafree.seriescountdown.interactors;

import com.arasthel.asyncjob.AsyncJob;
import com.dexafree.seriescountdown.model.Serie;

import java.util.List;

/**
 * Created by Carlos on 1/9/15.
 */
public abstract class BaseSeriesInteractor {

    public interface Callback {

        void onSeriesDownloaded(List<Serie> series);
        void onError();
    }

    protected Callback callback;

    public BaseSeriesInteractor(Callback callback){
        this.callback = callback;
    }

    public abstract void loadSeries();

    protected void sendResult(final List<Serie> series){
        AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
            @Override
            public void doInUIThread() {
                callback.onSeriesDownloaded(series);
            }
        });
    }

    protected void sendError(){
        AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
            @Override
            public void doInUIThread() {
                callback.onError();
            }
        });
    }

}
