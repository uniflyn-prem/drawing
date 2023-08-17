package com.example.canvasdrawing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canvasdrawing.Adapters.ToolsAdapters;
import com.example.canvasdrawing.Common.Common;
import com.example.canvasdrawing.Interface.ToolsListener;
import com.example.canvasdrawing.Model.ToolsItem;
import com.example.canvasdrawing.widget.PaintView;
import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements ToolsListener {
    private static final int REQUEST_PERMISSION = 1001;
    private static final int PICK_IMAGE = 1000;
    public PaintView mpaintView;
    public Dialog pentype_dialog,logout_dialog;
    public int colorBackground,colorBrush;
    public Context context;
    public int brushSize,eraserSize;
    public TextView text_normal,text_neon,text_square,text_steps;
    public LinearLayout dialogDone,dialogClose;
    public Button btn_cancel_dialog;
    public  String file_name;
    public File folder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        initTools();

    }
    private void initTools() {
        colorBackground = Color.WHITE;
        colorBrush =  Color.BLACK;
        eraserSize = brushSize = 14;
        mpaintView = findViewById(R.id.paint_view);
        RecyclerView recyclerView =  findViewById(R.id.recycler_view_tools);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        ToolsAdapters toolsAdapters = new ToolsAdapters(loadTools(),this);
        recyclerView.setAdapter(toolsAdapters);
    }

    private List<ToolsItem> loadTools() {
        List<ToolsItem> result = new ArrayList<>();
      //  result.add(new ToolsItem(R.drawable.baseline_auto_awesome_motion_24, Common.PENTYPE));
        result.add(new ToolsItem(R.drawable.baseline_brush_24, Common.BRUSH));
        result.add(new ToolsItem(R.drawable.img,Common.ERASER));
        //result.add(new ToolsItem(R.drawable.baseline_image_24,Common.IMAGE));
        result.add(new ToolsItem(R.drawable.baseline_palette_24,Common.COLORS));
        result.add(new ToolsItem(R.drawable.baseline_redo_24,Common.REDO));
        result.add(new ToolsItem(R.drawable.baseline_undo_24,Common.RETURN));
        result.add(new ToolsItem(R.drawable.baseline_grid_view_24,Common.BACKGROUND));

        return result;
    }

    @Override
    public void onSelected(String name) {
        switch(name){
            case Common.BRUSH:
                mpaintView.desableEraser();
                showDialogSize(false);
                break;

            case Common.PENTYPE:
             // penType();
                break;

            case Common.ERASER:
                mpaintView.enableEraser();
                showDialogSize(true);
                break;
            case Common.IMAGE:
                getImage();
                break;
            case Common.COLORS:
                updateColor(name);
                break;
            case Common.BACKGROUND:
                updateColor(name);
                break;
            case Common.REDO:
                mpaintView.redoAction();
                break;
            case Common.RETURN:
                mpaintView.returnLastAction();
                break;
        }

    }

    private void getImage() {
        Intent intent =new Intent(new Intent(Intent.ACTION_GET_CONTENT));
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"select picture"),PICK_IMAGE);
    }
    public void logOut(){
        logout_dialog = new Dialog(context);
        logout_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logout_dialog.setContentView(R.layout.log_out);
        logout_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logout_dialog.show();
        logout_dialog.setCancelable(false);

        dialogDone = logout_dialog.findViewById(R.id.yes);
        dialogClose = logout_dialog.findViewById(R.id.no);
        dialogDone.setOnClickListener(view -> {finish();});
        dialogClose.setOnClickListener(v -> logout_dialog.dismiss());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e("imageeeee","jgkhdlgdyguy");

        if (requestCode == PICK_IMAGE && data != null && resultCode == RESULT_OK){
            Uri pickImage = data.getData();
            String[] filepath = {MediaStore.Images.Media.DATA};
            Log.e("ytuuuyy","ghfukiuy");
            Log.e("premmmm", Arrays.toString(filepath));
            Cursor cursor = getContentResolver().query(pickImage,filepath,null,null,null);
            cursor.moveToFirst();

            //  String imagePath = cursor.getString(cursor.getColumnIndex(filepath[0]));

            // Log.e("imageeeee",imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile("",options);
            mpaintView.setImage(bitmap);
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateColor(String name) {
        int color;
        if (name.equals(Common.BACKGROUND)){
            color =colorBackground;
        }
        else{
            color = colorBrush;
        }
        // Java Code
        new ColorPickerDialog
                .Builder(this)
                .setTitle("Pick Theme")
                .setColorShape(ColorShape.SQAURE)
                .setDefaultColor(R.color.white)
                .setColorListener(new ColorListener() {
                    @Override
                    public void onColorSelected(int color, @NotNull String colorHex) {
                        if (name.equals(Common.BACKGROUND)){
                            colorBackground = color;
                            mpaintView.setColorBackground(colorBackground);
                        }
                        else{
                            colorBrush = color;
                            mpaintView.setBrushColor(colorBrush);
                        }
                    }
                }).setNegativeButton("cancel").build().show();

    }

    private void showDialogSize(boolean isEraser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog,null,false);
        TextView toolSelected = view.findViewById(R.id.status_tools_selected);
        TextView statusSize = view.findViewById(R.id.status_size);
        ImageView ivTools =  view.findViewById(R.id.iv_tools);
        SeekBar seekBar =  view.findViewById(R.id.seekbar_size);
        seekBar.setMax(99);
        if (isEraser){
            toolSelected.setText("Eraser Size");
            ivTools.setImageResource(R.drawable.img);
            statusSize.setText("Selected Size :"+eraserSize);
        }
        else {
            toolSelected.setText("Pen Size");
            ivTools.setImageResource(R.drawable.baseline_brush);
            statusSize.setText("selected Size:"+brushSize);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (isEraser){
                    eraserSize = i+1;
                    statusSize.setText("Selected Size :"+eraserSize);
                    mpaintView.setSizeEraser(eraserSize);

                }else{
                    brushSize = i+1;
                    statusSize.setText("Selected Size :"+brushSize);
                    mpaintView.setSizeBrush(brushSize);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setView(view);
        builder.show();
    }
    public void finishPaint(View view) {
        logOut();
    }
//    public void penType(){
//        pentype_dialog = new Dialog(context);
//        pentype_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        pentype_dialog.setContentView(R.layout.dialog_box);
//        pentype_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        pentype_dialog.show();
//        pentype_dialog.setCancelable(true);
//        text_normal = pentype_dialog.findViewById(R.id.txt_normal);
//        text_neon = pentype_dialog.findViewById(R.id.txt_round);
//        text_steps = pentype_dialog.findViewById(R.id.txt_steps);
//        text_square = pentype_dialog.findViewById(R.id.txt_square);
//        btn_cancel_dialog = pentype_dialog.findViewById(R.id.btn_cancel_dialog);
//        btn_cancel_dialog.setOnClickListener(view -> pentype_dialog.dismiss());
//        text_normal.setOnClickListener(view -> {pentype_dialog.dismiss();
//            mpaintView.setBrushColor(colorBrush);
//            mpaintView.setColorBackground(colorBackground);
//            mpaintView.setCurrentBrushType(PaintView.BrushType.NORMAL);
//        });
//        text_neon.setOnClickListener(view -> {pentype_dialog.dismiss();
//            mpaintView.setBrushColor(colorBrush);
//            mpaintView.setColorBackground(colorBackground);
//            mpaintView.setCurrentBrushType(PaintView.BrushType.NEON);
//        });
//        text_steps.setOnClickListener(view -> {pentype_dialog.dismiss();
//            mpaintView.setBrushColor(colorBrush);
//            mpaintView.setColorBackground(colorBackground);
//            mpaintView.setCurrentBrushType(PaintView.BrushType.STEPS);
//        });
//        text_square.setOnClickListener(view -> {pentype_dialog.dismiss();
//            mpaintView.setColorBackground(colorBackground);
//            mpaintView.setBrushColor(colorBrush);
//            mpaintView.setCurrentBrushType(PaintView.BrushType.SQUARE);
//        });
//
//    }

    public void shareApp(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String bodyText = "http://play.google.com/store/apps/details?id="+getPackageName();
        intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT,bodyText);
        startActivity(Intent.createChooser(intent,"share this app"));
    }
    public void showFiles(View view) {
        if (folder!=null){
            startActivity(new Intent(this,ListFilesAct.class));
        }
        else {
            Toast.makeText(this, " Please Draw and save something ", Toast.LENGTH_SHORT).show();
        }
}



    public void saveFile(View view) {

            saveBitmap();

    }

    private boolean saveBitmap() {
        Bitmap bitmap = mpaintView.getBitmap();
         file_name = UUID.randomUUID() +".png";
         folder = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+getString(R.string.app_name));
        if (!folder.exists()){
            folder.mkdirs();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(folder + File.separator + file_name);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
            Toast.makeText(this, "picture saved", Toast.LENGTH_SHORT).show();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_PERMISSION && grantResults.length>0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            saveBitmap();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}