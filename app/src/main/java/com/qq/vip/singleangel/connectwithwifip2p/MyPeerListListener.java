package com.qq.vip.singleangel.connectwithwifip2p;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.qq.vip.singleangel.connectwithwifip2p.ClassDefind.Device;
import com.qq.vip.singleangel.connectwithwifip2p.ClassDefind.Peers;
import com.qq.vip.singleangel.connectwithwifip2p.DebugTool.MyLog;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by singl on 2017/11/1.
 */

public class MyPeerListListener implements WifiP2pManager.PeerListListener {
    private static final String TAG = "MyPeerListListener";
    /**
     * 默认无参构造函数
     */
    public MyPeerListListener(){

    }
    @Override
    public void onPeersAvailable(WifiP2pDeviceList p2pDeviceList) {
        Collection<WifiP2pDevice> p2pDevices =
                (Collection<WifiP2pDevice>) p2pDeviceList.getDeviceList();
        if (p2pDevices.isEmpty()){   //该节点的Peer为空，即扫描结束后没有发现任何邻居节点
            MyLog.debug(TAG, "The Peer List is empty");
            //处理该情况，考虑重新扫描？
        }else {   //有该节点的邻居节点，考虑写入由Peers组成的链表中
            ArrayList<Peers> peersArrayList = new ArrayList<Peers>();
            for (WifiP2pDevice device : p2pDevices){
                String deviceMAC = device.deviceAddress;
                Peers peers = new Peers(Device.HashWithMAC(deviceMAC));
                peersArrayList.add(peers);
            }
        }
    }

    public static String getDeviceStatus(int deviceStatus) {
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE://该设备可用
                return "Available";
            case WifiP2pDevice.INVITED://已邀请
                return "Invited";
            case WifiP2pDevice.CONNECTED://该设备已连接
                return "Connected";
            case WifiP2pDevice.FAILED://失败
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE: //该设备不可用
                return "Unavailable";
            default:
                return "Unknown";

        }
    }
}
