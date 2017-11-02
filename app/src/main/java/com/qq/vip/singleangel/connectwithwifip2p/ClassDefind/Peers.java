package com.qq.vip.singleangel.connectwithwifip2p.ClassDefind;

/**
 * Created by singl on 2017/11/1.
 * 当前节点可以reach的节点，称为邻居节点
 */

public class Peers {
    private int deviceID;    //邻居设备ID
    private int rssi;        //Wifi P2P的信号强度
    private float distance;  //节点间的相对距离
    private int trend;       //节点间的运动趋势

    public Peers(int deviceID){
        this.deviceID = deviceID;
        this.rssi = 0;
        this.distance = 0;
        this.trend = 0;
    }

    public Peers(int deviceID, int rssi, float distance, int trend ){
        this.deviceID = deviceID;
        this.rssi = rssi;
        this.distance = distance;
        this.trend = trend;
    }

    public void setDeviceID(int deviceID){
        this.deviceID = deviceID;
    }
    public void setRSSI(int rssi){
        this.rssi = rssi;
    }
    public void setDistance(int distance){
        this.distance = distance;
    }
    public void setTrend(int trend){
        this.trend = trend;
    }

    public int getDeviceID(){
        return this.deviceID;
    }
    public int getRSSI(){
        return this.rssi;
    }
    public float getDistance(){
        return this.distance;
    }
    public int getTrend(){
        return this.trend;
    }

}
