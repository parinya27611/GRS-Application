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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    protected LocationManager locationManager;

    private Double Latitude = 0.00;
    private Double Longitude = 0.00;

    String provider;
    Marker mMarker;
    long minTime = 100;
    float minDistance = 1;


    Button btn_car;
    Button btn_car0;
    Button btn_car1;
    Button btn_car2;
    Button btn_car3;
    Button btn_car4;
    Button btn_motor;
    Button btn_motor0;
    @SuppressLint({"SetTextI18n", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
        checklocationservice();
        checkStrictMode();

        //ตัวนำทางไปเมนู----------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //-------------------------------------------------------------------------------

        //ส่วนของกุเกิลแมพ----------------------------------------------------------------------
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        //-------------------------------------------------------------------------------

        TextView textapp= findViewById(R.id.txt_app);
        textapp.setTextColor(Color.parseColor("#FFFFFF"));

        Button motor0 = findViewById(R.id.btn_motor0);
        Button car0 = findViewById(R.id.btn_car0);
        Button car1 = findViewById(R.id.btn_car1);
        Button car2 = findViewById(R.id.btn_car2);
        Button car3 = findViewById(R.id.btn_car3);
        Button car4 = findViewById(R.id.btn_car4);
        motor0.setVisibility(View.GONE);
        car0.setVisibility(View.GONE);
        car1.setVisibility(View.GONE);
        car2.setVisibility(View.GONE);
        car3.setVisibility(View.GONE);
        car4.setVisibility(View.GONE);


        btn_motor=findViewById(R.id.btn_motor);
        btn_motor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Button motor0 = findViewById(R.id.btn_motor0);
                btn_car.setBackgroundColor(Color.WHITE);
                motor0.setBackgroundColor(0xFF52BBAB);
                TextView textmotor = findViewById(R.id.btn_motor);
                textmotor.setTextColor(Color.parseColor("#FFFFFF"));
                TextView textcar = findViewById(R.id.btn_car);
                textcar.setTextColor(Color.parseColor("#000000"));

                Button car = findViewById(R.id.btn_car);
                Button car0 = findViewById(R.id.btn_car0);
                Button car1 = findViewById(R.id.btn_car1);
                Button car2 = findViewById(R.id.btn_car2);
                Button car3 = findViewById(R.id.btn_car3);
                Button car4 = findViewById(R.id.btn_car4);
                Button motor = findViewById(R.id.btn_motor);
                motor0.setVisibility(View.VISIBLE);
                motor.setVisibility(View.GONE);
                car.setVisibility(View.VISIBLE);
                car0.setVisibility(View.GONE);
                car1.setVisibility(View.GONE);
                car2.setVisibility(View.GONE);
                car3.setVisibility(View.GONE);
                car4.setVisibility(View.GONE);
                car1.setBackgroundColor(Color.WHITE);
                car2.setBackgroundColor(Color.WHITE);
                car3.setBackgroundColor(Color.WHITE);
                car4.setBackgroundColor(Color.WHITE);
                TextView textcar1= findViewById(R.id.btn_car1);
                TextView textcar2= findViewById(R.id.btn_car2);
                TextView textcar3= findViewById(R.id.btn_car3);
                TextView textcar4= findViewById(R.id.btn_car4);
                textcar1.setTextColor(Color.parseColor("#000000"));
                textcar2.setTextColor(Color.parseColor("#000000"));
                textcar3.setTextColor(Color.parseColor("#000000"));
                textcar4.setTextColor(Color.parseColor("#000000"));

                mMap.clear();
//เกี่ยวกับฐานข้อมูล---------------------------------------------------------------
                ArrayList<HashMap<String, String>> location = null;
                String url = new ConnectServer().IPc4_Address+"Getlocationowner.php";
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("Mark", String.valueOf(5)));

                try {

                    JSONArray data = new JSONArray(new  PostHTTP().getHttpPost(url,params));

                    location = new ArrayList<>();
                    HashMap<String, String> map;

                    for(int i = 0; i < data.length(); i++){
                        JSONObject c = data.getJSONObject(i);

                        map = new HashMap<>();
                        map.put("id_owner", c.getString("id_owner"));
                        map.put("owner_lat", c.getString("owner_lat"));
                        map.put("owner_lon", c.getString("owner_lon"));
                        map.put("owner_shopname", c.getString("owner_shopname"));
                        map.put("owner_email", c.getString("owner_email"));

                        location.add(map);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    //*** Marker (Loop) วนลูปตำแหน่งตามฐานข้อมูล-------------------------------------------
                    assert location != null;
                    for (int x = 0; x < location.size(); x++) {
                        Double latitudemotor = Double.parseDouble(Objects.requireNonNull(location.get(x).get("owner_lat")));
                        Double longitudemotor = Double.parseDouble(Objects.requireNonNull(location.get(x).get("owner_lon")));
                        final String emailmotor = location.get(x).get("owner_email");  ุึคด

                        MarkerOptions markermotor = new MarkerOptions().position(new LatLng(latitudemotor, longitudemotor))
                                .title("").snippet(emailmotor).icon(BitmapDescriptorFactory.fromResource(R.drawable.markmotor));
                        mMap.addMarker(markermotor);

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                            Marker currentShown;
                            public boolean onMarkerClick(Marker marker) {
                                if (marker.equals(currentShown)) {
                                    marker.hideInfoWindow();
                                    currentShown = null;
                                } else {
                                    marker.showInfoWindow();
                                    currentShown = marker;
                                }
                                return true;
                            }
                        });
                        //----------------------------------------------------------------------------
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            // Return null here, so that getInfoContents() is called next.
                            public View getInfoWindow(Marker arg0) {
                                if (arg0 != null && arg0.getTitle().equals("")) {
                                    final EditText shopname = findViewById(R.id.txt_nameshop);
                                    String xx = arg0.getSnippet();
                                    shopname.setText(xx);

                                    String latmy = Latitude.toString();
                                    String lonmy = Longitude.toString();

                                    Intent GO = new Intent(MainActivity.this, SelectActivity.class);
                                    GO.putExtra("owner_email", shopname.getText().toString());
                                    GO.putExtra("latmy", latmy);
                                    GO.putExtra("lonmy", lonmy);
                                    startActivity(GO);
                                }
                                return null;
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public View getInfoContents(Marker marker) {
                                return null;
                            }
                        });
                        //----------------------------------------------------------------------------


                    }
                }
                catch (Exception ignored){
                }




                //*** Focus & Zoom & Marker ตัวเอง------------------------------------------------
                LatLng coordinate = new LatLng(Latitude, Longitude);

                mMarker = mMap.addMarker(new MarkerOptions().position(coordinate)
                        .title("Your current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marki)));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));

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
            }
        });

        btn_motor0=findViewById(R.id.btn_motor0);
        btn_motor0.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                btn_car.setBackgroundColor(Color.WHITE);
                btn_motor.setBackgroundColor(Color.WHITE);
                TextView textcar = findViewById(R.id.btn_car);
                textcar.setTextColor(Color.parseColor("#000000"));
                TextView textmotor = findViewById(R.id.btn_motor);
                textmotor.setTextColor(Color.parseColor("#000000"));

                Button car = findViewById(R.id.btn_car);
                Button car0 = findViewById(R.id.btn_car0);
                Button car1 = findViewById(R.id.btn_car1);
                Button car2 = findViewById(R.id.btn_car2);
                Button car3 = findViewById(R.id.btn_car3);
                Button car4 = findViewById(R.id.btn_car4);
                Button motor = findViewById(R.id.btn_motor);
                Button motor0 = findViewById(R.id.btn_motor0);
                motor.setVisibility(View.VISIBLE);
                motor0.setVisibility(View.GONE);
                car.setVisibility(View.VISIBLE);
                car0.setVisibility(View.GONE);
                car1.setVisibility(View.GONE);
                car2.setVisibility(View.GONE);
                car3.setVisibility(View.GONE);
                car4.setVisibility(View.GONE);
                car1.setBackgroundColor(Color.WHITE);
                car2.setBackgroundColor(Color.WHITE);
                car3.setBackgroundColor(Color.WHITE);
                car4.setBackgroundColor(Color.WHITE);
                TextView textcar1= findViewById(R.id.btn_car1);
                TextView textcar2= findViewById(R.id.btn_car2);
                TextView textcar3= findViewById(R.id.btn_car3);
                TextView textcar4= findViewById(R.id.btn_car4);
                textcar1.setTextColor(Color.parseColor("#000000"));
                textcar2.setTextColor(Color.parseColor("#000000"));
                textcar3.setTextColor(Color.parseColor("#000000"));
                textcar4.setTextColor(Color.parseColor("#000000"));

                mMap.clear();

                //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------
                ArrayList<HashMap<String, String>> location = null;
                String url = new ConnectServer().IPc4_Address+"Getlocationowner.php";
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("Mark", String.valueOf(0)));

                try {

                    JSONArray data = new JSONArray(new  PostHTTP().getHttpPost(url,params));

                    location = new ArrayList<>();
                    HashMap<String, String> map;

                    for(int i = 0; i < data.length(); i++){
                        JSONObject c = data.getJSONObject(i);

                        map = new HashMap<>();
                        map.put("id_owner", c.getString("id_owner"));
                        map.put("owner_lat", c.getString("owner_lat"));
                        map.put("owner_lon", c.getString("owner_lon"));
                        map.put("owner_shopname", c.getString("owner_shopname"));
                        map.put("owner_email", c.getString("owner_email"));

                        location.add(map);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                try {
                    //*** Marker (Loop) วนลูปตำแหน่งตามฐานข้อมูล-------------------------------------------
                    assert location != null;
                    for (int x = 0; x < location.size(); x++) {
                        Double latitudemotor = Double.parseDouble(Objects.requireNonNull(location.get(x).get("owner_lat")));
                        Double longitudemotor = Double.parseDouble(Objects.requireNonNull(location.get(x).get("owner_lon")));
                        final String emailmotor = location.get(x).get("owner_email");

                        MarkerOptions markermotor = new MarkerOptions().position(new LatLng(latitudemotor, longitudemotor))
                                .title("").snippet(emailmotor).icon(BitmapDescriptorFactory.fromResource(R.drawable.marks));
                        mMap.addMarker(markermotor);

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                            Marker currentShown;
                            public boolean onMarkerClick(Marker marker) {
                                if (marker.equals(currentShown)) {
                                    marker.hideInfoWindow();
                                    currentShown = null;
                                } else {
                                    marker.showInfoWindow();
                                    currentShown = marker;
                                }
                                return true;
                            }
                        });
                        //----------------------------------------------------------------------------
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            // Return null here, so that getInfoContents() is called next.
                            public View getInfoWindow(Marker arg0) {
                                if (arg0 != null && arg0.getTitle().equals("")) {
                                    final EditText shopname = findViewById(R.id.txt_nameshop);
                                    String xx = arg0.getSnippet();
                                    shopname.setText(xx);

                                    String latmy = Latitude.toString();
                                    String lonmy = Longitude.toString();

                                    Intent GO = new Intent(MainActivity.this, SelectActivity.class);
                                    GO.putExtra("owner_email", shopname.getText().toString());
                                    GO.putExtra("latmy", latmy);
                                    GO.putExtra("lonmy", lonmy);
                                    startActivity(GO);
                                }
                                return null;
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public View getInfoContents(Marker marker) {
                                return null;
                            }
                        });
                        //----------------------------------------------------------------------------


                    }
                }
                catch (Exception ignored){
                }




                //*** Focus & Zoom & Marker ตัวเอง------------------------------------------------
                LatLng coordinate = new LatLng(Latitude, Longitude);

                mMarker = mMap.addMarker(new MarkerOptions().position(coordinate)
                        .title("Your current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marki)));

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
            }
        });



        btn_car0=findViewById(R.id.btn_car0);
        btn_car0.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                btn_car.setBackgroundColor(Color.WHITE);
                btn_motor.setBackgroundColor(Color.WHITE);
                TextView textcar = findViewById(R.id.btn_car);
                textcar.setTextColor(Color.parseColor("#000000"));
                TextView textmotor = findViewById(R.id.btn_motor);
                textmotor.setTextColor(Color.parseColor("#000000"));

                Button car = findViewById(R.id.btn_car);
                Button car0 = findViewById(R.id.btn_car0);
                Button car1 = findViewById(R.id.btn_car1);
                Button car2 = findViewById(R.id.btn_car2);
                Button car3 = findViewById(R.id.btn_car3);
                Button car4 = findViewById(R.id.btn_car4);
                car.setVisibility(View.VISIBLE);
                car0.setVisibility(View.GONE);
                car1.setVisibility(View.GONE);
                car2.setVisibility(View.GONE);
                car3.setVisibility(View.GONE);
                car4.setVisibility(View.GONE);

                car1.setBackgroundColor(Color.WHITE);
                car2.setBackgroundColor(Color.WHITE);
                car3.setBackgroundColor(Color.WHITE);
                car4.setBackgroundColor(Color.WHITE);
                TextView textcar1= findViewById(R.id.btn_car1);
                TextView textcar2= findViewById(R.id.btn_car2);
                TextView textcar3= findViewById(R.id.btn_car3);
                TextView textcar4= findViewById(R.id.btn_car4);
                textcar1.setTextColor(Color.parseColor("#000000"));
                textcar2.setTextColor(Color.parseColor("#000000"));
                textcar3.setTextColor(Color.parseColor("#000000"));
                textcar4.setTextColor(Color.parseColor("#000000"));

                mMap.clear();

            //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------
            ArrayList<HashMap<String, String>> location = null;
            String url = new ConnectServer().IPc4_Address+"Getlocationowner.php";
            List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Mark", String.valueOf(0)));


        try {

            JSONArray data = new JSONArray(new  PostHTTP().getHttpPost(url,params));

            location = new ArrayList<>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<>();
                map.put("id_owner", c.getString("id_owner"));
                map.put("owner_lat", c.getString("owner_lat"));
                map.put("owner_lon", c.getString("owner_lon"));
                map.put("owner_shopname", c.getString("owner_shopname"));
                map.put("owner_email", c.getString("owner_email"));

                location.add(map);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


                try {
                    //*** Marker (Loop) วนลูปตำแหน่งตามฐานข้อมูล-------------------------------------------
                    assert location != null;
                    for (int x = 0; x < location.size(); x++) {
                        Double latitudemotor = Double.parseDouble(Objects.requireNonNull(location.get(x).get("owner_lat")));
                        Double longitudemotor = Double.parseDouble(Objects.requireNonNull(location.get(x).get("owner_lon")));
                        final String emailmotor = location.get(x).get("owner_email");

                        MarkerOptions markermotor = new MarkerOptions().position(new LatLng(latitudemotor, longitudemotor))
                                .title("").snippet(emailmotor).icon(BitmapDescriptorFactory.fromResource(R.drawable.marks));
                        mMap.addMarker(markermotor);

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                            Marker currentShown;
                            public boolean onMarkerClick(Marker marker) {
                                if (marker.equals(currentShown)) {
                                    marker.hideInfoWindow();
                                    currentShown = null;
                                } else {
                                    marker.showInfoWindow();
                                    currentShown = marker;
                                }
                                return true;
                            }
                        });
                        //----------------------------------------------------------------------------
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            // Return null here, so that getInfoContents() is called next.
                            public View getInfoWindow(Marker arg0) {
                                if (arg0 != null && arg0.getTitle().equals("")) {
                                    final EditText shopname = findViewById(R.id.txt_nameshop);
                                    String xx = arg0.getSnippet();
                                    shopname.setText(xx);

                                    String latmy = Latitude.toString();
                                    String lonmy = Longitude.toString();

                                    Intent GO = new Intent(MainActivity.this, SelectActivity.class);
                                    GO.putExtra("owner_email", shopname.getText().toString());
                                    GO.putExtra("latmy", latmy);
                                    GO.putExtra("lonmy", lonmy);
                                    startActivity(GO);
                                }
                                return null;
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public View getInfoContents(Marker marker) {
                                return null;
                            }
                        });
                        //----------------------------------------------------------------------------


                    }
                }
                catch (Exception ignored){
                }




            //*** Focus & Zoom & Marker ตัวเอง------------------------------------------------
            LatLng coordinate = new LatLng(Latitude, Longitude);

            mMarker = mMap.addMarker(new MarkerOptions().position(coordinate)
                .title("Your current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marki)));

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
            }
        });





        btn_car=findViewById(R.id.btn_car);
        btn_car.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Button car0 = findViewById(R.id.btn_car0);
                car0.setBackgroundColor(0xFF52BBAB);
                btn_motor.setBackgroundColor(Color.WHITE);
                btn_motor0.setBackgroundColor(Color.WHITE);
                TextView textcar0 = findViewById(R.id.btn_car0);
                textcar0.setTextColor(Color.parseColor("#FFFFFF"));
                TextView textmotor = findViewById(R.id.btn_motor);
                textmotor.setTextColor(Color.parseColor("#000000"));

                Button car1 = findViewById(R.id.btn_car1);
                Button car2 = findViewById(R.id.btn_car2);
                Button car3 = findViewById(R.id.btn_car3);
                Button car4 = findViewById(R.id.btn_car4);
                Button motor = findViewById(R.id.btn_motor);
                Button motor0 = findViewById(R.id.btn_motor0);
                motor.setVisibility(View.VISIBLE);
                motor0.setVisibility(View.GONE);
                car0.setVisibility(View.VISIBLE);
                car1.setVisibility(View.VISIBLE);
                car2.setVisibility(View.VISIBLE);
                car3.setVisibility(View.VISIBLE);
                car4.setVisibility(View.VISIBLE);
            }

        });


        btn_car1=findViewById(R.id.btn_car1);
        btn_car1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Button car = findViewById(R.id.btn_car);
                car.setBackgroundColor(0xFF52BBAB);
                btn_motor.setBackgroundColor(Color.WHITE);
                TextView textcar= findViewById(R.id.btn_car);
                textcar.setTextColor(Color.parseColor("#FFFFFF"));
                TextView textmotor = findViewById(R.id.btn_motor);
                textmotor.setTextColor(Color.parseColor("#000000"));
                Button car0 = findViewById(R.id.btn_car0);
                Button car1 = findViewById(R.id.btn_car1);
                Button car2 = findViewById(R.id.btn_car2);
                Button car3 = findViewById(R.id.btn_car3);
                Button car4 = findViewById(R.id.btn_car4);
                Button motor = findViewById(R.id.btn_motor);
                Button motor0 = findViewById(R.id.btn_motor0);
                motor.setVisibility(View.VISIBLE);
                motor0.setVisibility(View.GONE);
                car0.setVisibility(View.GONE);
                car1.setVisibility(View.GONE);
                car2.setVisibility(View.GONE);
                car3.setVisibility(View.GONE);
                car4.setVisibility(View.GONE);
                car1.setBackgroundColor(0xFF52BBAB);
                car2.setBackgroundColor(Color.WHITE);
                car3.setBackgroundColor(Color.WHITE);
                car4.setBackgroundColor(Color.WHITE);
                TextView textcar1= findViewById(R.id.btn_car1);
                TextView textcar2= findViewById(R.id.btn_car2);
                TextView textcar3= findViewById(R.id.btn_car3);
                TextView textcar4= findViewById(R.id.btn_car4);
                textcar1.setTextColor(Color.parseColor("#FFFFFF"));
                textcar2.setTextColor(Color.parseColor("#000000"));
                textcar3.setTextColor(Color.parseColor("#000000"));
                textcar4.setTextColor(Color.parseColor("#000000"));


                mMap.clear();
                //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------
                ArrayList<HashMap<String, String>> locationcar = null;
                String urlcar = new ConnectServer().IPc4_Address+"Getlocationowner.php";
                List<NameValuePair> paramscar = new ArrayList<>();
                paramscar.add(new BasicNameValuePair("Mark", String.valueOf(1)));

                try {

                    JSONArray data = new JSONArray(new  PostHTTP().getHttpPost(urlcar,paramscar));

                    locationcar = new ArrayList<>();
                    HashMap<String, String> map;

                    for(int i = 0; i < data.length(); i++){
                        JSONObject c = data.getJSONObject(i);

                        map = new HashMap<>();
                        map.put("id_owner", c.getString("id_owner"));
                        map.put("owner_lat", c.getString("owner_lat"));
                        map.put("owner_lon", c.getString("owner_lon"));
                        map.put("owner_shopname", c.getString("owner_shopname"));
                        map.put("owner_email", c.getString("owner_email"));
                        locationcar.add(map);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



                try {
                    //*** Marker (Loop) วนลูปตำแหน่งตามฐานข้อมูล-------------------------------------------
                    assert locationcar != null;
                    for (int x = 0; x < locationcar.size(); x++) {
                        Double latitudecar = Double.parseDouble(Objects.requireNonNull(locationcar.get(x).get("owner_lat")));
                        Double longitudecar = Double.parseDouble(Objects.requireNonNull(locationcar.get(x).get("owner_lon")));
                        final String emailcar = locationcar.get(x).get("owner_email");

                        MarkerOptions markercar = new MarkerOptions().position(new LatLng(latitudecar, longitudecar))
                                .title("").snippet(emailcar).icon(BitmapDescriptorFactory.fromResource(R.drawable.markcar));
                        mMap.addMarker(markercar);

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                            Marker currentShown;
                            public boolean onMarkerClick(Marker marker) {
                                if (marker.equals(currentShown)) {
                                    marker.hideInfoWindow();
                                    currentShown = null;
                                } else {
                                    marker.showInfoWindow();
                                    currentShown = marker;
                                }
                                return true;
                            }
                        });
                        //----------------------------------------------------------------------------
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            // Return null here, so that getInfoContents() is called next.
                            public View getInfoWindow(Marker arg0) {
                                if (arg0 != null && arg0.getTitle().equals("")) {
                                    final EditText shopname = findViewById(R.id.txt_nameshop);
                                    String xx = arg0.getSnippet();
                                    shopname.setText(xx);

                                    String latmy = Latitude.toString();
                                    String lonmy = Longitude.toString();

                                    Intent GO = new Intent(MainActivity.this, SelectActivity.class);
                                    GO.putExtra("owner_email", shopname.getText().toString());
                                    GO.putExtra("latmy", latmy);
                                    GO.putExtra("lonmy", lonmy);
                                    startActivity(GO);
                                }
                                return null;
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public View getInfoContents(Marker marker) {
                                return null;
                            }
                        });
                        //----------------------------------------------------------------------------


                    }
                }
                catch (Exception ignored){
                }



                //*** Focus & Zoom & Marker ตัวเอง------------------------------------------------
                LatLng coordinate = new LatLng(Latitude, Longitude);

                mMarker = mMap.addMarker(new MarkerOptions().position(coordinate)
                        .title("Your current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marki)));
                //                mMap.animateCamera(CameraUpdateFactory.newLatLng(coordinate));
                //                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));

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
            }
        });




    btn_car2=findViewById(R.id.btn_car2);
        btn_car2.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Button car = findViewById(R.id.btn_car);
            car.setBackgroundColor(0xFF52BBAB);
            btn_motor.setBackgroundColor(Color.WHITE);
            TextView textcar= findViewById(R.id.btn_car);
            textcar.setTextColor(Color.parseColor("#FFFFFF"));
            TextView textmotor = findViewById(R.id.btn_motor);
            textmotor.setTextColor(Color.parseColor("#000000"));
            Button car0 = findViewById(R.id.btn_car0);
            Button car1 = findViewById(R.id.btn_car1);
            Button car2 = findViewById(R.id.btn_car2);
            Button car3 = findViewById(R.id.btn_car3);
            Button car4 = findViewById(R.id.btn_car4);
            Button motor = findViewById(R.id.btn_motor);
            Button motor0 = findViewById(R.id.btn_motor0);
            motor.setVisibility(View.VISIBLE);
            motor0.setVisibility(View.GONE);
            car0.setVisibility(View.GONE);
            car1.setVisibility(View.GONE);
            car2.setVisibility(View.GONE);
            car3.setVisibility(View.GONE);
            car4.setVisibility(View.GONE);
            car1.setBackgroundColor(Color.WHITE);
            car2.setBackgroundColor(0xFF52BBAB);
            car3.setBackgroundColor(Color.WHITE);
            car4.setBackgroundColor(Color.WHITE);
            TextView textcar1= findViewById(R.id.btn_car1);
            TextView textcar2= findViewById(R.id.btn_car2);
            TextView textcar3= findViewById(R.id.btn_car3);
            TextView textcar4= findViewById(R.id.btn_car4);
            textcar1.setTextColor(Color.parseColor("#000000"));
            textcar2.setTextColor(Color.parseColor("#FFFFFF"));
            textcar3.setTextColor(Color.parseColor("#000000"));
            textcar4.setTextColor(Color.parseColor("#000000"));

            mMap.clear();
            //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------
            ArrayList<HashMap<String, String>> locationcar = null;
            String urlcar = new ConnectServer().IPc4_Address+"Getlocationowner.php";
            List<NameValuePair> paramscar = new ArrayList<>();
            paramscar.add(new BasicNameValuePair("Mark", String.valueOf(2)));

            try {

                JSONArray data = new JSONArray(new  PostHTTP().getHttpPost(urlcar,paramscar));

                locationcar = new ArrayList<>();
                HashMap<String, String> map;

                for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);

                    map = new HashMap<>();
                    map.put("id_owner", c.getString("id_owner"));
                    map.put("owner_lat", c.getString("owner_lat"));
                    map.put("owner_lon", c.getString("owner_lon"));
                    map.put("owner_shopname", c.getString("owner_shopname"));
                    map.put("owner_email", c.getString("owner_email"));
                    locationcar.add(map);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }



            try {
                //*** Marker (Loop) วนลูปตำแหน่งตามฐานข้อมูล-------------------------------------------
                assert locationcar != null;
                for (int x = 0; x < locationcar.size(); x++) {
                    Double latitudecar = Double.parseDouble(Objects.requireNonNull(locationcar.get(x).get("owner_lat")));
                    Double longitudecar = Double.parseDouble(Objects.requireNonNull(locationcar.get(x).get("owner_lon")));
                    final String emailcar = locationcar.get(x).get("owner_email");

                    MarkerOptions markercar = new MarkerOptions().position(new LatLng(latitudecar, longitudecar))
                            .title("").snippet(emailcar).icon(BitmapDescriptorFactory.fromResource(R.drawable.markcar));
                    mMap.addMarker(markercar);

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                        Marker currentShown;
                        public boolean onMarkerClick(Marker marker) {
                            if (marker.equals(currentShown)) {
                                marker.hideInfoWindow();
                                currentShown = null;
                            } else {
                                marker.showInfoWindow();
                                currentShown = marker;
                            }
                            return true;
                        }
                    });
                    //----------------------------------------------------------------------------
                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        // Return null here, so that getInfoContents() is called next.
                        public View getInfoWindow(Marker arg0) {
                            if (arg0 != null && arg0.getTitle().equals("")) {
                                final EditText shopname = findViewById(R.id.txt_nameshop);
                                String xx = arg0.getSnippet();
                                shopname.setText(xx);

                                String latmy = Latitude.toString();
                                String lonmy = Longitude.toString();

                                Intent GO = new Intent(MainActivity.this, SelectActivity.class);
                                GO.putExtra("owner_email", shopname.getText().toString());
                                GO.putExtra("latmy", latmy);
                                GO.putExtra("lonmy", lonmy);
                                startActivity(GO);
                            }
                            return null;
                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public View getInfoContents(Marker marker) {
                            return null;
                        }
                    });
                    //----------------------------------------------------------------------------


                }
            }
            catch (Exception ignored){
            }



            //*** Focus & Zoom & Marker ตัวเอง------------------------------------------------
            LatLng coordinate = new LatLng(Latitude, Longitude);

            mMarker = mMap.addMarker(new MarkerOptions().position(coordinate)
                    .title("Your current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marki)));
            //                mMap.animateCamera(CameraUpdateFactory.newLatLng(coordinate));
            //                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));

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
        }
    });


        btn_car3=findViewById(R.id.btn_car3);
        btn_car3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Button car = findViewById(R.id.btn_car);
                car.setBackgroundColor(0xFF52BBAB);
                btn_motor.setBackgroundColor(Color.WHITE);
                TextView textcar= findViewById(R.id.btn_car);
                textcar.setTextColor(Color.parseColor("#FFFFFF"));
                TextView textmotor = findViewById(R.id.btn_motor);
                textmotor.setTextColor(Color.parseColor("#000000"));
                Button car0 = findViewById(R.id.btn_car0);
                Button car1 = findViewById(R.id.btn_car1);
                Button car2 = findViewById(R.id.btn_car2);
                Button car3 = findViewById(R.id.btn_car3);
                Button car4 = findViewById(R.id.btn_car4);
                Button motor = findViewById(R.id.btn_motor);
                Button motor0 = findViewById(R.id.btn_motor0);
                motor.setVisibility(View.VISIBLE);
                motor0.setVisibility(View.GONE);
                car0.setVisibility(View.GONE);
                car1.setVisibility(View.GONE);
                car2.setVisibility(View.GONE);
                car3.setVisibility(View.GONE);
                car4.setVisibility(View.GONE);
                car1.setBackgroundColor(Color.WHITE);
                car2.setBackgroundColor(Color.WHITE);
                car3.setBackgroundColor(0xFF52BBAB);
                car4.setBackgroundColor(Color.WHITE);
                TextView textcar1= findViewById(R.id.btn_car1);
                TextView textcar2= findViewById(R.id.btn_car2);
                TextView textcar3= findViewById(R.id.btn_car3);
                TextView textcar4= findViewById(R.id.btn_car4);
                textcar1.setTextColor(Color.parseColor("#000000"));
                textcar2.setTextColor(Color.parseColor("#000000"));
                textcar3.setTextColor(Color.parseColor("#FFFFFF"));
                textcar4.setTextColor(Color.parseColor("#000000"));

                mMap.clear();
                //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------
                ArrayList<HashMap<String, String>> locationcar = null;
                String urlcar = new ConnectServer().IPc4_Address+"Getlocationowner.php";
                List<NameValuePair> paramscar = new ArrayList<>();
                paramscar.add(new BasicNameValuePair("Mark", String.valueOf(3)));

                try {

                    JSONArray data = new JSONArray(new  PostHTTP().getHttpPost(urlcar,paramscar));

                    locationcar = new ArrayList<>();
                    HashMap<String, String> map;

                    for(int i = 0; i < data.length(); i++){
                        JSONObject c = data.getJSONObject(i);

                        map = new HashMap<>();
                        map.put("id_owner", c.getString("id_owner"));
                        map.put("owner_lat", c.getString("owner_lat"));
                        map.put("owner_lon", c.getString("owner_lon"));
                        map.put("owner_shopname", c.getString("owner_shopname"));
                        map.put("owner_email", c.getString("owner_email"));
                        locationcar.add(map);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



                try {
                    //*** Marker (Loop) วนลูปตำแหน่งตามฐานข้อมูล-------------------------------------------
                    assert locationcar != null;
                    for (int x = 0; x < locationcar.size(); x++) {
                        Double latitudecar = Double.parseDouble(Objects.requireNonNull(locationcar.get(x).get("owner_lat")));
                        Double longitudecar = Double.parseDouble(Objects.requireNonNull(locationcar.get(x).get("owner_lon")));
                        final String emailcar = locationcar.get(x).get("owner_email");

                        MarkerOptions markercar = new MarkerOptions().position(new LatLng(latitudecar, longitudecar))
                                .title("").snippet(emailcar).icon(BitmapDescriptorFactory.fromResource(R.drawable.markcar));
                        mMap.addMarker(markercar);

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                            Marker currentShown;
                            public boolean onMarkerClick(Marker marker) {
                                if (marker.equals(currentShown)) {
                                    marker.hideInfoWindow();
                                    currentShown = null;
                                } else {
                                    marker.showInfoWindow();
                                    currentShown = marker;
                                }
                                return true;
                            }
                        });
                        //----------------------------------------------------------------------------
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            // Return null here, so that getInfoContents() is called next.
                            public View getInfoWindow(Marker arg0) {
                                if (arg0 != null && arg0.getTitle().equals("")) {
                                    final EditText shopname = findViewById(R.id.txt_nameshop);
                                    String xx = arg0.getSnippet();
                                    shopname.setText(xx);

                                    String latmy = Latitude.toString();
                                    String lonmy = Longitude.toString();

                                    Intent GO = new Intent(MainActivity.this, SelectActivity.class);
                                    GO.putExtra("owner_email", shopname.getText().toString());
                                    GO.putExtra("latmy", latmy);
                                    GO.putExtra("lonmy", lonmy);
                                    startActivity(GO);
                                }
                                return null;
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public View getInfoContents(Marker marker) {
                                return null;
                            }
                        });
                        //----------------------------------------------------------------------------


                    }
                }
                catch (Exception ignored){
                }



                //*** Focus & Zoom & Marker ตัวเอง------------------------------------------------
                LatLng coordinate = new LatLng(Latitude, Longitude);

                mMarker = mMap.addMarker(new MarkerOptions().position(coordinate)
                        .title("Your current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marki)));
                //                mMap.animateCamera(CameraUpdateFactory.newLatLng(coordinate));
                //                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));

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
            }
        });


        btn_car4=findViewById(R.id.btn_car4);
        btn_car4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Button car = findViewById(R.id.btn_car);
                car.setBackgroundColor(0xFF52BBAB);
                btn_motor.setBackgroundColor(Color.WHITE);
                TextView textcar= findViewById(R.id.btn_car);
                textcar.setTextColor(Color.parseColor("#FFFFFF"));
                TextView textmotor = findViewById(R.id.btn_motor);
                textmotor.setTextColor(Color.parseColor("#000000"));
                Button car0 = findViewById(R.id.btn_car0);
                Button car1 = findViewById(R.id.btn_car1);
                Button car2 = findViewById(R.id.btn_car2);
                Button car3 = findViewById(R.id.btn_car3);
                Button car4 = findViewById(R.id.btn_car4);
                Button motor = findViewById(R.id.btn_motor);
                Button motor0 = findViewById(R.id.btn_motor0);
                motor.setVisibility(View.VISIBLE);
                motor0.setVisibility(View.GONE);
                car0.setVisibility(View.GONE);
                car1.setVisibility(View.GONE);
                car2.setVisibility(View.GONE);
                car3.setVisibility(View.GONE);
                car4.setVisibility(View.GONE);
                car1.setBackgroundColor(Color.WHITE);
                car2.setBackgroundColor(Color.WHITE);
                car3.setBackgroundColor(Color.WHITE);
                car4.setBackgroundColor(0xFF52BBAB);
                TextView textcar1= findViewById(R.id.btn_car1);
                TextView textcar2= findViewById(R.id.btn_car2);
                TextView textcar3= findViewById(R.id.btn_car3);
                TextView textcar4= findViewById(R.id.btn_car4);
                textcar1.setTextColor(Color.parseColor("#000000"));
                textcar2.setTextColor(Color.parseColor("#000000"));
                textcar3.setTextColor(Color.parseColor("#000000"));
                textcar4.setTextColor(Color.parseColor("#FFFFFF"));

                mMap.clear();
                //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------
                ArrayList<HashMap<String, String>> locationcar = null;
                String urlcar = new ConnectServer().IPc4_Address+"Getlocationowner.php";
                List<NameValuePair> paramscar = new ArrayList<>();
                paramscar.add(new BasicNameValuePair("Mark", String.valueOf(4)));

                try {

                    JSONArray data = new JSONArray(new  PostHTTP().getHttpPost(urlcar,paramscar));

                    locationcar = new ArrayList<>();
                    HashMap<String, String> map;

                    for(int i = 0; i < data.length(); i++){
                        JSONObject c = data.getJSONObject(i);

                        map = new HashMap<>();
                        map.put("id_owner", c.getString("id_owner"));
                        map.put("owner_lat", c.getString("owner_lat"));
                        map.put("owner_lon", c.getString("owner_lon"));
                        map.put("owner_shopname", c.getString("owner_shopname"));
                        map.put("owner_email", c.getString("owner_email"));
                        locationcar.add(map);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



                try {
                    //*** Marker (Loop) วนลูปตำแหน่งตามฐานข้อมูล-------------------------------------------
                    assert locationcar != null;
                    for (int x = 0; x < locationcar.size(); x++) {
                        Double latitudecar = Double.parseDouble(Objects.requireNonNull(locationcar.get(x).get("owner_lat")));
                        Double longitudecar = Double.parseDouble(Objects.requireNonNull(locationcar.get(x).get("owner_lon")));
                        final String emailcar = locationcar.get(x).get("owner_email");

                        MarkerOptions markercar = new MarkerOptions().position(new LatLng(latitudecar, longitudecar))
                                .title("").snippet(emailcar).icon(BitmapDescriptorFactory.fromResource(R.drawable.markcar));
                        mMap.addMarker(markercar);

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                            Marker currentShown;
                            public boolean onMarkerClick(Marker marker) {
                                if (marker.equals(currentShown)) {
                                    marker.hideInfoWindow();
                                    currentShown = null;
                                } else {
                                    marker.showInfoWindow();
                                    currentShown = marker;
                                }
                                return true;
                            }
                        });
                        //----------------------------------------------------------------------------
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            // Return null here, so that getInfoContents() is called next.
                            public View getInfoWindow(Marker arg0) {
                                if (arg0 != null && arg0.getTitle().equals("")) {
                                    final EditText shopname = findViewById(R.id.txt_nameshop);
                                    String xx = arg0.getSnippet();
                                    shopname.setText(xx);

                                    String latmy = Latitude.toString();
                                    String lonmy = Longitude.toString();

                                    Intent GO = new Intent(MainActivity.this, SelectActivity.class);
                                    GO.putExtra("owner_email", shopname.getText().toString());
                                    GO.putExtra("latmy", latmy);
                                    GO.putExtra("lonmy", lonmy);
                                    startActivity(GO);
                                }
                                return null;
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public View getInfoContents(Marker marker) {
                                return null;
                            }
                        });
                        //----------------------------------------------------------------------------


                    }
                }
                catch (Exception ignored){
                }



                //*** Focus & Zoom & Marker ตัวเอง------------------------------------------------
                LatLng coordinate = new LatLng(Latitude, Longitude);

                mMarker = mMap.addMarker(new MarkerOptions().position(coordinate)
                        .title("Your current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marki)));
                //                mMap.animateCamera(CameraUpdateFactory.newLatLng(coordinate));
                //                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));

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
            }
        });


    }

    //ส่วนของเมนู------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //คำสั่งในเมนูให้ลิ้งไปไหน----------------------------------------------------------------------------
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.txt_login) {
            // Handle the camera action
            Intent GO = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(GO);
        } else if (id == R.id.txt_report) {
            Intent GO = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(GO);
        } else if (id == R.id.txt_about) {
            Intent GO = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(GO);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //ปิดเมนู---------------------------------------------------------------------------


    //เริ่ม googlemap-----------------------------------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getLocatioGPS();

        if (mMarker != null)
            mMarker.remove();


        //เกี่ยวกับฐานข้อมูล---------------------------------------------------------------
        ArrayList<HashMap<String, String>> location = null;
        String url = new ConnectServer().IPc4_Address+"Getlocationowner.php";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Mark", String.valueOf(0)));


        try {

            JSONArray data = new JSONArray(new  PostHTTP().getHttpPost(url,params));

            location = new ArrayList<>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<>();
                map.put("id_owner", c.getString("id_owner"));
                map.put("owner_lat", c.getString("owner_lat"));
                map.put("owner_lon", c.getString("owner_lon"));
                map.put("owner_shopname", c.getString("owner_shopname"));
                map.put("owner_email", c.getString("owner_email"));

                location.add(map);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        try {
            //*** Marker (Loop) วนลูปตำแหน่งตามฐานข้อมูล-------------------------------------------
            assert location != null;
            for (int x = 0; x < location.size(); x++) {
                Double latitudemotor = Double.parseDouble(Objects.requireNonNull(location.get(x).get("owner_lat")));
                Double longitudemotor = Double.parseDouble(Objects.requireNonNull(location.get(x).get("owner_lon")));
                final String emailmotor = location.get(x).get("owner_email");

                MarkerOptions markermotor = new MarkerOptions().position(new LatLng(latitudemotor, longitudemotor))
                        .title("").snippet(emailmotor).icon(BitmapDescriptorFactory.fromResource(R.drawable.marks));
                mMap.addMarker(markermotor);

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                    Marker currentShown;
                    public boolean onMarkerClick(Marker marker) {
                        if (marker.equals(currentShown)) {
                            marker.hideInfoWindow();
                            currentShown = null;
                        } else {
                            marker.showInfoWindow();
                            currentShown = marker;
                        }
                        return true;
                    }
                });
                //----------------------------------------------------------------------------
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    // Return null here, so that getInfoContents() is called next.
                    public View getInfoWindow(Marker arg0) {
                        if (arg0 != null && arg0.getTitle().equals("")) {
                            final EditText shopname = findViewById(R.id.txt_nameshop);
                            String xx = arg0.getSnippet();
                            shopname.setText(xx);

                            String latmy = Latitude.toString();
                            String lonmy = Longitude.toString();

                            Intent GO = new Intent(MainActivity.this, SelectActivity.class);
                            GO.putExtra("owner_email", shopname.getText().toString());
                            GO.putExtra("latmy", latmy);
                            GO.putExtra("lonmy", lonmy);
                            startActivity(GO);
                        }
                        return null;
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public View getInfoContents(Marker marker) {
                        return null;
                    }
                });
                //----------------------------------------------------------------------------


            }
        }
        catch (Exception ignored){
        }




        //*** Focus & Zoom & Marker ตัวเอง------------------------------------------------
        LatLng coordinate = new LatLng(Latitude, Longitude);

        mMarker = mMap.addMarker(new MarkerOptions().position(coordinate)
                .title("Your current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marki)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));

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
    }
    //ปิดgooglemap-----------------------------------------------------------------------------


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

    @Override
    public void onLocationChanged(Location loc) {

        Latitude = loc.getLatitude();
        Longitude = loc.getLongitude();

        if(mMarker != null)
            mMarker.remove();

        //*** Focus & Zoom
        LatLng coordinate = new LatLng(Latitude, Longitude);


        //*** Marker
        mMarker = mMap.addMarker(new MarkerOptions().position(coordinate)
                .title("Your current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marki)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));


    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // Log.d("Latitude","disable");
    }
    @Override
    public void onProviderEnabled(String s) {
        // Log.d("Latitude","enable");
    }
    @Override
    public void onProviderDisabled(String s) {
        // Log.d("Latitude","status");
    }
    //ปิดหาโลเคชัน------------------------------------------------------------------------------


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


    //ขอเปอมิชัน สำหรับ 6.0 --------------------------------------------------------------------------
    public void checkPermission() {
        String[] Permission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
        };
        if (!hasPermissions(this, Permission)) {
            ActivityCompat.requestPermissions(this, Permission, 1);
        }

    }
    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("กรุณาปฏิบัติตามคำบอกด้านล่าง");
                    builder.setMessage("กรุณากดออกจากระบบ" +
                            " แล้วกดเปิดแอพพลิเคชันใหม่");
                    builder.setPositiveButton("ออกจากระบบ", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            finish();
                        }
                    });
                    builder.show();

                    return false;
                }
            }
        }
        return true;
    }
    //-----------------------------------------------------------------------------------------


    //checkStrictMode-------------------------------------------------------------------------
    public void checkStrictMode(){
        //*** Permission StrictMode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    //-----------------------------------------------------------------------------------------

    //กดหาโลเคชันตัวเอง-------------------------------------------------------------------------
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
    //font -------------------------------------------------------------------------------------
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    //-----------------------------------------------------------------------------------------

}
