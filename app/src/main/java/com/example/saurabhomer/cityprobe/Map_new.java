package com.example.saurabhomer.cityprobe;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Map_new extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    boolean mapReady=false;
    MarkerOptions rention;
    MarkerOptions krikland;
    MarkerOptions everett;
    MarkerOptions lymnwood;
    MarkerOptions montlake;
    MarkerOptions kent;
    MarkerOptions showare;
    static final CameraPosition SEATTLE= CameraPosition.builder()
            .target(new LatLng(47.6204,-122.3491))
            .zoom(10)
            .bearing(0)
            .tilt(45)
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rention=new MarkerOptions()
                .position(new LatLng(47.489805,-122.120502))
                .title("Renton")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
        krikland=new MarkerOptions()
                .position(new LatLng(47.7301986,-122.1768858))
                .title("KrikLand")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
        everett=new MarkerOptions()
                .position(new LatLng(47.978748,-122.202001))
                .title("Everett")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
        lymnwood=new MarkerOptions()
                .position(new LatLng(47.819533,-122.32288))
                .title("LynmWood")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
        montlake=new MarkerOptions()
                .position(new LatLng(47.7973733,-122.3281771))
                .title("MontLake")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
        kent=new MarkerOptions()
                .position(new LatLng(47.385938,-122.258212))
                .title("KentVally")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
        showare=new MarkerOptions()
                .position(new LatLng(47.38702,-122.23986))
                .title("Showare")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady=true;
        this.googleMap=googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.addMarker(rention);
        googleMap.addMarker(krikland);
        googleMap.addMarker(everett);
        googleMap.addMarker(lymnwood);
        googleMap.addMarker(montlake);
        googleMap.addMarker(kent);
        googleMap.addMarker(showare);
        flyTo(SEATTLE);
    }
    private void flyTo(CameraPosition target) {
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(target),5000,null);
        googleMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(47.899805,-122.120502))
                .add(new LatLng(47.7301986,-122.1768858))
                .add(new LatLng(47.978748,-122.202001))
                .add(new LatLng(47.819533,-122.32288))
                .add(new LatLng(47.7973733,-122.3281771))
                .add(new LatLng(47.385938,-122.258212))
                .add(new LatLng(47.38702,-122.23986))
                .add(new LatLng(47.899805,-122.120502)));
        googleMap.addCircle(new CircleOptions()
                .center(new LatLng(47.899805,-122.120502))
                .radius(5000)
                .strokeColor(Color.GREEN)
                .fillColor(Color.argb(64,0,255,0)));
    }

}