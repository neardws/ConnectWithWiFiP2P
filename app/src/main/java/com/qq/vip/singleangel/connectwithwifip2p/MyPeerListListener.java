package com.qq.vip.singleangel.connectwithwifip2p;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.util.Collection;

/**
 * Created by singl on 2017/11/1.
 */

public class MyPeerListListener implements WifiP2pManager.PeerListListener {
    public MyPeerListListener(){

    }
    @Override
    public void onPeersAvailable(WifiP2pDeviceList p2pDeviceList) {
        Collection<WifiP2pDevice> p2pDevices =
                (Collection<WifiP2pDevice>) p2pDeviceList.getDeviceList();
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
