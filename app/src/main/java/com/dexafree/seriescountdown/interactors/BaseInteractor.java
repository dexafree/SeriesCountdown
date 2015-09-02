package com.dexafree.seriescountdown.interactors;

import com.arasthel.asyncjob.AsyncJob;

import java.util.List;

/**
 * Created by Carlos on 2/9/15.
 */
public abstract class BaseInteractor<T> {

    public interface Callback<T> {
        void showResults(List<T> results);
        void showError();
    }

    protected Callback<T> callback;

    public BaseInteractor(Callback callback) {
        this.callback = callback;
    }

    protected void sendResults(List<T> results){
        AsyncJob.doOnMainThread(() -> callback.showResults(results));
    }

    protected void sendError(){
        AsyncJob.doOnMainThread(callback::showError);
    }

    public abstract void loadData();

}
