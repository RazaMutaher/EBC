package com.English.BolChal.Urdu;

/**
 * Created by Owais Nizami on 3/17/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper implements MovieListener {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "EbcDatabase.db";

    private static final String TABLE_NAME1 = "activity1_table";
    private static final String TABLE_NAME2 = "activity2_table";

    private static final String KEY_ID_tbl1 = "IdA";
    private static final String KEY_LISTING1 = "ListingA";
    private static final String KEY_URDU1 = "UrduA";


    private static final String KEY_ID_tbl2 = "IdB";
    private static final String KEY_LISTING2 = "ListingB";
    private static final String KEY_URDU2 = "UrduB";
    private static final String KEY_FILE = "FileName";
    private static final String KEY_TRANS = "TransB";
    private static final String KEY_LISTING_ID_A = "Listing_id_A";

    String CREATE_TABLE1 = "CREATE TABLE " + TABLE_NAME1 + " (" + KEY_ID_tbl1 + " INTEGER," + KEY_LISTING1 + " TEXT," + KEY_URDU1 + " TEXT)";
    String DROP_TABLE1 = "DROP TABLE IF EXISTS " + TABLE_NAME1;

    String CREATE_TABLE2 = "CREATE TABLE " + TABLE_NAME2 + " (" + KEY_ID_tbl2 + " INTEGER," + KEY_LISTING2 + " TEXT," + KEY_URDU2 + " TEXT," + KEY_TRANS + " TEXT," + KEY_LISTING_ID_A + " INTEGER," + KEY_FILE + " TEXT)";
    String DROP_TABLE2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;


    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE1);
        db.execSQL(CREATE_TABLE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE1);
        db.execSQL(DROP_TABLE2);

        onCreate(db);
    }

    @Override
    public void addDataA(Movie city) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID_tbl1, city.getId());
            values.put(KEY_LISTING1, city.getListing());
            values.put(KEY_URDU1, city.getUrdu());
            db.insert(TABLE_NAME1, null, values);
            db.close();
        } catch (Exception e) {
            Log.e("problem", e + "");
        }
    }

    @Override
    public void addDataB(Movie2 city) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID_tbl2, city.getIdB());
            values.put(KEY_LISTING2, city.getListingB());
            values.put(KEY_URDU2, city.getUrduB());
            values.put(KEY_TRANS, city.getTransliteration());
            values.put(KEY_FILE, city.getFileName());
            values.put(KEY_LISTING_ID_A, city.getListing_id_A());

            db.insert(TABLE_NAME2, null, values);
            db.close();
        } catch (Exception e) {
            Log.e("problem", e + "");
        }
    }

    @Override
    public ArrayList<Movie> getAllDataA() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Movie> dataList = null;
        try {
            dataList = new ArrayList<Movie>();
            String QUERY = "SELECT * FROM " + TABLE_NAME1;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    Movie city = new Movie();
                    //city.setId(cursor.getInt(0));
                    city.setListing(cursor.getString(1));
                    city.setUrdu(cursor.getString(2));
                    dataList.add(city);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return dataList;
    }

    public int getDb1id(String english) {
        SQLiteDatabase db = this.getReadableDatabase();
        // String QUERY = "SELECT "+ KEY_ID_tbl1 + " FROM " +TABLE_NAME1 + " WHERE " + KEY_LISTING1+ " = " + english ;
        int userId = 1;

        try {

            //Cursor c1 = db.rawQuery("SELECT * FROM tbl1 WHERE name = ?", new String[]{english});

            Cursor c = db.rawQuery("SELECT " + KEY_ID_tbl1 + " FROM " + TABLE_NAME1 + " WHERE " + KEY_LISTING1 + " = ?", new String[]{english});
            if (c.moveToFirst()) {
                userId = c.getInt(0);
            }

            return userId;
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return 0;
    }

    public void deleteAllfromA()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_NAME1,null,null);
            db.execSQL("delete from "+ TABLE_NAME1);
            db.close();
        } catch (Exception e) {
            Log.e("problem", e + "");
        }
    }

    public void deleteAllfromB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_NAME2,null,null);
            db.execSQL("delete from "+ TABLE_NAME2);
            db.close();
        } catch (Exception e) {
            Log.e("problem", e + "");
        }
    }

    public int getDb1id2(String english) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = 1;

        try {

            Cursor c = db.rawQuery("SELECT " + KEY_ID_tbl2 + " FROM " + TABLE_NAME2 + " WHERE " + KEY_LISTING2 + " = ?", new String[]{english});
            if (c.moveToFirst()) {
                userId = c.getInt(0);
            }
            return userId;
        } catch (Exception e) {
            Log.e("error", e + "");
        }

        return 0;
    }

    @Override
    public ArrayList<Movie2> getAllDataB() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Movie2> dataList = null;
        try {
            dataList = new ArrayList<Movie2>();
            String QUERY = "SELECT * FROM " + TABLE_NAME2;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    Movie2 city = new Movie2();
                    //city.setId(cursor.getInt(0));
                    city.setListingB(cursor.getString(1));
                    city.setUrduB(cursor.getString(2));
                    city.setTransliteration(cursor.getString(3));

                    dataList.add(city);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return dataList;
    }

    @Override
    public ArrayList<Movie2> getSomeDataB(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Movie2> dataList = null;
        try {
            dataList = new ArrayList<Movie2>();
            String QUERY = "SELECT * FROM " + TABLE_NAME2 + " where " + KEY_LISTING_ID_A + "=" + id;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    Movie2 city = new Movie2();
                    //city.setId(cursor.getInt(0));
                    city.setListingB(cursor.getString(1));
                    city.setUrduB(cursor.getString(2));
                    city.setTransliteration(cursor.getString(3));

                    dataList.add(city);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return dataList;
    }


    @Override
    public int getDataCount() {
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String QUERY = "SELECT * FROM " + TABLE_NAME1;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            db.close();
            return num;
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return 0;
    }

    @Override
    public int getDataCountB() {
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String QUERY = "SELECT * FROM " + TABLE_NAME2;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            db.close();
            return num;
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return 0;
    }

    public String getSoundFileName(int id) {
        String name = "owais";
        String idToString;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            //String QUERY = "SELECT " + KEY_FILE + " FROM " + TABLE_NAME2 + " where " + KEY_ID_tbl2 + "=" + id;
            //String QUERY = "Select " + KEY_FILE + "from " + TABLE_NAME2  + " where " + KEY_ID_tbl2 + " = ?";

            Cursor c = db.rawQuery("Select " + KEY_FILE + "from " + TABLE_NAME2  + " where " + KEY_ID_tbl2 + " = ?", new String[]{name});

            //Cursor c = db.rawQuery(QUERY, null);
            //Cursor c = db.rawQuery(QUERY, new int[] {id});

            // if (c.moveToFirst()) {
            c.moveToFirst();
            name = c.getString(0);
            //}
            return name;
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return name;
    }


    public String getFileName(String english) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = "owa";
        String name2 = english;

        try {

            Cursor c = db.rawQuery("SELECT " + KEY_FILE + " FROM " + TABLE_NAME2 + " WHERE " + KEY_LISTING2 + " = ?", new String[]{english});
            if (c.moveToFirst()) {
                c.moveToFirst();
                name = c.getString(0);
            }
            return name;
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return name;
    }
}