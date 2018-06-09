package kooxda.saim.com.mybook.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Android on 1/16/2017.
 */
public class SharedPrefDatabase {
    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_FULL_NAME = "KEY_FULL_NAME";
    public static final String KEY_EMAIL = "KEY_EMAIL";
    public static final String KEY_MOBILE = "KEY_MOBILE";
    public static final String KEY_PASS = "KEY_PASS";
    public static final String KEY_AGE = "KEY_AGE";
    public static final String KEY_PHOTO = "KEY_PHOTO";
    public static final String KEY_ADDRESS = "KEY_ADDRESS";


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    public SharedPrefDatabase(Context ctx) {
        this.context = ctx;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = sharedPreferences.edit();
    }

    public void StoreID(String data){
        editor.putString(KEY_ID, data);
        editor.commit();
    }
    public String RetriveID(){
        String text = sharedPreferences.getString(KEY_ID, null);
        return text;
    }

    public void StoreName(String data){
        editor.putString(KEY_FULL_NAME, data);
        editor.commit();
    }
    public String RetriveName(){
        String text = sharedPreferences.getString(KEY_FULL_NAME, null);
        return text;
    }

    public void StoreEmail(String data){
        editor.putString(KEY_EMAIL, data);
        editor.commit();
    }
    public String RetriveEmail(){
        String text = sharedPreferences.getString(KEY_EMAIL, null);
        return text;
    }

    public void StoreMobile(String data){
        editor.putString(KEY_MOBILE, data);
        editor.commit();
    }
    public String RetriveMobile(){
        String text = sharedPreferences.getString(KEY_MOBILE, null);
        return text;
    }

    public void StorePass(String data){
        editor.putString(KEY_PASS, data);
        editor.commit();
    }
    public String RetrivePass(){
        String text = sharedPreferences.getString(KEY_PASS, null);
        return text;
    }

    public void StoreAge(String data){
        editor.putString(KEY_AGE, data);
        editor.commit();
    }
    public String RetriveAge(){
        String text = sharedPreferences.getString(KEY_AGE, null);
        return text;
    }

    public void StorePhoto(String data){
        editor.putString(KEY_PHOTO, data);
        editor.commit();
    }
    public String RetrivePhoto(){
        String text = sharedPreferences.getString(KEY_PHOTO, null);
        return text;
    }

    public void StoreAddress(String data){
        editor.putString(KEY_ADDRESS, data);
        editor.commit();
    }
    public String RetriveAddress(){
        String text = sharedPreferences.getString(KEY_ADDRESS, null);
        return text;
    }
}
