package simplegamer003.flickrdemoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    EditText search;
    Button searchBtn;
    int imageTotal = 20;
    ImageGridViewAdapter imageGridViewAdapter;
    String flickrsearch = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ce6897bab612b7d7b1d9a1c3e18d242c&text=";
    String flickrlogin = "http://api.flickr.com/services/rest/?method=flickr.test.login&api_key=%s";
    String flickr_photosets_getPhotos = "https://www.flickr.com/services/rest/?method=flickr.photosets.getPhotos&format=%s&api_key=%s&photoset_id=%s";
    String flickr_photos_getSizes = "https://www.flickr.com/services/rest/?method=flickr.photos.getSizes&format=%s&api_key=%s&photo_id=%s";
    String flickrapikey = "ce6897bab612b7d7b1d9a1c3e18d242c";
    String flickrsecret = "4b9a6139e420111f";
    String searchFor;
    String json = "&format=json";
    String limit = "&per_page=20";
    String[] id = new String[imageTotal];
    int[] farm = new int[imageTotal];
    int[] server = new int[imageTotal];
    String[] secret = new String[imageTotal];
    String[] urls = new String[imageTotal];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gridView = (GridView)findViewById(R.id.image_grid);

        search = (EditText)findViewById(R.id.search_bar);
        searchBtn = (Button)findViewById(R.id.search_image_btn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFor = search.getText().toString();
                flickrsearch = flickrsearch+searchFor+json+limit;
                Log.d("URL",flickrsearch);
                new LoadImages().execute();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.upload);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent,0);
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadImages extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;

        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading images from Flickr. Please wait...");
            progressDialog.show();
        }

        protected void onProgressUpdate(Void... params){
            super.onProgressUpdate();
            progressDialog.setMessage(String.format("Loading images from Flickr. Please wait..."));
        }

        protected Void doInBackground(Void... params) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(flickrsearch);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }

            String urli = result.toString();
            String req = urli.substring(86,urli.length()-1);
            JSONArray photos;
            try {
                photos = new JSONArray(req);
                for (int i = 0;  i < imageTotal; i++) {
                    JSONObject photo = photos.getJSONObject(i);
                    id[i] = photo.getString("id");
                    farm[i] = photo.getInt("farm");
                    server[i] = photo.getInt("server");
                    secret[i] = photo.getString("secret");
                    /*Log.d("id", Double.toString(id[i]));
                    Log.d("farm", Integer.toString(farm[i]));
                    Log.d("server", Integer.toString(server[i]));
                    Log.d("secret", secret[i]);*/
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("JSON",req);
            makeUrls();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            progressDialog.dismiss();
            gridView.setAdapter(new ImageGridViewAdapter(MainActivity.this,urls));
        }
    }

    private void makeUrls() {
        for (int i = 0; i < imageTotal; i++){
            String idstr = id[i];
            String farmstr = String.valueOf(farm[i]);
            String serverstr = String.valueOf(server[i]);
            String secretstr = secret[i];
            urls[i] = "http://farm"+farmstr+".staticflickr.com/"+serverstr+"/"+idstr+"_"+secretstr+".jpg";
            Log.d("ImageURL", urls[i]);
        }
    }
}

