package com.qq.vip.singleangel.connectwithwifip2p;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.qq.vip.singleangel.connectwithwifip2p.ClassDefind.Device;
import com.qq.vip.singleangel.connectwithwifip2p.ClassDefind.Peers;
import com.qq.vip.singleangel.connectwithwifip2p.DebugTool.MyLog;

import java.util.ArrayList;

/**
 * Created by singl on 2017/11/2.
 */

public class WiFiP2PService extends Service implements WifiP2pManager.ChannelListener {
    private static final String TAG = "WiFiP2PService";

    private WifiP2pManager manager;  //WifiP2P的管理器
    private WifiP2pManager.Channel channel;  //WifiP2P的信道
    private BroadcastReceiver receiver = null;   //广播接收，WifiP2P的各种消息
    private final IntentFilter intentFilter = new IntentFilter();
    private boolean retryChannel = false;
    /**
     * WiFiP2PService中存放的信息包括
     * 节点的信息Device和该节点的邻居节点信息peersArrayList
     */
    private Device device; //当前节点的信息
    private ArrayList<Peers> peersArrayList = new ArrayList<Peers>();

    public static final String DISCOVERY = "Discovery";
    public static final String CONNECT = "Connect";
    public static final String START_SERVER = "StartServer";
    public static final String START_CLIENT = "StartClient";
    public static final String SEND_FILE = "SendFile";  //发送文件，

    @Override
    /**
     * 服务开启，与UI解耦
     * 只调用一次，之后使用onStartCommand()执行相关操作
     */
    public void onCreate() {
        super.onCreate();

        MyLog.newMyLog();

        /**
         *得到本节点的mac地址信息，用它生成一个Device对象
         */
        device = new Device(getMacAdd());

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
    }

    /**
     *使用该函数来调用，可以执行来实现Discovery、Connect等功能
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**
         * deal with intent
         */
        String action = intent.getAction();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 结束服务
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 网络原因信道丢失或者远程主机主动断开连接或者客户端断开连接
     */
    @Override
    public void onChannelDisconnected() {
        if (manager != null && !retryChannel){  //重试一次
            MyLog.debug(TAG, "Channel lost. Trying again");
            retryChannel = true;
            manager.initialize(this, getMainLooper(), this);
        }else {
            MyLog.debug(TAG, "Severe! Channel is probably lost permanently. Try Disable/Re-Enable P2P.");
            device.setChannelDisconnected(Device.CHANNEL_DISCONNECTED);
        }
    }

    /**
     * WIFI P2P 相关操作函数，Discover、Connect
     */
    /**
     * discoverStarted = false 意味着discover过程结束或者未开始
     * discoverStarted = true 意味着discover过程开始但是未结束
     * connectStarted 同上
     */
    private boolean discoverStarted = DISCOVER_NOT_STARTED;
    private boolean connectStarted = CONNECT_NOT_STARTED;

    private final static boolean DISCOVER_STARTED = true;
    private final static boolean DISCOVER_NOT_STARTED = false;
    private final static boolean CONNECT_STARTED = true;
    private final static boolean CONNECT_NOT_STARTED = false;


