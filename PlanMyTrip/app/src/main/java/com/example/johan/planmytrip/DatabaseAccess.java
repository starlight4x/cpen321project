package com.example.johan.planmytrip;

/**
 * Created by james on 01/11/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    //private String route_num = "099";
    private String route_id;
    private String trip_id;
    private String stop_id;
    private String prev_id = "";

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {

        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {

        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getStops(String route_num) {
        List<String> list = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT * FROM routes WHERE route_short_name LIKE '%" + route_num + "%'", null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    route_id = cursor.getString(0);
                    Log.d("route id", route_id);
                }
            } finally {
                Log.d("route id", "error");
                cursor.close();
            }
        }

        cursor = database.rawQuery("SELECT * FROM trips WHERE route_id=" + route_id, null);
        if (cursor != null) {
            try {
                if (cursor.moveToPosition(2)) {
                    trip_id = cursor.getString(2);
                    Log.d("trip id", trip_id);
                }
            } finally {
                cursor.close();
            }
        }
        cursor = database.rawQuery("SELECT * FROM stop_times WHERE trip_id=" + trip_id, null);
        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                stop_id = cursor.getString(3);
                Log.d("stop id", stop_id);
                Cursor c2 = database.rawQuery("SELECT * FROM stops WHERE stop_id=" + stop_id, null);
                if (c2 != null) {
                    try {
                        if (c2.moveToFirst()) {
                            if(c2.getString(1) != prev_id) {
                                list.add(c2.getString(2));
                                prev_id = c2.getString(1);
                            }
                        }
                    } finally {
                        c2.close();
                    }
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        else
            Log.d("SQLite error", "bad query");
        return list;
    }
}