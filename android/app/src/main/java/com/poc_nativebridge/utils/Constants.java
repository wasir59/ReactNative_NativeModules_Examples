package com.poc_nativebridge.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.poc_nativebridge.BuildConfig;

import java.net.InetAddress;

/**
 * Created by Mr. Wasir on 14,September,2018
 */
public class Constants {
    public static final int requestCode_READ_WRITE_EXTERNAL_STORAGE = 100;
    public static final int REQUEST_CODE_SELECT_VIDEO_FILE = 101;
    public static final int REQUEST_CODE_SELECT_VIDEO_FILE_AFTER_KK = 102;
    public static final int CAMERA_REQUEST = 103;
    public static final int CAMERA_PERMISSION_REQUEST = 104;
    public static final int SELECT_GALLERY_IMAGE_REQUEST = 105;
    public static final int REQUEST_TAKE_GALLERY_VIDEO = 106;
    public static final int REQUEST_TAKE_GALLERY_VIDEO_FOR_IMAGE_CAP = 107;

    public static final int PACKET_SIZE = 1200;
    public static final int SOCKET_TIMEOUT = 1000;
    public static final int RECEIVED_PACKET_SIZE = 1200;


    public final class PickerType {
        public static final int REQUEST_FOR_CAMERA = 1;
        public static final int REQUEST_FOR_STORAGE = 2;
    }

    public static final String[] PERMISSIONS_REQ = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void debugLog(String logTag, String s) {
        if (BuildConfig.Debug) {
            Log.d(logTag, logTag + " ->" + s);
        }
    }

    public static void infoLog(String logTag, String s) {
        if (BuildConfig.Debug) {
            Log.i(logTag, s);
        }
    }

    public static void infoLog(String s) {
        if (BuildConfig.Debug) {
            Log.i("ringidbd", s);
        }
    }


    public static void errorLog(String logTag, String s) {
        if (BuildConfig.Debug) {
            Log.e(logTag, logTag + " ->" + s);
        }
    }


    public static boolean check_WRITE_EXTERNAL_STORAGE_Permission(Activity activity) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            return true;
        }
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public static class ServerInfo {

        public static InetAddress SERVER_IP;
        public static int SERVER_PORT = 1500;
    }

    public static final class CALL_KEY {
        public static final String PACKET_TYPE = "pkType";
        public static final String USER_ID = "userID";
        public static final String RECEIVER_ID = "receiverID";
    }

    public static final class CALL_STATE {

        public final static byte VOICE_MEDIA = 0;
        public final static byte VOICE_REGISTER = 1;
        public final static byte VOICE_UNREGISTERED = 2;
        public static final byte VOICE_REGISTER_CONFIRMATION = 3;
        public final static byte KEEPALIVE = 4;
        public final static byte CALLING = 5;
        public final static byte RINGING = 6;
        public final static byte IN_CALL = 7;
        public final static byte ANSWER = 8;
        public final static byte BUSY = 9;
        public final static byte CANCELED = 10;
        public final static byte CONNECTED = 11;
        public final static byte DISCONNECTED = 12;
        public final static byte BYE = 13;
        public final static byte NO_ANSWER = 15;

    }
}
