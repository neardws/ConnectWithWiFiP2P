package com.qq.vip.singleangel.connectwithwifip2p.ClassDefind;

import java.util.Date;

/**
 * Created by singl on 2017/11/1.
 * 节点在WIFI P2P中的信息
 */
public class Device {
    private int deviceID;           //设备的ID号
    private boolean isConnected;    //是否连接标志位
    private boolean isGroupOwner;   //是否是GO标志位
    private String deviceName;      //设备名字
    private String macAdd;          //设备MAC地址
    private String ipAdd;           //设备IP地址

    public final static boolean CONNECTED = true;
    public final static boolean NOT_CONNECTED = false;
    public final static boolean IS_GROUPOWNER = true;
    public final static boolean NOT_GROUPOWNER = false;
    public final static String NULL_STRING = "";

    /**
     * 构造函数，默认只有mac地址来构造Device，之后可以用SET函数将其他值传入
     * @param macAdd
     */
    public Device(String macAdd){
        this.deviceID = HashWithMAC(macAdd);
        this.isConnected = NOT_CONNECTED;
        this.isGroupOwner = NOT_GROUPOWNER;
        this.deviceName = NULL_STRING;
        this.macAdd = macAdd;
        this.ipAdd = NULL_STRING;
    }

    /**
     * 构造函数，缺少IP地址信息的构造函数
     * @param deviceID
     * @param isConnected
     * @param isGroupOwner
     * @param deviceName
     * @param macAdd
     */
    public Device(int deviceID, boolean isConnected, boolean isGroupOwner,
                  String deviceName, String macAdd){
        this.deviceID = HashWithMAC(macAdd);
        this.isConnected = isConnected;
        this.isGroupOwner = isGroupOwner;
        this.deviceName = deviceName;
        this.macAdd = macAdd;
        this.ipAdd = NULL_STRING;
    }

    /**
     * 构造函数，拥有全部参数的构造函数
     * @param deviceID
     * @param isConnected
     * @param isGroupOwner
     * @param deviceName
     * @param macAdd
     * @param ipAdd
     */
    public Device(int deviceID, boolean isConnected, boolean isGroupOwner,
                  String deviceName, String macAdd, String ipAdd){
        this.deviceID = HashWithMAC(macAdd);
        this.isConnected = isConnected;
        this.isGroupOwner = isGroupOwner;
        this.deviceName = deviceName;
        this.macAdd = macAdd;
        this.ipAdd = ipAdd;
    }

    /**
     * 使用哈希函数将MAC地址生成固定位数的INT型数
     * 传入的MAC地址满足格式xx:xx:xx:xx:xx:xx，其中x=0-9,A-F
     * @param macAdd
     * @return
     */
    public int HashWithMAC(String macAdd){
        String removeString = macAdd.replace(":","");
        /**
         * 格式检查
         */
        int i = 0;
        for (; i < removeString.length(); i++){
            char c = removeString.charAt(i);
            if ('A'<= c){
                if (c <= 'F'){ //c在A到F之间
                    //do noting
                }else {
                    return 0;
                }
            }else if (c <= '9'){
                if ('0' <= c){  //c在0到9之间
                    //do noting
                }else {   //错误参数
                    return 0;
                }
             }
        }
        if (i == (removeString.length() - 1) && i == 11){
            //do noting
        }else {
            return 0;  //位数出错
        }
        int hash = removeString.hashCode();
        return hash;
    }

   public void setDeviceID(String macAdd){
       if (HashWithMAC(macAdd) == 0){

       }else {
           this.deviceID = HashWithMAC(macAdd);
       }
   }
    public void setConnected(boolean isConnected){
        this.isConnected = isConnected;
    }
    public void setGroupOwner(boolean isGroupOwner){
        this.isGroupOwner = isGroupOwner;
    }

}
