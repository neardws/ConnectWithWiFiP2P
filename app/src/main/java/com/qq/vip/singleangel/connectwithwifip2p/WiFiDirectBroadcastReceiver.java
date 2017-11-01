package com.qq.vip.singleangel.connectwithwifip2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;

import java.net.InetAddress;
import java.util.Collection;

/**
 * 接收WifiP2P 发来的消息
 * Created by singl on 2017/10/30.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver{

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel){
        this.manager = manager;
        this.channel = channel;
    }

    @Override
    /**
     * 在WiFi P2P设备进行连接时，首先先检查WIFI的状态，即是否开启，在WIFI开启的状态下才可以进行连接
     * 这些广播信息在系统接收到相应的信息后自动触发，所以我们只需更改里面内容即可
     */
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //Wifi 状态更改
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            //获取到WIFI状态信息
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,
                    WifiP2pManager.WIFI_P2P_STATE_DISABLED);
            //如果WIFI开启
            if (WifiP2pManager.WIFI_P2P_STATE_ENABLED == state){
                //设置标志位
            }else if (WifiP2pManager.WIFI_P2P_STATE_DISABLED == state){  //WIFI关闭
                //看看是否调用什么函数开启WIFI
            }else {  //出错参数

            }
        }
        //设备Discovery开始或停止时触发
        else if (WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION.equals(action)){
            //获得到Discovery信息，即是否进行Discovery过程
            int discoveryState = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE,
                    WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED);
            //Discovery启动
            if (WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED == discoveryState){
                //设置标志位
            }else if (WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED == discoveryState){
                //设置标志位
            }else { //参数错误

            }
        }
        //Discover Peers，在这里请求Peers List
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            if (manager != null){
                /**
                WifiP2pDeviceList p2pDeviceList = (WifiP2pDeviceList)intent
                        .getParcelableExtra(WifiP2pManager.EXTRA_P2P_DEVICE_LIST);
                Collection<WifiP2pDevice> p2pDevices =
                        (Collection<WifiP2pDevice>) p2pDeviceList.getDeviceList(); **/
                manager.requestPeers(channel, new MyPeerListListener()); //这里需要一个PeerListListener
            }else {  //manager为空对象，出错

            }

        }
        //连接状态发生改变
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            if (manager != null){
                NetworkInfo networkInfo = (NetworkInfo) intent
                        .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                boolean isConnected = networkInfo.isConnected();  //网络是否连接
                if (isConnected){
                   //可以看到这里通过NetworkInfo可以得到很多有用的信息
                    /**
                    //得到Wifip2p信息
                    WifiP2pInfo wifiP2pInfo = (WifiP2pInfo) intent
                            .getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
                    boolean isGroupOwner = (boolean) wifiP2pInfo.isGroupOwner;  //已连接，该节点是否为GO
                    InetAddress groupOwnerAddress = (InetAddress) wifiP2pInfo.groupOwnerAddress;
                    String groupOwnerIPAdd = (String) groupOwnerAddress.getHostAddress();  //GO的IP地址

                    WifiP2pGroup wifiP2pGroup = (WifiP2pGroup) intent
                            .getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_GROUP);
                     **/

                    manager.requestConnectionInfo(channel,new MyConnecstionInfoListener());//这里需要一个ConnectionInfoListener
                }else {   //未连接
                    //设置标志位
                }
            }else {  //manager为空对象，出错

            }
        }
        //该设备的信息发生改变
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
            //该节点的信息
            WifiP2pDevice device = (WifiP2pDevice) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            if (device != null){
                String deviceName = device.deviceName;  //节点的名字
                boolean isGroupOwner = device.isGroupOwner();  //该节点是否为GO
                String deviceAddress = device.deviceAddress;  //该节点的mac地址
                int deviceStatus = device.status;

            }

        }

    }
}
