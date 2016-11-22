package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import model.MyMemory;

/**
 * Created by james on 16-11-21.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    private ArrayList<MyMemory> memoryList = new ArrayList<>();

    public DataBaseHandler(Context context) {
        super(context,Constants.DATABASE_NAME,null,Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //create memory table
        String CREATE_MEMORY_TABLE = "CREATE TABLE " + Constants.TABLE_NAME +
                "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.TITLE_NAME +
                " TEXT, " + Constants.CONTENT_NAME + " TEXT, " + Constants.DATE_NAME +
                " LONG);";

        sqLiteDatabase.execSQL(CREATE_MEMORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        //drop the old table
        sqLiteDatabase.execSQL("DROP IF EXISTS "+ Constants.TABLE_NAME);

        //create a new one
        onCreate(sqLiteDatabase);
    }

    //add memory to the table
    public void addMemory(MyMemory mem){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TITLE_NAME, mem.getTitle());
        values.put(Constants.CONTENT_NAME, mem.getContent());
        values.put(Constants.DATE_NAME, java.lang.System.currentTimeMillis());

        sqLiteDatabase.insert(Constants.TABLE_NAME, null, values);
        sqLiteDatabase.close();
    }

    //get Memory
    public ArrayList<MyMemory> getMemory(){

        String SELECT_QUERY = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID,
            Constants.TITLE_NAME, Constants.CONTENT_NAME, Constants.DATE_NAME},
                null, null, null, null, Constants.DATE_NAME + " DESC");

        //loop through the cursor
        if(cursor.moveToFirst()){
            do {
                //do something here...
                MyMemory mem = new MyMemory();
                mem.setTitle(cursor.getString(cursor.getColumnIndex(Constants.TITLE_NAME)));
                mem.setContent(cursor.getString(cursor.getColumnIndex(Constants.CONTENT_NAME)));
                mem.setItemID(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String dateData = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DATE_NAME))).getTime());
                mem.setRecordDate(dateData);
                memoryList.add(mem);
            }while(cursor.moveToNext());
        }

        return memoryList;
    }

    //delete the memory
    public void deleteMemory(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Constants.TABLE_NAME,Constants.KEY_ID + " =? ",
                new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }

    //update the memory
    public void updateMemory(MyMemory mem){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.TITLE_NAME, mem.getTitle());
        values.put(Constants.CONTENT_NAME, mem.getContent());
        values.put(Constants.DATE_NAME, java.lang.System.currentTimeMillis());
        sqLiteDatabase.update(Constants.TABLE_NAME, values, Constants.KEY_ID + " =? ", new String[]{String.valueOf(mem.getItemID())});
    }
}
