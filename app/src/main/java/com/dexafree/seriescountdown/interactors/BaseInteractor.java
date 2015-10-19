package com.dexafree.seriescountdown.interactors;

import com.arasthel.asyncjob.AsyncJob;

public abstract class BaseInteractor<T> {

    public interface Callback<T> {
        void onDataDownloaded(T data);
        void onError();
    }

    private Callback<T> callback;

    public BaseInteractor(Callback<T> callback){
        this.callback = callback;
    }

    protected void sendResult(T result){
        AsyncJob.doOnMainThread(() -> callback.onDataDownloaded(result));
    }

    protected void sendError(){
        AsyncJob.doOnMainThread(callback::onError);
    }
}
