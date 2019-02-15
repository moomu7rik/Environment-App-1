package com.example.saurabhomer.cityprobe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class wifiActivity extends AppCompatActivity {

    private Button b;

    private LocationManager locationManager;
    private LocationListener listener;
    Location s = new Location("");
    Location e = new Location("");
    private static final String IMAGE_DIRECTORY_NAME = "GPS_STORAGE";
    private static final String POL_DIRECTORY_NAME = "POL_STORAGE";
    FileOutputStream fOut = null;
    OutputStreamWriter myOutWriter = null;
    String timeStamp = "";
    String file_name = "";
    public String gpsstorage = "";
    String data;
    String msg;
    File file1;
    double tpm1 = 0.0;
    double tpm2 = 0.0;
    double tno2 = 0.0;
    double tco2 = 0.0, tpm10 = 0.0, th = 0.0, tt = 0.0, tco = 0.0;
    double pmt1 = 0.0;
    double pm2 = 0.0;
    double not2 = 0.0;
    double cot2 = 0.0, pmt10 = 0.0, h = 0.0, t = 0.0, cot = 0.0;
    EditText editName;
    Button buttonSubmit, buttonReset;
    String ip;
    TextView result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        editName  = (EditText) findViewById(R.id.editName);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonReset = (Button) findViewById(R.id.buttonReset);
        result = (TextView) findViewById(R.id.tvResult);
        /*
            Submit Button
        */
        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                 ip= editName.getText().toString();
                result.setText("IP :\t" + ip );



            }
        });


        /*
            Reset Button
        */
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editName.setText("");
                result.setText("");
                editName.requestFocus();
            }
        });



        File mediaStorageDir = new File(
                Environment
                        .getExternalStorageDirectory().getAbsoluteFile() + "/" + IMAGE_DIRECTORY_NAME + "/" + POL_DIRECTORY_NAME
        );

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");

            }
        }
        timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        file_name = "pollution_File_" + timeStamp + ".txt";
        gpsstorage = mediaStorageDir.getPath() + File.separator
                + file_name;
        file1 = new File(gpsstorage);
        b = (Button) findViewById(R.id.button);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {

                    double lat = location.getLatitude();
                    double longe = location.getLongitude();
                   cli c = new cli();
                  try {
                        msg=c.client(ip);
                      result.setText("MSG :\t" + msg );
                    } catch (IOException e1) {
                       e1.printStackTrace();
                   }

                    String[] arr_data = msg.split(",");





                    if(arr_data.length>9) {

                        pmt1 =Double.valueOf(arr_data[2].trim());
                        pm2= Double.valueOf(arr_data[3].trim());
                        pmt10=   Double.valueOf(arr_data[4].trim());
                        try {
                            not2=   Double.valueOf(arr_data[5].trim());
                        }
                        catch(NumberFormatException ex){ // handle your exception
                            not2=100;
                        }

                        cot2=   Double.valueOf(arr_data[6].trim());
                        cot=    Double.valueOf(arr_data[7].trim());
                        h=  Double.valueOf(arr_data[8].trim());
                        t=   Double.valueOf(arr_data[9].trim());
                        if (tpm1 != 0) {
                            tpm1 = (tpm1 + Double.valueOf(arr_data[2].trim())) / 2;

                        } else {
                            tpm1 =Double.valueOf(arr_data[2].trim());
                        }
                        if (tpm2 != 0) {
                            tpm2 = (tpm2 + Double.valueOf(arr_data[3].trim())) / 2;

                        } else {
                            tpm2 =Double.valueOf(arr_data[3].trim());
                        }
                        if (tpm10 != 0) {
                            tpm10 = (tpm10 + Double.valueOf(arr_data[4].trim())) / 2;

                        } else {
                            tpm10 = Double.valueOf(arr_data[4].trim());
                        }
                        if (tno2 != 0) {
                            tno2 = (tno2 + not2) / 2;

                        } else {
                            tno2 = not2;
                        }
                        if (tco2 != 0) {
                            tco2 = (tco2 + Double.valueOf(arr_data[6].trim())) / 2;

                        } else {
                            tco2 = Double.valueOf(arr_data[6].trim());
                        }
                        if (tco != 0) {
                            tco = (tco + Double.valueOf(arr_data[7].trim())) / 2;

                        } else {
                            tco = Double.valueOf(arr_data[7].trim());
                        }
                        if (th != 0) {
                            th = (th + Double.valueOf(arr_data[8].trim())) / 2;

                        } else {
                            th =Double.valueOf(arr_data[8].trim());
                        }
                        if (tt != 0) {
                            tt = (tt + Double.valueOf(arr_data[9].trim())) / 2;

                        } else {
                            tt = Double.valueOf(arr_data[9].trim());
                        }
                    }


                    if ((tpm1 != 0 && tpm2 != 0 && tpm10 != 0 && tno2 != 0 && tco2 != 0 && tco != 0 && th != 0 && tt != 0)) {
                        data = lat + ":" + longe + ":" + tpm1 + ":" + tpm2 + ":" + tpm10 + ":" + tno2 + ":" + tco2 + ":" + tco + ":" + th + ":" + tt;
                        tpm1 = 0;
                        tpm2 = 0;
                        tpm10 = 0;
                        tno2 = 0;
                        tco2 = 0;
                        tco = 0;
                        th = 0;
                        tt = 0;
                    } else {
                        data = lat + ":" + longe + ":" + pmt1 + ":" + pm2 + ":" + pmt10 + ":" + not2 + ":" + cot2 + ":" + cot + ":" + h + ":" + t;
                    }
                    try {

                        fOut = new FileOutputStream(file1, true);
                        myOutWriter = new OutputStreamWriter(fOut);
                        myOutWriter.append(data).append("\n");
                        myOutWriter.close();
                        fOut.close();

                        //    fos = openFileOutput(file_name, Context.MODE_APPEND);
                        //   fos.write(loc.getBytes());

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);


            }
        };
        configure_button();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        b.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {

                locationManager.requestLocationUpdates("gps", 3000, 0, listener);

            }
        });

    }
}
