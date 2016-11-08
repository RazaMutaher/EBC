package com.English.BolChal.Urdu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Owais Nizami on 3/31/2016.
 */

public class SoundListener extends AppCompatActivity implements View.OnClickListener{
    ImageView speakerIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_row2);
        speakerIcon = (ImageView)findViewById(R.id.speaker);
        speakerIcon.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


   /* public void soundFunction(View view) {
        Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }*/
}