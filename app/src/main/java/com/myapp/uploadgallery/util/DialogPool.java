package com.myapp.uploadgallery.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class DialogPool {
    private Map<String, AlertDialog> pool = new HashMap<>();

    @Inject
    DialogPool() {
    }

    public void showDialog(String tag, Context context, @StringRes int title,
                           @StringRes int message,
                           @StringRes int ok, DialogInterface.OnClickListener listenerOk,
                           @StringRes int cancel, DialogInterface.OnClickListener listenerCancel) {
        AlertDialog upload = pool.get(tag);
        if (null == upload || !upload.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message);
            if (ok != 0) {
                builder.setPositiveButton(ok, listenerOk);
            }
            if (cancel != 0) {
                builder.setNegativeButton(cancel, listenerCancel);
            }
            final AlertDialog alertDialog = builder.create();
            pool.put(tag, alertDialog);
            alertDialog.show();
        }
    }
}
