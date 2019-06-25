package com.grs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReportActivity extends AppCompatActivity {

    EditText email;
    EditText text1 ;
    Button sent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView textapp= findViewById(R.id.txt_app);
        textapp.setTextColor(Color.parseColor("#FFFFFF"));


        email = findViewById(R.id.txt_email);
        text1 = findViewById(R.id.txt_text);
         sent = findViewById(R.id.btn_sent);


        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });


        //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------

        //ลิ้งหน้า------------------------------------------------------------------

        Button back = findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GO = new Intent(ReportActivity.this, MainActivity.class);
                startActivity(GO);
            }
        });

    }

    boolean isEmail(EditText text){
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email)&& Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmptyy(EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void checkData() {

        if (!isEmail(email)) {
            AlertDialog.Builder builder1 =
                    new AlertDialog.Builder(ReportActivity.this);
            builder1.setMessage("กรถณากรอกอีเมลให้ถูกต้อง!!");
            builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder1.show();
        } else {
            if (isEmptyy(text1)) {
                AlertDialog.Builder builder1 =
                        new AlertDialog.Builder(ReportActivity.this);
                builder1.setMessage("กรุณากรอกข้อความ!!");
                builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder1.show();

            } else {
                String mail = email.getText().toString();
                String Txt = text1.getText().toString();

                String url = new ConnectServer().IPc4_Address + "InsertReport.php";

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("mail", mail));
                params.add(new BasicNameValuePair("Txt", Txt));

                String resultServer = new PostHTTP().getHttpPost(url, params);

                String strStatusID = "0";
                String strMemberID = "0";
                String strError = "Unknow Status!";

                JSONObject c;
                try {
                    c = new JSONObject(resultServer);
                    strStatusID = c.getString("StatusID");
                    strMemberID = c.getString("MemberID");
                    strError = c.getString("Error");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if(strStatusID.equals("0"))
                {
                    AlertDialog.Builder builder1 =
                            new AlertDialog.Builder(ReportActivity.this);
                    builder1.setTitle("Error!!");
                    builder1.setIcon(android.R.drawable.btn_star_big_on);
                    builder1.setPositiveButton("Close", null);
                    builder1.setMessage(strError);
                    builder1.show();
                }
                else if(strStatusID.equals("1"))
                {
                    Intent newActivity = new Intent(ReportActivity.this, MainActivity.class);
                    startActivity(newActivity);
                }

            }
        }
    }

    //font ------------------------------------------------------------------------------------
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
    //-----------------------------------------------------------------------------------------

}
