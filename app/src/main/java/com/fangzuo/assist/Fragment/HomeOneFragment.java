package com.fangzuo.assist.Fragment;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.Activity.DealPic2Activity;
import com.fangzuo.assist.Activity.DealPicActivity;
import com.fangzuo.assist.Activity.PicHistoryActivity;
import com.fangzuo.assist.Activity.SignActivity;
import com.fangzuo.assist.Activity.SetActivity;
import com.fangzuo.assist.Adapter.HomeOneAdapter;
import com.fangzuo.assist.Beans.SettingList;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.GetSettingList;
import com.fangzuo.assist.Utils.Lg;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.fangzuo.assist.Activity.DealPicActivity.baseLoc;


public class HomeOneFragment extends BaseFragment {
    @BindView(R.id.ry_data)
    EasyRecyclerView ryData;
    HomeOneAdapter adapter;
    Unbinder unbinder;
    private FragmentActivity mContext;

    public HomeOneFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_one, container, false);
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
        ryData.setAdapter(adapter = new HomeOneAdapter(mContext));
        ryData.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        adapter.addAll(GetSettingList.getAppList());
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
                SettingList tv = (SettingList) adapter.getAllData().get(position);
                Lg.e("listitem", tv);
                switch (tv.activity) {
                    case Config.SetActivity://箱码调拨单
                        SetActivity.start(mContext);
                        break;
                    case Config.SignActivity://箱码调拨单
                        SignActivity.start(mContext);
                        break;
                    case Config.DealPicActivity://箱码调拨单
                        DealPicActivity.start(mContext,"");
                        break;
                    case Config.PicHistoryActivity://箱码调拨单
                        PicHistoryActivity.start(mContext);
                        break;
                    case Config.DealPic2Activity://箱码调拨单
                        DealPic2Activity.start(mContext,"");
                        break;


                }
            }
        });
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
