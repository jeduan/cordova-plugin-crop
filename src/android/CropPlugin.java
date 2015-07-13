package com.jeduan.crop;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

public class CropPlugin extends CordovaPlugin {
    @Override
    protected void pluginInitialize() {

    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
      if (action.equals("cropImage")) {
        return true;
      }
      return false;
    }
}