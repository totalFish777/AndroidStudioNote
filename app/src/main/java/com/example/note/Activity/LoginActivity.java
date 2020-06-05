package com.example.note.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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

public class LoginActivity extends AppCompatActivity {
    EditText et_Tel,et_pass;
    Button btn_login,btn_regist;
    Switch sw;

    RequestQueue requestQueue;
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_pass = findViewById(R.id.et_pass);
        et_Tel = findViewById(R.id.et_tel);
        btn_login = findViewById(R.id.btn_login);
        btn_regist = findViewById(R.id.btn_regist);
        sw = findViewById(R.id.rb);

        requestQueue = Volley.newRequestQueue(this);

        sp = getSharedPreferences("name",MODE_PRIVATE);
        String tel = sp.getString("tel","");
        String pass = sp.getString("pass","");
        et_pass.setText(pass);
        et_Tel.setText(tel);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(et_Tel.getText().toString(),et_pass.getText().toString());
            }
        });
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
    public void login(final String tel,final String pass){

        String url = Constants.LOGIN_URL+"?tel="+tel+"&pass="+pass;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                Result rs = gson.fromJson(s,Result.class);
                if(rs.isSuccess){
                    if(sw.isChecked()){
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("tel",tel);
                        editor.putString("pass",pass);
                        editor.commit();
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("tel",tel);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"用户名或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
