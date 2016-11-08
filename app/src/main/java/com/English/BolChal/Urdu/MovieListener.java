package com.English.BolChal.Urdu;

/**
 * Created by Owais Nizami on 3/17/2016.
 */
import java.util.ArrayList;

public interface MovieListener {

    public void addDataA(Movie movie);

    public void addDataB(Movie2 movie);

    public ArrayList<Movie> getAllDataA();

    public ArrayList<Movie2> getAllDataB();

    public ArrayList<Movie2> getSomeDataB(int id);

    public int getDataCount();

    public int getDataCountB();
}