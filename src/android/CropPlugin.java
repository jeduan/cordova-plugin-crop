package com.jeduan.crop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    private Uri inputUri;
    private Uri outputUri;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("cropImage")) {
            String imagePath = args.getString(0);

            this.inputUri = Uri.parse(imagePath);
            this.outputUri = Uri.fromFile(new File(getTempDirectoryPath() + "/" + System.currentTimeMillis() + "-cropped.jpg"));

            PluginResult pr = new PluginResult(PluginResult.Status.NO_RESULT);
            pr.setKeepCallback(true);
            callbackContext.sendPluginResult(pr);
            this.callbackContext = callbackContext;

            cordova.setActivityResultCallback(this);
            startCropActivity(args);
            return true;
        }
        return false;
    }

    private void startCropActivity(JSONArray args) {
        int[] aspectRatio = readAspect(args);
        if (aspectRatio != null && aspectRatio.length == 2) {
            Crop.of(this.inputUri, this.outputUri)
                    .withAspect(aspectRatio[0], aspectRatio[1])
                    .start(cordova.getActivity());
        } else {
            Crop.of(this.inputUri, this.outputUri)
                    .start(cordova.getActivity());
        }
    }

    private int[] readAspect(JSONArray args) {
        try {
            if (args.length() > 1) {
                JSONArray jsonArray = args.getJSONArray(1);
                return new int[]{jsonArray.getInt(0), jsonArray.getInt(1)};
            } else {
                return null;
            }
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == Crop.REQUEST_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = Crop.getOutput(intent);
                this.callbackContext.success("file://" + imageUri.getPath() + "?" + System.currentTimeMillis());
                this.callbackContext = null;
            } else if (resultCode == Crop.RESULT_ERROR) {
                try {
                    JSONObject err = new JSONObject();
                    err.put("message", "Error on cropping");
                    err.put("code", String.valueOf(resultCode));
                    this.callbackContext.error(err);
                    this.callbackContext = null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                try {
                    JSONObject err = new JSONObject();
                    err.put("message", "User cancelled");
                    err.put("code", "userCancelled");
                    this.callbackContext.error(err);
                    this.callbackContext = null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    public Bundle onSaveInstanceState() {
        Bundle state = new Bundle();

        if (this.inputUri != null) {
            state.putString("inputUri", this.inputUri.toString());
        }

        if (this.outputUri != null) {
            state.putString("outputUri", this.outputUri.toString());
        }

        return state;
    }

    public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {

        if (state.containsKey("inputUri")) {
            this.inputUri = Uri.parse(state.getString("inputUri"));
        }

        if (state.containsKey("outputUri")) {
            this.inputUri = Uri.parse(state.getString("outputUri"));
        }

        this.callbackContext = callbackContext;
    }
}
