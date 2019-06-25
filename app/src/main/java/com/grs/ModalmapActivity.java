package com.grs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ModalmapActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    protected LocationManager locationManager;

    private Double Latitude = 0.00;
    private Double Longitude = 0.00;

    TextView latt;
    TextView lonn;
    TextView emaill;

    String provider;
    Marker mMarker;
    long minTime = 1000;
    float minDistance = 0;


    @SuppressLint({"CutPasteId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modalmap);
        Objects.requireNonNull(getSupportActionBar()).hide();
        checklocationservice();
        checkStrictMode();
        addInfo1();

        TextView textapp= findViewById(R.id.txt_app);
        textapp.setTextColor(Color.parseColor("#FFFFFF"));

        //ส่วนของกุเกิลแมพ----------------------------------------------------------------------
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        //-------------------------------------------------------------------------------


        EditText txt_lat=findViewById(R.id.txt_lat);
            txt_lat.setVisibility(View.GONE);
        EditText txt_lon=findViewById(R.id.txt_lon);
            txt_lon.setVisibility(View.GONE);
        EditText txt_email=findViewById(R.id.txt_email);
            txt_email.setVisibility(View.GONE);
        EditText txt_lon1=findViewById(R.id.txt_lon1);
            txt_lon1.setVisibility(View.GONE);

        lonn = findViewById(R.id.txt_lon);
        latt = findViewById(R.id.txt_lat);
        emaill = findViewById(R.id.txt_email);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String owner_email = bundle.getString("owner_email");
            emaill.setText("" + owner_email);
        }

        Button close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GO = new Intent(ModalmapActivity.this, OwnerActivity.class);
                EditText Emailsent = findViewById(R.id.txt_email);
                GO.putExtra("MemberID", Emailsent.getText().toString());
                startActivity(GO);
            }
        });

    }


    @Override
    public void onLocationChanged(Location loc) {
        Latitude = loc.getLatitude();
        Longitude = loc.getLongitude();

        if(mMarker != null)
            mMarker.remove();


//
//        //*** Focus & Zoom
//        LatLng coordinate = new LatLng(Latitude, Longitude);
//
//
//        //*** Marker
//        mMarker = mMap.addMarker(new MarkerOptions().position(coordinate)
//                .title("Your current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marki)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getLocatioGPS();


        //แตะแล้วจุดขึ้น---------------------------------------------------------------
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onMapClick(LatLng point) {

                if (mMarker != null)
                    mMarker.remove();

                LatLng newloca = new LatLng(point.latitude, point.longitude);
                MarkerOptions marker = new MarkerOptions().position(newloca)
                        .title("Your current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marki));
                mMarker = mMap.addMarker(marker);

                mMap.animateCamera (CameraUpdateFactory.newLatLng(newloca));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newloca, 15));


                System.out.println(point.latitude+"---"+ point.longitude);

                double lat = point.latitude;
                double lon = point.longitude;

                latt.setText("" + lat);
                lonn.setText("" + lon);

                Toast.makeText(getBaseContext(), "Lat: " + lat + ", Lon: "+lon, Toast.LENGTH_LONG).show();


            }
        });
        //ปิด แตะแล้วจุดขึ้น-----------------------------------------------------------------------------


    }


    //หาโลเคชัน----------------------------------------------------------------------------
    public void getLocatioGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (provider != null && !provider.equals("")) {
            // Get the location from the given provider

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                // int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, minTime, minDistance, this);
            if(location!=null){
                onLocationChanged(location);
            }
            else{
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }
    }

    //เช็คว่าเปิดโลเคชันหรือยัง-------------------------------------------------------------------------
    public void checklocationservice(){
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            assert lm != null;
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ignored) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ignored) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Warning Location Services!!");
            dialog.setMessage("Please Enable Location Services.");
            dialog.setNegativeButton("Cancel", null);
            dialog.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface paramDialogInterface, int arg1) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });

            dialog.show();
        }
    }
    //-----------------------------------------------------------------------------------------

    //กดหาโลเคชันตัวเอง-------------------------------------------------------------------------
    @SuppressLint("SetTextI18n")
    public void onMylocation(View view) {
        if (mMarker != null)
            mMarker.remove();


        //*** Focus & Zoom & Marker ตัวเอง------------------------------------------------
        LatLng coordinate = new LatLng(Latitude, Longitude);

        mMarker = mMap.addMarker(new MarkerOptions().position(coordinate)
                .title("Your current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marki)));
        mMap.animateCamera (CameraUpdateFactory.newLatLng(coordinate));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));
        //----------------------------------------------------------------------------

        // ฟังก์ชันประเภทของแมพ-----------------------------------------------------------------
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings settings = mMap.getUiSettings();
        //กดที่ จุด มีสัญลักขึ้น
        settings.setMapToolbarEnabled(false);
        //2นิ้วหมุนกล้อง
        settings.setRotateGesturesEnabled(false);
        //ปุ่มซูม
        settings.setZoomControlsEnabled(false);
        //นิ้วซูม
        settings.setZoomGesturesEnabled(true);
        //------------------------------------------------------------------------------

        latt.setText("" + Latitude);
        lonn.setText("" + Longitude);

        Toast.makeText(getBaseContext(), "Lat: " + Latitude + ", Lon: "+Longitude, Toast.LENGTH_LONG).show();

    }
    //-----------------------------------------------------------------------------------------

    //กดหาโลเคชันตัวเอง-------------------------------------------------------------------------
    public void zoommap(View view) {
        if (view.getId() == R.id.btn_zoomin) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if (view.getId() == R.id.btn_zoomout) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }
    //-----------------------------------------------------------------------------------------


    //checkStrictMode-------------------------------------------------------------------------
    public void checkStrictMode(){
        //*** Permission StrictMode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    //-----------------------------------------------------------------------------------------



    public void addInfo1()
    {
        //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------
        final EditText email = findViewById(R.id.txt_email);
        final EditText lat = findViewById(R.id.txt_lat);
        final EditText lon = findViewById(R.id.txt_lon);
        Button modalmap = findViewById(R.id.btn_sentlatlon);
        modalmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Lat = lat.getText().toString();
                String Lon = lon.getText().toString();


                String url = new ConnectServer().IPc4_Address+"Insertlatlon.php";

                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("Email", Email));
                params.add(new BasicNameValuePair("Lat", Lat));
                params.add(new BasicNameValuePair("Lon", Lon));

                String resultServer  = new  PostHTTP().getHttpPost(url,params);

                //เช็คใน php ว่า โชว์คำว่าTS หหรือไม่ ถ้าโชว์ ให้ทำอะไร ถ้าไม่โชว์ให้ทำอะไร
                if (resultServer.equals("Com"))
                {
                    //ไปอีก1หน้าใช้คำสั่ง inten + ชื่อตัวแปร อะไรก็ได้
                    Intent GO = new Intent(ModalmapActivity.this, OwnerActivity.class);
                    EditText Emailsent = findViewById(R.id.txt_email);
                    GO.putExtra("MemberID", Emailsent.getText().toString());
                    startActivity(GO);

                }
                else
                {
                    //Tose
                    Toast.makeText(ModalmapActivity.this,"CreateUser Fail",Toast.LENGTH_SHORT).show();

                }

            }
        });
        //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------

    }



    //font -------------------------------------------------------------------------------------
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
    //-----------------------------------------------------------------------------------------


}
