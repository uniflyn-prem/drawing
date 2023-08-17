package com.example.canvasdrawing.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canvasdrawing.Interface.ViewOnClick;
import com.example.canvasdrawing.R;
import com.example.canvasdrawing.ViewHolder.FileViewHolder;
import com.example.canvasdrawing.viewFileAct;

import java.io.File;
import java.util.List;

public class FilesAdapters extends RecyclerView.Adapter<FileViewHolder> {
    private Context mcontext;
    private List<File> fileList;

    public FilesAdapters(Context mcontext, List<File> fileList) {
        this.mcontext = mcontext;
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.row_file,parent,false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.imageView.setImageURI(Uri.fromFile(fileList.get(position)));
        holder.setViewOnClick(new ViewOnClick() {
            @Override
            public void onClick(int pos) {
                Intent intent = new Intent(mcontext, viewFileAct.class);
                intent.setData(Uri.fromFile(fileList.get(pos)));
                mcontext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }
}
