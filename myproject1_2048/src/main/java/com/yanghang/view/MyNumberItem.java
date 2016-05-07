package com.yanghang.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/3/21.
 */
public class MyNumberItem extends FrameLayout {


    private TextView mtv;
    private int number;


    public MyNumberItem(Context context) {
        super(context);

        initView(0);
    }

    public MyNumberItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(0);
    }

    public MyNumberItem(Context context,int number){
        super(context);

        initView(number);
    }

    public void initView(int number){

        setBackgroundColor(Color.GRAY);
        mtv = new TextView(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(5, 5, 5, 5);
        mtv.setGravity(Gravity.CENTER);

       /* mtv.setText(number + "");
        mtv.setBackgroundColor(Color.GRAY);*/

        setTextNumber(number);

        addView(mtv, layoutParams);
        this.number=number;

    }


    //向外界返回当前控件内保存的数字
    public int getNumber(){
        return number;
    }


    //设置对应的数字对应的背景颜色
    public void setTextNumber(int num){

        number=num;

        if(num==0){
            mtv.setText("");
            mtv.setBackgroundColor(Color.GRAY);
        }else{

            mtv.setText(num+"");
            switch (num){

                case 2:
                    mtv.setBackgroundColor(0xFFFFFACD);
                    break;
                case 4:
                    mtv.setBackgroundColor(0xFFFFB6C1);
                    break;
                case 8:
                    mtv.setBackgroundColor(0xFFF0E68C);
                    break;
                case 16:
                    mtv.setBackgroundColor(0xFFD8BFD8);
                    break;
                case 32:
                    mtv.setBackgroundColor(0xFFF4A460);
                    break;
                case 64:
                    mtv.setBackgroundColor(0xFF00ff00);
                    break;
                case 128:
                    mtv.setBackgroundColor(0xFF00E00D);
                    break;
                case 256:
                    mtv.setBackgroundColor(0xFFADD8E6);
                    break;
                case 512:
                    mtv.setBackgroundColor(0xFF228B22);
                    break;
                case 1024:
                    mtv.setBackgroundColor(0xFF8B4513);
                    break;
                case 2048:
                    mtv.setBackgroundColor(0xFF800080);
                    break;
                case 4096:
                    mtv.setBackgroundColor(0xFFFFD700);
                    break;

            }
        }
    }


}
