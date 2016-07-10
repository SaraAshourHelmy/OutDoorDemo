package mediasci.com.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.InputStream;

import mediasci.com.models.Advertise;
import mediasci.com.outdoordemo.AdvertiseActivity;
import mediasci.com.outdoordemo.R;

/**
 * Created by sara on 7/4/2016.
 */
public class DBUtil {

    public static final String DBName = "outdoor.sqlite";

    public static void SetupDatabase(Context context) {

        InputStream inputStream = context.getResources().openRawResource(R.raw.outdoor);
        SQLiteDatabase db = context.openOrCreateDatabase("test", context.MODE_PRIVATE, null);

        try {

            byte[] db_bytes = new byte[inputStream.available()];
            inputStream.read(db_bytes);
            FileOutputStream outputStream = new FileOutputStream("/data/data/" +
                    context.getPackageName() + "/databases/" + DBName);
            outputStream.write(db_bytes);
            outputStream.flush();
            outputStream.close();
            Log.e("db", "created");

        } catch (Exception e) {
            Log.e("db_error", e + "");
        }
    }

    public static SQLiteDatabase GetDB(Context context) {

        return context.openOrCreateDatabase(DBName,
                context.MODE_PRIVATE, null);
    }

    public static void CloseDb(SQLiteDatabase db) {

        db.close();
    }

    public static String BuildJson(Advertise advertise) {
        // build json object
        JSONObject data = new JSONObject();
        try {
            data.put("token", AdvertiseActivity.token);
            data.put("signID", "-1");
            data.put("address", advertise.getAddress());
            data.put("revise", advertise.getRevise());
            data.put("note", advertise.getNote());
            data.put("gbs_location", advertise.getLatitude() + "," + advertise.getLongitude());
            data.put("gbs_address", advertise.getGps_address());
            data.put("title", advertise.getTitle());
            data.put("type", advertise.getType());
            data.put("advertiserID", -1);


        } catch (Exception e) {
            Log.e("json_error", e + "");
        }
        return data.toString();
    }
}
