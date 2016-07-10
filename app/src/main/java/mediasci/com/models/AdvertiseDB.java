package mediasci.com.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.Currency;

import mediasci.com.Util.DBUtil;

/**
 * Created by sara on 7/4/2016.
 */
public class AdvertiseDB {

    public static void InsertAdvertise(Context context, Advertise advertise) {

        SQLiteDatabase db = DBUtil.GetDB(context);
        try {

            String query = "insert or replace into advertise(gps_address,address,title," +
                    "type,revise,note,img,lat,lng) values(?,?,?,?,?,?,?,?,?)";

            SQLiteStatement insertState = db.compileStatement(query);
            insertState.clearBindings();
            insertState.bindString(1, advertise.getGps_address());
            insertState.bindString(2, advertise.getAddress());
            insertState.bindString(3, advertise.getTitle());
            insertState.bindLong(4, advertise.getType());
            insertState.bindLong(5, advertise.getRevise());
            insertState.bindString(6, advertise.getNote());
            insertState.bindBlob(7, advertise.getImg());
            insertState.bindDouble(8, advertise.getLatitude());
            insertState.bindDouble(9, advertise.getLongitude());

            insertState.executeInsert();
            insertState.clearBindings();

            Log.e("insert", "done");
        } catch (Exception e) {
            Log.e("insert_error", e + "");
        }

        DBUtil.CloseDb(db);
    }

    public static ArrayList<Advertise> GetAdvertise(Context context) {
        ArrayList<Advertise> lst_ads = new ArrayList<>();
        Advertise advertise;
        SQLiteDatabase db = DBUtil.GetDB(context);
        try {
            String query = "select * from advertise";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    advertise = new Advertise();
                    advertise.setGps_address(cursor.
                            getString(cursor.getColumnIndex("gps_address")));
                    advertise.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    advertise.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    advertise.setType(cursor.getInt(cursor.getColumnIndex("type")));
                    advertise.setRevise(cursor.getInt(cursor.getColumnIndex("revise")));
                    advertise.setNote(cursor.getString(cursor.getColumnIndex("note")));
                    advertise.setImg(cursor.getBlob(cursor.getColumnIndex("img")));
                    advertise.setLatitude(cursor.getDouble(cursor.getColumnIndex("lat")));
                    advertise.setLongitude(cursor.getDouble(cursor.getColumnIndex("lng")));

                    lst_ads.add(advertise);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("get_ads_error", e + "");
        }
        return lst_ads;
    }
}
