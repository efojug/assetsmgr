package com.efojug.assetsmgr.util;

import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

public class Utils {
    public static void showSnackbar(View v, String text) {
        Snackbar snackbar = Snackbar.make(v, text, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) snackbarView.getLayoutParams();
        layoutParams.bottomMargin = 250;
        snackbarView.setLayoutParams(layoutParams);
        snackbar.show();
    }
}
