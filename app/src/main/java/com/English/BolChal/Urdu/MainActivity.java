package com.English.BolChal.Urdu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Movie> dataList;
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    DBHandler handler;

    NavigationView navigationView = null;
    Toolbar toolbar = null;


    ImageView imageView;
    ProgressBar progressBar;
    Context context;
    public String imageName;
    String imageUrl;

    String actionAfterClick;
    String actionType;
    LinearLayout laySCreen1;
    Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataList = new ArrayList<Movie>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();


        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new GettingImages().execute();

        handler = new DBHandler(this);

        NetworkUtils utils = new NetworkUtils(MainActivity.this);
        // if (handler.getDataCount() == 0 && utils.isConnectingToInternet()) {
        if ( utils.isConnectingToInternet()) {
            handler.deleteAllfromA();
            new DataFetcherTask().execute();
        } else {
            //Toast.makeText(getApplicationContext(), "Count is " + handler.getDataCount(), Toast.LENGTH_SHORT).show();
            dataList = handler.getAllDataA();
            mAdapter = new MoviesAdapter(MainActivity.this, dataList);
            recyclerView.setAdapter(mAdapter);
            /*if(dataList == null)
            {
                Toast.makeText(getApplicationContext(), "No Data to display. Connect to internet please ", Toast.LENGTH_LONG).show();
            }*/
        }


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Movie movie = dataList.get(position);
                Intent i = new Intent(view.getContext(), MainActivity2.class);
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Category")
                        .setAction(movie.getListing())
                        .build());
                int idd = handler.getDb1id(movie.getListing());
                i.putExtra("Id", idd);
                startActivity(i);

                Toast.makeText(getApplicationContext(), movie.getListing() + " is selected!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }
        ));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.i("Activity Name: ", "first");
        mTracker.setScreenName("listing A");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }


    public void usingPicasso(String completeUrl) {
        imageView = (ImageView) findViewById(R.id.image);
        context = imageView.getContext();
        progressBar = (ProgressBar) findViewById(R.id.loading);

        Picasso.with(context)
                .load(completeUrl)
                .error(R.drawable.splash_img)
                .into(imageView, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                    }
                });

        Picasso.with(context)
                .load(completeUrl)
                .placeholder(R.drawable.splash_img)
                .error(R.drawable.splash_img)
                .resize(50, 50)
                .into(target);

    }


    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/actress_wallpaper.jpg");
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            if (placeHolderDrawable != null) {
            }
        }
    };


    class GettingImages extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String serverData = null;// String object to store fetched data from server

            try {
                URL url = new URL("http://englishbolchal.com/admin3313/home/json_listing_a_campaign");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();

                os.flush();
                os.close();

                String TAG = "Response";
                int responseCode = conn.getResponseCode();
                Log.i(TAG, "POST Response Code :: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // print result
                    Log.i(TAG, response.toString());
                    serverData = response.toString();
                }

                try {
                    JSONObject jsonObject = new JSONObject(serverData);
                    JSONArray jsonArray = jsonObject.getJSONArray("a_campaigns");
                    JSONObject jsonObjectCity = jsonArray.getJSONObject(0);
                    String image = jsonObjectCity.getString("image");
                    String action = jsonObjectCity.getString("action_after_click");
                    String actionOnClick = jsonObjectCity.getString("action_on_click");
                    actionAfterClick = action;
                    actionType = actionOnClick;
                    imageName = image;


                } catch (JSONException e) {
                    e.printStackTrace();
                }
//Json Parsing code end
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            imageUrl = "http://englishbolchal.com/admin3313/assets/images/" + imageName;
            usingPicasso(imageUrl);

            laySCreen1 = (LinearLayout) findViewById(R.id.buttonBar);
            laySCreen1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Advertisement 1")
                            .setAction("Add Clicked")
                            .build());
                    if (actionType == null)
                    {
                        actionType = "owais";
                    }

                    NetworkUtils utils = new NetworkUtils(MainActivity.this);
                    if (utils.isConnectingToInternet()) {

                        switch (actionType) {
                            case "site_link":
                                if (!actionAfterClick.startsWith("http://") && !actionAfterClick.startsWith("https://")) {
                                    actionAfterClick = "http://" + actionAfterClick;
                                }
                                //actionAfterClick = "https://play.google.com/store/apps/details?id=com.whatsapp&hl=en";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(actionAfterClick));
                                startActivity(intent);
                                break;

                            case "number":
                                Intent intent2 = new Intent(Intent.ACTION_DIAL);
                                intent2.setData(Uri.parse("tel:" + actionAfterClick));
                                startActivity(intent2);
                                break;

                            case "play_store_link":
                                if (!actionAfterClick.startsWith("http://") && !actionAfterClick.startsWith("https://")) {
                                    actionAfterClick = "http://" + actionAfterClick;
                                }
                                Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(actionAfterClick));
                                startActivity(intent3);
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "TO LET Contact # 03004235505", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connectivity!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }


    class DataFetcherTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String serverData = null;// String object to store fetched data from server

            try {
                URL url = new URL("http://englishbolchal.com/admin3313/home/json_categories");

                //String POST_PARAMS = "email=" + params[0] + "&password=" + params[1];
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();

                //os.write(POST_PARAMS.getBytes());
                os.flush();
                os.close();

                String TAG = "Response";
                int responseCode = conn.getResponseCode();
                Log.i(TAG, "POST Response Code :: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // print result
                    Log.i(TAG, response.toString());
                    serverData = response.toString();
                }

                try {
                    dataList = new ArrayList<Movie>();
                    JSONObject jsonObject = new JSONObject(serverData);
                    JSONArray jsonArray = jsonObject.getJSONArray("a_categories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectCity = jsonArray.getJSONObject(i);
                        String listing = jsonObjectCity.getString("listings");
                        String urdu = jsonObjectCity.getString("urdu");
                        int id = jsonObjectCity.getInt("id");
                        Movie movie = new Movie();
                        movie.setListing(listing);
                        movie.setUrdu(urdu);
                        movie.setId(id);
                        handler.addDataA(movie);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//Json Parsing code end
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dataList = handler.getAllDataA();
            mAdapter = new MoviesAdapter(MainActivity.this, dataList);
            recyclerView.setAdapter(mAdapter);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {

            // Handle the camera action
        } else if (id == R.id.about_us) {
            Intent i = new Intent(this,About.class);
            startActivity(i);

        } else if (id == R.id.more_apps) {

            Uri uri = Uri.parse("https://play.google.com/store?hl=en");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        } else if (id == R.id.share) {
            Uri uri = Uri.parse("https://www.facebook.com/English-Bol-Chal-1058963334154996/?fref=ts");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
