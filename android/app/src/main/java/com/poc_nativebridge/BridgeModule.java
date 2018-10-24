package com.poc_nativebridge;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.poc_nativebridge.mergevideo.model.VideoMergeRepoImpl;
import com.poc_nativebridge.mergevideo.presenter.MergeVideoCallBacks;
import com.poc_nativebridge.mergevideo.presenter.MergeVideoPresenter;
import com.poc_nativebridge.utils.Constants;
import com.poc_nativebridge.utils.CustomException;

import static com.poc_nativebridge.utils.Constants.REQUEST_CODE_SELECT_VIDEO_FILE;
import static com.poc_nativebridge.utils.Constants.REQUEST_CODE_SELECT_VIDEO_FILE_AFTER_KK;


public class BridgeModule extends ReactContextBaseJavaModule implements MergeVideoCallBacks.UpdateView {
    private static final String TAG = BridgeModule.class.getSimpleName();
    private Context mContext;
    private MergeVideoPresenter mergeVideoPresenter;
    private Activity mActivity;
    public BridgeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext.getApplicationContext();
        mActivity = getCurrentActivity();
        mergeVideoPresenter = new MergeVideoPresenter(new VideoMergeRepoImpl(), this);
    }

    @Override
    public String getName() {
        return "BridgeSample";
    }


    private void selectVideoFile() {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType(mActivity.getString(R.string.v_type));
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            mActivity.startActivityForResult(Intent.createChooser(intent, mActivity.getString(R.string.select_videos)), REQUEST_CODE_SELECT_VIDEO_FILE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType(mActivity.getString(R.string.v_type));
            mActivity.startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO_FILE_AFTER_KK);
        }
    }

    private void checkPermission() {
        if (Constants.check_WRITE_EXTERNAL_STORAGE_Permission(mActivity)) {
            selectVideoFile();
            Constants.debugLog(TAG,"checkPermission NO");
        } else {
            ActivityCompat.requestPermissions(mActivity, Constants.PERMISSIONS_REQ, Constants.requestCode_READ_WRITE_EXTERNAL_STORAGE);
            Constants.debugLog(TAG,"checkPermission YES");
        }
    }
    @ReactMethod
    public void showNotification(String message) {
        Toast.makeText(getReactApplicationContext(), message, Toast.LENGTH_LONG).show();
        sendNotification(message);


        if (mActivity != null) {
            checkPermission();
        }
    }


    private void sendNotification(String messageBody) {
        Intent intent = new Intent(mContext, Main2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = mContext.getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(mContext, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(mContext.getString(R.string.fcm_message))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    @Override
    public void mergeSuccess(String finalPath) {

    }

    @Override
    public void mergeFailed(CustomException e) {

    }

    @Override
    public void showProgress(boolean isActive) {

    }
}
