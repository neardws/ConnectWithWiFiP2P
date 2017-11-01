package com.qq.vip.singleangel.connectwithwifip2p.ClassDefind;

/**
 * Information中主要保存着节点的位置和运动信息
 * 这些信息由节点的传感器获得
 * Created by singl on 2017/11/1.
 */

public class Information {
    private static final String TAG = "Information";

    private int deviceID;           //设备ID
    private float speed;            //设备的速度
    private float acceleration;     //节点的加速度
    private float direction;        //节点的方向信息
    private double gpsLongitude;    //节点的经度
    private double gpsLatiude;      //节点的纬度

    /**
     * 默认构造函数，需要节点的ID信息，由Device中的deviceID确定
     * @param deviceID
     */
    public Information(int deviceID){
        this.deviceID = deviceID;
        this.speed = 0;
        this.acceleration = 0;
        this.direction = 0;
        this.gpsLongitude = 0;
        this.gpsLatiude = 0;
    }

    /**
     * 构造函数，拥有所有参数
     * @param deviceID
     * @param speed
     * @param acceleration
     * @param direction
     * @param gpsLongitude
     * @param gpsLatiude
     */
    public Information(int deviceID, float speed, float acceleration,
                       float direction, double gpsLongitude, double gpsLatiude){
        this.deviceID = deviceID;
        this.speed = speed;
        this.acceleration = acceleration;
        this.direction = direction;
        this.gpsLongitude = gpsLongitude;
        this.gpsLatiude = gpsLatiude;
    }

    /**
     * 设置参数函数，值得注意的是deviceID这个主键的参数只能在构造函数中赋值
     * @param speed
     */
    public void setSpeed(float speed){
        if (checkBigThanZero(speed)){
            this.speed = speed;
        }
    }
    public void setAcceleration(float acceleration){
        if (checkBigThanZero(acceleration)){
            this.acceleration = acceleration;
        }
    }
    public void setDirection(float direction){
        if (checkDirection(direction)){
            this.direction = direction;
        }
    }
    public void setGpsLongitude(double gpsLongitude){
        this.gpsLongitude = gpsLongitude;
    }
    public void setGpsLatiude(double gpsLatiude){
        this.gpsLatiude = gpsLatiude;
    }

    /**
     * 返回参数函数
     * @return
     */
    public int getDeviceID(){
        return this.deviceID;
    }
    public float getSpeed(){
        return this.speed;
    }
    public float getAcceleration(){
        return this.acceleration;
    }
    public float getDirection(){
        return this.direction;
    }
    public double getGpsLongitude(){
        return this.gpsLongitude;
    }
    public double getGpsLatiude(){
        return this.gpsLatiude;
    }

    /**
     * 格式检查函数
     * @return
     */
    public boolean checkID(int deviceID){
        //检查该deviceID是否真的存在，即是否和Device中符合
        return true;
    }
    public boolean checkBigThanZero(float num){
        if (num >= 0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 检查节点与正北方向的偏转角度，该值应该在0~360之间
     * @param direction
     * @return
     */
    public boolean checkDirection(float direction){
        if (0 <= direction && direction <= 360){
            return true;
        }else {
            return false;
        }
    }
}
