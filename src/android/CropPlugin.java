package com.jeduan.crop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.soundcloud.android.crop.Crop;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class CropPlugin extends CordovaPlugin {
    private CallbackContext callbackContext;
    private int quality;
    private Uri inputUri;
    private Uri outputUri;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
      if (action.equals("cropImage")) {
          this.callbackContext = callbackContext;
          cordova.setActivityResultCallback(this);
          String imagePath = args.getString(0);
          JSONObject params = args.getJSONObject(1);

          if (params.has("quality")) {
              this.quality = params.getInt("quality");
          } else {
              this.quality = 100;
          }
          imagePath = stripFileProtocol(imagePath);

          this.inputUri = Uri.fromFile(new File(imagePath));
          this.outputUri = Uri.fromFile(new File(getTempDirectoryPath() + "/cropped.jpg"));

          PluginResult pr = new PluginResult(PluginResult.Status.NO_RESULT);
          pr.setKeepCallback(true);
          callbackContext.sendPluginResult(pr);

          Crop.of(this.inputUri, this.outputUri).asSquare().start(cordova.getActivity());
          return true;
      }
      return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == Crop.REQUEST_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = Crop.getOutput(intent);
                this.callbackContext.success(imageUri.getPath());
            } else if (resultCode == Crop.RESULT_ERROR) {
                this.callbackContext.error(resultCode);
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private String getTempDirectoryPath() {
        File cache = null;

        // SD Card Mounted
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cache = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/Android/data/" + cordova.getActivity().getPackageName() + "/cache/");
        }
        // Use internal storage
        else {
            cache = cordova.getActivity().getCacheDir();
        }

        // Create the cache directory if it doesn't exist
        cache.mkdirs();
        return cache.getAbsolutePath();
    }

    private String stripFileProtocol(String uriString) {
        if (uriString.startsWith("file://")) {
            uriString = uriString.substring(7);
        }
        return uriString;
    }
}