package com.example.picturescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowData extends AppCompatActivity {
MyDataBaseHelper myDataBaseHelper;
    SQLiteDatabase sqLiteDatabase;
    ListView listView;
    String []text;
    int []id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        myDataBaseHelper = new MyDataBaseHelper(ShowData.this);
//        create method
        findid();
        dis();


    }
    //for display data
    private void dis() {
        sqLiteDatabase=myDataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select *from thing",null);
        if(cursor.getCount()>0){
            id= new int[cursor.getCount()];
            text= new String[cursor.getCount()];

            int i=0;
            while (cursor.moveToNext()){
                id[i]=cursor.getInt(0);
                text[i]=cursor.getString(1);
                i++;
            }
            Custom adapter=new Custom();
            listView.setAdapter(adapter);
        }
    }

    private void findid() {
        listView = findViewById(R.id.listviewid);

    }

    private class Custom extends BaseAdapter {
        @Override
        public int getCount() {
            return text.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView,textView1;
            ImageView edit,delete;
            convertView = LayoutInflater.from(ShowData.this).inflate(R.layout.singledata,parent,false);
            textView = convertView.findViewById(R.id.txt_name);
            delete = convertView.findViewById(R.id.delete_data);
            textView.setText(text[position]);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",id[position]);
                    bundle.putString("name",text[position]);
                    Intent intent = new Intent(ShowData.this,MainActivity.class);
                    intent.putExtra("userdata",bundle);
                    startActivity(intent);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqLiteDatabase= myDataBaseHelper.getWritableDatabase();
                    long recd = sqLiteDatabase.delete("thing","id="+id[position],null);
                    if (recd!=1){
                        Toast.makeText(ShowData.this, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                        dis();
                    }
                }
            });

            return convertView;
        }
    }
}