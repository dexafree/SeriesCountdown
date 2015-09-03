package com.dexafree.seriescountdown.interactors;

import com.arasthel.asyncjob.AsyncJob;
import com.dexafree.seriescountdown.interfaces.IBaseView;
import com.dexafree.seriescountdown.model.Serie;

import java.util.List;

import rx.Observer;

/**
 * Created by Carlos on 1/9/15.
 */
public abstract class BaseSeriesInteractor {


    public BaseSeriesInteractor(IBaseView view){
        this.callback = callback;
    }

    public abstract void loadSeries(Observer<Serie> observer);

}
