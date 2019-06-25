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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CreateUserActivity extends AppCompatActivity {
    EditText fname;
    EditText lname;
    EditText email;
    EditText pass ;
    EditText cpass ;
    Button regis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Objects.requireNonNull(getSupportActionBar()).hide();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView textapp= findViewById(R.id.txt_app);
        textapp.setTextColor(Color.parseColor("#FFFFFF"));

        fname = findViewById(R.id.txt_fname);
        lname = findViewById(R.id.txt_lname);
        email = findViewById(R.id.txt_email);
        regis = findViewById(R.id.btn_regis);

        pass = findViewById(R.id.txt_pass);
        pass.setTransformationMethod(new PasswordTransformationMethod());

        cpass = findViewById(R.id.txt_cpass);
        cpass.setTransformationMethod(new PasswordTransformationMethod());

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });

        //ลิ้งหน้า------------------------------------------------------------------

        Button close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent GO = new Intent(CreateUserActivity.this,LoginActivity.class);
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
        if (isEmptyy(fname)) {
            AlertDialog.Builder builder1 =
                    new AlertDialog.Builder(CreateUserActivity.this);
            builder1.setMessage("กรุรากรอกชื่อ!!");
            builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder1.show();
        } else {
            if (isEmptyy(lname)) {
                AlertDialog.Builder builder1 =
                        new AlertDialog.Builder(CreateUserActivity.this);
                builder1.setMessage("กรุณากรอกนามสกุล!!");
                builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder1.show();
            } else {
                    if (!isEmail(email)) {
                        AlertDialog.Builder builder1 =
                                new AlertDialog.Builder(CreateUserActivity.this);
                        builder1.setMessage("กรุณากรอกอีเมลให้ถูกต้อง!!");
                        builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        builder1.show();
                    } else {

                        if (isEmptyy(pass)) {
                            AlertDialog.Builder builder1 =
                                    new AlertDialog.Builder(CreateUserActivity.this);
                            builder1.setMessage("กรุณากรอกรหัสผ่าน!!");
                            builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                            builder1.show();

                        } else {
                            if (isEmptyy(cpass)) {
                                AlertDialog.Builder builder1 =
                                        new AlertDialog.Builder(CreateUserActivity.this);
                                builder1.setMessage("กรุณากรอกรหัสผ่าน!!");
                                builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                                builder1.show();

                            } else {
                                if (!pass.getText().toString().trim().equals(cpass.getText().toString().trim())) {
                                    AlertDialog.Builder builder1 =
                                            new AlertDialog.Builder(CreateUserActivity.this);
                                    builder1.setMessage("กรุณากรอกรหัสผ่านให้เหมือนกัน!!");
                                    builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                                    builder1.show();

                                } else {

                                    String Fname = fname.getText().toString();
                                    String Lname = lname.getText().toString();
                                    String Email = email.getText().toString();
                                    String Pass = pass.getText().toString();

                                    String url = new ConnectServer().IPc4_Address + "InsertRegis.php";

                                    List<NameValuePair> params = new ArrayList<>();
                                    params.add(new BasicNameValuePair("Fname", Fname));
                                    params.add(new BasicNameValuePair("Lname", Lname));
                                    params.add(new BasicNameValuePair("Email", Email));
                                    params.add(new BasicNameValuePair("Pass", Pass));

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
                                                new AlertDialog.Builder(CreateUserActivity.this);
                                        builder1.setTitle("Error!!");
                                        builder1.setIcon(android.R.drawable.btn_star_big_on);
                                        builder1.setPositiveButton("Close", null);
                                        builder1.setMessage(strError);
                                        builder1.show();
                                    }
                                    else if(strStatusID.equals("1"))
                                    {
                                        Intent newActivity = new Intent(CreateUserActivity.this, LoginActivity.class);
                                        startActivity(newActivity);
                                    }
                                }
                            }
                        }
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
