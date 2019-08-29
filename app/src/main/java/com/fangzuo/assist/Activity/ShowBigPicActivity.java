package com.fangzuo.assist.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.BitmapUtil;
import com.fangzuo.assist.Utils.DragImageView;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowBigPicActivity extends AppCompatActivity {
    @BindView(R.id.div_main)
    DragImageView dragImageView;
    private Bitmap mBitmap = null;
    private int window_width, window_height;// 控件宽度
    private int state_height;// 状态栏的高度
    private ViewTreeObserver viewTreeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_pic);
        ButterKnife.bind(this);
//        mSurfaceView = (SurfaceView) this.findViewById(R.id.surfaceview);
//        mSurfaceHolder = mSurfaceView.getHolder();
//        mScaleGestureDetector = new ScaleGestureDetector(this,
//                new ScaleGestureListener());
        mBitmap = Hawk.get("pic", null);
        /** 获取可見区域高度 **/
        WindowManager manager = getWindowManager();
        window_width = manager.getDefaultDisplay().getWidth();
        window_height = manager.getDefaultDisplay().getHeight();
        // 设置图片
        dragImageView.setImageBitmap(mBitmap);
        dragImageView.setmActivity(this);//注入Activity.
        /** 测量状态栏高度 **/
        viewTreeObserver = dragImageView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (state_height == 0) {
                            // 获取状况栏高度
                            Rect frame = new Rect();
                            getWindow().getDecorView()
                                    .getWindowVisibleDisplayFrame(frame);
                            state_height = frame.top;
                            dragImageView.setScreen_H(window_height-state_height);
                            dragImageView.setScreen_W(window_width);
                        }

                    }
                });

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ShowBigPicActivity.class);
        context.startActivity(intent);
    }
}
