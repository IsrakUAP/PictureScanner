package com.example.picturescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

public class ShowData extends AppCompatActivity {
MyDataBaseHelper myDataBaseHelper;
SQLiteDatabase sqLiteDatabase;
ListView listView;
    String[] text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        listView= findViewById(R.id.listviewid);
        Intent intent = getIntent();



    }
}