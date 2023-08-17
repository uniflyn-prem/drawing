package com.example.canvasdrawing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.canvasdrawing.Adapters.FilesAdapters;
import com.example.canvasdrawing.Common.Common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListFilesAct extends AppCompatActivity {
    List<File> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list_files);
        initToolbar();
        initViews();
    }
    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_files);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        fileList = loadFile();
        FilesAdapters filesAdapters = new FilesAdapters(this,fileList);
        recyclerView.setAdapter(filesAdapters);
    }

    private List<File> loadFile() {
        ArrayList<File> inFiles = new ArrayList<>();
        File parendDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + getString(R.string.app_name));
        File[] files = parendDir.listFiles();
        Log.e("fileeeee", Arrays.toString(files));
        for (File file : files) {
            if (file.getName().endsWith(".png")) {
                inFiles.add(file);
            }

            TextView textView = findViewById(R.id.status_empty);
            ImageView imageView = findViewById(R.id.img_code);
          if (file.length()>6){
                imageView.setVisibility(View.GONE);
               }
          else {
              imageView.setVisibility(View.GONE);
          }
            if (files.length>0){
                textView.setVisibility(View.GONE);
            }
            else {
                textView.setVisibility(View.VISIBLE);

            }
        }
        return inFiles;

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("pictures");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return true;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.DELETE)){
            deleteFile(item.getOrder());
            initViews();
        }
        return super.onContextItemSelected(item);
    }

    private void deleteFile(int order) {
        fileList.get(order).delete();
        fileList.remove(order);
    }
}