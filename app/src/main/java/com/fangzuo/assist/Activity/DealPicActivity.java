package com.fangzuo.assist.Activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.ImageBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.ImageUtil;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.piccut.CropImageActivity;
import com.fangzuo.assist.widget.piccut.SelectPhotoDialog;
import com.orhanobut.hawk.Hawk;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DealPicActivity extends BaseActivity {

    @BindView(R.id.tv_move_right)
    TextView tvMRight;
    @BindView(R.id.tv_move_bot)
    TextView tvMBot;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.sb_left)
    SeekBar sbLeft;
    @BindView(R.id.sb_bt)
    SeekBar sbBt;
    @BindView(R.id.et_w)
    EditText etW;
    @BindView(R.id.et_h)
    EditText etH;
    @BindView(R.id.bg_set)
    CardView bgSet;
    public static String baseLoc = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    public static final String Pic_Path   = Environment.getExternalStorageDirectory().getAbsolutePath()+"/fangzuo/fzkj-get.jpg";

    String getPicFormPhone="";
    private int logoW;
    private int logoH;
    private byte[] imagebyte;
    // 持有这个动画的引用，让他可以在动画执行中途取消
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private SelectPhotoDialog selectPhotoDialog;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_get_pic);
        ButterKnife.bind(this);
        selectPhotoDialog = new SelectPhotoDialog(DealPicActivity.this, R.style.CustomDialog);
        // 系统默认的短动画执行时间 200
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        DownLoad("http://192.168.0.136:8081/Assist/img/bg.jpg");
    }

    @Override
    protected void initData() {
        sbLeft.setMax(500);
        sbBt.setMax(500);
        logoW = Hawk.get(Config.Logo_W, 100);
        logoH = Hawk.get(Config.Logo_H, 100);
        left = Hawk.get(Config.Logo_Left, 84);
        bottm = Hawk.get(Config.Logo_Bottom, 337);
        sbLeft.setProgress(left);
        sbBt.setProgress(bottm);
        etH.setText(logoH + "");
        etW.setText(logoW + "");
        tvMBot.setText("向下移动" + bottm);
        tvMRight.setText("向右移动" + left);
        //获取跳转过来的图片地址
        String pic = getIntent().getStringExtra("pic_loc");
        if ("".equals(pic)){
            Glide.with(DealPicActivity.this)
                    .load("http://192.168.0.136:8081/Assist/img/test.jpg")
//                .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            //加载完成后的处理
                            iv.setImageDrawable(resource);
                            basePic = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                            setNewBitmap();
                        }
                    });
        }else{
            Glide.with(DealPicActivity.this)
                    .load(pic)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            //加载完成后的处理
                            iv.setImageDrawable(resource);
                            basePic = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                            setNewBitmap();
                        }
                    });
        }


    }

    @Override
    protected void initListener() {
        sbLeft.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Lg.e("当前Left",progress);
                left = progress;
                Hawk.put(Config.Logo_Left, progress);
                tvMRight.setText("向右移动" + left);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setNewBitmap();
            }
        });
        sbBt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Lg.e("当前Bottom",progress);
                bottm = progress;
                Hawk.put(Config.Logo_Bottom, progress);
                tvMBot.setText("向下移动" + bottm);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setNewBitmap();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Hawk.put(Config.Logo_W, MathUtil.toInt(etW.getText().toString()));
        Hawk.put(Config.Logo_H, MathUtil.toInt(etH.getText().toString()));
        super.onDestroy();

    }



    @OnClick({R.id.btn1, R.id.btn_add, R.id.btn2, R.id.iv_upload, R.id.btn4, R.id.iv, R.id.tv_back, R.id.iv_set, R.id.iv_get_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                SignActivity.start(mContext);
                break;
            case R.id.iv_get_pic:
                getPic();
                break;
            case R.id.btn_add:
                setNewBitmap();
//                Glide.with(DealPicActivity.this)
////                        .asGif()
////                        .load("http://192.168.0.105:8080/Assist/img/logo.gif")
//                        .load(R.drawable.test)
//                        .into(iv);
                break;
            case R.id.btn2:
                ImageUtil.saveBitmap(((BitmapDrawable) iv.getDrawable()).getBitmap(), baseLoc+"fangzuo/fzkj"+ CommonUtil.getTimeLong(false)+".jpg");
                break;
            case R.id.iv_upload:
                uploadPic();
                break;
            case R.id.btn4:
//                basePic = ((BitmapDrawable) iv.getDrawable()).getBitmap();

//                saveBitmap(((BitmapDrawable) iv.getDrawable()).getBitmap(), path3);

//                String target = Environment.getExternalStorageDirectory()
//                        + "/NewApp"+getTimeLong(false)+".jpg";
//                File file = new File(target);
//                Glide.with(DealPicActivity.this)
//                        .load(file)
//                        .into(iv);
                break;
            case R.id.iv_set:
                if (bgSet.getVisibility() == View.GONE) {
                    etH.setText(logoH + "");
                    etW.setText(logoW + "");
                    bgSet.setVisibility(View.VISIBLE);
                } else {
                    logoH = MathUtil.toInt(etH.getText().toString());
                    logoW = MathUtil.toInt(etW.getText().toString());
                    Hawk.put(Config.Logo_W, logoW);
                    Hawk.put(Config.Logo_H, logoH);
                    bgSet.setVisibility(View.GONE);
                    setNewBitmap();
                }
                break;
            case R.id.iv:
                if (bgSet.getVisibility() == View.GONE) {
                    if (null==((BitmapDrawable) iv.getDrawable()))return;
                    Hawk.put("pic",((BitmapDrawable) iv.getDrawable()).getBitmap());
                    ShowBigPicActivity.start(mContext);
//                    etH.setText(logoH + "");
//                    etW.setText(logoW + "");
//                    bgSet.setVisibility(View.VISIBLE);
                } else {
                    logoH = MathUtil.toInt(etH.getText().toString());
                    logoW = MathUtil.toInt(etW.getText().toString());
                    Hawk.put(Config.Logo_W, logoW);
                    Hawk.put(Config.Logo_H, logoH);
                    bgSet.setVisibility(View.GONE);
                    setNewBitmap();
                }
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    //上传图片到服务器
    private void uploadPic() {
        ImageBean bean = new ImageBean();
        bean.bitmapByte = ImageUtil.getBitmap2Byte(((BitmapDrawable) iv.getDrawable()).getBitmap());
        App.getRService().doIOAction("ImageUpload", gson.toJson(bean), new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                Lg.e("上传成功");
                Toast.showText(mContext, "上传成功");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Lg.e("上传失败" + e.getMessage());
                Toast.showText(mContext, "上传失败" + e.getMessage());
            }
        });
    }

    int left = 0;
    int bottm = 0;
    Bitmap basePic;

    //设置水印位置及大小
    private void setNewBitmap() {
        Lg.e("当前left-bottm", left + "-" + bottm);
        if (null == basePic) return;
        //获取原始图片
//        Bitmap sourBitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        //水印图片
        Bitmap waterBitmap = BitmapFactory.decodeFile(SignActivity.path);
        if (null == waterBitmap)return;
//        Bitmap watermarkBitmap = ImageUtil.createWaterMaskCenter(sourBitmap, waterBitmap);
        Bitmap watermarkBitmap = ImageUtil.createWaterMaskLeftTop(basePic, waterBitmap, left, bottm, MathUtil.toInt(etW.getText().toString()), MathUtil.toInt(etH.getText().toString()));
//        watermarkBitmap = ImageUtil.createWaterMaskRightBottom(watermarkBitmap, waterBitmap, 0, 0);
//        watermarkBitmap = ImageUtil.createWaterMaskLeftTop(watermarkBitmap, waterBitmap, 0, 0);
//        watermarkBitmap = ImageUtil.createWaterMaskRightTop(watermarkBitmap, waterBitmap, 0, 0);

        iv.setImageBitmap(watermarkBitmap);
    }

    private void getPic(){
        selectPhotoDialog.setDialogCallBack(new SelectPhotoDialog.SelectPhoteDialogCallBack() {
            @Override
            public void OnclickLiseten(int id) {
                switch (id) {
                    case SelectPhotoDialog.BN_TAKEPHOTO:
//                        File file = new File(Pic_Path);
//                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
//                                mContext.getPackageName()+".new.provider",
//                                file);
                        getPicFormPhone =baseLoc+"fangzuo/fzkj-camera"+CommonUtil.getTimeLong(false)+".jpg";
                        CropImageActivity.startActivity(DealPicActivity.this, getPicFormPhone, true, 3);
                        selectPhotoDialog.dismiss();
                        break;
                    case SelectPhotoDialog.BN_SELECTPHOTO:
                        getPicFormPhone =baseLoc+"fangzuo/fzkj-camera"+CommonUtil.getTimeLong(false)+".jpg";
                        CropImageActivity.startActivity(DealPicActivity.this, getPicFormPhone, false, 2);
                        selectPhotoDialog.dismiss();
                        break;
                    case SelectPhotoDialog.BN_CANCEL:
                        selectPhotoDialog.dismiss();
                        break;
                }
            }
        });
        selectPhotoDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
//                case REQUEST_NICKNAME:
////                    binding.tvNickName.setText(data.getStringExtra(RESULT));
//                    break;
//                case REQUEST_MOTTO:
////                    binding.tvMotto.setText(data.getStringExtra(RESULT));
//                    break;
                case 2:
                    Log.e("pp", "获取到图片");
                    // Glide.with(EditMyInfoActivity.this).load(new File(URL.PATH_SELECT_AVATAR)).into(binding.ciAvatar);
                    Bitmap bitmap = BitmapFactory.decodeFile(getPicFormPhone);
                    if (bitmap != null) {
                        imagebyte = ImageUtil.getBitmap2Byte(bitmap);
                    }
                    iv.setImageBitmap(bitmap);
                    basePic = bitmap;
                    break;
                case 3:
                    Log.e("pp", "获取到图片");
                    // Glide.with(EditMyInfoActivity.this).load(new File(URL.PATH_SELECT_AVATAR)).into(binding.ciAvatar);
                    Bitmap bitmap2 = BitmapFactory.decodeFile(getPicFormPhone);
                    if (bitmap2 != null) {
                        imagebyte = ImageUtil.getBitmap2Byte(bitmap2);
                    }
                    iv.setImageBitmap(bitmap2);
                    basePic = bitmap2;
                    break;
            }
        }
    }

    public static void start(Context context,String loc) {
        Intent intent = new Intent(context, DealPicActivity.class);
        intent.putExtra("pic_loc",loc);
        context.startActivity(intent);
    }
    @Override
    protected void OnReceive(String code) {

    }
}
