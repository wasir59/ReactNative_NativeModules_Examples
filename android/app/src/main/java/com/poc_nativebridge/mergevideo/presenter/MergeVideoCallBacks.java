package com.poc_nativebridge.mergevideo.presenter;

import android.content.Context;
import android.content.Intent;

import com.poc_nativebridge.utils.CustomException;

/**
 * Created by Mr. Wasir on 15,September,2018
 */
public interface MergeVideoCallBacks {
    interface UpdateView {
        void mergeSuccess(String finalPath);

        void mergeFailed(CustomException e);

        void showProgress(boolean isActive);
    }

    interface UserAction {
        void mergeProcessOne(int requestCode, int resultCode, Intent data, Context context);
    }

}
