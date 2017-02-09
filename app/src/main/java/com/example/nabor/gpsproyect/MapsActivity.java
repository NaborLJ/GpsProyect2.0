package com.example.nabor.gpsproyect;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final int LOCATION_REQUEST_CODE = 1;
    private GoogleMap gMap;
    public static double auxLat =42.237701;
    public static double auxLng = -8.714187;
    CircleOptions circle;
    public static Marker mark;
    LatLng center = new LatLng(42.237701, -8.714187);
    int radio = 100;
    private GoogleApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;
        gMap.setOnMapClickListener(this);
        LatLng marcaBusqueda = new LatLng(auxLat, auxLng);
        mark = gMap.addMarker(new MarkerOptions().position(marcaBusqueda).title("Marca").snippet("Marca Buscada"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(marcaBusqueda));
        mark.setVisible(false);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }

        gMap.getUiSettings().setZoomControlsEnabled(true);

        circle = new CircleOptions()
                .center(center)
                .radius(radio)
                .strokeColor(Color.parseColor("#70E48F"))
                .strokeWidth(4)
                .fillColor(Color.argb(32, 33, 150, 243));

        Circle auxcircle = gMap.addCircle(circle);

    }

}
