package com.example.SliverCareWithLancher;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;
import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static com.example.SliverCareWithLancher.App.CHANNEL_ID;

public class CareService extends Service {
    private CareServiceSingleton css;
    private TextToSpeech tts;
    private double latitude;
    private double longitude;
    private String E_PhoneNumber;

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LoadServiceFile();
        if(latitude<0||longitude<0|| this.E_PhoneNumber.equals("null")||this.E_PhoneNumber==null||this.E_PhoneNumber.equals(""))
        {
            Toast.makeText(getApplicationContext(),"서비스에 필요한 설정이 제대로 되지 않았습니다.",Toast.LENGTH_LONG).show();
            return START_NOT_STICKY;
        }

        //Notification notification=new NotificationCompat.Builder(this,CHANNEL_ID).setContentTitle("Care Service").setContentText("케어서비스 실행중").setSmallIcon(R.drawable.ic_android_black_24dp).setContentIntent(pendingIntent).build();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("Care Service").setContentText("케어서비스 실행중").setSmallIcon(R.drawable.ic_android_black_24dp).build();

        startForeground(1, notification);

        //할일
        Log.e("[Service]", "BackgroundServiceStart");



        //stopSelf();


        css = CareServiceSingleton.getInstance(getApplicationContext(),latitude,longitude,E_PhoneNumber);
        css.threadStart();

        return START_STICKY;

        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "서비스 정지", Toast.LENGTH_SHORT).show();
        css.threadStop();
        super.onDestroy();

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    private void LoadServiceFile()
    {
        FileInputStream inFs;

        try {
            inFs = openFileInput("Service.txt");
            byte[] txt = new byte[500];
            inFs.read(txt);
            String str = new String(txt).trim();

            inFs.close();
            String[]op=str.split("/");

            this.latitude=Double.parseDouble(op[1]);
            this.longitude=Double.parseDouble(op[2]);
            this.E_PhoneNumber=op[3];

        }catch (Exception e)
        {
            Log.e("[INFO]","LoadFail");

            this.latitude=-1.0;
            this.longitude=-1.0;
            this.E_PhoneNumber=null;
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = openFileOutput("Service.txt", Context.MODE_PRIVATE);
                fileOutputStream.write(new String("false/null/null/null").getBytes());
                fileOutputStream.close();

            } catch (Exception e2) {
                e2.printStackTrace();

            }

            e.printStackTrace();
        }





    }





}

