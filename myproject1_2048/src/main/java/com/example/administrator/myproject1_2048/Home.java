package com.example.administrator.myproject1_2048;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.MyApplication;
import com.yanghang.view.GameView;


public class Home extends ActionBarActivity implements View.OnClickListener {

    private static Home mActivity;
    private GameView mgameView;

    private Button bt_home_revert;
    private Button bt_home_restart;
    private Button bt_home_options;

    private TextView tv_home_score;
    private TextView tv_home_record;
    private TextView tv_home_target;

    private MyApplication myApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        /**
         * 隐藏标题栏
         */
        getSupportActionBar().hide();


        mActivity=this;
        mgameView = new GameView(this);


        RelativeLayout rl_home_grilLayout=(RelativeLayout)findViewById(R.id.rl_home_gridlayout);
        rl_home_grilLayout.addView(mgameView);


        bt_home_revert = (Button)findViewById(R.id.bt_home_revert);
        bt_home_restart = (Button)findViewById(R.id.bt_home_restart);
        bt_home_options = (Button)findViewById(R.id.bt_home_options);

        bt_home_revert.setOnClickListener(this);
        bt_home_restart.setOnClickListener(this);
        bt_home_options.setOnClickListener(this);

        tv_home_score = (TextView)findViewById(R.id.tv_home_score);
        tv_home_record = (TextView)findViewById(R.id.tv_home_record);
        tv_home_target = (TextView)findViewById(R.id.tv_home_goal);

        myApplication=(MyApplication)getApplication();

        tv_home_target.setText(myApplication.getTarget()+"");
        tv_home_record.setText(myApplication.getHighestRecord()+"");

    }



    public static Home getActivity(){

        return mActivity;
    }




    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.bt_home_revert:
                revert();
                break;

            case R.id.bt_home_restart:
                restart();
                break;

            case R.id.bt_home_options:
                options();
                break;
        }
    }


    /**
     * 重新开始
     */
    public void restart(){

        new AlertDialog.Builder(this)
                .setMessage("您确定要重新开始游戏吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mgameView.restart();
                    }
                })
                .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).show();
    }


    /**
     * 返回上一步
     */
    public void revert(){

        mgameView.revert();
    }


    /**
     * 设置界面
     */
    public void options(){

        Intent intent = new Intent(this,options.class);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("onActivityResult","程序到达");
        updateTargetScore(myApplication.getTarget());
        mgameView.restart();

    }


    public void updateCurrentScore(int score){

        tv_home_score.setText(score + "");
    }

    public void updateHighestScore(int score){

        tv_home_record.setText(score+"");
    }

    public void updateTargetScore(int score){
        tv_home_target.setText(score+"");
    }
}
