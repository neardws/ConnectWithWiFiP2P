package com.qq.vip.singleangel.connectwithwifip2p.ClassDefind;

import com.qq.vip.singleangel.connectwithwifip2p.DebugTool.MyLog;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by singl on 2017/11/1.
 * 节点在WIFI P2P中的信息
 */
public class Device implements Serializable{

    private final static String TAG = "Device";

    private int deviceID;           //设备的ID号
    private String deviceName;      //设备名字
    private String macAdd;          //设备MAC地址
    private String ipAdd;           //设备IP地址

    /**
     * 标志位，FLAG
     */
    private boolean isWiFiEnabled;  //Wifi是否开启
    private boolean isDiscover;     //是否在发现状态
    private boolean isConnected;    //是否连接标志位
    private boolean isGroupOwner;   //是否是GO标志位
    private boolean isChannelDisconnected;  //信道断开

    public final static boolean CONNECTED = true;
    public final static boolean NOT_CONNECTED = false;
    public final static boolean IS_GROUPOWNER = true;
    public final static boolean NOT_GROUPOWNER = false;
    public final static boolean WIFI_ENABLED = true;
    public final static boolean WIFI_DISABLED = false;
    public final static boolean DISCOVERED = true;
    public final static boolean NOT_DISCOVERED = false;
    public final static boolean CHANNEL_CONNECTED = true;
    public final static boolean CHANNEL_DISCONNECTED = false;
    public final static String NULL_STRING = "";

    /**
     * 构造函数，默认只有mac地址来构造Device，之后可以用SET函数将其他值传入
     * @param macAdd
     */
    public Device(String macAdd){
        if (checkHash(HashWithMAC(macAdd))){
            this.deviceID = HashWithMAC(macAdd);
        }
        this.deviceName = NULL_STRING;
        this.macAdd = macAdd;
        this.ipAdd = NULL_STRING;
        this.isConnected = NOT_CONNECTED;
        this.isGroupOwner = NOT_GROUPOWNER;
        this.isWiFiEnabled = WIFI_DISABLED;
        this.isDiscover = NOT_DISCOVERED;
        this.isChannelDisconnected = CHANNEL_DISCONNECTED;
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
                  boolean isWiFiEnabled, boolean isDiscover, boolean isChannelDisconnected,
                  String deviceName, String macAdd){
        if (checkHash(HashWithMAC(macAdd))){
            this.deviceID = HashWithMAC(macAdd);
        }
        this.isConnected = isConnected;
        this.isGroupOwner = isGroupOwner;
        this.deviceName = deviceName;
        this.macAdd = macAdd;
        this.ipAdd = NULL_STRING;
        this.isWiFiEnabled = isWiFiEnabled;
        this.isDiscover = isDiscover;
        this.isChannelDisconnected = isChannelDisconnected;
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
                  boolean isWiFiEnabled, boolean isDiscover, boolean isChannelDisconnected,
                  String deviceName, String macAdd, String ipAdd){
        if (checkHash(HashWithMAC(macAdd))){
            this.deviceID = HashWithMAC(macAdd);
        }
        this.isConnected = isConnected;
        this.isGroupOwner = isGroupOwner;
        this.deviceName = deviceName;
        this.macAdd = macAdd;
        this.ipAdd = ipAdd;
        this.isWiFiEnabled = isWiFiEnabled;
        this.isDiscover = isDiscover;
        this.isChannelDisconnected = isChannelDisconnected;
    }

    /**
     * 使用哈希函数将MAC地址生成固定位数的INT型数
     * 传入的MAC地址满足格式xx:xx:xx:xx:xx:xx，其中x=0-9,A-F
     * @param macAdd
     * @return
     */
    public static int HashWithMAC(String macAdd){
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

    public boolean checkHash(int zero){
        if (0 == zero){   //生成的hash出错
            MyLog.debug(TAG, "Hash is going wrong.");
            return false;
        }else {
            return true;
        }
    }

    /**
     * 设置参数函数
     * @param macAdd
     */
    public void setDeviceID(String macAdd){
       if (checkHash(HashWithMAC(macAdd))){
           this.deviceID = HashWithMAC(macAdd);
       }
   }
    public void setConnected(boolean isConnected){
        this.isConnected = isConnected;
    }
    public void setGroupOwner(boolean isGroupOwner){
        this.isGroupOwner = isGroupOwner;
    }
    public void setDeviceName(String deviceName){
        this.deviceName = deviceName;
    }
    public void setMacAdd(String macAdd){
        this.macAdd = macAdd;
    }
    public void setIpAdd(String ipAdd){
        this.ipAdd = ipAdd;
    }
    public void setWifiEnabled(boolean isWiFiEnabled){
        this.isWiFiEnabled = isWiFiEnabled;
    }
    public void setDiscover(boolean isDiscover){
        this.isDiscover = isDiscover;
    }
    public void setChannelDisconnected(boolean isChannelDisconnected){
        this.isChannelDisconnected = isChannelDisconnected;
    }
    /**
     * 得到参数函数
     * @return
     */
    public int getDeviceID(){
        return this.deviceID;
    }
    public boolean isConnected(){
        return this.isConnected;
    }
    public boolean isGroupOwner(){
        return this.isGroupOwner;
    }
    public String getDeviceName(){
        return this.deviceName;
    }
    public String getMacAdd(){
        return this.macAdd;
    }
    public String getIpAdd(){
        return this.ipAdd;
    }
    public boolean isWiFiEnabled(){
        return this.isWiFiEnabled;
    }
    public boolean isDiscover(){
        return this.isDiscover;
    }
    public boolean isChannelDisconnected(){
        return this.isChannelDisconnected;
    }

    /**
     * 使用Device对象来新建一个新的Device对象
     * @param device
     * @return
     */
    public boolean updateDevice(Device device){
        setDeviceID(device.getMacAdd());
        setConnected(device.isConnected());
        setGroupOwner(device.isGroupOwner());
        setDeviceName(device.getDeviceName());
        setMacAdd(device.getMacAdd());
        setIpAdd(device.getIpAdd());
        setWifiEnabled(device.isWiFiEnabled());
        setDiscover(device.isDiscover());
        setChannelDisconnected(device.isChannelDisconnected());
        return true;
    }


}
