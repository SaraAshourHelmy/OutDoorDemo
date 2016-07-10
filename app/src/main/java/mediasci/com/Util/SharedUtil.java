package mediasci.com.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sara on 7/4/2016.
 */
public class SharedUtil {

    public static SharedPreferences shared_check;
    public static SharedPreferences.Editor editor;
    public static final String SharedCheck = "CHECK";

    public SharedUtil(Context context) {
        shared_check = context.getSharedPreferences(SharedCheck, context.MODE_PRIVATE);
        editor = shared_check.edit();

    }

    public static void SetCheck() {
        editor.putBoolean("check", true);
        editor.commit();
    }

    public static boolean GetCheck() {

        return shared_check.getBoolean("check", false);
    }

}
