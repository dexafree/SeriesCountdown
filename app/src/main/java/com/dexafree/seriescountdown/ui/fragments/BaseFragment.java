package com.dexafree.seriescountdown.ui.fragments;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.dexafree.seriescountdown.interfaces.IBaseView;

/**
 * Created by Carlos on 3/9/15.
 */
public abstract class BaseFragment extends Fragment implements IBaseView {

    protected void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}
