package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.UseTimeBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fangzuo.assist.Utils.CommonUtil.dealTime;

public class SetActivity extends BaseActivity {

    @BindView(R.id.ed_ip)
    EditText edIp;
    @BindView(R.id.ed_port)
    EditText edPort;
    @BindView(R.id.ed_pic_address)
    EditText edPicAddress;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private BasicShareUtil share;
    private String string;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_set);
        mContext = this;
        ButterKnife.bind(this);
        tvTitle.setText("服务器设置");
        share = BasicShareUtil.getInstance(mContext);
//        if (!share.getIP().equals("")) {
            edIp.setText(share.getIP());
//        }

//        if (!share.getPort().equals("")) {
            edPort.setText(share.getPort());
//        }

        edPicAddress.setText(Hawk.get(Config.PicServerAddress,"pic"));
    }

    @Override
    protected void initData() {
//        if (null != Hawk.get(Config.SaveTime, null)) {
//            UseTimeBean bean=Hawk.get(Config.SaveTime);
//            tvEndtime.setText("有效期：" + dealTime(bean.endTime) + "   用户码：" + Hawk.get(Config.PDA_IMIE, "获取失败")+"  注册码："+Hawk.get(Config.PDA_RegisterCode,"获取失败"));
//        } else {
//            tvEndtime.setText("获取时间失效" + "   用户码：" + Hawk.get(Config.PDA_IMIE, "获取失败")+"  注册码："+Hawk.get(Config.PDA_RegisterCode,"获取失败"));
//        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void OnReceive(String code) {
        Toast.showText(mContext, code);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btn_save,R.id.btn_back, R.id.tv_title, R.id.btn_loginout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                checkIp4Close();
                break;
            case R.id.tv_title:
                break;
            case R.id.btn_save:
                if (!edPort.getText().toString().equals("") && !edIp.getText().toString().equals("")) {
                    share.setIP(edIp.getText().toString());
                    share.setPort(edPort.getText().toString());
                    Hawk.put(Config.PicServerAddress,edPicAddress.getText().toString());
//                    finish();
                }
                break;
            case R.id.btn_loginout:
                testTomcat();
                break;
        }
    }
    private void testTomcat() {
        LoadingUtil.showDialog(mContext, "测试中...");
        share.setIP(edIp.getText().toString());
        share.setPort(edPort.getText().toString());
        final long nowtime = System.currentTimeMillis();
        App.getRService().getTest("", new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                LoadingUtil.dismiss();
                long endTime = System.currentTimeMillis();
                LoadingUtil.showAlter(mContext,"成功","测试成功，耗时"+(endTime - nowtime));
//                tvResult.setTextColor(green);
//                tvResult.setTextSize(18);
//                tvResult.setText("结果:获取150kb数据所需时间" + (endTime - nowtime) + "获取数据:" + commonResponse.returnJson);
//                tvResult.setText("结果:连接TomCat服务器成功~ \n获取150kb数据所需时间" + (endTime - nowtime) );
            }

            @Override
            public void onError(Throwable e) {
                LoadingUtil.dismiss();
                Lg.e("测试失败"+e.getMessage());
                LoadingUtil.showAlter(mContext,"ERROR","测试失败"+e.getMessage());
//                tvResult.setTextColor(red);
//                tvResult.setText(e.toString());
            }
        });
    }

    private void checkIp4Close(){
        if (!share.getIP().equals(edIp.getText().toString()) || !share.getPort().equals(edPort.getText().toString()) ){
            new AlertDialog.Builder(mContext)
                    .setTitle("提示")
                    .setMessage("是否保存更改的信息")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            share.setIP(edIp.getText().toString());
                            share.setPort(edPort.getText().toString());
                            finish();
                        }
                    })
                    .setNeutralButton("不保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .create().show();
        }else{
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        checkIp4Close();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SetActivity.class);
        context.startActivity(intent);
    }
}
