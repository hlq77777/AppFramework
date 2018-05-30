package com.android.kingwong.appframework.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by KingWong on 2017/8/22.
 */
public class CountDownTimerTextView extends AppCompatTextView {
    long millisInFuture = 0;
    OnCountDownFinishListener onCountDownFinishListener;


    public CountDownTimerTextView(Context context) {
        super(context);
    }

    public CountDownTimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountDownTimerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        if(myCount==null){
//            myCount = new MyCount();
//        }
    }

    public void setMillisInFuture(long millisInFuture) {
        this.millisInFuture = millisInFuture;
        setText(secToTime((int) millisInFuture/1000));
    }
    public void startCountDown(){
        Message message = handler.obtainMessage(1);     // Message
        if(!handler.hasMessages(1)){
            handler.sendMessageDelayed(message, 1000);
        }
    }
    public void stopCountDown(){
        handler.removeMessages(1);
    }
    public void setOnCountDownFinishListener(OnCountDownFinishListener onCountDownFinishListener){
        this.onCountDownFinishListener = onCountDownFinishListener;
    }


    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
    public interface OnCountDownFinishListener{
        void onCountDownFinish();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        if(handler!=null&&handler.hasMessages(1)){
//            handler.removeMessages(1);
//        }
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){         // handle message
            switch (msg.what) {
                case 1:
                    millisInFuture = millisInFuture - 1000;
                    if(millisInFuture > 0){
                        setText(secToTime((int) (millisInFuture / 1000)));
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);
                    }else{
                        if(onCountDownFinishListener!=null){
                            onCountDownFinishListener.onCountDownFinish();
                        }
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}

