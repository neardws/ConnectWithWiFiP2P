package com.qq.vip.singleangel.connectwithwifip2p.DebugTool;

import android.content.IntentFilter;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by singl on 2017/11/1.
 */

public class MyLog {
    private static final String TAG = "MyLog";
    private static final String LOG_FILE_URL = Environment.getExternalStorageDirectory() + "/"
            + "logfile"  + "/log" + ".txt";
    /**
     * 构造函数 do nothing
     */
    public MyLog(){}

    /**
     * 生成LOG文件保存LOG信息
     */
    public static void newMyLog(){
        File file = new File(LOG_FILE_URL);
        if (!file.exists()){ //该文件不存在
            boolean isSuccess = false;
            try {
                isSuccess = file.createNewFile();  //新建该文件
            }catch (IOException e){
                e.printStackTrace();
            }
            if (isSuccess){
                Log.d(TAG, "Create log file success!");
            }else {
                Log.d(TAG, "Create log file failed");
            }
        }else { //该文件已存在
            //do nothing
        }
    }
    public static void debug(String TAG, String context){
        //得到当前时间，加上Debug内容写入LOG
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = simpleDateFormat.format(date);
        Log.d(TAG,nowTime + context);

        //将这些信息写入到MyLog文件中
        File file = new File(LOG_FILE_URL);
        try {
            FileWriter fileWriter = new FileWriter(file,true);
            fileWriter.write(nowTime + "  " + TAG + "  " + context +"\n");
            fileWriter.flush();
            fileWriter.close();
        }catch (IOException e){
            Log.d(TAG, "Writing Log file failed.");
        }
    }
}
