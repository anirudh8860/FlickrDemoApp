package simplegamer003.flickrdemoapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    EditText search;
    Button searchBtn;
    int imageTotal = 20;
    String flickrsearch = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ee3b2f1b3dc6dee8786f9e3d481315c9&text=";
    String searchFor;
    String json = "&format=json";
    String limit = "&per_page=20";
    String[] id = new String[imageTotal];
    int[] farm = new int[imageTotal];
    int[] server = new int[imageTotal];
    String[] secret = new String[imageTotal];
    String[] urls = new String[imageTotal];
    private static final int LOAD_IMAGE = 100;
    File fileUri;


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
            public void onClick(View view) {
                searchFor = search.getText().toString();
                flickrsearch = flickrsearch+searchFor+json+limit;
                Log.d("URL",flickrsearch);
                if (hasInternetConnection())
                    new LoadImages().execute();
                else Snackbar.make(view, "Intenet not connected", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });



        FloatingActionButton select = (FloatingActionButton) findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto,0);
            }
        });

        FloatingActionButton upload = (FloatingActionButton) findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileUri == null) {
                    Toast.makeText(getApplicationContext(), "Please pick photo", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("Filepath", String.valueOf(fileUri));

                Intent intent = new Intent(getApplicationContext(), FlickrjActivity.class);
                intent.putExtra("flickImagePath", fileUri.getAbsolutePath());
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try {
            switch (requestCode) {
                case 0:
                    Uri selectedImage1 = imageReturnedIntent.getData();
                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage1);
                    Uri tempUri = getImageUri(getApplicationContext(),bitmap1);
                    fileUri = new File(getRealPathFromURI(tempUri));
                    break;
            }
        }
        catch (Exception e) {
            Log.e("Filepickerror",e.toString());
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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

    public  boolean hasInternetConnection(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            connected = true;
        return connected;
    }
}

