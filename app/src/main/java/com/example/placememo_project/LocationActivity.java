package com.example.placememo_project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.example.placememo_project.databinding.ActivityLocationBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static com.example.placememo_project.InsertActivity.locationName;
import static com.example.placememo_project.IntroActivity.permission;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private final static String TAG = "LocationActivity : ";
    public static Context lContext;
    ActivityLocationBinding lBinding;
    private int icon, clickicon;
    private GoogleMap mMap;
    LocationManager manager;
    double longitude;
    double latitude;
    private boolean iconcheck = false;
    final Geocoder geocoder = new Geocoder(this);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lBinding = DataBindingUtil.setContentView(this, R.layout.activity_location);
        lContext = this;
        lBinding.btnAddIcon.setOnClickListener(this);
        lBinding.btnAddlocation.setOnClickListener(this);
        lBinding.btnSerch.setOnClickListener(this);
        lBinding.btnMylocation.setOnClickListener(this);
        lBinding.serchLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
               if(i== EditorInfo.IME_ACTION_SEARCH){
                   locationSerch();
               }
                return false;
            }
        });
        lBinding.locationName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    lBinding.locationName.setText("");
                }
            }
        });
        if (permission) {  // --  위치기능을 켯다면 위치정보를 받아오는 메소드 실행
            Toast.makeText(this, "위치 정보 받아오는중..", Toast.LENGTH_SHORT).show();
            startLocation();
        } else {  // --  키지않았다면 기본 지도 표시
            Toast.makeText(this, "위치 기능 사용 불가", Toast.LENGTH_SHORT).show();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.removeUpdates(mLocation);
    }

    public void startLocation() {
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        long minTime = 0;
        float minDistance = 0;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocation);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mLocation);
    }

    private void stopLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.removeUpdates(mLocation);

    }


    LocationListener mLocation = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(LocationActivity.this);
            Toast.makeText(getApplicationContext(),latitude + ", " + longitude,Toast.LENGTH_SHORT).show();
            stopLocation();
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
    };

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        LatLng selectLocation = new LatLng(latitude, longitude);
        marker(selectLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectLocation, 15));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions mOptions = new MarkerOptions();
                mOptions.title("마커 좌표");
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                LatLng selectLocation = new LatLng(latitude, longitude);
                marker(selectLocation);
            }
        });
    }

    private void marker(LatLng selectLocation) {
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(selectLocation);
        markerOptions.title("위치");
        markerOptions.snippet("메모 알림 위치");
        mMap.addMarker(markerOptions);

    }

    public double getDistance(double lat1 , double lng1 , double lat2 , double lng2 ){
        double distance;

        Location locationA = new Location("point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lng1);

        Location locationB = new Location("point B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lng2);

        distance = locationA.distanceTo(locationB);

        return distance;
    }

    @Override
    public void onClick(View v) {
        if (v == lBinding.btnSerch) {
            locationSerch();

        } else if (v == lBinding.btnMylocation) {
            startLocation();
        } else if (v == lBinding.btnAddIcon) {
            Intent intent = new Intent(this, IconActivity.class);
            startActivityForResult(intent, 0522);
        } else if (v == lBinding.btnAddlocation) {
            if(!locationName.contains(lBinding.locationName.getText().toString()) && iconcheck) {
                Intent intent = new Intent();
                intent.putExtra("name", lBinding.locationName.getText().toString());
                intent.putExtra("icon", icon);
                intent.putExtra("clickicon", clickicon);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                setResult(RESULT_OK, intent);
                finish();
            }else{
                if(locationName.contains(lBinding.locationName.getText().toString())) Toast.makeText(this,"이미 존재하는 장소명 입니다.",Toast.LENGTH_LONG).show();
                if(!iconcheck) Toast.makeText(this,"아이콘을 선택해주세요.",Toast.LENGTH_LONG).show();
            }
        }

    }

    private void locationSerch() {
        List<Address> list = null;
        String address = lBinding.serchLocation.getText().toString();
        lBinding.locationName.setText(lBinding.serchLocation.getText().toString());
        lBinding.locationName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                return;
            }
        });
        try{
            list = geocoder.getFromLocationName(address,10);
        }catch (IOException e){
            Log.d(TAG,"btnSerch");
        }
        if(list != null){
            if(list.size() == 0) {
                Toast.makeText(this,"해당되는 주소 정보가 없습니다.",Toast.LENGTH_LONG).show();
            }else{
                Address address1 = list.get(0);
                latitude = address1.getLatitude();
                longitude = address1.getLongitude();
                LatLng selectLocation = new LatLng(latitude, longitude);
                marker(selectLocation);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectLocation, 15));
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            icon = data.getIntExtra("icon", -1);
            clickicon = data.getIntExtra("clickicon", -1);
            lBinding.btnAddIcon.setBackgroundResource(data.getIntExtra("icon", -1));
            iconcheck = true;
        } else {
            Log.d(TAG, String.valueOf(resultCode));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return true;
    }




}
