package com.dexafree.seriescountdown.ui;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.dexafree.seriescountdown.R;

/**
 * BaseActivity
 * Every Activity of this Application will extend BaseActivity
 * Also, it implements IBaseView, the most basic View interface implementation
 */
public class BaseActivity extends AppCompatActivity {


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
