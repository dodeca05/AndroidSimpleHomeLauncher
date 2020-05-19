package com.example.SliverCareWithLancher;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;


public class CareThread extends Thread {
    private String noticetxt;
    private TextToSpeech tts;
    private boolean flag;
    private Context context;
    private int state;
    private double homeLatitude;
    private double homeLongitude;
    private String E_PhoneNumber;
    private Location homeLocation;
    private Location lastLocation;
    private int count;

    private int i;

    public CareThread(Context context, TextToSpeech tts,double homeLatitude,double homeLongitude,String E_PhoneNumber) {
        this.context = context;
        this.tts = tts;
        flag = true;
        state=0;
        this.homeLatitude=homeLatitude;
        this.homeLongitude=homeLongitude;
        this.E_PhoneNumber=E_PhoneNumber;
        this.count=0;
        this.homeLocation=new Location("Home");
        this.homeLocation.setLongitude(this.homeLongitude);
        this.homeLocation.setLatitude(this.homeLatitude);

        this.lastLocation=new Location("Last");

    }

    public void stopf() {
        flag = false;
    }

    @Override
    public void run() {
        noticetxt="GPS테스트";
        i = 0;
       while(flag) {




            handler.sendEmptyMessage(0);
            try {
                long time=1000*60;
                sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        i = -1;
        handler.sendEmptyMessage(0);
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            GpsTracker gpsTracker = new GpsTracker(context);

            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            Location now=new Location("Now");
            now.setLatitude(latitude);
            now.setLongitude(longitude);

            float distance=homeLocation.distanceTo(now);
            try {
                //전송


            } catch (Exception e) {

                e.printStackTrace();
            }
            switch (state)
            {
                case 0:
                    if(distance>=60){
                        state=1;
                        count=0;
                        tts.speak("외출하시나요? 혹시 가스밸브나 전기 스위치를 확인하셨나요? 잊으신 물건 없는지 확인하시고 오늘도 좋은 하루 되세요.", TextToSpeech.QUEUE_FLUSH, null);
                        lastLocation.setLongitude(longitude);
                        lastLocation.setLatitude(latitude);
                    }else{

                        count++;
                        if(count>=4320){
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(E_PhoneNumber, null,"해당 핸드폰의 주인이 한곳에서 장시간 움직이지 않았습니다.", null, null);
                            count-=60;
                        }
                    }

                    break;

                case 1:
                    float temp =lastLocation.distanceTo(now);
                    if(temp<100.0f)
                    {
                        count++;
                        if(count>=240)
                        {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(E_PhoneNumber, null,"해당 핸드폰의 주인이 한곳에서 장시간 움직이지 않았습니다.", null, null);
                        }
                    }
                    else
                    {
                        lastLocation.setLongitude(longitude);
                        lastLocation.setLatitude(latitude);
                        count=0;
                        if(distance<=30.0f)
                        {
                            state=0;
                            tts.speak("집에 돌아오신 것을 환영합니다.", TextToSpeech.QUEUE_FLUSH, null);
                        }

                    }






                    break;

            }
            //Toast.makeText(context, "{서비스"+state+"}현재위치 \n위도 " + latitude + "\n경도 " + longitude+"\n집과의 거리" + distance, Toast.LENGTH_LONG).show();
            //Toast.makeText(context.getApplicationContext(), noticetxt, Toast.LENGTH_SHORT).show();
            //tts.speak(noticetxt, TextToSpeech.QUEUE_FLUSH, null);
            super.handleMessage(msg);

        }

    };





}


