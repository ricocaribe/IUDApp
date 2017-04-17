package com.lme.iudapp.utilidades;


import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lme.iudapp.R;

public class ServerException extends Exception
{
    private Activity activity;

    private ServerException(String message, Activity activity) {
        this.activity = activity;
        showErrorAlert(message);
    }

    public static ServerException internalServerError(Activity activity) {
        return new ServerException(activity.getResources().getString(R.string.mensaje_error_internal), activity);
    }

    public static ServerException userNotFoundError(Activity activity) {
        return new ServerException(activity.getResources().getString(R.string.mensaje_error_not_found), activity);
    }

    public void showErrorAlert(final String error){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle(activity.getResources().getString(R.string.app_name))
                        .setMessage(error)
                        .setPositiveButton(activity.getResources().getString(R.string.mensaje_error_refresh), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //activity.getUsers();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
}
