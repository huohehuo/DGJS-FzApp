package com.fangzuo.assist.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.Activity.DealPic2Activity;
import com.fangzuo.assist.Activity.DealPicActivity;
import com.fangzuo.assist.Activity.PicHistoryActivity;
import com.fangzuo.assist.Activity.SetActivity;
import com.fangzuo.assist.Activity.SignActivity;
import com.fangzuo.assist.Adapter.HomeOneAdapter;
import com.fangzuo.assist.Adapter.PicHistoryAdapter;
import com.fangzuo.assist.Beans.SettingList;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.FileManager;
import com.fangzuo.assist.Utils.GetSettingList;
import com.fangzuo.assist.Utils.Lg;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.fangzuo.assist.Activity.DealPicActivity.baseLoc;


public class HomeTowFragment extends BaseFragment {
    @BindView(R.id.ry_data)
    EasyRecyclerView ryData;
    PicHistoryAdapter adapter;
    FileManager fileManager;
    Unbinder unbinder;
    private FragmentActivity mContext;

    public HomeTowFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_two, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void initView() {
        mContext = getActivity();
        //生成文件夹
        File f = new File(baseLoc+"fangzuo");
        if (!f.exists()) {
            f.mkdir();
        }
    }

    @Override
    protected void OnReceive(String barCode) {

    }

    //    P1OneAdapter adapter;
    @Override
    protected void initData() {
//        String getPermit=share.getString(ShareInfo.USER_PERMIT);
//        String[] arylist = getPermit.split("\\-"); // 这样才能得到正确的结果
        fileManager = FileManager.getInstance(getActivity());
        ryData.setAdapter(adapter = new PicHistoryAdapter(mContext));
        ryData.setLayoutManager(new GridLayoutManager(mContext, 2));
//        Lg.e("得到图片列表2", fileManager.getImgListByDir(baseLoc + "fangzuo/"));
        adapter.addAll(fileManager.getImgListByDir(baseLoc + "fangzuo/"));
    }

//    private AlertDialog.Builder builder;
//    String[] items_sout = new String[]{"原单", "销售订单下推销售出库单","VMI销售订单下推销售出库单","退货通知单下推销售退货单"};
//    String[] items_tb = new String[]{"挑板领料1", "挑板入库1"};
//    String[] items_tb2 = new String[]{"挑板领料2", "挑板入库2"};
//    String[] items_tb3 = new String[]{"挑板领料3", "挑板入库3"};
//    String[] items_pk = new String[]{"盘亏入库", "VMI盘亏入库"};
//    String[] items_gb = new String[]{"改板领料", "改板入库"};
//    String[] items_dc = new String[]{"代存出库", "代存入库"};
////    String[] items_db = new String[]{"组织间调拨", "跨组织调拨", "调拨申请单下推直接调拨单", "VMI调拨申请单下推直接调拨单"};
//    String[] items_db = new String[]{"组织间调拨", "跨组织调拨", "调拨申请单下推直接调拨单"};
////    String[] items_in_out = new String[]{"样板出库", "第三方货物入库","第三方货物出库","出库申请单下推其他出库单"};
//    String[] items_in_out = new String[]{"样板出库", "第三方货物入库","第三方货物出库"};

    @Override
    protected void initListener() {
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
                                adapter.clear();
                                adapter.addAll(fileManager.getImgListByDir(baseLoc + "fangzuo/"));
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .create().show();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null!=adapter){
            adapter.clear();
            adapter.addAll(fileManager.getImgListByDir(baseLoc + "fangzuo/"));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume

        } else {
            //相当于Fragment的onPause

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

   /* @OnClick({R.id.cv_sign, R.id.cv_deal_pic, R.id.cv_setting, R.id.cv_history_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_sign:
                SignActivity.start(mContext);
                break;
            case R.id.cv_deal_pic:
                DealPicActivity.start(mContext);
                break;
            case R.id.cv_setting:
                SetActivity.start(mContext);
                break;
            case R.id.cv_history_pic:
                PicHistoryActivity.start(mContext);
                break;
        }
    }*/
}
