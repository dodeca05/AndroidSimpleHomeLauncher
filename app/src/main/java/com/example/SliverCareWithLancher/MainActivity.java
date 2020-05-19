package com.example.SliverCareWithLancher;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;

    private boolean svc;


    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private ImageButton callBtn;
    private ImageButton topbtn;
    private ImageButton leftbtn;
    private ImageButton rightbtn;
    private ImageButton bottombtn;
    private TextView time;
    private TextView toptext;
    private TextView lefttext;
    private TextView righttext;
    private TextView bottomtext;

    private String pkg,lab;
    private String toppkg,leftpkg,rightpkg,bottompkg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        int BarHeight=0;
        Context context=this.getApplicationContext();
        BarHeight=context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(BarHeight>0){
            BarHeight=context.getResources().getDimensionPixelSize(BarHeight);
            Log.e("[INFO]","Padding Sucess : "+BarHeight);
        }
        else
        {
            Log.e("[INFO]","Padding Fail");
            BarHeight=0;
        }
        TextView textView = (TextView) findViewById(R.id.timeText);
        textView.setPadding(0, BarHeight, 0, 0);




        PermissionCheck(Manifest.permission.SEND_SMS);
        PermissionCheck(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        PermissionCheck(Manifest.permission.ACCESS_FINE_LOCATION);
        PermissionCheck(Manifest.permission.ACCESS_COARSE_LOCATION);
        time=(TextView)findViewById(R.id.timeText);
        ImageButton b=(ImageButton)findViewById(R.id.imageButton3r);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AllAppActivity.class);
                intent.putExtra("mode","Run");
                startActivity(intent);
            }
        });
        callBtn=(ImageButton)findViewById(R.id.imageButton1l);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





             startActivity(new Intent("android.intent.action.DIAL"));






            }
        });
        topbtn=(ImageButton)findViewById(R.id.imageButton1r);
        leftbtn=(ImageButton)findViewById(R.id.imageButton2l);
        rightbtn=(ImageButton)findViewById(R.id.imageButton2r);
        bottombtn=(ImageButton)findViewById(R.id.imageButton3l);
        toptext=(TextView)findViewById(R.id.texttop);
        lefttext=(TextView)findViewById(R.id.textleft);
        righttext=(TextView)findViewById(R.id.textright);
        bottomtext=(TextView)findViewById(R.id.textbottom);
        LoadServiceFile();
        TimeWidget tw=new TimeWidget(time);
        tw.start();
        IconLoad();
        toppkg=null;
        leftpkg=null;
        rightpkg=null;
        bottompkg=null;
//////
        svc=false;
        /////////


        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }
    }
    private void PermissionCheck(String permission)
    {
        /*
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.D)== PackageManager.PERMISSION_DENIED)
        {

        }*/
        if (ContextCompat.checkSelfPermission(this, permission)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,permission)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        1);
            }
        }




    }





    private void IconLoad()
    {

        PermissionCheck(Manifest.permission.SEND_SMS);
        PermissionCheck(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        PermissionCheck(Manifest.permission.ACCESS_FINE_LOCATION);
        PermissionCheck(Manifest.permission.ACCESS_COARSE_LOCATION);
        try{
            FileInputStream inFs=openFileInput("MainUIApps.txt");
            byte[] txt=new byte[500];
            inFs.read(txt);
            inFs.close();
            String str=new String(txt).trim();
            String []apps=str.split("\n");
            String []temp;

            if(apps.length<4)return;
            temp=apps[0].split("/");
            pkg=temp[0];
            lab=temp[1];
            if(!pkg.equals("null")&&!lab.equals("null"))
            {
                Drawable icon = getPackageManager().getApplicationIcon(pkg);
                toppkg=pkg;
                topbtn.setImageDrawable(icon);
                toptext.setText(lab);
                toptext.setBackgroundColor(Color.parseColor("#80FFFFFF"));
                topbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(toppkg==null)return;
                        Intent intent = getPackageManager().getLaunchIntentForPackage(toppkg);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });


            }
            else{
                topbtn.setImageDrawable(null);
                toptext.setText(null);
                toptext.setBackgroundColor(Color.parseColor("#00000000"));
            }

            temp=apps[1].split("/");
            pkg=temp[0];
            lab=temp[1];
            if(!pkg.equals("null")&&!lab.equals("null"))
            {
                Drawable icon = getPackageManager().getApplicationIcon(pkg);
                leftbtn.setImageDrawable(icon);
                lefttext.setText(lab);
                lefttext.setBackgroundColor(Color.parseColor("#80FFFFFF"));
                leftpkg=pkg;
                leftbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(leftpkg==null)return;
                        Intent intent = getPackageManager().getLaunchIntentForPackage(leftpkg);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });


            }
            else{
                leftbtn.setImageDrawable(null);
                lefttext.setText(null);
                lefttext.setBackgroundColor(Color.parseColor("#00000000"));
            }

            temp=apps[2].split("/");
            pkg=temp[0];
            lab=temp[1];
            if(!pkg.equals("null")&&!lab.equals("null"))
            {
                Drawable icon = getPackageManager().getApplicationIcon(pkg);
                rightbtn.setImageDrawable(icon);
                righttext.setText(lab);
                righttext.setBackgroundColor(Color.parseColor("#80FFFFFF"));
                rightpkg=pkg;
                rightbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(rightpkg==null)return;
                        Intent intent = getPackageManager().getLaunchIntentForPackage(rightpkg);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });
            }
            else
            {
                rightbtn.setImageDrawable(null);
                righttext.setText(null);
                righttext.setBackgroundColor(Color.parseColor("#00000000"));
            }

            temp=apps[3].split("/");
            pkg=temp[0];
            lab=temp[1];
            if(!pkg.equals("null")&&!lab.equals("null"))
            {
                Drawable icon = getPackageManager().getApplicationIcon(pkg);
                bottombtn.setImageDrawable(icon);
                bottomtext.setText(lab);
                bottomtext.setBackgroundColor(Color.parseColor("#80FFFFFF"));
                bottompkg=pkg;
                bottombtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(bottompkg==null)return;
                        Intent intent = getPackageManager().getLaunchIntentForPackage(bottompkg);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });

            }
            else
            {
                bottombtn.setImageDrawable(null);
                bottomtext.setText(null);
                bottomtext.setBackgroundColor(Color.parseColor("#00000000"));
            }





        }catch (Exception e){
            Log.e("[Load]","fail");
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){return false;}

    @Override
    public void onResume() {

        super.onResume();
       IconLoad();







    }



////////////
@Override
public void onRequestPermissionsResult(int permsRequestCode,
                                       @NonNull String[] permissions,
                                       @NonNull int[] grandResults) {

    if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

        // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

        boolean check_result = true;


        // 모든 퍼미션을 허용했는지 체크합니다.

        for (int result : grandResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                check_result = false;
                break;
            }
        }


        if ( check_result ) {

            //위치 값을 가져올 수 있음
            ;
        }
        else {
            // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                finish();


            }else {

                Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

            }
        }

    }
}

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }






    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    ////////////
    private void LoadServiceFile() {
        FileInputStream inFs;

        try {
            inFs = openFileInput("Service.txt");
            byte[] txt = new byte[500];
            inFs.read(txt);
            String str = new String(txt).trim();

            inFs.close();
            String[] op = str.split("/");
            if (op[0].equals("true")) {
                Intent serviceIntent=new Intent(getApplicationContext(),CareService.class);
                    startService(serviceIntent);

            }


        } catch (Exception e) {
            Log.e("[INFO]", "LoadFail");

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
