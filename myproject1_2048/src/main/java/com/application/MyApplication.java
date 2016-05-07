package com.application;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/3/23.
 */
public class MyApplication extends Application {


    private SharedPreferences sp;
    private int lineNumber;
    private int highestRecord;
    private int target;

    @Override
    public void onCreate() {
        super.onCreate();


        sp = getSharedPreferences("information", MODE_PRIVATE);
        lineNumber=sp.getInt("lineNumber",4);
        highestRecord=sp.getInt("highestRecord",0);
        target=sp.getInt("target",2048);


    }



    public int getLinenumber(){
        return lineNumber;
    }

    public int getHighestRecord(){
        return highestRecord;
    }

    public int getTarget(){
        return target;
    }



    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("lineNumber",lineNumber);
        edit.commit();
    }


    public void setTarget(int target) {
        this.target = target;
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("target",target);
        edit.commit();
    }

    public void setHighestRecord(int highestRecord) {

        this.highestRecord = highestRecord;
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("highestRecord",highestRecord);
        edit.commit();
    }
}
