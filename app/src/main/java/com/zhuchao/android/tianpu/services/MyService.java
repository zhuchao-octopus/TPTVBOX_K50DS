package com.zhuchao.android.tianpu.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.zhuchao.android.libfilemanager.devices.MySerialPort;
import com.zhuchao.android.tianpu.R;
import com.zhuchao.android.tianpu.utils.ForegroundAppUtil;
import com.zhuchao.android.tianpu.utils.TypeTool;
import com.zhuchao.android.tianpu.views.dialogs.Mac_Dialog;
import com.zhuchao.android.tianpu.views.dialogs.MusicDialog;
import com.zhuchao.android.tianpu.views.dialogs.Sound_Effect_Dialog;
import com.zhuchao.android.video.OMedia;

import java.util.List;

public class MyService extends Service {
    private static int MicVolume = 0;
    private static int MusicVolume = 0;
    private static String mTopPackageName;
    private final String TAG = "MyService";
    private MySerialPort MyPortDevice = new MySerialPort(this);
    private byte[] SerialPortReceiveBuffer;
    private String data;
    private Handler SerialPortReceivehandler;
    private MusicDialog dialog;
    private Sound_Effect_Dialog sdialog;
    private Mac_Dialog mdialog;
    private Callback actionCallback;
    private byte[] bytes;
    private int h = 0;
    private MyReceiver receiver = null;
    //private String mType = "";
    private byte tbb[] = {0, 0, 0, 0};
    private byte[] SetMusicVolume = {0x02, 0x01, 0x02, 0x00, 0x00, 0x02, 0x00, 0x04, 0x00, 0x0b, 0x7E};//设置音乐音量  K70//
    private byte[] SetMusicVolumeK50 = {0x01, 0x01, 0x02, 0x00, 0x00, 0x02, 0x00, 0x04, 0x00, 0x0a, 0x7E};//设置音乐音量  K50
    private byte[] I2SChanelApp = {0x01, 0x01, 0x05, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x09, 0x7E};//最后使用的app  K50
    private byte[] QueryStateK50 = {0x01, 0x01, 0x06, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x09, 0x7E};//初始状态  K50
    private boolean isCharging = false;
    private MediaPlayer mMediaPlayer = null;
    private Handler handler = new Handler();
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            mTopPackageName = ForegroundAppUtil.getForegroundActivityName(getApplicationContext());
            //Toast.makeText(getApplicationContext(), foregroundActivityName, Toast.LENGTH_SHORT).show();

