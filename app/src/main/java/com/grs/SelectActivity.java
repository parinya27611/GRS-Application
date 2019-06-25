package com.grs;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Objects.requireNonNull(getSupportActionBar()).hide();
        checkStrictMode();

        TextView textapp= findViewById(R.id.txt_app);
        textapp.setTextColor(Color.parseColor("#FFFFFF"));

        TextView latx = findViewById(R.id.txt_lat);
        TextView lonx = findViewById(R.id.txt_lon);
        TextView nameurl = findViewById(R.id.txt_nameurl);
        TextView txt_latmy = findViewById(R.id.txt_latmy);
        TextView txt_lonmy = findViewById(R.id.txt_lonmy);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String latmy = bundle.getString("latmy");
            String lonmy = bundle.getString("lonmy");
            txt_latmy.setText("" + latmy);
            txt_lonmy.setText("" + lonmy);
        }

        showInfo();

        double Latitude = Double.parseDouble((String) latx.getText());
        double Longitude = Double.parseDouble((String) lonx.getText());
        double Latitudemy = Double.parseDouble((String) txt_latmy.getText());
        double Longitudemy = Double.parseDouble((String) txt_lonmy.getText());


        final LatLng sydney = new LatLng(Latitude, Longitude);
        final LatLng myHome = new LatLng(Latitudemy, Longitudemy);


        latx.setVisibility(View.GONE);
        lonx.setVisibility(View.GONE);
        nameurl.setVisibility(View.GONE);
        txt_latmy.setVisibility(View.GONE);
        txt_lonmy.setVisibility(View.GONE);




        Button letgo = findViewById(R.id.btn_letgo);
        letgo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent GO = new Intent(SelectActivity.this,MainActivity.class);
                startActivity(GO);

                switch (view.getId()) {
                    case R.id.btn_letgo:
                        openGoogleMap(myHome, sydney);
                        break;
                }


            }
        });

        Button back = findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent GO = new Intent(SelectActivity.this,MainActivity.class);
                startActivity(GO);
            }
        });

    }

    private void openGoogleMap(LatLng src, LatLng dest) {
        String url = "http://maps.google.com/maps?saddr="+src.latitude+","+src.longitude+"&daddr="+dest.latitude+","+dest.longitude+"&mode=driving";
        Uri gmmIntentUri = Uri.parse(url);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        try
        {
            startActivity(mapIntent);
        }
        catch(ActivityNotFoundException ex)
        {
            try
            {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(unrestrictedIntent);
            }
            catch(ActivityNotFoundException innerEx)
            {
                Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void showInfo()
    {
        //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------
        final TextView shopname = findViewById(R.id.txt_shopname);
        final TextView shoptype = findViewById(R.id.txt_type);
        final TextView address = findViewById(R.id.txt_address);
        final TextView timeop = findViewById(R.id.txt_timeop);
        final TextView timecl = findViewById(R.id.txt_timecl);
        final TextView tel = findViewById(R.id.txt_tel);
        final TextView latx = findViewById(R.id.txt_lat);
        final TextView lonx = findViewById(R.id.txt_lon);
        final TextView nameurl = findViewById(R.id.txt_nameurl);


        String url = new ConnectServer().IPc4_Address+"GetOwner.php";

        Intent intent= getIntent();
        final String MemberID = intent.getStringExtra("owner_email");


        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("sMemberID", MemberID));

        String resultServer  = new  PostHTTP().getHttpPost(url,params);

        //เช็คใน php ว่า โชว์คำว่าTS หหรือไม่ ถ้าโชว์ ให้ทำอะไร ถ้าไม่โชว์ให้ทำอะไร


        String strOwner_shopname;
        String strOwner_shoptype;
        String strOwner_address;
        String strOwner_timeop;
        String strOwner_timecl;
        String strOwner_tel;
        String strOwner_lat;
        String strOwner_lon;
        String strOwner_nameurl ;

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strOwner_shopname = c.getString("ow_shopname");
            strOwner_shoptype = c.getString("ow_type");
            strOwner_address = c.getString("ow_address");
            strOwner_timeop = c.getString("ow_open");
            strOwner_timecl = c.getString("ow_close");
            strOwner_tel = c.getString("ow_phone");
            strOwner_lat = c.getString("ow_lat");
            strOwner_lon = c.getString("ow_lon");
            strOwner_nameurl = c.getString("ow_url");

            if(!strOwner_shopname.equals(""))
            {
                shopname.setText("ชื่อร้าน : "+strOwner_shopname);
                shoptype.setText(strOwner_shoptype);
                address.setText(strOwner_address);
                timeop.setText("เวลาเปิด "+strOwner_timeop+" น.");
                timecl.setText("เวลาปิด "+strOwner_timecl+" น.");
                tel.setText(strOwner_tel);
                latx.setText(strOwner_lat);
                lonx.setText(strOwner_lon);
                nameurl.setText(strOwner_nameurl);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String u = nameurl.getText().toString();
        ImageView imageView = findViewById(R.id.txt_img);
        Picasso.get().load(u).into(imageView);
    }

    //checkStrictMode-------------------------------------------------------------------------
    public void checkStrictMode(){
        //*** Permission StrictMode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    //-----------------------------------------------------------------------------------------



    //font ------------------------------------------------------------------------------------
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
    //-----------------------------------------------------------------------------------------



}

