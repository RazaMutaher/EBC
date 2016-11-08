package com.English.BolChal.Urdu;

/**
 * Created by Owais Nizami on 3/8/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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

public class Splashscreen extends Activity {


    ImageView imageView;
    ProgressBar progressBar;

    Context context;
    public String imageName;
    String imageUrl;


    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);

    }
    /** Called when the activity is first created. */
    Thread splashTread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new GettingImages().execute();

        StartAnimations();
    }


    public void usingPicasso(String completeUrl) {
        imageView = (ImageView) findViewById(R.id.splash);
        context = imageView.getContext();
        //String currentUrl = "http://sree.cc/wp-content/uploads/schogini_team.png";
        progressBar = (ProgressBar) findViewById(R.id.loadingSplash);

        Picasso.with(context)
               // .load(completeUrl)
                .load(R.drawable.splash_img)
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
                URL url = new URL("http://englishbolchal.com/admin3313/home/json_splash_screens");

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
                    JSONArray jsonArray = jsonObject.getJSONArray("splash_screens");
                    JSONObject jsonObjectCity = jsonArray.getJSONObject(0);
                    String image = jsonObjectCity.getString("image");
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
        }
    }




    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(Splashscreen.this,
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Splashscreen.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Splashscreen.this.finish();
                }

            }
        };
        splashTread.start();

    }
}