package com.poc_nativebridge.mergevideo.model;

import android.content.Context;
import android.content.Intent;

import com.poc_nativebridge.utils.CustomException;


/**
 * Created by Mr. Wasir on 15,September,2018
 */
public interface VideoMergeRepo {

    interface VideoPathCallBack {
        void success(String finalVideoPath);

        void failed(CustomException e);
    }

    void getVideoPath(VideoPathCallBack videoPathCallBack, int requestCode, int resultCode, Intent data, Context context);

}
