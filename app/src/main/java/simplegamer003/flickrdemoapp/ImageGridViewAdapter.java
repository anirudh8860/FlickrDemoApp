package simplegamer003.flickrdemoapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Anirudh on 19-05-2017.
 */

class ImageGridViewAdapter extends BaseAdapter{
    private Context mContext;
    int imageTotal = 20;
    public static String[] urls = new String[20];

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
            MainActivity m = new MainActivity();
            for (int i = 0; i < imageTotal; i++){
                String idstr = String.valueOf(m.id[i]);
                String farmstr = String.valueOf(m.farm[i]);
                String serverstr = String.valueOf(m.server[i]);
                String secretstr = m.secret[i];
                urls[i] = "http://farm"+farmstr+".staticflickr.com/"+serverstr+"/"+idstr+"_"+secretstr+".jpg";
                Log.d("ImageURL", urls[i]);
            }
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        for (int i = 0; i < imageTotal; i++)
            Picasso.with(mContext).load(urls[i]).resize(50,50).placeholder(R.drawable.loader).fit().centerCrop().into(imageView);

        return imageView;
    }
}
