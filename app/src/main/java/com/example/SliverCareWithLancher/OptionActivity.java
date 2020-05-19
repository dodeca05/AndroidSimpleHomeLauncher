package com.example.SliverCareWithLancher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class OptionActivity extends AppCompatActivity {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button setHomeBtn;
    private Button setPhoneNumBtn;
    private ToggleButton tbtn;

    private int cur=0;
    private String []pkgs;
    private String []labs;

    private boolean IsServiceRun;
    private double latitude;
    private double longitude;
    private String E_PhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_DeviceDefault);
        setContentView(R.layout.activity_option);
        setTitle("런쳐 설정");
        LoadServiceFile();
        pkgs=new String[4];
        labs=new String[4];
        for(int i=0;i<4;i++)
        {
            pkgs[i]="null";
            labs[i]="null";
        }

        btn1=(Button)findViewById(R.id.opbtn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur=0;
                Intent intent=new Intent(getApplicationContext(),AllAppActivity.class);
                intent.putExtra("mode","Select");
                startActivityForResult(intent,1001);
            }
        });

        btn2=(Button)findViewById(R.id.opbtn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur=1;
                Intent intent=new Intent(getApplicationContext(),AllAppActivity.class);
                intent.putExtra("mode","Select");
                startActivityForResult(intent,1001);
            }
        });

        btn3=(Button)findViewById(R.id.opbtn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur=2;
                Intent intent=new Intent(getApplicationContext(),AllAppActivity.class);
                intent.putExtra("mode","Select");
                startActivityForResult(intent,1001);
            }
        });

        btn4=(Button)findViewById(R.id.opbtn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur=3;
                Intent intent=new Intent(getApplicationContext(),AllAppActivity.class);
                intent.putExtra("mode","Select");
                startActivityForResult(intent,1001);
            }
        });
        tbtn=(ToggleButton)findViewById(R.id.CareServiceBtn);
        if(this.IsServiceRun)tbtn.setChecked(true);
        tbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent serviceIntent=new Intent(getApplicationContext(),CareService.class);
                if(tbtn.isChecked()){
                    if(latitude<0||longitude<0|| E_PhoneNumber.equals("null")||E_PhoneNumber==null||E_PhoneNumber.equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"서비스에 필요한 설정이 제대로 되지 않았습니다.",Toast.LENGTH_LONG).show();
                        tbtn.setChecked(false);
                       return;
                    }
                    Log.e("[Service]","시작");
                    startService(serviceIntent);
                    IsServiceRun=true;
                }
                else{
                    Log.e("[Service]","중지");
                    stopService(serviceIntent);
                    IsServiceRun=false;
                }

                SaveServiceFile();


            }
        });
        setPhoneNumBtn=(Button)findViewById(R.id.setEMphonebtn);
        setPhoneNumBtn.setText("비상연락처("+E_PhoneNumber+")");
        setPhoneNumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputE_PhoneNumber();
                setPhoneNumBtn.setText("비상연락처("+E_PhoneNumber+")");
            }
        });



        setHomeBtn=(Button)findViewById(R.id.sethomebtn);
        if(this.longitude>=0&&this.latitude>=0)
            setHomeBtn.setText("집 설정하기("+this.latitude+"/"+this.longitude+")");




        setHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GpsTracker gpsTracker = new GpsTracker(getApplicationContext());

                latitude = gpsTracker.getLatitude();//위도
                longitude = gpsTracker.getLongitude();//경도

                SaveServiceFile();
                setHomeBtn.setText("집 설정하기("+latitude+"/"+longitude+")");


                Toast.makeText(getApplicationContext(), "위도 " + latitude + "경도 " + longitude+"로 집이 설정되었습니다.", Toast.LENGTH_LONG).show();

            }
        });




        loadbtn();
    }
    private void loadbtn()
    {

        FileInputStream inFs= null;
        try {
            inFs = openFileInput("MainUIApps.txt");
            byte[] txt=new byte[500];
            inFs.read(txt);
            inFs.close();
            String str=new String(txt).trim();
            Log.e("[INFO]","LoadOption\n"+str);
            String []apps=str.split("\n");
            btn1.setText("상단 바로가기 앱 설정 : null");
            btn2.setText("왼쪽 바로가기 앱 설정 : null");
            btn3.setText("오른쪽 바로가기 앱 설정 : null");
            btn4.setText("하단 바로가기 앱 설정 : null");
            if(apps.length<4) {

                return;
            }
            for(int i=0;i<4;i++)
            {
                pkgs[i]=apps[i].split("/")[0];
                labs[i]=apps[i].split("/")[1];


            }
            btn1.setText("상단 바로가기 앱 설정 : "+apps[0].split("/")[1]);
            btn2.setText("왼쪽 바로가기 앱 설정 : "+apps[1].split("/")[1]);
            btn3.setText("오른쪽 바로가기 앱 설정 : "+apps[2].split("/")[1]);
            btn4.setText("하단 바로가기 앱 설정 : "+apps[3].split("/")[1]);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) {
                //Toast.makeText(getApplicationContext(),data.getStringExtra("Label")+"을 선택함",Toast.LENGTH_LONG).show();
                if(data.getStringExtra("Pakage").equals("com.example.SliverCareWithLancher"))
                {
                    pkgs[cur] = "null";
                    labs[cur] = "null";

                }
                else{
                    pkgs[cur] = data.getStringExtra("Pakage");
                    labs[cur] = data.getStringExtra("Label");
                }
                try {

                    String txt = "";
                    for (int i = 0; i < 4; i++)
                    {
                        txt+=pkgs[i]+"/"+labs[i]+"\n";
                    }
                    FileOutputStream outFs=openFileOutput("MainUIApps.txt", Context.MODE_PRIVATE);
                    outFs.write(txt.getBytes());
                    outFs.close();
                    Toast.makeText(getApplicationContext(),"저장완료",Toast.LENGTH_LONG).show();
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"저장실패",Toast.LENGTH_LONG).show();
                }
                loadbtn();
            }
        }



    }

    private void SaveServiceFile()
    {
        String result="";
        if(this.IsServiceRun)
            result+="true/";
        else
            result+="false/";
        if(this.latitude>=0&&this.longitude>=0)
            result+=new Double(this.latitude).toString()+"/"+new Double(this.longitude).toString()+"/";
        else
            result+="null/null/";
        if(this.E_PhoneNumber==null||this.E_PhoneNumber.equals("")||this.E_PhoneNumber.equals("null"))
            result+="null";
        else
            result+=this.E_PhoneNumber;

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput("Service.txt", Context.MODE_PRIVATE);

            fileOutputStream.write(result.getBytes());
            Log.e("[INFO]","Save Sucess "+result);
            fileOutputStream.close();

        } catch (Exception e2) {
            e2.printStackTrace();

        }
    }
    private void LoadServiceFile()
    {
        FileInputStream inFs;

        try {
            inFs = openFileInput("Service.txt");
            byte[] txt = new byte[500];
            inFs.read(txt);
            String str = new String(txt).trim();
            Log.e("[INFO]",str);
            inFs.close();
            String[]op=str.split("/");
            this.IsServiceRun=op[0].equals("true");
            this.latitude=Double.parseDouble(op[1]);
            this.longitude=Double.parseDouble(op[2]);
            this.E_PhoneNumber=op[3];

        }catch (Exception e)
        {
            Log.e("[INFO]","LoadFail");
            this.IsServiceRun=false;
            this.latitude=-1.0;
            this.longitude=-1.0;
            this.E_PhoneNumber="";
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
private void InputE_PhoneNumber()
{
    final EditText editText=new EditText(this);
    editText.setInputType(InputType.TYPE_CLASS_PHONE);
    AlertDialog.Builder builder=new AlertDialog.Builder(this);
    builder.setTitle("비상연락처 입력");
    builder.setMessage("비상시 문자를 보낼 번호를 입력하세요");
    builder.setView(editText);
    builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            E_PhoneNumber=editText.getText().toString();
            setPhoneNumBtn.setText("비상연락처("+E_PhoneNumber+")");
            SaveServiceFile();
        }
    });
    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    });
    builder.show();


}

}
