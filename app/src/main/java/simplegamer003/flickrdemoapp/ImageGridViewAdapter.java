package simplegamer003.flickrdemoapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Anirudh on 19-05-2017.
 */

class ImageGridViewAdapter extends BaseAdapter{
    private Context mContext;
    int imageTotal = 25;
    public static String[] urls = new String[25];

    public ImageGridViewAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return imageTotal;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        return imageView;
    }
}
