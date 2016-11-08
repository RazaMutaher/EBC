package com.English.BolChal.Urdu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MoviesAdapter2 mAdapter;
    DBHandler handler2;
    private ArrayList<Movie2> dataList2;
    int id_fromA;
    ImageView imageView;
    ProgressBar progressBar;
    Context context;
    public String imageName;
    String imageUrl;
    String name;
    String transition;
    String transitionUrl;
    String actionAfterClick;
    String actionType;
    LinearLayout layScreen2;
    Tracker mTracker;
    //GettingTransition gt;
    Vibrator vibe;
    String msg;
    Movie2 m;

    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();


        vibe = (Vibrator) MainActivity2.this.getSystemService(Context.VIBRATOR_SERVICE);


        dataList2 = new ArrayList<Movie2>();
        new GettingImages().execute();
        //GettingTransition gt = new GettingTransition(getApplicationContext());
        NetworkUtils utils2 = new NetworkUtils(MainActivity2.this);

      //  if (utils2.isConnectingToInternet()){
       //     gt = new GettingTransition(getApplicationContext());
      //      gt.execute();
       // }

        Intent intent = getIntent();
        id_fromA = intent.getIntExtra("Id", 1);

        //Toast.makeText(getApplicationContext(), "ID is " + id_fromA, Toast.LENGTH_SHORT).show();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        handler2 = new DBHandler(this);
        NetworkUtils utils = new NetworkUtils(MainActivity2.this);
        //if (handler2.getDataCountB() == 0 && utils.isConnectingToInternet()) {
        if ( utils.isConnectingToInternet()) {
            handler2.deleteAllfromB();
            new DataFetcherTask().execute();
            // Toast.makeText(getApplicationContext(), "Count is " + handler2.getDataCountB(), Toast.LENGTH_SHORT).show();
        } else {
            dataList2 = handler2.getSomeDataB(id_fromA);
            mAdapter = new MoviesAdapter2(MainActivity2.this, dataList2);
            recyclerView.setAdapter(mAdapter);
        }

        Log.i("Activity Name: ", "Second");
        mTracker.setScreenName("Listing B");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());





        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie2 movie2 = dataList2.get(position);
                msg = movie2.getListingB();

                int idd = handler2.getDb1id2(movie2.getListingB());

                name = handler2.getFileName(movie2.getListingB());

            }

            @Override
            public void onLongClick(View view, int position) {
                if(mPlayer!=null && mPlayer.isPlaying()){
                    mPlayer.stop();
                }
            }
        }
        ));

    }


    @Override
    public void onResume()
    {
        super.onResume();

    }



    public void usingPicasso(String completeUrl) {
        imageView = (ImageView) findViewById(R.id.image2);
        context = imageView.getContext();
        //String currentUrl = "http://i.imgur.com/DvpvklR.png";
        //String currentUrl = "http://sree.cc/wp-content/uploads/schogini_team.png";
        progressBar = (ProgressBar) findViewById(R.id.loading2);

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


    public void soundFunction(View view) {

        String url = "http://englishbolchal.com/admin3313/assets/uploads/" + name;

        //Toast.makeText(getApplicationContext(),url , Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),movie2.getListingB() , Toast.LENGTH_SHORT).show();


        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        vibe.vibrate(80);
        try {
            mPlayer.setDataSource(url);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "Please check your internet connectivity!", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "Please check your internet connectivity!", Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "Please check your internet connectivity!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "Please check your internet connectivity!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Please check your internet connectivity!", Toast.LENGTH_SHORT).show();
        }
        mPlayer.start();

    }

    public void sendMessage(View view)
    {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        //msg = m.getListingB();
        smsIntent.putExtra("sms_body",msg);
        startActivity(smsIntent);
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




  /*  class GettingTransition extends AsyncTask<Void, Void, String> {
        Context context;

        public GettingTransition(Context context) {
            this.context = context.getApplicationContext();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String serverData = null;// String object to store fetched data from server

            try {
                URL url = new URL("http://englishbolchal.com/admin3313/home/json_transition");

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
                    JSONArray jsonArray = jsonObject.getJSONArray("transition");
                    JSONObject jsonObjectCity = jsonArray.getJSONObject(0);
                    String image = jsonObjectCity.getString("image");
                    transition = image;


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

            transitionUrl = "http://englishbolchal.com/admin3313/assets/images/" + transition;

            class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
                ImageView bmImage;

                public DownloadImageTask(ImageView bmImage) {
                    this.bmImage = bmImage;
                }

                protected Bitmap doInBackground(String... urls) {
                    String urldisplay = urls[0];
                    Bitmap mIcon11 = null;
                    try {
                        InputStream in = new java.net.URL(urldisplay).openStream();
                        mIcon11 = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }
                    return mIcon11;
                }

                protected void onPostExecute(Bitmap result) {
                    bmImage.setImageBitmap(result);
                }
            }

            //usingPicassoTransition(transitionUrl);
            final Dialog dialog = new Dialog(MainActivity2.this);
            dialog.setContentView(R.layout.transition);
            dialog.setTitle("English Bol Chal");
            dialog.setCancelable(true);

            new DownloadImageTask((ImageView) dialog.findViewById(R.id.ImageViewTransition))
                    .execute(transitionUrl);

            Button button = (Button) dialog.findViewById(R.id.Button01);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            //now that the dialog is set up, it's time to show it
            dialog.show();
        }
    } */

    @Override
    public void onBackPressed()
    {
        //if (gt != null)
         //   gt.cancel(true);
        if(mPlayer.isPlaying()){
            mPlayer.stop();
        }


        super.onBackPressed();
    }



    class GettingImages extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String serverData = null;// String object to store fetched data from server

            try {
                URL url = new URL("http://englishbolchal.com/admin3313/home/json_listing_b_campaign");

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
                    JSONArray jsonArray = jsonObject.getJSONArray("b_campaigns");
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

            layScreen2 = (LinearLayout)findViewById(R.id.buttonBar2);
            layScreen2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Advertisement 2")
                            .setAction("Clicked")
                            .build());
                    if (actionType == null)
                    {
                        actionType = "owais";
                    }

                    NetworkUtils utils = new NetworkUtils(MainActivity2.this);
                    if (utils.isConnectingToInternet()){

                        switch (actionType)
                        {
                            case "site_link":
                                if (!actionAfterClick.startsWith("http://") && !actionAfterClick.startsWith("https://")) {
                                    actionAfterClick = "http://" + actionAfterClick;
                                }
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
                    }
                    else
                    {
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
                //URL url = new URL("http://beta.json-generator.com/api/json/get/GAqnlDN");
                URL url = new URL("http://englishbolchal.com/admin3313/home/json_category_items");

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

// Http Request Code end
// Json Parsing Code Start
                try {
                    dataList2 = new ArrayList<Movie2>();
                    JSONObject jsonObject = new JSONObject(serverData);
                    JSONArray jsonArray = jsonObject.getJSONArray("b_categories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectCity = jsonArray.getJSONObject(i);
                        int id = jsonObjectCity.getInt("id");
                        String english = jsonObjectCity.getString("english");
                        String trans = jsonObjectCity.getString("translitation");
                        String urdu = jsonObjectCity.getString("urdu");
                        String fileName = jsonObjectCity.getString("file_name");
                        int listing_id_A = jsonObjectCity.getInt("listing_a_id");

                        Movie2 movie = new Movie2();

                        movie.setIdB(id);
                        movie.setListingB(english);
                        movie.setTransliteration(trans);
                        movie.setUrduB(urdu);
                        movie.setFileName(fileName);
                        movie.setListing_id_A(listing_id_A);
                        handler2.addDataB(movie);
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
            dataList2 = handler2.getSomeDataB(id_fromA);

            mAdapter = new MoviesAdapter2(MainActivity2.this, dataList2);
            recyclerView.setAdapter(mAdapter);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity2.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity2.ClickListener clickListener) {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            //Intent i = new Intent(this, MainActivity.class);
            //startActivity(i);
            //finish();
            if(mPlayer!=null && mPlayer.isPlaying()){
                mPlayer.stop();
            }
            onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
