package young.com.sayingstory.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String APP_SETTINGS = "sharedPreference";

    private SharedPreferencesManager() {}

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static boolean getBooleanValue(Context context, String key) {
        boolean value =  getSharedPreferences(context).getBoolean(key, true);
        return value;
    }

    public static String getStringValue(Context context, String key) {
        String value =  getSharedPreferences(context).getString(key, null);
        return value;
    }
    public static void removeStringValue(Context context, String key) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(key);

        editor.commit();
    }
    public static void setStringValue(Context context, String key, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key , newValue);
        editor.commit();
    }

}