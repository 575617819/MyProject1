package com.yanghang.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.GridLayout;

import com.application.MyApplication;
import com.example.administrator.myproject1_2048.Home;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/21.
 */
public class GameView extends GridLayout {

    private Home mHome;

    private float startx=0;
    private float starty=0;
    private float endx;
    private float endy;

    private int mcolumecount;
    private int mrowcount;
    private int mmax;

    private List<Point> blanklist;
    private MyNumberItem[][] NumberItemMatrix;

    //记住上一次位置
    private int [][] histroyMatrix;
    //如果有记录才能恢复上一次位置
    private boolean canRevert;

    List<Integer> caculorlist;

    //屏幕宽度
    private int widthPixels;

    private int currentScore;
    private int highestScore;
    SharedPreferences sp;



    public GameView(Context context) {
        super(context);

        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public void init(){


        mHome=Home.getActivity();
        MyApplication myApplication = (MyApplication)mHome.getApplication();
        mmax=myApplication.getTarget();
        mcolumecount=myApplication.getLinenumber();
        mrowcount=myApplication.getLinenumber();
        highestScore=myApplication.getHighestRecord();



        blanklist=new ArrayList<Point>();
        NumberItemMatrix=new MyNumberItem[mrowcount][mcolumecount];
        caculorlist=new ArrayList<Integer>();
        histroyMatrix=new int[mrowcount][mcolumecount];
        canRevert=false;
        currentScore=0;


         /*   sp=getContext().getSharedPreferences("config",getContext().MODE_PRIVATE);
            highestScore = sp.getInt("highestScore", 0);*/



        //对代码进行封装
        //getSystemService属于上下文的方法
        WindowManager windowManager = (WindowManager)getContext().getSystemService(getContext().WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        widthPixels = displayMetrics.widthPixels;
        Log.i("width", widthPixels + "");// I/width: 480

        setColumnCount(mcolumecount);
        setRowCount(mrowcount);

        for(int i=0;i<mrowcount;i++){
            for(int j=0;j<mcolumecount;j++){
                MyNumberItem myNumberItem = new MyNumberItem(getContext(),0);

                //测试颜色是否设置生效
                //myNumberItem.setTextNumber(8);

                addView(myNumberItem, widthPixels / mcolumecount, widthPixels / mrowcount);

                NumberItemMatrix[i][j]=myNumberItem;

                Point point = new Point();
                point.x=i;
                point.y=j;
                blanklist.add(point);

            }
        }

        //添加产生随机数的方法
        addRandomNumber();
        addRandomNumber();
    }


    //设置两个随机数(产生随机数的方法)
    public void addRandomNumber(){

        updateBlankList();

        int size = blanklist.size();
        if(size>0){
            int location = (int)(Math.floor(Math.random()*size));
            Point point = blanklist.get(location);
            NumberItemMatrix[point.x][point.y].setTextNumber(Math.random() > 0.2d ? 2 : 4);
        }


    }


    public void updateBlankList(){

        blanklist.clear();
        for(int i=0;i<mrowcount;i++){
            for(int j=0;j<mcolumecount;j++){
                MyNumberItem myNumberItem = NumberItemMatrix[i][j];
                if(myNumberItem.getNumber()==0){
                    blanklist.add(new Point(i,j));
                }
            }
        }
    }


    //捕获滑动屏幕的方向

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch(event.getAction()){

            case MotionEvent.ACTION_DOWN:
                Log.i("GameView", "ACTION_DOWN");
                startx = event.getX();
                starty = event.getY();

                saveHistroy();

                break;

            case MotionEvent.ACTION_MOVE:
               /* Log.i("GameView","ACTION_MOVE");*/
                break;

            case MotionEvent.ACTION_UP:
                Log.i("GameView", "ACTION_UP");
                endx = event.getX();
                endy = event.getY();


                int absx =(int) Math.abs(startx - endx);
                int  absy = (int)Math.abs(starty - endy);




                if (absx != 0 && absy != 0) {
                    judgeDeriction(startx, starty, endx, endy);

                    //实时更新分数currentScore
                    updateCurrentScore();


                    //判断游戏是否结束：可以继续玩，成功了，gameover
                    handleResult(isOver());
                }



                break;
        }

        return true;
    }



    private void updateCurrentScore(){

        Log.i("currentScore",currentScore+"");
        mHome.updateCurrentScore(currentScore);
    }


    public void updateHighestScore(){


            mHome.updateHighestScore(currentScore);


    }


    public void restart(){
        removeAllViews();
        init();
        updateCurrentScore();
    }


    public void revert(){

        if(canRevert){
            for(int i=0;i<mrowcount;i++){
                for(int j=0;j<mcolumecount;j++){
                    NumberItemMatrix[i][j].setTextNumber(histroyMatrix[i][j]);
                }
            }
        }




    }


    public void saveHistroy(){

        for(int i=0;i<mrowcount;i++){
            for(int j=0;j<mcolumecount;j++){
                histroyMatrix[i][j]=NumberItemMatrix[i][j].getNumber();
            }

        }
        canRevert=true;
    }


    public void handleResult(int result){

        if(result==1){


            if(highestScore<currentScore){

                updateHighestScore();
                MyApplication application = (MyApplication)mHome.getApplication();
                application.setHighestRecord(currentScore);
            }


            new AlertDialog.Builder(getContext()).setTitle("Very good!")
                    .setMessage("本难度游戏您已挑战成功")
                    .setPositiveButton("再来一局",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    restart();
                }
            })
                    .setNegativeButton("退出游戏",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mHome.finish();
                        }
                    }).show();
        }
        else if(result==3){

            new AlertDialog.Builder(getContext()).setTitle("失败!")
                    .setMessage("本次游戏已结束")
                    .setPositiveButton("再来一局",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restart();
                        }
                    })
                    .setNegativeButton("退出游戏",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mHome.finish();
                        }
                    }).show();
        }

        else{
            addRandomNumber();
        }
    }


    public int isOver(){

        //遍历矩阵并判断有没有2048：有(成功）没有：updateBlankLlist();
        for(int i=0;i<mrowcount;i++){
            for(int j=0;j<mcolumecount;j++){
                if(NumberItemMatrix[i][j].getNumber()==mmax){

                    return 1;
                }

            }
        }


        updateBlankList();

        if(blanklist.size()==0){
            for(int i=0;i<mrowcount;i++){
                for(int j=0;j<mcolumecount-1;j++){
                    if(NumberItemMatrix[i][j].getNumber()==NumberItemMatrix[i][j+1].getNumber()){

                        return 2;
                    }
                }



            }

            for(int i=0;i<mcolumecount;i++){
                for(int j=0;j<mrowcount-1;j++){
                    if(NumberItemMatrix[j][i].getNumber()==NumberItemMatrix[j+1][i].getNumber()){
                        return 2;
                    }
                }
            }
            return 3;
        }


       return 2;
    }

    public void judgeDeriction(float startx,float starty,float endx,float endy){


        if(Math.abs(startx-endx)>Math.abs(starty-endy)) {

                if (startx > endx) {
                    Log.i("GameView", "left");
                    slideLeft();
                } else {
                    Log.i("GameView", "right");
                    slideRight();
                }

            }


            else{


                if(starty>endy){
                    Log.i("GameView","up");
                    slideUp();
                }else {
                    Log.i("GameView","down");
                    slideDown();
                }
        }




    }



    //上下左右滑动
    public void slideUp(){

        int prenumber=-1;
        for(int i=0;i<mrowcount;i++){
            for(int j=0;j<mcolumecount;j++){
                int number = NumberItemMatrix[j][i].getNumber();

                if(number!=0){
                    if(number!=prenumber&&prenumber!=-1){
                        caculorlist.add(prenumber);

                    }else if(prenumber!=-1){
                        caculorlist.add(number*2);
                        currentScore+=number*2;
                        prenumber=-1;
                        continue;
                    }
                    prenumber=number;
                }
            }

            //把最后一个number加入到集合中
            if(prenumber!=0&&prenumber!=-1){
                caculorlist.add(prenumber);
            }

            //放入矩阵中j
            for(int p=0;p<caculorlist.size();p++){
                NumberItemMatrix[p][i].setTextNumber(caculorlist.get(p));
            }

            //空白部分填入0
            for(int q=caculorlist.size();q<mcolumecount;q++){
                NumberItemMatrix[q][i].setTextNumber(0);
            }

            //重置prenumber 和 caculorlist
            caculorlist.clear();
            prenumber=-1;

        }

    }



    public void slideDown(){

        int prenumber=-1;
        for(int i=0;i<mrowcount;i++){
            for(int j=mcolumecount-1;j>=0;j--){
                int number = NumberItemMatrix[j][i].getNumber();

                if(number!=0){

                    if(number!=prenumber&&prenumber!=-1){
                        caculorlist.add(prenumber);

                    }else if(prenumber!=-1){
                        caculorlist.add(number*2);
                        currentScore+=number*2;
                        prenumber=-1;
                        continue;
                    }

                    prenumber=number;

                }
            }


            //把最后一个number加入到集合中
            if(prenumber!=0&&prenumber!=-1){
                caculorlist.add(prenumber);
            }


            //将得到的集合中的prenumber放入集合中
            for(int p=mcolumecount-1;p>=mcolumecount-caculorlist.size();p--){
                NumberItemMatrix[p][i].setTextNumber(caculorlist.get(mcolumecount-1-p));
            }


            //空白处用0填补
            for(int q=mcolumecount-1-caculorlist.size();q>=0;q--){
                NumberItemMatrix[q][i].setTextNumber(0);
            }

            //重置caculorlist 和 prenumber
            caculorlist.clear();
            prenumber=-1;
        }

    }


    public void slideRight(){

        int prenumber=-1;
        for(int i=0;i<mrowcount;i++){
            for(int j=mcolumecount-1;j>=0;j--){

                int number = NumberItemMatrix[i][j].getNumber();

                if(number!=0){

                    if(number!=prenumber&&prenumber!=-1) {
                        caculorlist.add(prenumber);
                    }
                        else if(prenumber!=-1){
                            caculorlist.add(number*2);
                            currentScore+=number*2;
                            prenumber=-1;
                            continue;
                        }

                        prenumber=number;
                    }
                }

                //将最后一个prenumber添加到caculorlist
                if(prenumber!=0&&prenumber!=-1){
                    caculorlist.add(prenumber);
                }

                //将caculorlist中的值填入矩阵中
                for(int p=mcolumecount-1;p>=mcolumecount-caculorlist.size();p--){
                    NumberItemMatrix[i][p].setTextNumber(caculorlist.get(mcolumecount-1-p));
                }

                //空白处填入数字0
                for(int q=mcolumecount-1-caculorlist.size();q>=0;q--){
                    NumberItemMatrix[i][q].setTextNumber(0);
                }

                //重置caculorlist 和 prenumber
                caculorlist.clear();
                prenumber=-1;
            }
        }


    public void slideLeft(){

        int prenumber=-1;
        for(int i=0;i<mrowcount;i++){
            for(int j=0;j<mcolumecount;j++){

                int number = NumberItemMatrix[i][j].getNumber();

                if(number!=0){

                    if(number!=prenumber&&prenumber!=-1){
                        caculorlist.add(prenumber);

                    }else if(prenumber!=-1){
                        caculorlist.add(number*2);
                        currentScore+=number*2;
                        prenumber=-1;
                        continue;
                    }

                    prenumber=number;
                }
            }

            //将最后一个prenumber加入到caculorlist
            if(prenumber!=0&&prenumber!=-1){
                caculorlist.add(prenumber);
            }

            //将caculorlist中的数值加入到矩阵中
            for(int p=0;p<caculorlist.size();p++){
                NumberItemMatrix[i][p].setTextNumber(caculorlist.get(p));
            }

            //空白填写0
            for(int q=caculorlist.size();q<mcolumecount;q++){
                NumberItemMatrix[i][q].setTextNumber(0);
            }

            //重置caculorlist和prenumber
            caculorlist.clear();
            prenumber=-1;
        }

    }
}
