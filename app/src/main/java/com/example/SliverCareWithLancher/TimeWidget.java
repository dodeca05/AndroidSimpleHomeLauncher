package com.example.SliverCareWithLancher;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeWidget extends Thread {
    private TextView textView;

    public TimeWidget(TextView textView)
    {
        this.textView=textView;
    }

    @Override
    public void run()
    {
        while(true) {
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("MM월dd일 \nhh시 mm분ss");
            textView.setText(sdf.format(date));
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
