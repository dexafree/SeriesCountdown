package com.dexafree.seriescountdown.ui.fragments;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.dexafree.seriescountdown.interfaces.IBaseView;

public abstract class BaseFragment extends Fragment implements IBaseView {

    protected void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}
