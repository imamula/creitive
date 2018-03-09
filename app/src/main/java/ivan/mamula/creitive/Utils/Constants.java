package ivan.mamula.creitive.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Ivan Mamula on 3/9/18.
 * mamula82@gmail.com
 * Skype id: mamula82
 * +381(0)642035511
 */
public class Constants {

    public static final String BASE_URL = "http://blogsdemo.creitiveapps.com";
    public static final int TIME_OUT_SECONDS = 10;
    public static final String KEY_TOKEN = "save_UsER_Token";
    private static SharedPreferences mSharedPreferences = null;
    private static Editor mEditor = null;
    public static final String LOGIN_CONTENT_TYPE_HEADER_VALUE = "application/json";
    public static final String LOGIN_CONTENT_TYPE_HEADER_NAME = "Content-Type";
    public static final String X_AUTHORIZE_HEADER_NAME = "X-Authorize";
    public static final String ACCEPT_HEADER = "Accept: application/json";
    public static final String WRONG_CREDENTIAL_MESSAGE = "unauthorized";
    public static final int PASSWORD_MIN_CHARS = 6;

    public static void saveToken(String token, Context context) {
        getEditor(context).putString(KEY_TOKEN, token).commit();
    }

    public static String getToken(Context context) {
        return getSharedPreferences(context).getString(KEY_TOKEN, null);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        if (mSharedPreferences == null) ;
        {
            mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    private static Editor getEditor(Context context) {
        if (mEditor == null) {
            mEditor = getSharedPreferences(context).edit();
        }
        return mEditor;
    }
}
