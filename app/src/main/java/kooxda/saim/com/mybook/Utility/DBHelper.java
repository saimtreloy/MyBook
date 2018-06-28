package kooxda.saim.com.mybook.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import kooxda.saim.com.mybook.Model.ModelContent;

/**
 * Created by NREL on 6/27/18.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyBook.db";
    public static final String CONTENT_TABLE_NAME = "save_video";
    public static final String CONTENT_COLUMN_ID = "id";
    public static final String CONTENT_COLUMN_NAME = "name";
    public static final String CONTENT_COLUMN_BANNER = "banner";
    public static final String CONTENT_COLUMN_LOCATION = "location";
    public static final String CONTENT_COLUMN_TYPE = "type";
    public static final String CONTENT_COLUMN_CATEGORY = "category";
    public static final String CONTENT_COLUMN_DATE_TIME = "date_time";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CONTENT_TABLE_NAME + " (id integer PRIMARY KEY, name text, banner text, location text, type text, category text, date_time text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertContent (Integer id, String name, String banner, String location, String type, String category, String date_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTENT_COLUMN_ID, id);
        contentValues.put(CONTENT_COLUMN_NAME, name);
        contentValues.put(CONTENT_COLUMN_BANNER, banner);
        contentValues.put(CONTENT_COLUMN_LOCATION, location);
        contentValues.put(CONTENT_COLUMN_TYPE, type);
        contentValues.put(CONTENT_COLUMN_CATEGORY, category);
        contentValues.put(CONTENT_COLUMN_DATE_TIME, date_time);
        db.insert(CONTENT_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM "+ CONTENT_TABLE_NAME + " WHERE " + CONTENT_COLUMN_ID + " = " + id + ";", null );
        return res;
    }

    public boolean getDataExits(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM "+ CONTENT_TABLE_NAME + " WHERE " + CONTENT_COLUMN_ID + " = " + id + ";", null );
        if (res.getCount() > 0){
            return false;
        } else {
            return true;
        }
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTENT_TABLE_NAME);
        return numRows;
    }

    public boolean updateContent (Integer id, String name, String banner, String location, String type, String category, String date_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTENT_COLUMN_NAME, name);
        contentValues.put(CONTENT_COLUMN_BANNER, banner);
        contentValues.put(CONTENT_COLUMN_LOCATION, location);
        contentValues.put(CONTENT_COLUMN_TYPE, type);
        contentValues.put(CONTENT_COLUMN_CATEGORY, category);
        contentValues.put(CONTENT_COLUMN_DATE_TIME, date_time);
        db.update(CONTENT_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CONTENT_TABLE_NAME, "id = ? ", new String[] { Integer.toString(id) });
    }

    public ArrayList<ModelContent> getAllContents() {
        ArrayList<ModelContent> array_list = new ArrayList<ModelContent>();
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + CONTENT_TABLE_NAME +";", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            ModelContent modelContent = new ModelContent(res.getInt(res.getColumnIndex(CONTENT_COLUMN_ID)) + "",
                    res.getString(res.getColumnIndex(CONTENT_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(CONTENT_COLUMN_BANNER)),
                    res.getString(res.getColumnIndex(CONTENT_COLUMN_LOCATION)),
                    res.getString(res.getColumnIndex(CONTENT_COLUMN_TYPE)),
                    res.getString(res.getColumnIndex(CONTENT_COLUMN_CATEGORY)),
                    res.getString(res.getColumnIndex(CONTENT_COLUMN_DATE_TIME)));

            array_list.add(modelContent);

            res.moveToNext();
        }
        return array_list;
    }
}
