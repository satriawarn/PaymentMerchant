package com.example.helper;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Aviroez on 05/03/2015.
 */
public class CustomProgressDialog extends ProgressDialog {
    public CustomProgressDialog(Context context) {
        super(context);
        super.setTitle("Loading");
        super.setCancelable(false);
        super.setMessage("Please wait...");
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    @Override
    public void setMessage(CharSequence message) {
        super.setMessage(message);
    }
}
