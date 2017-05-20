package simplegamer003.flickrdemoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Anirudh on 19-05-2017.
 */

class ImageGridViewAdapter extends ArrayAdapter {
    int imageTotal = 20;
    private Context context;
    private LayoutInflater inflater;

    private String[] imageUrls;

    public ImageGridViewAdapter(Context context, String[] imageUrls) {
        super(context, R.layout.image_layout, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.image_layout,parent, false);
        }
        Picasso.with(context).load(imageUrls[position]).placeholder(R.drawable.loader).fit().centerCrop().into((ImageView)convertView);

        return convertView;
    }

}
