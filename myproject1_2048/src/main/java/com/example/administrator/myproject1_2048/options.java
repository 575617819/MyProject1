package com.example.administrator.myproject1_2048;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.application.MyApplication;

public class options extends ActionBarActivity {


    MyApplication myApplication;

    Button bt_options_target;
    Button bt_options_line;
    Button bt_options_back;
    Button bt_options_confirm;

    private int target;
    private int line;
    private TextView tv_options_target;
    private TextView tv_options_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        /**
         * 隐藏标题栏
         */
        getSupportActionBar().hide();


        tv_options_target = (TextView) findViewById(R.id.tv_options_target);
        tv_options_line = (TextView) findViewById(R.id.tv_options_line);

        bt_options_target=(Button)findViewById(R.id.bt_options_target);
        bt_options_line=(Button)findViewById(R.id.bt_options_line);
        bt_options_back=(Button)findViewById(R.id.bt_options_back);
        bt_options_confirm=(Button)findViewById(R.id.bt_options_confirm);


        myApplication=(MyApplication)getApplication();
        line = myApplication.getLinenumber();
        target= myApplication.getTarget();

        bt_options_line.setText(line+"");
        bt_options_target.setText(target+"");


    }

    public void setTarget(View v){

        final String[] items={"64","128","256","512","1024","2048","4096"};

        new AlertDialog.Builder(this)
                .setTitle("选择目标值")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        target = Integer.parseInt(items[which]);
                        bt_options_target.setText(target + "");
                    }
                })
                .show();


    }

    public void setLine(View v){

        final String[] items={"3","4","5","6"};

        new AlertDialog.Builder(this)
                .setTitle("选取行列数")
                .setItems(items,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        line = Integer.parseInt(items[which]);
                        bt_options_line.setText(line+"");
                    }
                }).show();

    }


    public void confirm(View v){

        myApplication.setTarget(target);
        myApplication.setLineNumber(line);
        finish();


    }

    public void back(View v){

        finish();

    }
}
