package in.deepaksood.pcsmaproject.preferencemanagerpackage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by deepak on 29/4/16.
 */
public class PrefManager {
    public static final String TAG = PrefManager.class.getSimpleName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "BookXchange";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private static final String KEY_DISPLAY_NAME = "displayName";
    private static final String KEY_DISPLAY_EMAIL_ID = "displayEmailId";
    private static final String KEY_PHOTO_URL = "photoUrl";
    private static final String KEY_COVER_URL = "coverUrl";
    private static final String KEY_USER_CONTACT_NUM = "userContactNum";
    private static final String KEY_USER_LOCATION = "userLocation";

    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveUserData(String displayName, String displayEmailId, String photoUrl, String coverUrl, String userContactNum, String userLocation) {
        editor.putString(KEY_DISPLAY_NAME, displayName);
        editor.putString(KEY_DISPLAY_EMAIL_ID, displayEmailId);
        editor.putString(KEY_PHOTO_URL, photoUrl);
        editor.putString(KEY_COVER_URL, coverUrl);
        editor.putString(KEY_USER_CONTACT_NUM, userContactNum);
        editor.putString(KEY_USER_LOCATION, userLocation);
        editor.commit();
    }

    public void createLogin() {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        Log.v(TAG,"changed: true");
        editor.commit();
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN,false);
    }

}
