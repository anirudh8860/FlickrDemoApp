package simplegamer003.flickrdemoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    EditText search;
    Button searchBtn;
    ImageGridViewAdapter imageGridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        gridView = (GridView)findViewById(R.id.image_grid);
        initializeComponents();

        search = (EditText)findViewById(R.id.search_bar);
        searchBtn = (Button)findViewById(R.id.search_image_btn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void initializeComponents() {
        Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        gridView.setNumColumns(p.x / p.y);
        float spacing = 5;
        gridView.setPadding((int) spacing, (int) spacing, (int) spacing, (int) spacing);
        gridView.setVerticalSpacing((int) spacing);
        gridView.setHorizontalSpacing((int) spacing);
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

    class LoadImages extends AsyncTask<String, Integer, List>{
        private ProgressDialog progressDialog;

        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading images from Flickr. Please wait...");
            progressDialog.show();
        }

        protected void onProgressUpdate(Integer... values){
            //super.onProgressUpdate(values);
            //progressDialog.setMessage(String.format("Loading images from Flickr %s/%s. Please wait...", values[0], values[1]));
        }

        @Override
        protected List doInBackground(String... params) {
            return null;
        }

        protected void onPostExecute(List l){
            progressDialog.dismiss();
            gridView.setAdapter(imageGridViewAdapter);
            super.onPostExecute(l);
        }
    }
}

