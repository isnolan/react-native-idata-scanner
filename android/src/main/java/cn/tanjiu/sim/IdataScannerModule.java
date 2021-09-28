// IdataScannerModule.java

package com.yhostc.idata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdataScannerModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    private String BROADCASTNAME = "";
    private String CODENAME = "";
    private String codeStr="";
    private Pattern p = Pattern.compile("\\s*|\r|\n");
            /*\n 回车(\u000a)
              \t 水平制表符(\u0009)
              \s 空格(\u0008)
              \r 换行(\u000d)*/

    public IdataScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "IdataScanner";
    }

     /**
     * 设置广播名称
     * @param broadcastSettingName  PDA设置的广播名称
     * @param list  设置的对象数组，对象格式为{key:"",value:""}
     */
    @ReactMethod
    public void setBroadcastSetting(String broadcastSettingName, ReadableArray list) {
        // PDA设备的设置
        if (list.size()>0){
            for(int i=0;i<list.size();i++){
                setBroadcastReceiver(broadcastSettingName, list.getMap(i));
            }
        }
    }

    @ReactMethod
    public void getCode(String broadcastName,String setCodeName){
        BROADCASTNAME=broadcastName;
        CODENAME=setCodeName;
        Log.e("list","list ............................. adadasdas");

        try{
            getReactApplicationContext().unregisterReceiver(scanReceiver);
        }catch (IllegalArgumentException e){
        }
        initBroadcastReciever();
    }

    private BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCASTNAME)){
                String code=intent.getStringExtra(CODENAME);
                Matcher matcher = p.matcher(code);
                codeStr = matcher.replaceAll("");
                WritableMap params= Arguments.createMap();
                params.putString("code", codeStr);
                sendEvent(getReactApplicationContext(),"scannerCodeShow",params);
            }
        }
    };

    /**
     * 发送事件到JS
     */
    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params){
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName,params);
    }

    // 设置PDA工具项
    private void setBroadcastReceiver(String broadcastSettingName, ReadableMap map) {
        try{
            // 发送广播到扫描工具内的应用设置项
            Intent intent = new Intent(broadcastSettingName);
            // 修改扫描工具内应用设置
            intent.putExtra(map.getString("key"), map.getString("value"));
            getReactApplicationContext().sendBroadcast(intent);
        }catch (Exception e){
            Log.e("error",e.toString());
        }
    }

    //初始化广播
    private void initBroadcastReciever() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BROADCASTNAME);
        intentFilter.setPriority(Integer.MAX_VALUE);
        getReactApplicationContext().registerReceiver(scanReceiver,intentFilter);
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }
}
