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
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    EditText email ;
    EditText password ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        checkStrictMode();

        TextView textapp= findViewById(R.id.txt_app);
        textapp.setTextColor(Color.parseColor("#FFFFFF"));

        email = findViewById(R.id.txt_email);

        password = findViewById(R.id.txt_pass);
        password.setTransformationMethod(new PasswordTransformationMethod());

        Button login = findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });

        Button register = findViewById(R.id.register_create);
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent GO = new Intent(LoginActivity.this,CreateUserActivity.class);
                startActivity(GO);
            }
        });

        Button back = findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent GO = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(GO);
            }
        });

        TextView repass = findViewById(R.id.repass);
        repass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent GO = new Intent(LoginActivity.this,RepasswordActivity.class);
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
                    new AlertDialog.Builder(LoginActivity.this);
            builder1.setMessage("กรุณากรอกอีเมลให้ถูกต้อง!!");
            builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder1.show();
        } else {
            if (isEmptyy(password)) {
                AlertDialog.Builder builder1 =
                        new AlertDialog.Builder(LoginActivity.this);
                builder1.setMessage("กรุณากรอกรหัสผ่าน!!");
                builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder1.show();

            } else {

                String Email = email.getText().toString();
                String Pass = password.getText().toString();

                String url = new ConnectServer().IPc4_Address+"Login.php";

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("Email", Email));
                params.add(new BasicNameValuePair("Pass", Pass));

                String resultServer  = new  PostHTTP().getHttpPost(url,params);

                //คำสั่งตรวจสอบ แล้วส่งไอดีไปอีกหน้า

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


                // Prepare Login
                if(strStatusID.equals("0"))
                {
                    AlertDialog.Builder builder1 =
                            new AlertDialog.Builder(LoginActivity.this);
                    builder1.setTitle("Error!!");
                    builder1.setIcon(android.R.drawable.btn_star_big_on);
                    builder1.setPositiveButton("Close", null);
                    builder1.setMessage(strError);
                    builder1.show();
                }
                else if(strStatusID.equals("1"))
                {
                    Intent newActivity = new Intent(LoginActivity.this, OwnerActivity.class);
                    newActivity.putExtra("MemberID", strMemberID);
                    startActivity(newActivity);
                }
            }
        }
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
