package com.maicius.wake.InterChange;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maicius.wake.alarmClock.R;
import com.maicius.wake.web.WebService;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class Register extends Activity {
    private static final int CODE_ING = 1;   //已发送，倒计时
    private static final int CODE_REPEAT = 2;  //重新发送
    private static final int SMSDDK_HANDLER = 3;  //短信回调
    private int TIME = 60;//倒计时60s
    private String info;
    private ProgressDialog dialog;
    EditText userPhoneText, passwordText, nicknameText, verCodeText;
    String userPhone, password, nickname;
    Button registerVerCode;
    Button registerButton;
    //private static Handler handler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("maicius", "enter sign in");
        setContentView(R.layout.register);
        TextView Login = (TextView) findViewById(R.id.login);
        registerButton = (Button) findViewById(R.id.sign_up_button);
        userPhoneText = (EditText) findViewById(R.id.register_username);
        passwordText = (EditText) findViewById(R.id.register_password);
        nicknameText = (EditText) findViewById(R.id.register_nickname);
        registerVerCode = (Button)findViewById(R.id.register_ver_code);
        verCodeText = (EditText) findViewById(R.id.ver_code_text);


        initSDK();
        registerButton.setEnabled(false);
        Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LogIn();
            }
        });
        registerVerCode.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                userPhone = userPhoneText.getText().toString();
                password = passwordText.getText().toString();
                nickname =nicknameText.getText().toString();
                if (userPhoneText.getText().toString().equals("")){
                    raiseAlertDialog("提示","手机号不能为空");
                }
                else if(!isUserName(userPhoneText.getText().toString())){
                    raiseAlertDialog("提示","不能识别的手机号码");

                }
                else if (passwordText.getText().toString().equals("")) {
                    raiseAlertDialog("提示","密码不能为空");
                }
                else if(passwordText.getText().toString().length()<6
                        || passwordText.getText().toString().length()>16){
                    raiseAlertDialog("提示","密码长度必须在6-16位之间");
                }
                else{
                    new AlertDialog.Builder(Register.this)
                            .setTitle("发送短信")
                            .setMessage("我们将把验证码发送到以下号码:\n"+"+86:"+userPhone)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    SMSSDK.getVerificationCode("86", userPhone);
                                    registerVerCode.setClickable(false);
                                    new Thread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            for (int i = 60; i > 0; i--)
                                            {
                                                handler.sendEmptyMessage(CODE_ING);
                                                if (i <= 0)
                                                {
                                                    break;
                                                }
                                                try
                                                {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e)
                                                {
                                                    e.printStackTrace();
                                                }
                                            }
                                            handler.sendEmptyMessage(CODE_REPEAT);
                                        }
                                    }).start();
                                }
                            })
                            .create()
                            .show();
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPhone = userPhoneText.getText().toString();
                password = passwordText.getText().toString();
                nickname =nicknameText.getText().toString();

                if(!isUserName(userPhone)){
                    raiseAlertDialog("提示","不能识别的手机号码");
                }
                else if(passwordText.getText().toString().length()<6
                        || passwordText.getText().toString().length()>16){
                    raiseAlertDialog("提示","密码长度必须在6-16位之间");
                }
                else if(verCodeText.getText().toString().length() != 4)
                {raiseAlertDialog("提示","请输入4位验证码");
                }
                else{
                    SMSSDK.submitVerificationCode("86", userPhone, verCodeText.getText().toString());
                    dialog = new ProgressDialog(Register.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("正在注册，请稍后...");
                    dialog.setCancelable(false);
                    dialog.show();
                    //new Thread(new SignUpThread()).start();
                }
            }
        });
    }

    public class SignUpThread implements Runnable {
        public void run() {
            info = WebService.executeHttpGet(userPhone, password, nickname, WebService.State.Register);
            handler.post(new Runnable() {
                @Override
                public void run() {

                    dialog.dismiss();
                    if (info.equals("success")) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Register.this);
                        alertDialog.setTitle("注册成功").setMessage("欢迎来到Wake！");
                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Register.this.finish();
                                setContentView(R.layout.log_in);
                            }
                        });
                        alertDialog.create().show();

                    }
                    else{
                        raiseAlertDialog("注册信息","Sorry,注册失败");
                    }
                }
            });
        }
    }
    private void LogIn() {
        startActivity(new Intent(this, LogIn.class));
    }
    private boolean isUserName(String username){
        return Pattern.matches("[1][3578]\\d{9}", username);
    }
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    private void raiseAlertDialog(String title, String message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Register.this);
        alertDialog.setTitle(title).setMessage(message);
        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.create().show();
    }

  private void initSDK()
    {
        //SMSSDK.initSDK(this, "App Key", "App Secret");
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = SMSDDK_HANDLER;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }
    Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case CODE_ING://已发送,倒计时
                    //int TIME = 60;
                    registerVerCode.setText("重新发送("+--TIME+"s)");
                    break;
                case CODE_REPEAT://重新发送
                    registerVerCode.setText("获取验证码");
                    registerVerCode.setClickable(true);
                    break;
                case SMSDDK_HANDLER:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    //回调完成
                    if (result == SMSSDK.RESULT_COMPLETE)
                    {
                        registerButton.setEnabled(true);
                        //验证码验证成功
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE)
                        {
                            //Toast.makeText(Register.this, "验证成功", Toast.LENGTH_LONG).show();
                            new Thread(new SignUpThread()).start();
                        }
                        //已发送验证码
                        else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE)
                        {

                            Toast.makeText(getApplicationContext(), "验证码已经发送",
                                    Toast.LENGTH_SHORT).show();
                        } else
                        {
                            dialog.dismiss();
                            ((Throwable) data).printStackTrace();
                        }
                    }
                    if(result==SMSSDK.RESULT_ERROR)
                    {
                        dialog.dismiss();
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                Toast.makeText(getApplicationContext(), des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            //do something
                        }
                    }
                    break;
            }
        }
    };
}
