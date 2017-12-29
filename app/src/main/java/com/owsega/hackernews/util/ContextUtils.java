package com.owsega.hackernews.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class ContextUtils {

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null;
    }

    public static Snackbar createActionSnackbar(View view, String message,
                                                String action, OnClickListener listener) {
        return createSnackbar(view, message)
                .setAction(action, listener);
    }

    public static Snackbar createSnackbar(View view, String message) {
        return Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
    }
}
