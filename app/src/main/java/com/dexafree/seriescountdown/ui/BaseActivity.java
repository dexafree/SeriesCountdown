package com.dexafree.seriescountdown.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.dexafree.seriescountdown.R;
import com.dexafree.seriescountdown.interfaces.IBaseView;

/**
 * BaseActivity
 * Every Activity of this Application will extend BaseActivity
 * Also, it implements IBaseView, the most basic View interface implementation
 */
public class BaseActivity extends AppCompatActivity implements IBaseView {


    /**
     * Necessary method for implementing the IBaseView interface
     * @return Returns the Activity itself, as it'can also be used as a Context
     */
    @Override
    public Context getContext() {
        return this;
    }

    /**
     * Given that every activity will have a Toolbar (support-v7) with @id=toolbar,
     * this method sets it as the supportActionBar, and also sets the title
     */
    protected void setToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getTitle());
    }

    /**
     * Shows a toast with the text received
     * @param message Text that will be shown in the Toast
     */
    protected void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
