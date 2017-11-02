package com.qq.vip.singleangel.connectwithwifip2p;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.qq.vip.singleangel.connectwithwifip2p.ClassDefind.Device;
import com.qq.vip.singleangel.connectwithwifip2p.ClassDefind.Peers;

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

    @Override
    public void onChannelDisconnected() {

    }

    /**
     * 得到本机的Mac地址
     * @return
     */
    public static String getMacAdd(){
        String macAdd = null;
        return macAdd;
    }
}
