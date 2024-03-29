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
import com.fangzuo.assist.Fragment.P1OneFragment;
import com.fangzuo.assist.Fragment.PurchaseFragment;
import com.fangzuo.assist.Fragment.SaleFragment;
import com.fangzuo.assist.Fragment.SettingFragment;
import com.fangzuo.assist.Fragment.StorageFragment;
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

public class MenuActivity extends BaseActivity  implements EasyPermissions.PermissionCallbacks {


    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.iv_purchase)
    ImageView ivPurchase;
    @BindView(R.id.tv_purchase)
    TextView tvPurchase;
    @BindView(R.id.bottom_btn_purchase)
    LinearLayout bottomBtnPurchase;
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
    @BindView(R.id.bottom_btn_storage)
    LinearLayout bottomBtnStorage;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.bottom_btn_setting)
    LinearLayout bottomBtnSetting;
    @BindColor(R.color.bottombartv)
    int tvcolor;
    @BindColor(R.color.fragment_text)
    int tvColorUnClick;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.update_data)
    TextView updateData;
    @BindView(R.id.container)
    CoordinatorLayout container;
    private MenuActivity mContext;
    private FragmentTransaction ft;
    private Fragment curFragment;
    private DaoSession daoSession;
    private AsyncSession asyncSession;
    private AsyncSession asyncSession2;
    private long nowTime;
    private int size;
    private ProgressDialog pg;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    long nowTime = (long) msg.obj;
                    int size = msg.arg1;
                    long endTime = System.currentTimeMillis();
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("下载完成");
                    ab.setMessage("耗时:" + (endTime - nowTime) + "ms" + ",共插入" + size + "条数据");
                    ab.setPositiveButton("确认",null);
                    ab.create().show();
                    break;
                case 2:
                    Toast.showText(mContext, "线程2完成");
                    Log.e("线程2回调", "线程2完成");
                    break;
            }
        }
    };
    @Override
    public void initView() {
        setContentView(R.layout.activity_menu);
        mContext = this;
        BasicShareUtil share = BasicShareUtil.getInstance(mContext);
        DaoSession session = GreenDaoManager.getmInstance(mContext).getDaoSession();
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
        tvUser.setText("当前用户:" + ShareUtil.getInstance(mContext).getUserName());
        ivSale.setImageResource(R.mipmap.sale);
        tvSale.setTextColor(tvcolor);
    }


    @Override
    public void initListener() {
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownLoadData.getInstance(mContext,container,handler).alertToChoose();
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        resetBottomView();
                        ivPurchase.setImageResource(R.mipmap.purchase);
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
                        ivSetting.setImageResource(R.mipmap.setting_focus);
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
        P1OneFragment purchaseFragment = new P1OneFragment();
//        SaleFragment saleFragment = new SaleFragment();
//        StorageFragment storageFragment = new StorageFragment();
        SettingFragment settingFragment = new SettingFragment();
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
        ivPurchase.setImageResource(R.mipmap.purchase);
        tvPurchase.setTextColor(tvcolor);

    }

    @OnClick(R.id.bottom_btn_sale)
    public void onBottomBtnSaleClicked() {
        Log.e("bottomBar", "purchase");
        viewPager.setCurrentItem(1, true);
        resetBottomView();
        ivSale.setImageResource(R.mipmap.sale);
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
        ivSetting.setImageResource(R.mipmap.setting_focus);
        tvSetting.setTextColor(tvcolor);
    }

    private void resetBottomView() {
        ivPurchase.setImageResource(R.mipmap.unpurchase);
        ivSale.setImageResource(R.mipmap.unsale);
        ivStorage.setImageResource(R.mipmap.unstorage);
        ivSetting.setImageResource(R.mipmap.unsetting);
        tvPurchase.setTextColor(tvColorUnClick);
        tvSale.setTextColor(tvColorUnClick);
        tvSetting.setTextColor(tvColorUnClick);
        tvStorage.setTextColor(tvColorUnClick);
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        onBackPressed();
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
