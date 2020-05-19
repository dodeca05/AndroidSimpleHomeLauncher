package com.example.SliverCareWithLancher;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class AllAppActivity extends AppCompatActivity {


    private ListView listView;
    private List<ResolveInfo>ris;
    private String mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_app);
        Intent thisintent=getIntent();
        this.mode= thisintent.getStringExtra("mode");
       // Toast.makeText(getApplicationContext(),"Mode : "+this.mode,Toast.LENGTH_LONG).show();
        listView=(ListView)findViewById(R.id.list_item);

        Intent intent=new Intent(Intent.ACTION_MAIN,null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

         ris=getPackageManager().queryIntentActivities(intent,0);
         AppListViewAdapter mAppListViewAdapter=new AppListViewAdapter();
        if(ris!=null)
        {
            Log.e("[INFO]","Create App ListView");
            for(ResolveInfo ri: ris)
            {
                String pakageName=ri.activityInfo.packageName;
                String label=ri.loadLabel(getPackageManager()).toString();
                String className=ri.activityInfo.name;
                Drawable icon=ri.loadIcon(getPackageManager());
                if(pakageName.equals("com.example.SliverCareWithLancher"))continue;
                mAppListViewAdapter.addItem(icon,label,pakageName,className);
                Log.e("[INFO]","Append App : "+pakageName+":"+label+":"+className);

            }
            ////런처 설정 진입 버튼 추가
                if(mode.equals("Run"))
                mAppListViewAdapter.addItem(null,"런처설정","com.example.SliverCareWithLancher","com.example.SliverCareWithLancher.OptionActivity");
                else if(mode.equals("Select"))
                    mAppListViewAdapter.addItem(null,"미지정","com.example.SliverCareWithLancher","com.example.SliverCareWithLancher.OptionActivity");
            ///
        }
        else
            Log.e("[ERROR]","Create App ListView Fail");
        listView.setAdapter(mAppListViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppListViewAdapter alv=(AppListViewAdapter)parent.getAdapter();
                Log.e("[INFO]",position+":"+alv.getItem(position).getName()+"/"+alv.getItem(position).getClassname());
                if(mode.equals("Run")) {
                    if(!alv.getItem(position).getPakageName().equals("com.example.SliverCareWithLancher")) {
                        Intent intent = getPackageManager().getLaunchIntentForPackage(alv.getItem(position).getPakageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else{
                        Intent intent=new Intent(getApplicationContext(),OptionActivity.class);
                        startActivity(intent);

                    }
                }
                else if(mode.equals("Select")){
                    Intent intent=new Intent();
                    intent.putExtra("Pakage",alv.getItem(position).getPakageName());
                    intent.putExtra("Label",alv.getItem(position).getName());
                    setResult(RESULT_OK,intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error unknown Mode",Toast.LENGTH_LONG).show();

                }



            }
        });

    }
}
