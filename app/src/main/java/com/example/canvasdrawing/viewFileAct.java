package com.example.canvasdrawing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class viewFileAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_file);
        Intent intent = getIntent();
        if (intent != null) {
            ImageView imageView = findViewById(R.id.image);
            imageView.setImageURI(intent.getData());
        }
    }
    public void exportImage(View view) {
        ImageView imageView = findViewById(R.id.image);
        Drawable drawable = imageView.getDrawable();

        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                try {
                    // Save the bitmap to external storage or any desired location
                    String fileName = "exported_image.png";
                    File directory = new File(Environment.getExternalStorageDirectory(), "ExportedImages");
                    directory.mkdirs();
                    File outputFile = new File(directory, fileName);

                    FileOutputStream outputStream = new FileOutputStream(outputFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.close();

                    Toast.makeText(this, "Image exported successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Image export failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    }
