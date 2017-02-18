package cn.com.xibeiuniversity.xibeiuniversity.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.com.xibeiuniversity.xibeiuniversity.R;
import cn.com.xibeiuniversity.xibeiuniversity.base.BaseActivity;
import cn.com.xibeiuniversity.xibeiuniversity.bean.UserBean;
import cn.com.xibeiuniversity.xibeiuniversity.interfaces.LoginInterface;
import cn.com.xibeiuniversity.xibeiuniversity.utils.MyRequest;
import cn.com.xibeiuniversity.xibeiuniversity.utils.SharedUtil;
import cn.com.xibeiuniversity.xibeiuniversity.utils.ToastUtil;

/**
 * 文件名：LoginActivity
 * 描    述：登录类
 * 作    者：stt
 * 时    间：2017.01.04
 * 版    本：V1.0.0
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginInterface {
    private Context context;
    private TextView titleName;
    private EditText inputUsername, inputPassword;//用户名，密码
    private String username, password;
    private Button loginBtn;
    private TextView forgetPassword;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_login);
        context = this;
    }

    @Override
    protected void setDate(Bundle savedInstanceState) {

    }

    @Override
    protected void init() {
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("登录");
        inputUsername = (EditText) findViewById(R.id.login_username);
        inputPassword = (EditText) findViewById(R.id.login_password);
        inputUsername.setText(SharedUtil.getString(this, "userName"));
        inputPassword.setText(SharedUtil.getString(this, "passWord"));
        loginBtn = (Button) findViewById(R.id.login_button);
        loginBtn.setOnClickListener(this);
        forgetPassword = (TextView) findViewById(R.id.login_forget_password);
        forgetPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_button:
                username = inputUsername.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
                if (isEmpt()) {
                    MyRequest.loginRequest(this, username, password);
                }
                break;
            case R.id.login_forget_password:
                Intent intent = new Intent(this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    private boolean isEmpt() {
        boolean flag = false;
        if (TextUtils.isEmpty(username)) {
            ToastUtil.show(context, "请输入用户名");
            flag = false;
        } else if (TextUtils.isEmpty(password)) {
            ToastUtil.show(context, "请输入密码");
            flag = false;
        } else {
            flag = true;
        }
        return flag;
    }

    @Override
    public void login(UserBean userBean) {
        if (userBean.getPersonId()==0){
            ToastUtil.show(this,"用户名或者密码不对，请重新输入");
            return;
        }
        if (0 == userBean.getUserType()) {
            Intent intent = new Intent(this, EditorUserActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            this.finish();
        }
        if (1 == userBean.getUserType()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}
