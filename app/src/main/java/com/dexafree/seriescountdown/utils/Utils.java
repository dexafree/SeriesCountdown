package com.dexafree.seriescountdown.utils;

import android.os.Build;

public class Utils {

    public static boolean isLollipopOrHigher(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

}
