package kooxda.saim.com.mybook.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Android on 1/16/2017.
 */
public class SharedPrefDatabase {
    public static final String PREFS_KEY_ALBUM = "AOP_PREFS_ALBUM";
    public static final String PREFS_KEY_CONTENT_NAME = "AOP_PREFS_CONTENT_NAME";
    public static final String PREFS_KEY_CONTENT_DETAIL_VALUE = "AOP_PREFS_CONTENT_DETAIL_VALUE";
    public static final String PREFS_KEY_VIDEO_LINK = "AOP_PREFS_VIDEO_LINK";

    public static final String PREFS_KEY_CURRENT_LIST = "AOP_PREFS_CURRENT_LIST";
    public static final String PREFS_KEY_CURRENT_LIST_POS = "AOP_PREFS_CURRENT_LIST_POS";
    public static final String PREFS_KEY_TYPE = "AOP_PREFS_TYPE";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    public SharedPrefDatabase(Context ctx) {
        this.context = ctx;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = sharedPreferences.edit();
    }

    public void StoreAlbum(String data){
        editor.putString(PREFS_KEY_ALBUM, data);
        editor.commit();
    }
    public String RetriveAlbum(){
        String text = sharedPreferences.getString(PREFS_KEY_ALBUM, null);
        return text;
    }

    public void StoreContentName(String data){
        editor.putString(PREFS_KEY_CONTENT_NAME, data);
        editor.commit();
    }
    public String RetriveContentName(){
        String text = sharedPreferences.getString(PREFS_KEY_CONTENT_NAME, null);
        return text;
    }


    public void StoreContentDetailValue(String data){
        editor.putString(PREFS_KEY_CONTENT_DETAIL_VALUE, data);
        editor.commit();
    }
    public String RetriveContentDetailValue(){
        String text = sharedPreferences.getString(PREFS_KEY_CONTENT_DETAIL_VALUE, null);
        return text;
    }


    //Video Link
    public void StoreVideoLink(String data){
        editor.putString(PREFS_KEY_VIDEO_LINK, data);
        editor.commit();
    }
    public String RetriveVideoLink(){
        String text = sharedPreferences.getString(PREFS_KEY_VIDEO_LINK, null);
        return text;
    }

    public void StoreCurrentList(String data){
        editor.putString(PREFS_KEY_CURRENT_LIST, data);
        editor.commit();
    }
    public String RetriveCurrentList(){
        String text = sharedPreferences.getString(PREFS_KEY_CURRENT_LIST, null);
        return text;
    }

    public void StoreCurrentListPos(int data){
        editor.putInt(PREFS_KEY_CURRENT_LIST_POS, data);
        editor.commit();
    }
    public int RetriveCurrentListPos(){
        int text = sharedPreferences.getInt(PREFS_KEY_CURRENT_LIST_POS, 0);
        return text;
    }

    public void StoreType(String data){
        editor.putString(PREFS_KEY_TYPE, data);
        editor.commit();
    }
    public String RetriveType(){
        String text = sharedPreferences.getString(PREFS_KEY_TYPE, null);
        return text;
    }
}
