package com.example.nabor.gpsproyect;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener,GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {

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
    public static double latencia, longitud;
    private static final String LOGTAG = "android-localizacion";

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

    /**
     * Si la localización gps está activada, cargará el mapa y creará el area y la marca en su interior
     * @param googleMap
     */
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

    /**
     * Responderá a click en el mapa, localizará/actualizará nuestra posición / Futuramente mostrará la distancia cara la marca.
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);



    }

    /**
     * Petición de permisos para poder acceder a la localización.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (permissions.length > 0 &&
                    permissions[0].equals(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                gMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Error en los permisos", Toast.LENGTH_LONG).show();
            }

        }
    }

    /**
     * Calcula la distancia hasta la marca desde la posición actual / Futuramente implementado en onMapClick
     */
    public void calcDistanciaMarca() {

        double earthRadius = 6372.795477598;

        double distLaten = Math.toRadians(latencia- auxLat);
        double distLong = Math.toRadians(longitud- auxLng);
        double a = Math.sin(distLaten/2) * Math.sin(distLaten/2) +
                Math.cos(Math.toRadians(auxLat)) * Math.cos(Math.toRadians(latencia)) *
                        Math.sin(distLong/2) * Math.sin(distLong/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;
        double distMark=dist*1000;
        String distancia=String.valueOf(distMark);

        Toast.makeText(this, distancia+" metros hasta la marca", Toast.LENGTH_LONG).show();

        if(distMark>=150){
            circle.strokeColor(Color.parseColor("#DF0C0C"));
            mark.setVisible(false);
        }
        if (distMark>=100  && distMark < 150.00){
            circle.strokeColor(Color.parseColor("#F0973F"));
        }
        if(distMark <70 && distMark > 50){
            circle.strokeColor(Color.parseColor("#F4F41E"));

        }
        if(distMark<50 && distMark >20){
            circle.strokeColor(Color.parseColor("#3BFA21"));
        }
        if(distMark<=20){
            mark.setVisible(true);
        }

    }

    /**
     * Obtiene la latitud y la longitud
     * @param loc
     */
    private void updateUI(Location loc) {

        if (loc != null) {
            latencia=loc.getLatitude();
            longitud=loc.getLongitude();

        } else {

            Toast.makeText(this, "Latitud y Longitud desconocidas", Toast.LENGTH_LONG).show();

        }


    }

    /**
     * Metodos obligatorios implementados
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            updateUI(lastLocation);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOGTAG, "Interrumpida la conexión con Google Play Services");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOGTAG, "Error de conexión con Google Play Services");
    }
}
