package com.English.BolChal.Urdu;

/**
 * Created by Owais Nizami on 3/8/2016.
 */
public class Movie {
    private String listing, urdu;
    private int id;

    public Movie() {
    }

    public Movie(int id, String listing, String urdu)
    {
        this.id = id;
        this.listing = listing;
        this.urdu = urdu;
    }

    public Movie(String listing, String urdu) {
        this.listing = listing;
        this.urdu = urdu;
    }

    public void setId(int id){this.id = id;}

    public int getId(){return id;}

    public String getListing() {
        return listing;
    }

    public void setListing(String name) {
        this.listing = name;
    }

    public String getUrdu() {
        return urdu;
    }

    public void setUrdu(String urdu) {
        this.urdu = urdu;
    }
}