package com.fangzuo.assist.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.MenuFragmentAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Fragment.HomeOneFragment;
import com.fangzuo.assist.Fragment.HomeTowFragment;
import com.fangzuo.assist.Fragment.P1OneFragment;
import com.fangzuo.assist.Fragment.SettingFragment;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.DownLoadData;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.DaoSession;
import com.loopj.android.http.AsyncHttpClient;

import org.greenrobot.greendao.async.AsyncSession;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity  extends BaseActivity implements EasyPermissions.PermissionCallbacks {


    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.iv_purchase)
    ImageView ivPurchase;
    @BindView(R.id.tv_purchase)
    TextView tvPurchase;
    @BindView(R.id.iv_sale)
    ImageView ivSale;
    @BindView(R.id.tv_sale)
    TextView tvSale;
    @BindView(R.id.bottom_btn_sale)
    LinearLayout bottomBtnSale;
    @BindView(R.id.iv_storage)
    ImageView ivStorage;
    @BindView(R.id.tv_storage)
    TextView tvStorage;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindColor(R.color.bottombartv)
    int tvcolor;
    @BindColor(R.color.fragment_text)
    int tvColorUnClick;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private HomeActivity mContext;
    private FragmentTransaction ft;
    private Fragment curFragment;
    private DaoSession daoSession;
    private AsyncSession asyncSession;
    private AsyncSession asyncSession2;
    private long nowTime;
    private int size;
    private ProgressDialog pg;
    @Override
    public void initView() {
        setContentView(R.layout.activity_home);
        mContext = this;
        ButterKnife.bind(mContext);
        initFragments();
        Welcome();
        getPermisssion();

    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }


    private void Welcome() {
        Asynchttp.post(mContext, getBaseUrl() + WebApi.SETTIMEUSE, BasicShareUtil.getInstance(mContext).getMAC(), new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                Log.e("time Result", cBean.state + "");
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                Log.e("time Result", Msg);
            }
        });
    }


    @Override
    public void initData() {
//        tvUser.setText("当前用户:" + ShareUtil.getInstance(mContext).getUserName());
        ivPurchase.setImageResource(R.mipmap.bt_pic_fc);
        tvPurchase.setTextColor(tvcolor);
    }


    @Override
    public void initListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        resetBottomView();
                        ivPurchase.setImageResource(R.mipmap.bt_pic_fc);
                        tvPurchase.setTextColor(tvcolor);
                        break;
//                    case 1:
//                        resetBottomView();
//                        ivSale.setImageResource(R.mipmap.sale);
//                        tvSale.setTextColor(tvcolor);
//                        break;
//                    case 2:
//                        resetBottomView();
//                        ivStorage.setImageResource(R.mipmap.storage);
//                        tvStorage.setTextColor(tvcolor);
//                        break;
                    case 1:
                        resetBottomView();
                        ivSetting.setImageResource(R.mipmap.bt_set_fc);
                        tvSetting.setTextColor(tvcolor);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void OnReceive(String code) {

    }





    private void initFragments() {
        FragmentManager fm = getSupportFragmentManager();
        HomeOneFragment purchaseFragment = new HomeOneFragment();
//        SaleFragment saleFragment = new SaleFragment();
//        StorageFragment storageFragment = new StorageFragment();
        HomeTowFragment settingFragment = new HomeTowFragment();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(purchaseFragment);
//        fragments.add(saleFragment);
//        fragments.add(storageFragment);
        fragments.add(settingFragment);
        MenuFragmentAdapter menuFragmentAdapter = new MenuFragmentAdapter(fm, fragments);
        viewPager.setAdapter(menuFragmentAdapter);
        viewPager.setCurrentItem(0);
    }


    @OnClick(R.id.bottom_btn_purchase)
    public void onBottomBtnPurchaseClicked() {
        Log.e("bottomBar", "purchase");
        viewPager.setCurrentItem(0, true);
        resetBottomView();
        ivPurchase.setImageResource(R.mipmap.bt_pic_fc);
        tvPurchase.setTextColor(tvcolor);

    }

    @OnClick(R.id.bottom_btn_sale)
    public void onBottomBtnSaleClicked() {
        Log.e("bottomBar", "purchase");
        viewPager.setCurrentItem(1, true);
        resetBottomView();
        ivSale.setImageResource(R.mipmap.bt_set_fc);
        tvSale.setTextColor(tvcolor);
    }

    @OnClick(R.id.bottom_btn_storage)
    public void onBottomBtnStorageClicked() {
        Log.e("bottomBar", "purchase");
        viewPager.setCurrentItem(2, true);
        resetBottomView();
        ivStorage.setImageResource(R.mipmap.storage);
        tvStorage.setTextColor(tvcolor);
    }

    @OnClick(R.id.bottom_btn_setting)
    public void onBottomBtnSettingClicked() {
        viewPager.setCurrentItem(1, true);
        resetBottomView();
        ivSetting.setImageResource(R.mipmap.bt_set_fc);
        tvSetting.setTextColor(tvcolor);
    }

    private void resetBottomView() {
        ivPurchase.setImageResource(R.mipmap.bt_pic);
        ivSale.setImageResource(R.mipmap.bt_set);
        ivStorage.setImageResource(R.mipmap.unstorage);
        ivSetting.setImageResource(R.mipmap.bt_set);
        tvPurchase.setTextColor(tvColorUnClick);
        tvSale.setTextColor(tvColorUnClick);
        tvSetting.setTextColor(tvColorUnClick);
        tvStorage.setTextColor(tvColorUnClick);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
    //权限获取-------------------------------------------------------------
    private void getPermisssion() {
        String[] perm = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(mContext, perm)) {
            EasyPermissions.requestPermissions(this, "必要的权限", 0, perm);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.i("permisssion", "获取成功的权限" + perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.i("permisssion", "获取失败的权限" + perms);
    }

}
