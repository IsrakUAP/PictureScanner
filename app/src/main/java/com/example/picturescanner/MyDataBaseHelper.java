package com.example.picturescanner;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {
    public MyDataBaseHelper(@Nullable Context context) {
        super(context, "namedb", null, 7);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table thing(id integer primary key,text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if exists thing";
        db.execSQL(sql);
        onCreate(db);
    }
}
