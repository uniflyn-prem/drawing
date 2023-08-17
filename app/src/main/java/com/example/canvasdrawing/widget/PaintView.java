package com.example.canvasdrawing.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PaintView extends View {
    private Bitmap btmBackground,btmView,image;
    private Paint mpaint = new Paint();
    private Path mpath = new Path();
    private int colorBackground,sizeBrush,sizeEraser;
    private float mx,my;
    private Canvas mcanvas;
    private final int DEFFERENCE_SPACE = 4;
    private ArrayList<Bitmap> listAction =  new ArrayList<>();
    private ArrayList<Bitmap> redoList = new ArrayList<>();

    private int leftImage = 50, topImage = 50;

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        sizeEraser = sizeBrush = 14;
        colorBackground = Color.WHITE;
                mpaint.setColor(Color.BLACK);
                mpaint.setAntiAlias(true);
                mpaint.setDither(true);
                mpaint.setStyle(Paint.Style.STROKE);
                mpaint.setStrokeCap(Paint.Cap.ROUND);
                mpaint.setStrokeJoin(Paint.Join.ROUND);
                mpaint.setStrokeWidth(toPx(sizeBrush));

    }
//    public enum BrushType {
//        NORMAL,
//        SQUARE,
//        NEON,
//        STEPS
//    }
//    public BrushType currentBrushType = BrushType.NORMAL;
//    public void setCurrentBrushType(BrushType brushType) {
//        currentBrushType = brushType;
//        updatePaintSettings();
//    }
//    private void updatePaintSettings() {
//        switch (currentBrushType) {
//            case NORMAL:
//                mpaint.setStrokeJoin(Paint.Join.ROUND);
//                mpaint.setStrokeCap(Paint.Cap.ROUND);
//                mpaint.setStrokeWidth(sizeBrush);
//                mpaint.setColor(colorBackground);
//
//            case NEON:
//                mpaint.setStrokeJoin(Paint.Join.ROUND);
//                mpaint.setStrokeCap(Paint.Cap.ROUND);
//                mpaint.setStrokeWidth(sizeBrush);
//                mpaint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
//                break;
//
//            case SQUARE:
//                mpaint.setStrokeJoin(Paint.Join.BEVEL);
//                mpaint.setStrokeCap(Paint.Cap.SQUARE);
//                mpaint.setStrokeWidth(sizeBrush);
//                break;
//            case STEPS:
//                mpaint.setStrokeJoin(Paint.Join.MITER);
//                mpaint.setStrokeCap(Paint.Cap.BUTT);
//                mpaint.setStrokeWidth(sizeBrush);
//                mpaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
//                break;
//
//
//
//
//
//
//
//
//        }
   // }

    private float toPx(int sizeBrush){
        return sizeBrush*(getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        btmBackground = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        btmView = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        mcanvas =  new Canvas(btmView);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(colorBackground);
        canvas.drawBitmap(btmBackground,0,0,null);
        if (image!=null){
            canvas.drawBitmap(image,leftImage,topImage,null);
            canvas.drawBitmap(btmView,0,0,null);
        }
        canvas.drawBitmap(btmView,0,0,null);
    }
    public void setColorBackground(int color){
        colorBackground = color;
        invalidate();
    }
    public void setSizeBrush(int s){
        sizeBrush = s;
        mpaint.setStrokeWidth(toPx(sizeBrush));
    }
    public void setBrushColor(int color){
        mpaint.setColor(color);
    }
    public void setSizeEraser(int s){
        sizeEraser = s;
        mpaint.setStrokeWidth(toPx(sizeEraser));
    }
    public void enableEraser(){
        mpaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }
    public void desableEraser(){
        mpaint.setXfermode(null);
        mpaint.setShader(null);
        mpaint.setMaskFilter(null);
    }
    public void addLastAction(Bitmap bitmap){
        listAction.add(bitmap);
    }
//    public void returnLastAction(){
//        if (listAction.size()>0){
//            listAction.remove(listAction.size()-1);
//            if (listAction.size()>0){
//                btmView = listAction.get(listAction.size()-1);
//            }else{
//                btmView = Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_8888);
//            }
//            mcanvas =  new Canvas(btmView);
//            invalidate();
//        }

  //  }
  public void returnLastAction(){
      if (listAction.size()>0){
          Bitmap lastActionBitmap = listAction.remove(listAction.size()-1);
          redoList.add(lastActionBitmap); // Add undone action to the redo list
          updateCanvasFromActionList();
      }
  }



    public void redoAction() {
        if (redoList.size() > 0) {
            Bitmap redoBitmap = redoList.remove(redoList.size() - 1);
            listAction.add(redoBitmap);
            updateCanvasFromActionList();
        }
    }

    private void updateCanvasFromActionList() {
        if (listAction.size() > 0) {
            btmView = listAction.get(listAction.size() - 1);
        } else {
            btmView = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        }
        mcanvas = new Canvas(btmView);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                redoList.clear(); // Clear redo list when a new action starts
                touchStart(x,y);
                break;
            case  MotionEvent.ACTION_MOVE:
                touchMove(x,y);
                break;
            case  MotionEvent.ACTION_UP:
                addLastAction(getBitmap());
                touchUp();
                break;
        }
        return true;
    }

    private void touchUp() {
        mpath.reset();
    }

    private void touchStart(float x, float y) {
        mpath.moveTo(x,y);

        mx = x;
        my = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x-mx);
        float dy = Math.abs(x-my);
      if (dx>=DEFFERENCE_SPACE|| dy>=DEFFERENCE_SPACE){
          mpath.quadTo(x,y,(x+mx)/2,(y+my)/2);
          my = y;
          mx = x;
          mcanvas.drawPath(mpath,mpaint);
          invalidate();
      }

    }
    public Bitmap getBitmap(){
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        return  bitmap;
    }

    public void setImage(Bitmap bitmap) {
        image = Bitmap.createScaledBitmap(bitmap,getWidth()/2,getHeight()/2,true);
        invalidate();
    }

}
