package com.example.saurabhomer.cityprobe;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Co extends AppCompatActivity implements MapEventsReceiver {
    MapView map;
    private IMapController mapController;
    private Polyline polyline;
    private Polyline colorline;
    public int t=0;
    public int c=0;
    public float distance =0;
    public float tdistance =0;
    public double pm2f =0;
    public double Apm2f =0;
    public double Atq =0;
    Location s = new Location("");
    Location e = new Location("");
    GeoPoint start;
    GeoPoint end;


    private static final String IMAGE_DIRECTORY_NAME = "GPS_STORAGE";
    private static final String POL_DIRECTORY_NAME = "POL_STORAGE";

    String[] tmp = new String[10];
    String str="";
    double slat=0;
    double slong=0;



    ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
    ArrayList<GeoPoint> colorpts = new ArrayList<GeoPoint>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_co);

        map = (MapView) findViewById(R.id.co_map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(16.7);
        final GeoPoint startPoint = new GeoPoint(23.5748702,87.2937521, 2.2944);
        mapController.setCenter(startPoint);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);
        startMarker.setTitle("Durgapur");
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        map.getOverlays().add(0, mapEventsOverlay);
        File working = new File(
                Environment
                        .getExternalStorageDirectory().getAbsoluteFile()+"/"+IMAGE_DIRECTORY_NAME +"/"+POL_DIRECTORY_NAME
        );
        File[] allfiles ;
        allfiles = working.listFiles();
        for (File file : allfiles){
            if (file.getName().contains("txt")) {
                Log.d("working data", "filename:" + file.getName());

                try {
                    BufferedReader in2 = new BufferedReader(new FileReader(file));


                    while ((str = in2.readLine()) != null) {
                        tmp = str.split(":");

                        slat = Double.parseDouble(tmp[0].trim());
                        slong = Double.parseDouble(tmp[1].trim());
                        pm2f=Double.parseDouble(tmp[7].trim());
                        polyline = new Polyline();
                        colorline = new Polyline();
                        GeoPoint Point = new GeoPoint(slat, slong);
                        waypoints.add(Point);
                        colorpts.add(Point);
                        if (waypoints.size() == 1) {
                            start = new GeoPoint(slat, slong);
                            s.setLatitude(slat);
                            s.setLongitude(slong);
                            Apm2f=pm2f;
                            c=1;

                        } else if (waypoints.size() >= 2) {
                            if(Apm2f!=0){
                                Apm2f=((Apm2f*c)+pm2f)/(c+1);
                                c++;
                            }
                            else {
                                Apm2f=pm2f;
                                c=1;
                            }


                            //  polyline.setColor(0x12121212);
                            //    polyline.setWidth(3);
                            //    polyline.setVisible(true);


                            //    polyline.setPoints(waypoints);
                            //    map.getOverlays().add(polyline);



                            end = new GeoPoint(slat, slong);
                            e.setLatitude(slat);
                            e.setLongitude(slong);
                            distance = s.distanceTo(e);

                            tdistance = tdistance + distance;
                            s.setLatitude(e.getLatitude());
                            s.setLongitude(e.getLongitude());

                            if (tdistance < 100) {

                                t = t + 3;

                            } else if (tdistance >= 100) {
                                //  Atq=calAtq(Apm2f);
                                if(Apm2f >34){
                                    int maroon = Color.parseColor("#800000");
                                    colorline.setColor(maroon);
                                    colorline.setWidth(7);
                                    colorline.setVisible(true);
                                    colorline.setPoints(colorpts);
                                    map.getOverlays().add(colorline);

                                }
                               else if(Apm2f >17 && Apm2f <=34){
                                    colorline.setColor(Color.RED);
                                    colorline.setWidth(7);
                                    colorline.setVisible(true);
                                    colorline.setPoints(colorpts);
                                    map.getOverlays().add(colorline);

                                }

                               else if (Apm2f >10 && Apm2f <=17) {
                                    int orange = Color.parseColor("#ffa500");
                                    colorline.setColor(orange);
                                    colorline.setWidth(7);
                                    colorline.setVisible(true);


                                    colorline.setPoints(colorpts);
                                    map.getOverlays().add(colorline);
                                } else if (Apm2f>= 2.1 && Apm2f <= 10) {
                                    colorline.setColor(Color.YELLOW);

                                    colorline.setWidth(7);
                                    colorline.setVisible(true);


                                    colorline.setPoints(colorpts);
                                    map.getOverlays().add(colorline);
                                } else if (Apm2f>=1 && Apm2f <= 2) {
                                   int lgreen = Color.parseColor("#b2ff56");
                                    colorline.setColor(lgreen);
                                    colorline.setWidth(7);
                                    colorline.setVisible(true);


                                    colorline.setPoints(colorpts);
                                    map.getOverlays().add(colorline);
                                } else if(Apm2f <1){
                                    colorline.setColor(Color.GREEN);
                                    colorline.setWidth(7);
                                    colorline.setVisible(true);


                                    colorline.setPoints(colorpts);
                                    map.getOverlays().add(colorline);
                                }
                                tdistance = 0;
                                Apm2f=0;

                                colorpts.clear();
                                start = end;
                                colorpts.add(start);
                                t = 0;
                                c=0;


                            }
                        }

                    }

                } catch (IOException e) {
                    System.out.println("cannot read file");
                }

            }
            colorpts.clear();
            waypoints.clear();
            c=0;
            tdistance=0;
            Apm2f=0;
        }
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }
}