            handler.postDelayed(r, 1000);
        }
    };

    public static String GetTopPackageName() {
        return mTopPackageName;
    }

    public static int getMicVolume() {
        return MicVolume;
    }

    public static void setMicVolume(int micVolume) {
        MicVolume = micVolume;
    }

    public static int getMusicVolume() {
        return MusicVolume;
    }

    public static void setMusicVolume(int musicVolume) {
        MusicVolume = musicVolume;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SerialPortReceivehandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        boolean bRet = MyPortDevice.openPort("/dev/ttyS0", 9600, true);
        //boolean bRet = MyPortDevice.openPort("/dev/ttyS1", 9600, true);

        if (bRet == false) {
            Log.e("Service", "onCreate：串口打开失败！！！！！");
        } else {
            Log.e("Service", "onCreate：串口打开成功！！！！！");
            CheckSerialPortEvent();

            sendCommand(I2SChanelApp);
            sendCommand(QueryStateK50);
        }
    }

    public void sendCommand(byte[] bytes) {
        try {
            MyPortDevice.sendBuffer(bytes);
            Log.i("MyService.发送数据", TypeTool.ByteArrToHexStr(bytes, 0, bytes.length));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    public void setActionCallback(Callback actionCallback) {
        this.actionCallback = actionCallback;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //handler.postDelayed(r, 1000);
        return START_STICKY;
    }


    Handler mMyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //playMusic(context, R.raw.tp00);
                    break;
                case 1:
                    playMusic(null, R.raw.tp001);
                    break;
                case 2:
                    playMusic(null, R.raw.tp002);
                    break;
                case 3:
                    playMusic(null, R.raw.tp003);
                    break;
                case 4:
                    playMusic(null, R.raw.tp004);
                    break;
                case 5:
                    playMusic(null, R.raw.tp005);
                    break;
                case 6:
                    playMusic(null, R.raw.tp006);
                    break;
                case 7:
                    playMusic(null, R.raw.tp007);
                    break;
                case 8:
                    playMusic(null, R.raw.tp008);
                    break;
                case 9:
                    playMusic(null, R.raw.tp009);
                    break;
                case 10:
                    playMusic(null, R.raw.tp010);
                    break;
                case 11:
                    playMusic(null, R.raw.tp011);
                    break;
                case 12:
                    playMusic(null, R.raw.tp012);
                    break;
            }
        }

    };

    private void playMusic(final Context c, final int resID) {
        String suri = "android.resource://" + this.getApplicationContext().getPackageName() + "/" + resID;
        //Uri uri = Uri.parse(suri);
        AssetFileDescriptor afd = getResources().openRawResourceFd(resID);
        OMedia audio = new OMedia(suri);
        audio.with(this.getApplicationContext()).play(afd);
    }

    private void CheckSerialPortEvent() {
        //串口数据监听事件
        MyPortDevice.setOnDataReceiveListener(new MySerialPort.OnDataReceiveListener() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (data.length() >= 16) {
                        DataChange(data);
                    }
                }
            };

            @Override
            public void onDataReceive(Context context, byte[] buffer, int size) {
                SerialPortReceiveBuffer = buffer;
                data = TypeTool.ByteArrToHexStr(SerialPortReceiveBuffer, 0, size);

                if (buffer[2] == 0x06) {
                    MusicVolume = buffer[7];
                    MicVolume = buffer[8];
                    return;
                }
                //播放特效声
                if (buffer[2] == 0x21) {
                    byte result = buffer[5];
                    mMyHandler.sendEmptyMessage(result);
                    return;
                }
                //Setting
                if (buffer[2] == 0x01) {
                    byte result = buffer[7];
                    if (result == 0x20) {
                        Intent in = new Intent();
                        in.setClassName("com.android.settings", "com.android.settings.Settings");
                        startActivity(in);
                    }
                    return;
                }
                //是蓝牙连接
                if (buffer[2] == 0x24) {
                    byte result = buffer[7];
                    Intent intent = new Intent("BLUTOOLTH_STATUS");
                    intent.putExtra("BLUTOOLTH_STATUS", result == 0x01);
                    sendBroadcast(intent);
                    return;
                }
                //是否充电
                if (buffer[2] == 0x22) {
                    byte result = buffer[7];
                    Intent intent = new Intent("BATTERY_CHARGE");
                    if (result == 0x01)
                        isCharging = true;
                    else
                        isCharging = false;

                    intent.putExtra("isCharge", isCharging);
                    sendBroadcast(intent);
                    return;
                }
                //电量值
                if (buffer[2] == 0x23) {
                    Intent intent = new Intent("BATTERY_INFO");
                    int v = TypeTool.HexToInt(TypeTool.Byte2Hex(buffer[7]));
                    intent.putExtra("value", v);
                    sendBroadcast(intent);
                    Log.i("onDataReceive", data);
                    if ((v <= 10) && (!isCharging))
                        playMusic(context, R.raw.charge);
                    return;
                }

                if (buffer[2] == 0x25) {
                    int v = TypeTool.HexToInt(TypeTool.Byte2Hex(buffer[7]));
                    SystemProperties.set("ro.dsp.version", String.valueOf(v));
                    //System.setProperty("ro.dsp.version", String.valueOf(v));
                    Log.i(TAG, "SystemProperties.set(\"ro.dsp.version\", String.valueOf(v));v=" + v);
                    return;
                }


                if (null != SerialPortReceivehandler) {
                    SerialPortReceivehandler.post(runnable);
                } else {
                    Log.i("onDataReceive", "Data lost");
                }
            }

        });
    }

    private void DataChange(String data) {

        String type = data.substring(4, 8);
        String value = data.substring(12, 16);
        int v = Integer.valueOf(value, 16);
        Log.d("DataChange", "type=" + type + ",data=" + this.data + ",v=" + v);
        if (type.equals("0200")) {
            //音乐音量
            if ((v >= 0) && (v <= 60)) {
                showVolumeDialog(v, type);
                MusicVolume = v;
            }
        } else if (type.equals("0300")) {
            //mic音量
            if ((v >= 0) && (v <= 60)) {
                showVolumeDialog(v, type);
                MicVolume = v;
            }
        } else if (type.equals("0400")) {
            //音效
            switch (v) {
                case 1:
                    showEffectDialog3(R.drawable.u20, v, "1", type);
                    break;
                case 2:
                    showEffectDialog3(R.drawable.u20, v, "2", type);
                    break;
                case 3:
                    showEffectDialog3(R.drawable.u20, v, "3", type);
                    break;
                case 4:
                    showEffectDialog3(R.drawable.u20, v, "4", type);
                    break;
                case 5:
                    showEffectDialog3(R.drawable.u20, v, "5", type);
                    break;
                case 6:
                    showEffectDialog3(R.drawable.u20, v, "6", type);
                    break;
            }

        } else if (type.equals("0500")) {
            if (actionCallback != null) {
                if (isTopActivity("com.zhuchao.android.tianpu") == false && !data.equals("010105000002000000097E")) {
                    Intent i = new Intent();
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    ComponentName cn = new ComponentName("com.zhuchao.android.tianpu", "com.zhuchao.android.tianpu.activities.MainActivity");
                    i.setComponent(cn);

                    startActivity(i);
                }
                actionCallback.onDataChange(this.data);
            }
        } else if (type.equals("0700")) {
            //吉他
            showVolumeDialog2(R.drawable.jt, v, type);
        } else if (type.equals("0800")) {
            //监听
            showVolumeDialog2(R.drawable.ej, v, type);
        } else if (type.equals("0900")) {
            //直播
            showVolumeDialog2(R.drawable.zb, v, type);
        }

    }

    private void showVolumeDialog(int direction, String type) {
        if (dialog == null || dialog.isShowing() != true) {
            dialog = new MusicDialog(this);
            //dialog.setVolumeAdjustListener();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            //dialog.show();
        }
        dialog.adjustVolume(direction, true, type);
    }

    private void showVolumeDialog2(int resid, int v, String type) {
        if (dialog == null || dialog.isShowing() != true) {
            dialog = new MusicDialog(this);
            //dialog.setVolumeAdjustListener();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            //dialog.show();
        }
        dialog.setImageView(resid);
        dialog.adjustVolume(v, true, type);
    }

    private void showEffectDialog(int direction, String type) {
        if (sdialog == null || sdialog.isShowing() != true) {
            sdialog = new Sound_Effect_Dialog(this);
            //sdialog.setVolumeAdjustListener();
            sdialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            sdialog.show();
        }
        sdialog.adjustVolume(direction, true, type);
    }

    private void showEffectDialog3(int resID, int v, String Str, String type) {
        if (sdialog == null || sdialog.isShowing() != true) {
            sdialog = new Sound_Effect_Dialog(this);
            sdialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            sdialog.show();
        }
        sdialog.adjustVolume(v, true, type);
        sdialog.setContentValue(resID, Str);

    }

    private void setVolume(int i) {
        tbb = TypeTool.intToBytes(i);

        //SetMusicVolume[7] = tbb[3];
        //SetMusicVolume[8] = tbb[2];
        //SetMusicVolume[9] = com.zhuchao.android.tianpu.utils.TypeTool.BytesAdd(SetMusicVolume, 9);

        SetMusicVolumeK50[7] = tbb[3];
        SetMusicVolumeK50[8] = tbb[2];
        SetMusicVolumeK50[9] = TypeTool.BytesAdd(SetMusicVolumeK50, 9);


        ///MyPortDevice.sendBuffer(SetMusicVolume);
        MyPortDevice.sendBuffer(SetMusicVolumeK50);
        //Log.e("MyPortDevice", com.zhuchao.android.tianpu.utils.TypeTool.ByteArrToHexStr(bytes, 0, bytes.length));
    }

    private boolean isTopActivity(String packageName) {

        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            String strt = ForegroundAppUtil.getForegroundActivityName(getApplicationContext());
            if (packageName.equals(strt))
                return true;

        } else {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(5);
            if (tasksInfo.size() > 0) {
                //应用程序位于堆栈的顶层
                String str = tasksInfo.get(0).topActivity.getPackageName();
                if (packageName.contains(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static interface Callback {
        void onDataChange(String data);
    }

    public class Binder extends android.os.Binder {  //
        public MyService getService() {

            receiver = new MyReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.iflytek.xiri2.hal.volume");
            filter.addAction("com.iflytek.xiri2.hal.iflytekService");
            registerReceiver(receiver, filter);
            return MyService.this;
        }

    }

    public class MyReceiver extends BroadcastReceiver {
        //adb shell am broadcast -a com.iflytek.xiri2.hal.volume
        //adb shell am broadcast -a com.iflytek.xiri2.hal.volume --es volume 30
        //adb shell am broadcast -a com.zhuchao.android.tianpu.services
        @Override
        public void onReceive(Context context, Intent intent) {
            int data = -1;
            String mType = "0200";
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                data = bundle.getInt("volume", -1);
                mType = bundle.getString("type", "0200");
            }


            if (intent.getAction().equals("com.iflytek.xiri2.hal.volume")) {
                if (data > 60) data = 60;

                Log.d("MyReceiver--->", intent.getAction() + ":volume=" + data);

                if (intent.getAction() == "com.iflytek.xiri2.hal.volume") {
                    if ((data >= 0) && (data <= 60)) {
                        if (mType.equals("0200")) {
                            //音乐音量
                            showVolumeDialog(data, mType);
                            MusicVolume = data;
                            setVolume(MusicVolume);
                        } else if (mType.equals("0300")) {
                            //mic音量
                            showVolumeDialog(data, mType);
                            MicVolume = data;
                        } else {
                            showVolumeDialog(data, mType);
                            MusicVolume = data;
                        }
                    } else {
                        showVolumeDialog(MusicVolume, "0200");
                    }
                }
            }
            if (intent.getAction().equals("com.iflytek.xiri2.hal.iflytekService")) {
                byte[] bytes = bundle.getByteArray("SerialData");
                sendCommand(bytes);
            }
        }
    }

}
