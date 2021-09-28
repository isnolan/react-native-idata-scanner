interface broadcastSettingItem {
  key: string;
  value: string;
}

interface IdataScanner {
  setBroadcastSetting?: (broadcastSettingName: string, broadcastSettingList: Array<broadcastSettingItem>) => void;
  getCode?: (broadcastName: string, setCodeName: string) => void;
}

export default IdataScanner;
