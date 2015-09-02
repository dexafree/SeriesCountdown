package com.dexafree.seriescountdown.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.dexafree.seriescountdown.R;
import com.dexafree.seriescountdown.interfaces.IBaseView;

/**
 * Created by Carlos on 1/9/15.
 */
public class BaseActivity extends AppCompatActivity implements IBaseView {


    @Override
    public Context getContext() {
        return this;
    }

    protected void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getTitle());
    }

    protected void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
