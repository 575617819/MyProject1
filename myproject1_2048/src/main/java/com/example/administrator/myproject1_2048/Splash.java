package com.example.administrator.myproject1_2048;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {


    boolean jump=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Thread(){

            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(2500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(jump){
                            Intent intent = new Intent(getApplication(),Home.class);
                            startActivity(intent);
                            finish();
                        }


                    }
                });
            }
        }.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        jump=false;
    }
}
