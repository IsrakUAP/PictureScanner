package com.example.picturescanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
Button ButtonCapture,ButtonCopy,ButtonHistory;
TextView textViewData;
Bitmap bitmap;
MyDataBaseHelper myDataBaseHelper;
private static final int REQUEST_CAMERA_CODE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButtonCapture=findViewById(R.id.button_Capture);
        ButtonHistory=findViewById(R.id.button_history);
        ButtonCopy=findViewById(R.id.button_copy);
        textViewData=findViewById(R.id.text_data);
        myDataBaseHelper = new MyDataBaseHelper(this);
        SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.CAMERA
            },REQUEST_CAMERA_CODE);
        }
        ButtonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);
            }
        });
        ButtonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scanned_Text = textViewData.getText().toString();
                copytoClipboard(scanned_Text);
                if(v.getId()==R.id.button_copy)
                {
                    long rowID = myDataBaseHelper.insertData(scanned_Text);
                if(rowID==-1){
                    Toast.makeText(MainActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(MainActivity.this, "Row "+rowID+"is Successfully inserted", Toast.LENGTH_SHORT).show();
                }
                }

            }
        });
        ButtonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sr = ButtonHistory.getText().toString();
                Intent intent = new Intent(MainActivity.this,ShowData.class);
//
                startActivity(intent);
                if(v.getId()==R.id.button_history){
                    Cursor cursor = myDataBaseHelper.displayalldata();
                    if(cursor.getCount()==0){
                        showData("Error","No data");
                        return;
                    }
                    StringBuffer stringBuffer = new StringBuffer();
                    while(cursor.moveToNext())
                    {
                        stringBuffer.append("ID" + cursor.getString(0)+"\n");
                        stringBuffer.append("TEXT" + cursor.getString(1)+"\n");
                    }
                showData("Resultset",stringBuffer.toString());
                }
            }

            public void showData(String resultset, String toString) {
                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(resultset);
                builder.setMessage(toString);
                builder.setCancelable(true);
                builder.show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri=result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
                    getTextFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void getTextFromImage(Bitmap bitmap){
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if (!recognizer.isOperational()){
            Toast.makeText(MainActivity.this, "Error Happened", Toast.LENGTH_SHORT).show();
        }
        else {
            Frame frame= new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0; i<textBlockSparseArray.size();i++){
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");

            }
            textViewData.setText(stringBuilder.toString());
            ButtonCapture.setText("Retake");
            ButtonCopy.setVisibility(View.VISIBLE);
            ButtonHistory.setVisibility(View.VISIBLE);


        }
    }
private void copytoClipboard(String text){
    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clipData = ClipData.newPlainText("Copied Data",text);
    clipboardManager.setPrimaryClip(clipData);
    Toast.makeText(MainActivity.this, "Copied to ClipBoard", Toast.LENGTH_SHORT).show();
}
}
