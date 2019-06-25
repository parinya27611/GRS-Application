package com.grs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutActivity extends AppCompatActivity {

    private ListView jsonListview;
    private ListView jsonListviewabo;
    private ArrayList<String> exData;

    private ArrayList<String> exDataabo;



    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Objects.requireNonNull(getSupportActionBar()).hide();
        jsonListview = findViewById(R.id.listView1);
        exData = new ArrayList<>();

        TextView textapp= findViewById(R.id.txt_app);
        textapp.setTextColor(Color.parseColor("#FFFFFF"));

        new AsyncTask<Void, Void, Void>() {

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {

                //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------
                String url = new ConnectServer().IPc4_Address+"Getaboutser.php";
                try {

                    JSONArray data = new JSONArray(new GetHTTP().getHttpGet(url));

                    for(int i = 0; i < data.length(); i++){
                        JSONObject c = data.getJSONObject(i);

                        exData.add( c.getString("abo_textser"));
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ArrayAdapter<String>myAdapter = new ArrayAdapter<>(AboutActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, exData);
                jsonListview.setAdapter(myAdapter);
            }
        }.execute();


        jsonListviewabo = findViewById(R.id.listView2);
        exDataabo = new ArrayList<>();

        new AsyncTask<Void, Void, Void>() {

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {

                //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------
                String url = new ConnectServer().IPc4_Address+"Getaboutabo.php";
                try {

                    JSONArray data = new JSONArray(new GetHTTP().getHttpGet(url));

                    for(int i = 0; i < data.length(); i++){
                        JSONObject c = data.getJSONObject(i);

                        exDataabo.add( c.getString("abo_textabo"));
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ArrayAdapter<String>myAdapter = new ArrayAdapter<>(AboutActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, exDataabo);
                jsonListviewabo.setAdapter(myAdapter);
            }
        }.execute();


        Button back = findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent GO = new Intent(AboutActivity.this,MainActivity.class);
                startActivity(GO);
            }
        });
    }

    //font ------------------------------------------------------------------------------------
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
    //-----------------------------------------------------------------------------------------

}
