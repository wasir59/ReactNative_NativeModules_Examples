package com.poc_nativebridge.mergevideo.model;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.poc_nativebridge.utils.ConstantFunction;
import com.poc_nativebridge.utils.Constants;
import com.poc_nativebridge.utils.CustomException;

import java.io.File;
import java.util.ArrayList;

import static com.poc_nativebridge.utils.ConstantFunction.getPath;

/**
 * Created by Mr. Wasir on 15,September,2018
 */
public class VideoMergeRepoImpl implements VideoMergeRepo {
    private static final String TAG = VideoMergeRepoImpl.class.getSimpleName().toString();
    private FFmpeg ffmpeg;
    private String outputPath;

    @Override
    public void getVideoPath(final VideoPathCallBack videoPathCallBack, final int requestCode, int resultCode, final Intent data,Context context) {
        loadBinary(context);
        ArrayList<String> list = getSelectedVideos(context,requestCode, data);

        if (list == null) {
            videoPathCallBack.failed(new CustomException("Please select  videos"));
        } else if (list.size() < 2) {
            videoPathCallBack.failed(new CustomException("Please select at least 2 videos"));
        } else if (list.size() > 15) {
            videoPathCallBack.failed(new CustomException("Please select at most 15 videos"));
        } else {
            File moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            String filePrefix = "merged_video";
            String fileExtn = ".mp4";
            File dest = new File(moviesDir, filePrefix + fileExtn);
            outputPath = dest.getAbsolutePath();

            String[] cmd = ConstantFunction.CmdProvider.getVideoMergeCommand(list, outputPath);
            execFFmpegBinary(videoPathCallBack, cmd);
        }

    }

    private void execFFmpegBinary(final VideoPathCallBack videoPathCallBack, final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Constants.errorLog(TAG, "FAILED with output : " + s);
                    videoPathCallBack.failed(new CustomException(s));
                }

                @Override
                public void onSuccess(String s) {
                    Constants.debugLog(TAG, "Success: " + s);
                    videoPathCallBack.success(outputPath);

                }

                @Override
                public void onProgress(String s) {
                    Constants.debugLog(TAG, "onProgress " + s);
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
        }
    }

    @SuppressLint("NewApi")
    private ArrayList<String> getSelectedVideos(Context mContext,int requestCode, Intent data) {

        ArrayList<String> result = new ArrayList<>();

        ClipData clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item videoItem = clipData.getItemAt(i);
                Uri videoURI = videoItem.getUri();
                String filePath = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    filePath = getPath(mContext, videoURI);
                }
                result.add(filePath);
            }
        } else {
            Uri videoURI = data.getData();
            String filePath = getPath(mContext, videoURI);
            result.add(filePath);
        }
        return result;
    }

    public void loadBinary(Context context) {
        try {
            if (ffmpeg == null) {
                ffmpeg = FFmpeg.getInstance(context);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
//                    binaryLoadCallBacks.binaryLoadFailed();
                }

                @Override
                public void onSuccess() {
                    Constants.debugLog(TAG, "Binary Loaded Successfully");
                }
            });
        } catch (FFmpegNotSupportedException e) {
        } catch (Exception e) {
            Constants.debugLog(TAG, "EXception no loadBinary : " + e.toString());
        }
    }

}
