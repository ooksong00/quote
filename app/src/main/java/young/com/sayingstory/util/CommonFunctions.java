package young.com.sayingstory.util;


import android.content.Context;
import android.content.SharedPreferences;

import young.com.sayingstory.ui.constants.SharedPreferenceConst;

public class CommonFunctions {

    public static void setSignUpSharedPreference (Context context) {
        SharedPreferences.Editor editor =  SharedPreferencesManager.getSharedPreferences(context).edit();
        editor.putBoolean(SharedPreferenceConst.IS_SIGN_UP, true);
        editor.commit();
//        SharedPreferencesManager.setStringValue(this, "pushStatusText", mContext.getString(R.string.alarm_on));
//        FirebaseMessaging.getInstance().subscribeToTopic("/topics/alramOn");


    }
}
