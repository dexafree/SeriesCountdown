package com.dexafree.seriescountdown.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dexafree.seriescountdown.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MaterialRow extends RelativeLayout {

    @Bind(R.id.row_image)
    ImageView image;

    @Bind(R.id.row_content)
    TextView rowContent;

    @Bind(R.id.row_hint)
    TextView rowHint;

    @Bind(R.id.divider)
    View divider;

    @Bind(R.id.alarm_button)
    ImageView alarmButton;


    public MaterialRow(Context context) {
        super(context);
        init();
    }

    public MaterialRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaterialRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.row_material, this);
        ButterKnife.bind(this);
    }

    public void setImage(int resId){
        image.setImageResource(resId);
    }

    public void setImage(Drawable drawable){
        image.setImageDrawable(drawable);
    }

    public void setImage(Bitmap bitmap){
        image.setImageBitmap(bitmap);
    }

    public void setRowContent(String content){
        rowContent.setText(content);
    }

    public void setRowContent(int resId){
        rowContent.setText(resId);
    }

    public void setHintText(String content){
        rowHint.setText(content);
    }

    public void setHintText(int resId){
        rowHint.setText(resId);
    }

    public void showReminder(){
        divider.setVisibility(VISIBLE);
        alarmButton.setVisibility(VISIBLE);
    }

    public void hideReminder(){
        divider.setVisibility(GONE);
        alarmButton.setVisibility(GONE);
    }

    public void setReminderClick(View.OnClickListener listener){
        alarmButton.setOnClickListener(listener);
    }


}
