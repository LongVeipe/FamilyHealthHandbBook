package com.example.familyhealthhandbook;

import android.app.AlertDialog;
import android.content.Context;

import com.example.familyhealthhandbook.API.HandbookApi;
import com.example.familyhealthhandbook.API.HandbookClient;

public class Utils {
    public static HandbookApi getApi() {
        return HandbookClient.getHandbookClient().create(HandbookApi.class);
    }

    public static AlertDialog showDialogMessage(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).show();
        if (alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        return alertDialog;
    }
}
