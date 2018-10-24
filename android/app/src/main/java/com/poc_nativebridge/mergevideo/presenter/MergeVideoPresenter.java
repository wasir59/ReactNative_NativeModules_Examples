package com.poc_nativebridge.mergevideo.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;


import com.poc_nativebridge.mergevideo.model.VideoMergeRepo;
import com.poc_nativebridge.utils.CustomException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Mr. Wasir on 15,September,2018
 */
public class MergeVideoPresenter implements MergeVideoCallBacks.UserAction {

    private MergeVideoCallBacks.UpdateView updateView;
    private VideoMergeRepo videoMergeRepo;

    public MergeVideoPresenter(@NonNull VideoMergeRepo videoMergeRepo, @NonNull MergeVideoCallBacks.UpdateView updateView) {
        this.updateView = checkNotNull(updateView);
        this.videoMergeRepo = checkNotNull(videoMergeRepo);
    }

    @Override
    public void mergeProcessOne(int requestCode, int resultCode, Intent data, Context context) {
        updateView.showProgress(true);

        videoMergeRepo.getVideoPath(new VideoMergeRepo.VideoPathCallBack() {
            @Override
            public void success(String finalVideoPath) {

            }

            @Override
            public void failed(CustomException e) {

            }
        }, requestCode, resultCode, data,context);
//        videoMergeRepo.getVideoPath(new VideoMergeRepo().VideoPathCallBack() {
//            @Override
//            public void success(String finalPath) {
//                updateView.mergeSuccess(finalPath);
//                updateView.showProgress(false);
//            }
//
//            @Override
//            public void failed(CustomException e) {
//                updateView.showProgress(false);
//                updateView.mergeFailed(e);
//
//            }
//        }, requestCode, resultCode, data);
    }
}
