package com.poc_nativebridge.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mr. Wasir on 16,September,2018
 */
public class ConstantFunction {
    private static final String TAG = ConstantFunction.class.getSimpleName().toString();

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDuration(int mills) {
        int seconds = (int) (mills / 1000) % 60;
        int minutes = (int) ((mills / (1000 * 60)) % 60);
        int hours = (int) ((mills / (1000 * 60 * 60)) % 24);
        return hours + ":" + minutes + ":" + seconds;
    }


    public static final class CmdProvider {


        public static String[] getAddTextCommand(String textToAdd, String inputPath, String outPath, File mFileFont) {
            String[] addTextcomplexCommand = {"-y", "-i", inputPath, "-vf", "drawtext=fontfile='" + mFileFont.getAbsolutePath() + "':" +
                    "text='" + textToAdd + "':" + "fontcolor=white: fontsize=50: box=1: boxcolor=black@0.05: x=(w-text_w)/2: y=(h-text_h)/2", "-codec:a", "copy", outPath};

            Constants.debugLog(TAG, Arrays.toString(addTextcomplexCommand));
            return addTextcomplexCommand;
        }

        public static String[] getImageCmd(String inputPath, String ss, String outPath) {
//            [-y, -i, /storage/emulated/0/parse/v2.mp4, -an, -r, 1, -ss, 0, -t, 13, /storage/emulated/0/Pictures/VideoEditor/extract_picture%03d.jpg]
//            ffmpeg -i input_file.mp4 -ss 01:23:45 -vframes 1 output.jpg
            String[] cmd = {"-y", "-i", inputPath, "-ss", ss, "-vframes", "1", outPath};

            Constants.debugLog(TAG, "getImageCmd " + Arrays.toString(cmd));
            return cmd;
        }

        public static String[] getVideoMergeCommand(ArrayList<String> files, String outPath) {


            ArrayList<String> cmdList = new ArrayList<>();
            cmdList.add("-y");
            addFiles(cmdList, files);
            addScale(cmdList, files);
            cmdList.add(outPath);
            Object[] arr = cmdList.toArray();
            String[] cmd = Arrays.copyOf(arr, arr.length, String[].class);

         /*   String complexCommand[] = {"-y", "-i", "", "-i","", "-i","",
                    "-filter_complex", "[0:v]scale=480x640,setsar=1[v0];[1:v]scale=480x640,setsar=1[v1];[2:v]scale=480x640,setsar=1[v2];[v0][0:a][v1][1:a][v2][2:a]concat=n=3:v=1:a=1",
                    "-ab", "48000", "-ac", "2", "-ar", "22050", "-s", "480x640", "-vcodec", "libx264", "-crf", "27", "-preset", "ultrafast", outPath};
*/
            Constants.debugLog(TAG, "" + cmd.length);
            return cmd;
        }

        public static void addFiles(ArrayList<String> cmd, ArrayList<String> files) {
            for (String file : files) {
                cmd.add("-i");
                cmd.add(file);
            }
            cmd.add("-filter_complex");
        }

        public static void addScale(ArrayList<String> cmd, ArrayList<String> files) {
            String vPath = "", scalePart = "", codecPart = "";
            int i = 0;
            for (String path : files) {

                if (files.size() - 1 == i) {
                    vPath = vPath + path + ",-filter complex";
                    scalePart = scalePart + "[" + i + ":v]scale=240x320,setsar=1[v" + i + "];";
                    codecPart = codecPart + "[v" + i + "][" + i + ":a]concat=n=" + files.size() + ":v=1:a=1";
                } else {
                    vPath = vPath + path + ",-i,";
                    scalePart = scalePart + "[" + i + ":v" + "]scale=240x320,setsar=1[v" + i + "];";
                    codecPart = codecPart + "[v" + i + "][" + i + ":a]";
                }
                i++;
            }
            cmd.add(scalePart + codecPart);

            cmd.add("-ab");
            cmd.add("48000");
            cmd.add("-ac");
            cmd.add("2");

            cmd.add("-ar");
            cmd.add("22050");
            cmd.add("-s");
            cmd.add("240x320");
            cmd.add("-vcodec");


            cmd.add("libx264");
            cmd.add("-crf");
            cmd.add("27");
            cmd.add("-preset");
            cmd.add("ultrafast");

        }
    }
}
