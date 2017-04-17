package com.lme.iudapp.utilidades;


import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lme.iudapp.MainActivity;
import com.lme.iudapp.R;

public class ServerException extends Exception
{

    private ServerException(String message) {
        super(message);
    }

    public static ServerException internalServerError(Activity activity) {
        return new ServerException(activity.getResources().getString(R.string.mensaje_error_internal));
    }

    public static ServerException userNotFoundError(Activity activity) {
        return new ServerException(activity.getResources().getString(R.string.mensaje_error_not_found));
    }
}