    /**
     * 发现函数，执行前提条件，检查Wifi开启情况
     * @param channel
     */
    public void discoverPeers(WifiP2pManager.Channel channel){
        if (discoverStarted == DISCOVER_NOT_STARTED){ //未启动发现过程
            discoverStarted = DISCOVER_STARTED;    //设置标志位已启动发现
            /**
             * 因为之前可能有过发现过程
             * 我们将Peer列表进行重置
             */
            peersArrayList.clear();  //重置ArrayList
            manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    MyLog.debug(TAG, "Discover Peers success.");
                    device.setDiscover(Device.DISCOVERED);          //这次发现过程结果是成功的
                    discoverStarted = DISCOVER_NOT_STARTED;         //设置标志位发现过程以结束
                }

                @Override
                public void onFailure(int reason) {
                    MyLog.debug(TAG, "Discover Peers failed.");
                    device.setDiscover(Device.NOT_DISCOVERED);   //这次发现过程结果是失败的
                    discoverStarted = DISCOVER_NOT_STARTED;     //设置标志位发现过程以结束
                }
            });
        }else if (discoverStarted == DISCOVER_STARTED){
            /**
             * 防止已有一个发现过程启动的情况下，再次执行可能会导致错误
             * 发现过程已启动，先运行停止发现过程，再启动该操作
             */
            stopDiscoverPeers(channel);
            discoverPeers(channel); //调用本身
        }else {
            MyLog.debug(TAG, "DiscoverPeers else part.");
        }
    }

    /**
     * 停止发现过程
     * @param channel
     */
    public void stopDiscoverPeers(WifiP2pManager.Channel channel){
        /**
         * 发现过程正在进行
         */
        if (discoverStarted == DISCOVER_STARTED){
            manager.stopPeerDiscovery(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    /**
                     * 这里值得注意的是Device中的discover信息只代表着上一次的discover过程的结果
                     * 成功的话就是Device.DISCOVERED
                     * 失败的话就是Device.NOT_DISCOVERED
                     */
                    MyLog.debug(TAG, "STOP Discover Peers success.");
                    //device.setDiscover(Device.NOT_DISCOVERED);
                }

                @Override
                public void onFailure(int reason) {
                    MyLog.debug(TAG, "STOP Discover Peers failed.");
                    //device.setDiscover(Device.DISCOVERED);
                }
            });
        }else if (discoverStarted == DISCOVER_NOT_STARTED){ //发现没有启动，故不需要禁止
            MyLog.debug(TAG, "Stop Discover peers second part, do nothing.");
        }else {
            MyLog.debug(TAG, "Stop Discover peers third part.");
        }
    }

    /**
     * 连接函数，前提条件，未连接
     * 检查上一次发现过程的结果，即是否成功
     * 如果成功，即device.isDiscover = Device.DISCOVERED
     * 那么意味着获得了Peer列表，并且连接对象应该在Peer列表中
     * @param config
     */
    public void connect(WifiP2pConfig config){
        /**
         * 没有连接过程正在执行
         */
        if (connectStarted == CONNECT_NOT_STARTED){
            /**
             * 未连接的状态
             */
            connectStarted = CONNECT_STARTED; //节点现在开始连接过程
            if (Device.NOT_CONNECTED == device.isConnected()){
                /**
                 * 该节点发现成功并且未连接，最好情况
                 */
                if (Device.DISCOVERED == device.isDiscover()){
                    /**
                     * 要连接的节点在该节点的邻居节点列表中，即可连接，config参数正确
                     */
                    if (isInPeers(config.deviceAddress)){
                        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                            @Override
                            public void onSuccess() {
                                MyLog.debug(TAG, "Connect success.");
                                device.setConnected(Device.CONNECTED);
                                connectStarted = CONNECT_NOT_STARTED;
                            }

                            @Override
                            public void onFailure(int reason) {
                                MyLog.debug(TAG, "Connect failed.");
                                device.setConnected(Device.NOT_CONNECTED);
                                connectStarted = CONNECT_NOT_STARTED;
                            }
                        });
                    }else {
                        connectStarted = CONNECT_NOT_STARTED;
                        MyLog.debug(TAG, "Connect error config ,the mac address is error");
                    }

                }else if (Device.NOT_DISCOVERED == device.isDiscover()){
                    /**
                     * 该节点上一次发现失败，即没有邻居节点列表信息，那么我们需要将
                     * 在连接前重新发现一次,之后再重试
                     */
                    MyLog.debug(TAG, "Connect need correct config, try later.");
                    discoverPeers(channel);
                    connectStarted = CONNECT_NOT_STARTED;
                }else {
                    MyLog.debug(TAG, "Connect Third IF loop third part.");
                    connectStarted = CONNECT_NOT_STARTED;
                }

            }else if (Device.CONNECTED == device.isConnected()){
                /**
                 * 已成功连接，断开之后再连
                 */
                disConnect();
                connect(config);
            }else {
                connectStarted = CONNECT_NOT_STARTED;
                MyLog.debug(TAG, "Connect Second IF loop third part.");
            }
        }else if (connectStarted == CONNECT_STARTED){
            /**
             * 已有一个连接过程正在进行
             * 调用cancelConnect取消，然后再调用该过程
             */
            cancelConnect(channel);
            connect(config);
        }

    }

    /**
     * 在节点进行连接，但是还未连接成功的状态可以调用该函数取消连接
     * 前提条件开始连接但是还未成功的状态
     * @param channel
     */
    public void cancelConnect(WifiP2pManager.Channel channel){
        if (connectStarted == CONNECT_STARTED){
            manager.cancelConnect(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    connectStarted = CONNECT_NOT_STARTED;
                    device.setConnected(Device.NOT_CONNECTED);
                    MyLog.debug(TAG, "Cancel Connect success.");
                }

                @Override
                public void onFailure(int reason) {
                    MyLog.debug(TAG, "Cancel Connect failed.");
                }
            });
        }else {
            MyLog.debug(TAG, "Cancel Connect do nothing.");
        }
    }

    /**
     * 在连接成功后可以调用该函数将连接断开
     * 前提条件是Device.CONNECTED == device.isConnected()
     */
    public void disConnect(){
        if (Device.CONNECTED == device.isConnected()){  //当前状态，已连接
            manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    MyLog.debug(TAG, "Disconnect success.");
                    device.setConnected(Device.NOT_CONNECTED);
                }

                @Override
                public void onFailure(int reason) {
                    MyLog.debug(TAG, "Disconnect failed.Reason is "+ String.valueOf(reason));
                    device.setConnected(Device.CONNECTED);
                }
            });
        }else if (Device.NOT_CONNECTED == device.isConnected()){
            MyLog.debug(TAG, "Disconnect do nothing.");
            //do nothing
        }else {
            MyLog.debug(TAG, "Disconnect third in if range.");
        }

    }


    /**
     * 得到本机的Mac地址
     * @return
     */
    public static String getMacAdd(){
        String macAdd = null;
        return macAdd;
    }

    /**
     * 使用d来更新该类下的device
     * @param d
     * @return
     */
    public boolean updateDevice(Device d){
        device.updateDevice(d);
        return true;
    }

    /**
     * 检查该mac地址的节点是否在该节点的邻居节点列表中
     * @param macAdd
     * @return
     */
    public boolean isInPeers(String macAdd){
        int deviceID = Device.HashWithMAC(macAdd);
        if (peersArrayList == null){
            MyLog.debug(TAG, "isInPeers: NULL POINT ERROR");
            return false;
        }else {
            if (!peersArrayList.isEmpty()){
                for (Peers peers : peersArrayList){
                    if (peers.getDeviceID() == deviceID){
                        MyLog.debug(TAG, "isInPeers? YES");
                        return true;
                    }
                }
            }
            MyLog.debug(TAG, "isInPeers? NO");
            return false;
        }
    }


}
