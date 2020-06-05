package com.example.note;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.note.bean.NoteBean;
import com.example.note.constant.Constants;
import com.example.note.result.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
ListView lv;
Button btn;
RequestQueue requestQueue;
String tel;
ArrayList<NoteBean> datas;
MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.lv);
        btn = findViewById(R.id.btn_add);

        requestQueue = Volley.newRequestQueue(this);
        tel = getIntent().getStringExtra("tel");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String url = Constants.DELETE_NOTE+"?id="+datas.get(position).getId();
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        Result result = gson.fromJson(s,Result.class);
                        if (result.isSuccess){
                            datas.remove(position);
                            myAdapter = new MyAdapter();
                            lv.setAdapter(myAdapter);
                            Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),result.msg,Toast.LENGTH_SHORT).show();
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
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String url = Constants.GET_ALL_NOTES+"?tel="+tel;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                datas = gson.fromJson(s,new TypeToken<ArrayList<NoteBean>>(){}.getType());
                myAdapter = new MyAdapter();
                lv.setAdapter(myAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    public class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item,null);
            TextView tv_title = convertView.findViewById(R.id.tv_title);
            TextView tv_content = convertView.findViewById(R.id.tv_content);

            tv_title.setText(datas.get(position).getTitle());
            tv_content.setText(datas.get(position).getContent());
            return convertView;
        }
    }
}
