package by.deniotokiari.irc.client.irc.client.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogHelper {

    public static void show(Context context, int sections, int title, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (title != 0) {
            builder.setTitle(title);
        }

        builder.setItems(sections, listener);
        builder.setCancelable(true);

        builder.create().show();
    }

}
