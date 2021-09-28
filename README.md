# react-native-idata-scanner

基于 [iData 95w 系列](http://www.cxds.com.cn/CxIm_ArticleView.asp?id=149) 安卓手持 PDA 终端设备的 React Native 实现

![iData 95w Series](http://www.cxds.com.cn/Integralmall/images/article/image/20160226/20160226150280978097.jpg)

## Getting started

`$ npm install react-native-idata-scanner --save`

### Mostly automatic installation

`$ react-native link react-native-idata-scanner`

## Usage

```javascript
import IdataScanner from 'react-native-idata-scanner';
import {NativeEventEmitter} from 'react-native';

...

// 设置扫码选项(当PDA设备能通过广播进行设置的时候可用)
const rows = [{key: 'barcode_send_mode', value: 'BROADCAST'}];
IdataScanner.setBroadcastSetting('com.android.scanner.service_settings', rows);

// 广播和接收字段(接收的广播名和接收的字段名)
IdataScanner.getCode('android.intent.action.SCANRESULT', 'value');

const eventEmitter = new NativeEventEmitter();
const listener = eventEmitter.addListener('scannerCodeShow', ({code}) => {
    console.log(`->code:${code}`)
});
```
