package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.PicHistoryAdapter;
import com.fangzuo.assist.Adapter.PicServerAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.FileManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.LoadingUtil;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fangzuo.assist.Activity.DealPicActivity.baseLoc;

public class Pic4ServerActivity extends BaseActivity {

    @BindView(R.id.ry_data)
    EasyRecyclerView ryData;
    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    PicServerAdapter adapter;
    FileManager fileManager;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_pic_history);
        mContext = this;
        ButterKnife.bind(this);
        tvTitle.setText("图片文件");
        fileManager = FileManager.getInstance(this);
        ryData.setAdapter(adapter = new PicServerAdapter(mContext));
        ryData.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
//        Lg.e("得到图片列表2", fileManager.getImgListByDir(baseLoc + "fangzuo/"));
//        adapter.addAll(fileManager.getImgListByDir(baseLoc + "fangzuo/"));
    }

    @Override
    protected void initData() {
        Lg.e("正在获取服务器照片");
        App.getRService().doIOAction("ImageGetting", Hawk.get(Config.PicServerAddress,"pic"), new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                LoadingUtil.dismiss();
                DownloadReturnBean dBean = JsonCreater.gson.fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null!=dBean.imgAddresses && dBean.imgAddresses.size()>0){
                    if (dBean.imgAddresses.get(0).Error.equals("")){
                        adapter.addAll(dBean.imgAddresses);
                    }else{
                        Toast.showText(mContext,dBean.imgAddresses.get(0).Error);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadingUtil.dismiss();
                Lg.e("获取失败" + e.getMessage());
                Toast.showText(mContext, "获取失败" + e.getMessage());
            }
        });
    }

    @Override
    protected void initListener() {
        btnBack.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                finish();
            }
        });
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                DealPicActivity.start(mContext, CommonUtil.getPicServerUrl()+adapter.getItem(position).getPicName());
            }
        });
//
//        adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(final int position) {
//                new AlertDialog.Builder(mContext)
//                        .setTitle("是否删除")
//                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                fileManager.deletePic(adapter.getItem(position));
//                                adapter.notifyDataSetChanged();
//                            }
//                        })
//                        .create().show();
//                return true;
//            }
//        });


    }






    @Override
    protected void OnReceive(String code) {

    }
    public static void start(Context context) {
        Intent intent = new Intent(context, Pic4ServerActivity.class);
        context.startActivity(intent);
    }

}
