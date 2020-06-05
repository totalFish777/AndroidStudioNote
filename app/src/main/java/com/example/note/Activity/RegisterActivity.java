package com.example.note.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.note.MainActivity;
import com.example.note.R;
import com.example.note.constant.Constants;
import com.example.note.result.Result;
import com.google.gson.Gson;

public class RegisterActivity extends AppCompatActivity {

    EditText et_name,et_pass,et_pass_conf,et_tel,et_qq,et_wechat;
    Button btn;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        et_name = findViewById(R.id.et_name);
        et_pass = findViewById(R.id.et_pass);
        et_pass_conf = findViewById(R.id.et_pass_conf);
        et_tel = findViewById(R.id.et_tel);
        et_qq = findViewById(R.id.et_qq);
        et_wechat = findViewById(R.id.et_wechat);

        requestQueue = Volley.newRequestQueue(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(et_name.getText().toString(),et_pass.getText().toString(),et_pass_conf.getText().toString(),
                        et_tel.getText().toString(),et_qq.getText().toString(),et_wechat.getText().toString());
            }
        });
    }
    public void register(String name,String pass,String passConf,final String tel,final String qq,final String wechat){
        if(tel==null||tel.equals("")){
            Toast.makeText(this,"电话不允许为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass==null||pass.equals("")){
            Toast.makeText(this,"密码不允许为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pass.equals(passConf)){
            Toast.makeText(this,"两次密码不一致,请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
        if(name==null||name.equals("")){
            Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(qq==null||qq.equals("")){
            Toast.makeText(this,"qq账号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(wechat==null||wechat.equals("")){
            Toast.makeText(this,"微信账号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Constants.REGISTER_URL+"?name="+name+"&pass="+pass+"&tel="+tel+"&qq="+qq+"&wechat="+wechat;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                Result result = gson.fromJson(s, Result.class);
                if (result.isSuccess) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("tel", tel);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), result.msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"网络出错",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }
}
