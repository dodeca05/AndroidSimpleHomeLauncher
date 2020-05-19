package com.example.SliverCareWithLancher;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

import javax.crypto.Cipher;

import static android.speech.tts.TextToSpeech.ERROR;

public class CareServiceSingleton {
    private static  CareServiceSingleton instance;
    private CareThread ct;
    private TextToSpeech tts;
    private Context context;
    private boolean IsItRun;
    private double homeLatitude;
    private double homeLongitude;



    private String E_PhoneNumber;

    private  CareServiceSingleton(Context context ,double homeLatitude,double homeLongitude,String E_PhoneNumber){
        this.homeLatitude=homeLatitude;
        this.homeLongitude=homeLongitude;
        IsItRun=false;
        this.context=context;
        this.E_PhoneNumber=E_PhoneNumber;
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });


    }

    public static synchronized  CareServiceSingleton getInstance(Context context,double homeLatitude,double homeLongitude,String E_PhoneNumber){
        if(instance == null){
            instance = new  CareServiceSingleton(context,homeLatitude,homeLongitude,E_PhoneNumber);
        }
        else
        {
            instance.setHomeLatitude(homeLatitude);
            instance.setHomeLongitude(homeLongitude);
            instance.setE_PhoneNumber(E_PhoneNumber);
        }
        return instance;
    }

    public boolean threadStart(){
        if(IsItRun) return false;
        IsItRun=true;



        ct=new CareThread(this.context,tts,homeLatitude,homeLongitude,E_PhoneNumber);
        ct.start();
        return true;
    }

    public boolean threadStop(){
        if(!IsItRun)return false;
        IsItRun=false;
        ct.stopf();
        return true;
    }

    public void setHomeLatitude(double homeLatitude) {
        this.homeLatitude = homeLatitude;
    }

    public void setHomeLongitude(double homeLongitude) {
        this.homeLongitude = homeLongitude;
    }

    public void setE_PhoneNumber(String e_PhoneNumber) {
        E_PhoneNumber = e_PhoneNumber;
    }


}
