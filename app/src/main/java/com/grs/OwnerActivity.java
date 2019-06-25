package com.grs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OwnerActivity extends AppCompatActivity implements View.OnClickListener{

    EditText txt_shopname;
    EditText txt_type;
    EditText txt_address;
    EditText txt_timeop;
    EditText txt_timecl;
    EditText txt_tel;
    EditText txt_lat;
    EditText txt_lon;


    String [] SPINNERLIST = {"ร้านซ่อมมอเตอร์ไซค์","ร้านซ่อมรถยนต์-เครื่องยนต์","ร้านซ่อมรถยนต์-กระจกรถยนต์","ร้านซ่อมรถยนต์-พ่นสี","ร้านซ่อมรถยนต์-ยาง"};

    private Button btn_upimg , btn_owner , btn_open_modalmap;
    private ImageView txt_img;
    private static final  int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        Objects.requireNonNull(getSupportActionBar()).hide();
        checkStrictMode();
        showInfo();
        addItemsOnSpinner2();
        requestStoragePermission();

        TextView textapp= findViewById(R.id.txt_app);
        textapp.setTextColor(Color.parseColor("#FFFFFF"));

        Button close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GO = new Intent(OwnerActivity.this, MainActivity.class);
                startActivity(GO);
            }
        });


        //Setting clicklistener
        btn_upimg = findViewById(R.id.btn_upimg);
        btn_owner = findViewById(R.id.btn_owner);
        btn_open_modalmap = findViewById(R.id.btn_open_modalmap);
        txt_img = findViewById(R.id.txt_img);

        btn_upimg.setOnClickListener(this);
        btn_owner.setOnClickListener(this);
        btn_open_modalmap.setOnClickListener(this);

        EditText txt_nameimg=findViewById(R.id.txt_nameimg);
            txt_nameimg.setVisibility(View.GONE);
        EditText txt_nameurl=findViewById(R.id.txt_nameurl);
            txt_nameurl.setVisibility(View.GONE);
        EditText txt_oldname=findViewById(R.id.txt_oldname);
            txt_oldname.setVisibility(View.GONE);


        EditText txt_email=findViewById(R.id.txt_email);
            txt_email.setFocusable(false);
            txt_email.setClickable(false);
        EditText txt_fname=findViewById(R.id.txt_fname);
            txt_fname.setFocusable(false);
            txt_fname.setClickable(false);
        EditText txt_lname=findViewById(R.id.txt_lname);
            txt_lname.setFocusable(false);
            txt_lname.setClickable(false);



            txt_shopname = findViewById(R.id.txt_shopname);
            txt_type = findViewById(R.id.txt_type);
            txt_address = findViewById(R.id.txt_address);
            txt_timeop = findViewById(R.id.txt_timeop);
            txt_timecl = findViewById(R.id.txt_timecl);
            txt_tel = findViewById(R.id.txt_tel);
            txt_lat = findViewById(R.id.txt_lat);
            txt_lon = findViewById(R.id.txt_lon);

        txt_timeop.addTextChangedListener(new TextWatcher() {
            int prevL = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevL = txt_timeop.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if ((prevL < length) && (length == 2 )) {
                    editable.append(":");
                }
            }
        });

        txt_timecl.addTextChangedListener(new TextWatcher() {
            int prevL = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevL = txt_timecl.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if ((prevL < length) && (length == 2 )) {
                    editable.append(":");
                }
            }
        });

    }

    boolean isEmptyy(EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean isPhone(EditText text){
        CharSequence phone = text.getText().toString();
        return (!TextUtils.isEmpty(phone)&& Patterns.PHONE.matcher(phone).matches());
    }

    void checkData2() {
        if (isEmptyy(txt_shopname)) {
            AlertDialog.Builder builder1 =
                    new AlertDialog.Builder(OwnerActivity.this);
            builder1.setMessage("กรุณากรอกชื่อร้านค้า!!");
            builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder1.show();
        } else {
            if (isEmptyy(txt_type)) {
                AlertDialog.Builder builder1 =
                        new AlertDialog.Builder(OwnerActivity.this);
                builder1.setMessage("กรุณาเลือกประเภทร้านค้า!!");
                builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder1.show();
            } else {
                if (isEmptyy(txt_address)) {
                    AlertDialog.Builder builder1 =
                            new AlertDialog.Builder(OwnerActivity.this);
                    builder1.setMessage("กรุณากรอกที่อยู่ร้านค้า!!");
                    builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    builder1.show();
                } else {

                    if (isEmptyy(txt_timeop)) {
                        AlertDialog.Builder builder1 =
                                new AlertDialog.Builder(OwnerActivity.this);
                        builder1.setMessage("กรุณากรอกเวลาเปิดร้าน!!");
                        builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        builder1.show();

                    } else {
                        if (isEmptyy(txt_timecl)) {
                            AlertDialog.Builder builder1 =
                                    new AlertDialog.Builder(OwnerActivity.this);
                            builder1.setMessage("กรุณากรอกเวลาปิดร้าน!!");
                            builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                            builder1.show();

                        } else {
                            if (!isPhone(txt_tel)) {
                                AlertDialog.Builder builder1 =
                                        new AlertDialog.Builder(OwnerActivity.this);
                                builder1.setMessage("กรุณากรอกเบอโทรศัพท์!!");
                                builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                                builder1.show();

                            } else {
                                if (!isPhone(txt_lat)) {
                                    AlertDialog.Builder builder1 =
                                            new AlertDialog.Builder(OwnerActivity.this);
                                    builder1.setMessage("กรถณากรอกค่า latitude หรือกดที่รูปแผนที่ด้านขวา!!");
                                    builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                                    builder1.show();

                                } else {
                                    if (!isPhone(txt_lon)) {
                                        AlertDialog.Builder builder1 =
                                                new AlertDialog.Builder(OwnerActivity.this);
                                        builder1.setMessage("กรถณากรอกค่า longitude หรือกดที่รูปแผนที่ด้านขวา!!");
                                        builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                        builder1.show();

                                    } else {


                                        final EditText email = findViewById(R.id.txt_email);
                                        final EditText fname = findViewById(R.id.txt_fname);
                                        final EditText lname = findViewById(R.id.txt_lname);
                                        final EditText shopname = findViewById(R.id.txt_shopname);
                                        final EditText shoptype = findViewById(R.id.txt_type);
                                        final EditText address = findViewById(R.id.txt_address);
                                        final EditText timeop = findViewById(R.id.txt_timeop);
                                        final EditText timecl = findViewById(R.id.txt_timecl);
                                        final EditText tel = findViewById(R.id.txt_tel);
                                        @SuppressLint("CutPasteId") final EditText lat = findViewById(R.id.txt_lat);
                                        @SuppressLint("CutPasteId") final EditText lon = findViewById(R.id.txt_lon);

                                        String Email = email.getText().toString();
                                        String Fname = fname.getText().toString();
                                        String Lname = lname.getText().toString();
                                        String Shopname = shopname.getText().toString();
                                        String Shoptype = shoptype.getText().toString();
                                        String Address = address.getText().toString();
                                        String Timeop = timeop.getText().toString();
                                        String Timecl = timecl.getText().toString();
                                        String Tel = tel.getText().toString();
                                        String Lat = lat.getText().toString();
                                        String Lon = lon.getText().toString();
                                        String url = new ConnectServer().IPc4_Address + "InsertOwner1.php";

                                        List<NameValuePair> params = new ArrayList<>();
                                        params.add(new BasicNameValuePair("Email", Email));
                                        params.add(new BasicNameValuePair("Fname", Fname));
                                        params.add(new BasicNameValuePair("Lname", Lname));
                                        params.add(new BasicNameValuePair("Shopname", Shopname));
                                        params.add(new BasicNameValuePair("Shoptype", Shoptype));
                                        params.add(new BasicNameValuePair("Address", Address));
                                        params.add(new BasicNameValuePair("Timeop", Timeop));
                                        params.add(new BasicNameValuePair("Timecl", Timecl));
                                        params.add(new BasicNameValuePair("Tel", Tel));
                                        params.add(new BasicNameValuePair("Lat", Lat));
                                        params.add(new BasicNameValuePair("Lon", Lon));

                                        String resultServer = new PostHTTP().getHttpPost(url, params);

                                        //เช็คใน php ว่า โชว์คำว่าTS หหรือไม่ ถ้าโชว์ ให้ทำอะไร ถ้าไม่โชว์ให้ทำอะไร
                                        if (resultServer.equals("Com")) {
                                            //ไปอีก1หน้าใช้คำสั่ง inten + ชื่อตัวแปร อะไรก็ได้
                                            Intent GO = new Intent(OwnerActivity.this, MainActivity.class);
                                            GO.putExtra("owner_email", Email);
                                            startActivity(GO);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public void addInfo1() {
        if (isEmptyy(txt_shopname)) {
            AlertDialog.Builder builder1 =
                    new AlertDialog.Builder(OwnerActivity.this);
            builder1.setMessage("กรุณากรอกชื่อร้านค้า!!");
            builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder1.show();
        } else {
            if (isEmptyy(txt_type)) {
                AlertDialog.Builder builder1 =
                        new AlertDialog.Builder(OwnerActivity.this);
                builder1.setMessage("กรุณาเลือกประเภทร้านค้า!!");
                builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder1.show();
            } else {
                if (isEmptyy(txt_address)) {
                    AlertDialog.Builder builder1 =
                            new AlertDialog.Builder(OwnerActivity.this);
                    builder1.setMessage("กรุณากรอกที่อยู่ร้านค้า!!");
                    builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    builder1.show();
                } else {

                    if (isEmptyy(txt_timeop)) {
                        AlertDialog.Builder builder1 =
                                new AlertDialog.Builder(OwnerActivity.this);
                        builder1.setMessage("กรุณากรอกเวลาเปิดร้าน!!");
                        builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        builder1.show();

                    } else {
                        if (isEmptyy(txt_timecl)) {
                            AlertDialog.Builder builder1 =
                                    new AlertDialog.Builder(OwnerActivity.this);
                            builder1.setMessage("กรุณากรอกเวลาปิดร้าน!!");
                            builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                            builder1.show();

                        } else {
                            if (!isPhone(txt_tel)) {
                                AlertDialog.Builder builder1 =
                                        new AlertDialog.Builder(OwnerActivity.this);
                                builder1.setMessage("กรุณากรอกเบอโทรศัพท์!!");
                                builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                                builder1.show();
                            } else {
                                        final EditText email = findViewById(R.id.txt_email);
                                        final EditText fname = findViewById(R.id.txt_fname);
                                        final EditText lname = findViewById(R.id.txt_lname);
                                        final EditText shopname = findViewById(R.id.txt_shopname);
                                        final EditText shoptype = findViewById(R.id.txt_type);
                                        final EditText address = findViewById(R.id.txt_address);
                                        final EditText timeop = findViewById(R.id.txt_timeop);
                                        final EditText timecl = findViewById(R.id.txt_timecl);
                                        final EditText tel = findViewById(R.id.txt_tel);
                                        @SuppressLint("CutPasteId") final EditText lat = findViewById(R.id.txt_lat);
                                        @SuppressLint("CutPasteId") final EditText lon = findViewById(R.id.txt_lon);

                                        String Email = email.getText().toString();
                                        String Fname = fname.getText().toString();
                                        String Lname = lname.getText().toString();
                                        String Shopname = shopname.getText().toString();
                                        String Shoptype = shoptype.getText().toString();
                                        String Address = address.getText().toString();
                                        String Timeop = timeop.getText().toString();
                                        String Timecl = timecl.getText().toString();
                                        String Tel = tel.getText().toString();
                                        String Lat = lat.getText().toString();
                                        String Lon = lon.getText().toString();
                                        String url = new ConnectServer().IPc4_Address + "InsertOwner1.php";

                                        List<NameValuePair> params = new ArrayList<>();
                                        params.add(new BasicNameValuePair("Email", Email));
                                        params.add(new BasicNameValuePair("Fname", Fname));
                                        params.add(new BasicNameValuePair("Lname", Lname));
                                        params.add(new BasicNameValuePair("Shopname", Shopname));
                                        params.add(new BasicNameValuePair("Shoptype", Shoptype));
                                        params.add(new BasicNameValuePair("Address", Address));
                                        params.add(new BasicNameValuePair("Timeop", Timeop));
                                        params.add(new BasicNameValuePair("Timecl", Timecl));
                                        params.add(new BasicNameValuePair("Tel", Tel));
                                        params.add(new BasicNameValuePair("Lat", Lat));
                                        params.add(new BasicNameValuePair("Lon", Lon));

                                        String resultServer = new PostHTTP().getHttpPost(url, params);

                                        //เช็คใน php ว่า โชว์คำว่าTS หหรือไม่ ถ้าโชว์ ให้ทำอะไร ถ้าไม่โชว์ให้ทำอะไร
                                        if (resultServer.equals("Com")) {
                                            //ไปอีก1หน้าใช้คำสั่ง inten + ชื่อตัวแปร อะไรก็ได้
                                            Intent GO = new Intent(OwnerActivity.this, ModalmapActivity.class);
                                            GO.putExtra("owner_email", Email);
                                            startActivity(GO);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }



    private void uploadimg()
    {
        final EditText email = findViewById(R.id.txt_email);
        final EditText oldimage = findViewById(R.id.txt_nameimg);

        String Email = email.getText().toString();
        String Oldimage = oldimage.getText().toString();
        String path = getPath(filePath);

        try {
            String uploadid = UUID.randomUUID().toString();
            String UPLOAD_URL = new ConnectServer().IPc4_Address+"InsertOwner.php";
            new MultipartUploadRequest(this, uploadid, UPLOAD_URL)
                    .addFileToUpload(path, "image")
                    .addParameter("Email",Email)
                    .addParameter("Oldimage",Oldimage)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
        }catch (Exception ignored){
        }
    }

    @SuppressLint("SetTextI18n")
    public void showInfo()
    {
        //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------
        final EditText email = findViewById(R.id.txt_email);
        final EditText fname = findViewById(R.id.txt_fname);
        final EditText lname = findViewById(R.id.txt_lname);
        final EditText shopname = findViewById(R.id.txt_shopname);
        final EditText shoptype = findViewById(R.id.txt_type);
        final EditText address = findViewById(R.id.txt_address);
        final EditText timeop = findViewById(R.id.txt_timeop);
        final EditText timecl = findViewById(R.id.txt_timecl);
        final EditText tel = findViewById(R.id.txt_tel);
        final EditText latx = findViewById(R.id.txt_lat);
        final EditText lonx = findViewById(R.id.txt_lon);
        final EditText nameimg = findViewById(R.id.txt_nameimg);
        final EditText nameurl = findViewById(R.id.txt_nameurl);

        String url = new ConnectServer().IPc4_Address+"GetOwner.php";

        Intent intent= getIntent();
        final String MemberID = intent.getStringExtra("MemberID");



        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("sMemberID", MemberID));

        String resultServer  = new  PostHTTP().getHttpPost(url,params);

        //เช็คใน php ว่า โชว์คำว่าTS หหรือไม่ ถ้าโชว์ ให้ทำอะไร ถ้าไม่โชว์ให้ทำอะไร

        String strOwner_email;
        String strOwner_fname;
        String strOwner_lname;
        String strOwner_shopname;
        String strOwner_shoptype;
        String strOwner_address;
        String strOwner_timeop;
        String strOwner_timecl;
        String strOwner_tel;
        String strOwner_lat;
        String strOwner_lon;
        String strOwner_nameimg;
        String strOwner_nameurl ;

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strOwner_email = c.getString("ow_email");
            strOwner_fname = c.getString("ow_fname");
            strOwner_lname = c.getString("ow_lname");
            strOwner_shopname = c.getString("ow_shopname");
            strOwner_shoptype = c.getString("ow_type");
            strOwner_address = c.getString("ow_address");
            strOwner_timeop = c.getString("ow_open");
            strOwner_timecl = c.getString("ow_close");
            strOwner_tel = c.getString("ow_phone");
            strOwner_lat = c.getString("ow_lat");
            strOwner_lon = c.getString("ow_lon");
            strOwner_nameimg = c.getString("ow_img");
            strOwner_nameurl = c.getString("ow_url");

            if(!strOwner_email.equals(""))
            {
                email.setText(strOwner_email);
                fname.setText(strOwner_fname);
                lname.setText(strOwner_lname);
                shopname.setText(strOwner_shopname);
                shoptype.setText(strOwner_shoptype);
                address.setText(strOwner_address);
                timeop.setText(strOwner_timeop);
                timecl.setText(strOwner_timecl);
                tel.setText(strOwner_tel);
                latx.setText(strOwner_lat);
                lonx.setText(strOwner_lon);
                nameimg.setText(strOwner_nameimg);
                nameurl.setText(strOwner_nameurl);

            }
            else
            {
                email.setText("-");
                fname.setText("-");
                lname.setText("-");
                shopname.setText("-");
                shoptype.setText("-");
                address.setText("-");
                timeop.setText("-");
                timecl.setText("-");
                tel.setText("-");
                latx.setText("-");
                lonx.setText("-");
                nameimg.setText("-");
                nameurl.setText("1234");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String u = nameurl.getText().toString();
        ImageView imageView = findViewById(R.id.txt_img);
        Picasso.get().load(u).into(imageView);
    }


    //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------

    //checkStrictMode-------------------------------------------------------------------------
    public void checkStrictMode(){
        //*** Permission StrictMode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    //-----------------------------------------------------------------------------------------


    //ที่เลือกประเภทร้านค้า ------------------------------------------------------------------------------------
    public void addItemsOnSpinner2() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        MaterialBetterSpinner betterSpinner = findViewById(R.id.txt_type);
        betterSpinner.setAdapter(arrayAdapter);
    }
    //-----------------------------------------------------------------------------------------


    //อัพรุป ชอเปอมิชัน------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestStoragePermission() {
        String[] Permission = {Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        if (!hasPermissions(this, Permission)) {
            ActivityCompat.requestPermissions(this, Permission, 1);
        }
    }
    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK ) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                txt_img.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

            final EditText oldname = findViewById(R.id.txt_oldname);
            oldname.setText("1234");

        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        assert cursor != null;
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        if (view == btn_owner){
            final EditText oldname = findViewById(R.id.txt_oldname);
            String u = oldname.getText().toString();
            if (!u.isEmpty()) {
                uploadimg();
                checkData2();
            }else {
                checkData2();
            }
        }

        if (view == btn_open_modalmap){
            final EditText oldname = findViewById(R.id.txt_oldname);
            String u = oldname.getText().toString();
            if (!u.isEmpty()) {
                uploadimg();
                addInfo1();
            }else {
                addInfo1();
            }
        }

        if (view == btn_upimg) {
            requestStoragePermission();
            showFileChooser();
        }
    }

//-----------------------------------------------------------------------------------------


    //font ------------------------------------------------------------------------------------
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }


    //-----------------------------------------------------------------------------------------


}
