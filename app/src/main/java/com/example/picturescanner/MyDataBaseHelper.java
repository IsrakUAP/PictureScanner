package com.example.picturescanner;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Text.db";
    private static final String TABLE_NAME = "picture_details";
    private static final String ID = "_id";
    private static final String TEXT = "text";
    private static final int VERSION_NUMBER = 1;
    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+TEXT+" VARCHAR(255));";
    private static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    private Context context;
    public MyDataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 4);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            Toast.makeText(context, "On create is called", Toast.LENGTH_SHORT).show();
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }catch (Exception e){
            Toast.makeText(context, "Exception"+e, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
   public long insertData(String text){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put(TEXT,text);
       long rowid= sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
       return rowid;
   }
  public Cursor displayalldata(){

       SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
      Cursor cursor = sqLiteDatabase.rawQuery(SELECT_ALL,null);
      return cursor;
   }
}
