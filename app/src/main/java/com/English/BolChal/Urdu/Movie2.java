package com.English.BolChal.Urdu;

/**
 * Created by Owais Nizami on 3/9/2016.
 */
public class Movie2 {

    //private String listingB, transliteration, urduB;
    private String listingB , transliteration, urduB, fileName;
    private int idB,listing_id_A;

    public Movie2() {
    }

    public Movie2(String listing, String transliteration, String urduB) {
        this.listingB = listing;
        this.transliteration = transliteration;
        this.urduB = urduB;
    }

    public Movie2(String listing, String transliteration, String urduB, String fileName, int listing_id_A) {
        this.listingB = listing;
        this.transliteration = transliteration;
        this.urduB = urduB;
        this.fileName = fileName;
        this.listing_id_A = listing_id_A;
    }

    public Movie2(int id, String listing, String transliteration, String urduB, String fileName, int listing_id_A) {
        this.idB = id;
        this.listingB = listing;
        this.transliteration = transliteration;
        this.urduB = urduB;
        this.fileName = fileName;
        this.listing_id_A = listing_id_A;
    }

    public int getListing_id_A() {
        return listing_id_A;
    }

    public void setListing_id_A(int listing_id_A) {
        this.listing_id_A = listing_id_A;
    }

    public void setIdB(int idB) {
        this.idB = idB;
    }

    public int getIdB() {
        return idB;
    }

    public String getListingB() {
        return listingB;
    }

    public void setListingB(String name) {
        this.listingB = name;
    }

    public String getUrduB() {
        return urduB;
    }

    public void setUrduB(String urduB) {
        this.urduB = urduB;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName(){return fileName;}

}
