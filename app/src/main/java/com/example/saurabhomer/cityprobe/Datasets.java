package com.example.saurabhomer.cityprobe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static com.example.saurabhomer.cityprobe.Bluetooth_set.STATE_CONNECTED;
import static com.example.saurabhomer.cityprobe.Bluetooth_set.STATE_CONNECTING;
import static com.example.saurabhomer.cityprobe.Bluetooth_set.STATE_CONNECTION_FAILED;
import static com.example.saurabhomer.cityprobe.Bluetooth_set.STATE_LISTNING;
import static com.example.saurabhomer.cityprobe.Bluetooth_set.STATE_MESSAGE_RECEIVED;
import static com.example.saurabhomer.cityprobe.MainActivity.connectbt;
import static com.example.saurabhomer.cityprobe.CardMenu.flag_datasets;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class Datasets extends AppCompatActivity {
    private Button b;

    private LocationManager locationManager;
    private LocationListener listener;
    Location s = new Location("");
    Location e = new Location("");
    private static final String IMAGE_DIRECTORY_NAME = "GPS_STORAGE";
    private static final String POL_DIRECTORY_NAME = "POL_STORAGE";
    FileOutputStream fOut = null;
    OutputStreamWriter myOutWriter =null;
    String timeStamp ="";
    String file_name ="";
    public String gpsstorage="";
    String data;
    double tpm1 =0.0;
    double tpm2=0.0;
    double tno2=0.0;
    double tco2=0.0, tpm10=0.0 , th=0.0 , tt=0.0 , tco=0.0;
    double pmt1 =0.0;
    double pm2=0.0;
    double not2=0.0;
    double cot2=0.0, pmt10=0.0 , h=0.0 , t=0.0 , cot=0.0;

    BluetoothSocket socket;
    Datasets.SendReceive sendReceive;
    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    File file;
    private static ToneGenerator toneGenerator;
    int snooze_count = 0;
    FirebaseDatabase database;
    DatabaseReference Ref;

    int sms_send_time = 0;
    TextView data_msg,alertmsg;
    private boolean plotData = true;
    String dgas = null;
    File file1;

    boolean cond=true;
    private TextView status;
    String dt;
    String te;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datasets);


        data_msg = (TextView) findViewById(R.id.data_msg);
        alertmsg = (TextView) findViewById(R.id.alertmsg);
        status = (TextView) findViewById(R.id.status);
        data_msg.setMovementMethod(new ScrollingMovementMethod());
         FirebaseApp.initializeApp(this);
        database=FirebaseDatabase.getInstance();
        Ref=database.getReference("Data");
        File mediaStorageDir = new File(
                Environment
                        .getExternalStorageDirectory().getAbsoluteFile() + "/" + IMAGE_DIRECTORY_NAME +"/" +POL_DIRECTORY_NAME
        );


        // Create the storage directory if it does not exist
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
                    if((tpm1!=0 && tpm2!=0 && tpm10!=0 && tno2!=0 && tco2!=0 && tco!=0 && th!=0 && tt!=0) ) {
                        data = lat + ":" + longe + ":" + tpm1 + ":" + tpm2 + ":" + tpm10 + ":" + tno2 + ":" + tco2 + ":" + tco + ":" + th + ":" + tt;
                        tpm1=0 ; tpm2=0 ; tpm10=0 ; tno2=0 ; tco2=0 ; tco=0; th=0 ; tt=0 ;
                    }
                    else {
                        data = lat + ":" + longe + ":" + pmt1 + ":" + pm2 + ":" + pmt10 + ":" + not2 + ":" + cot2 + ":" + cot + ":" + h + ":" + t;
                    }
                    try {

                        fOut = new FileOutputStream(file1,true);
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
        Datasets.ClientClass clientClass = new Datasets.ClientClass(Bluetooth_set.connectbt);
        clientClass.start();

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
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String currentDate = sdf.format(new Date());
            String filename=currentDate;
            switch (msg.what){
                case STATE_LISTNING:
                    status.setText("Listning");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection_Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    String readBuffr=(String)msg.obj;

                    String tempMsg = readBuffr;

                    String path = Environment.getExternalStorageDirectory()+"/BluetoothApp/";
                    file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    RandomAccessFile raf;
                    try{
                        raf=new RandomAccessFile(file+"/Filename.txt","rw");
                        raf.seek(raf.length());
                        raf.write(tempMsg.getBytes());
                        raf.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }


                    String[] arr_data = tempMsg.split(",");
                    String dispaly_data=data_msg.getText().toString()+"\n"+tempMsg;

                    data_msg.setText(dispaly_data);


              /*     if(arr_data.length>9) {
                        data=arr_data[0].trim()+":"+arr_data[1].trim()+":"+arr_data[2].trim()+":"+arr_data[3].trim()+":"+arr_data[4].trim()+":"+arr_data[5].trim()+":"+arr_data[6].trim()+":"+arr_data[7].trim()+":"+arr_data[8].trim()+":"+arr_data[9].trim();


                        try {

                            fOut = new FileOutputStream(file1, true);
                            myOutWriter = new OutputStreamWriter(fOut);
                            myOutWriter.append(data).append("\n");
                            myOutWriter.close();
                            fOut.close();

                            //    fos = openFileOutput(file_name, Context.MODE_APPEND);
                            //   fos.write(loc.getBytes());

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            // TODO Auto-generated catch block
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

*/
                    if(arr_data.length>9) {
                        dt=arr_data[0].trim();
                        te=arr_data[1].trim();
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
                    AQI aqi=new AQI();
                    String worning_msg="";
                    if(arr_data.length>9) {

                       // Ref.push().setValue(new DataModel(java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()),String.valueOf(0.00),String.valueOf(0.00).trim(),arr_data[0].trim(),arr_data[1].trim(),arr_data[2].trim(),arr_data[3].trim(),arr_data[4].trim(),arr_data[5].trim(),arr_data[6].trim(),arr_data[7].trim(),arr_data[8].trim(),arr_data[9].trim()));
                        worning_msg += aqi.aqiTest((float) Double.parseDouble(arr_data[4].trim()), 0, 50, 51, 100, 101, 250, 251, 350, 351, 430, "PM10");
                        worning_msg +=   aqi.aqiTest(Float.parseFloat(arr_data[3].trim()),0,30,31,60,61,90,91,120,121,250,"PM2.5");
                        worning_msg += aqi.aqiTest(Float.parseFloat(arr_data[7].trim()), 0.0f, 1.0f, 1.1f, 2.0f, 2.1f, 10.0f, 10.0f, 17.0f, 17.0f, 34.0f, "CO");
                        worning_msg += aqi.aqiTest(Float.parseFloat(arr_data[6].trim()), 0, 40, 41, 80, 81, 180, 181, 280, 281, 400, "CO2");
                        if (worning_msg.trim() != null && worning_msg.trim() != "" && snooze_count == 0) {
                            alertmsg.setText(worning_msg);
//                        //Toast.makeText(MainActivity.this, "" + worning_msg, Toast.LENGTH_SHORT).show();
                            playTone();
                            if (sms_send_time == 120) {
                                //SmsManager smsManager = SmsManager.getDefault();
                                //smsManager.sendTextMessage("+919434789009", null, "alert sms:"+worning_msg, null, null);
                                //  sms_send_time=0;

                            } else {
                                sms_send_time++;

                            }
//
                            if (worning_msg == "" || worning_msg == null) {
                                alertmsg.setText("");

                            }
                            if (snooze_count != 0) {
                                snooze_count--;
                            }


//                    if(flag==1){
//
//                        if(dgas=="pm1")
//                        {
//                            Log.e("A","pm1");
//
//
//                        }
//                        else if(dgas=="pm25")
//                        {
//                            Log.e("A","pm25");
//
//
//
//                        }
//                        else if(dgas=="pm10")
//                        {
//                            Log.e("A","pm10");
//
//
//
//                        }
//                        else if(dgas=="no2")
//                        {
//                            Log.e("A","no2");
//
////
//
//                        }
//                        else if(dgas=="co")
//                        {
//                            Log.e("A","co");
//
//
//                        }
//                        else if(dgas=="co2")
//                        {
//                            Log.e("A","co2");
//
//                        }
//                        plotData = false;
//                    }
//
//                    msg_box.append(tempMsg);
//                    break;
                        }
                    }
               }
            return true;
        }
    });

     void playTone( ) {

        try {

            if (toneGenerator == null) {
                toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
            }
            toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 900);

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (toneGenerator != null) {
                        //Log.d(TAG, "ToneGenerator released");
                        toneGenerator.release();
                        toneGenerator = null;
                    }
                }

            }, 900);
        } catch (Exception e) {
            Log.d("ex", "Exception while playing sound:" + e);
        }
    }

    private class ClientClass extends Thread {
       BluetoothDevice device;


        public ClientClass(BluetoothDevice device1) {
            device = device1;

                socket = CardMenu.socket;

        }

        public void run() {

              // socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);
               sendReceive = new Datasets.SendReceive(CardMenu.socket);
                sendReceive.start();

        }
    }
    public void btnsnooze(View view) {
        snooze_count=60;
    }
    private class SendReceive extends Thread {
        private final BluetoothSocket bluetoothSocket;
        InputStream inputStream;
        OutputStream outputStream;

        public SendReceive(BluetoothSocket socket) {
            bluetoothSocket = socket;

            InputStream tempIn = null;
            OutputStream tempOut = null;
            try {
                tempIn = bluetoothSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                tempOut = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

           inputStream = tempIn;
            outputStream = tempOut;

        }

        public void run() {
            byte[] buffer = new byte[1024];
            final byte delimiter = 10;
            int bytes;
            int readBufferPosition = 0;
            while (cond) {

                try {

                    int bytesAvailable = inputStream.available();

                    if (bytesAvailable > 0) {
                        byte[] packetByte = new byte[bytesAvailable];
                        inputStream.read(packetByte);
                        for (int i = 0; i < bytesAvailable; i++) {
                            byte b = packetByte[i];
                            if (b == delimiter) {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(buffer, 0, encodedBytes, 0, encodedBytes.length);
                                final String data = new String(encodedBytes);
                                readBufferPosition = 0;

                                handler.obtainMessage(STATE_MESSAGE_RECEIVED, data).sendToTarget();


                            } else
                                {
                                buffer[readBufferPosition++] = b;
                            }
                        }

                    }

                    //bytes = inputStream.read(buffer);
                    //handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        cond=false;

    }


}