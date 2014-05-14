package by.deniotokiari.irc.client.irc.client.adapter;

import android.content.Context;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ImageView;

public class CommonCursorAdapter extends SimpleCursorAdapter {

    public CommonCursorAdapter(Context context, int layout, String[] from, int[] to, ViewBinder viewBinder) {
        super(context, layout, null, from, to, 2);

        setViewBinder(viewBinder);
    }

    @Override
    public void setViewImage(ImageView v, String value) {
        super.setViewImage(v, value);
    }

}
