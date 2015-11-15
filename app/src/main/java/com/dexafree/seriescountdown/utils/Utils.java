package com.dexafree.seriescountdown.utils;

import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public class Utils {

    public static boolean isLollipopOrHigher(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static <T, K> List<K> map(Iterable<T> list, Func1<T, K> function){
        List<K> newList = new ArrayList<>();

        for(T item : list){
            newList.add(function.call(item));
        }

        return newList;
    }

    public static <T> List<T> filter(Iterable<T> list, Func1<T, Boolean> function){
        List<T> newList = new ArrayList<>();

        for(T item : list){
            if(function.call(item)) {
                newList.add(item);
            }
        }

        return newList;
    }



}
