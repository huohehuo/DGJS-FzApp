package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.PicHistoryAdapter;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.FileManager;
import com.fangzuo.assist.Utils.Lg;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fangzuo.assist.Activity.DealPicActivity.baseLoc;

public class PicHistoryActivity extends BaseActivity {

    @BindView(R.id.ry_data)
    EasyRecyclerView ryData;
    PicHistoryAdapter adapter;
    FileManager fileManager;
    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_pic_history);
        mContext = this;
        ButterKnife.bind(this);
        tvTitle.setText("历史文件");
        fileManager = FileManager.getInstance(this);
        ryData.setAdapter(adapter = new PicHistoryAdapter(mContext));
        ryData.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        Lg.e("得到图片列表2", fileManager.getImgListByDir(baseLoc + "fangzuo/"));
        adapter.addAll(fileManager.getImgListByDir(baseLoc + "fangzuo/"));
    }

    @Override
    protected void initData() {

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
                DealPicActivity.start(mContext,adapter.getItem(position));
            }
        });

        adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final int position) {
                new AlertDialog.Builder(mContext)
                        .setTitle("是否删除")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fileManager.deletePic(adapter.getItem(position));
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .create().show();
                return true;
            }
        });


    }






    @Override
    protected void OnReceive(String code) {

    }
    public static void start(Context context) {
        Intent intent = new Intent(context, PicHistoryActivity.class);
        context.startActivity(intent);
    }

}
