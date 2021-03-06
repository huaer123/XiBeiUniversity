package cn.com.xibeiuniversity.xibeiuniversity.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.xibeiuniversity.xibeiuniversity.R;
import cn.com.xibeiuniversity.xibeiuniversity.base.BaseActivity;

/**
 * 文件名：ForgetPasswordActivity
 * 描    述：忘记密码的类
 * 作    者：stt
 * 时    间：2017.01.19
 * 版    本：V1.0.0
 */

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private TextView titleName;
    private LinearLayout back;
    private TextView callPhone;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_forget_password);
    }

    @Override
    protected void setDate(Bundle savedInstanceState) {

    }

    @Override
    protected void init() {
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("忘记密码");
        back = (LinearLayout) findViewById(R.id.title_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        callPhone = (TextView) findViewById(R.id.forget_password_call);
        callPhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                XiBeiApp.finishTop();
                break;
            case R.id.forget_password_call:
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "6566988"));
                startActivity(phoneIntent);
                break;
        }
    }
}
