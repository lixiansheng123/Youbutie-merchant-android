package com.yuedong.youbutie_merchant_android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


import com.yuedong.youbutie_merchant_android.app.Config;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Administrator on 2015/12/11.
 */
public class CropHelper {

    public static final String TAG = "CropHelper";

    public static final int REQUEST_CROP = 127;
    public static final int REQUEST_CAMERA = 128;

    public static String getCropCacheFileName() {
        return "cropCache_" + System.currentTimeMillis() + ".jpg";
    }

    public static Uri buildUri() {
        File dir = new File(Config.DIR_CROP_PIC);
        if(!dir.exists())dir.mkdirs();
        return Uri.fromFile(dir).buildUpon().appendPath(getCropCacheFileName()).build();
    }

    public static void handleResult(CropHandler handler, int requestCode, int resultCode, Intent data) {
        if (handler == null)
            return;

        if (resultCode == Activity.RESULT_CANCELED) {
            handler.onCropCancel();
        } else if (resultCode == Activity.RESULT_OK) {
            CropParams cropParams = handler.getCropParams();
            if (cropParams == null) {
                handler.onCropFailed("CropHandler's params MUST NOT be null!");
                return;
            }
            switch (requestCode) {
                case REQUEST_CROP:
                    if (isPhotoReallyCropped(handler.getCropParams().uri)) {
                        Log.d(TAG, "Photo cropped!");
                        handler.onPhotoCropped(handler.getCropParams().uri);
                        break;
                    } else {
                        Activity context = handler.getContext();
                        if (context != null) {
                            String path = CropFileUtils.getSmartFilePath(context, data.getData());
                            boolean result = CropFileUtils.copyFile(path, handler.getCropParams().uri.getPath());
                            if (!result) {
                                handler.onCropFailed("Unknown error occurred!");
                                break;
                            }
                        } else {
                            handler.onCropFailed("CropHandler's context MUST NOT be null!");
                        }
                    }
                case REQUEST_CAMERA:
                    Intent intent = buildCropFromUriIntent(handler.getCropParams());
                    Activity context = handler.getContext();
                    if (context != null) {
                        context.startActivityForResult(intent, REQUEST_CROP);
                    } else {
                        handler.onCropFailed("CropHandler's context MUST NOT be null!");
                    }
                    break;
            }
        }
    }

    public static boolean isPhotoReallyCropped(Uri uri) {
        File file = new File(uri.getPath());
        long length = file.length();
        return length > 0;
    }

    public static boolean clearCachedCropFile(Uri uri) {
        if (uri == null)
            return false;

        File file = new File(uri.getPath());
        if (file.exists()) {
            boolean result = file.delete();
            if (result)
                Log.i(TAG, "Cached crop file cleared.");
            else
                Log.e(TAG, "Failed to clear cached crop file.");
            return result;
        } else {
            Log.w(TAG, "Trying to clear cached crop file but it does not exist.");
        }
        return false;
    }

    public static Intent buildCropFromUriIntent(CropParams params) {
        return buildCropIntent("com.android.camera.action.CROP", params);
    }

    public static Intent buildCropFromGalleryIntent(CropParams params) {
        return buildCropIntent(Intent.ACTION_PICK, params);
    }

    public static Intent buildCaptureIntent(Uri uri) {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, uri);
    }

    public static Intent buildCropIntent(String action, CropParams params) {
        return new Intent(action, null).setDataAndType(params.uri, params.type)
                // .setType(params.type)
                .putExtra("crop", params.crop).putExtra("scale", params.scale).putExtra("aspectX", params.aspectX)
                .putExtra("aspectY", params.aspectY).putExtra("outputX", params.outputX)
                .putExtra("outputY", params.outputY).putExtra("return-data", params.returnData)
                .putExtra("outputFormat", params.outputFormat).putExtra("noFaceDetection", params.noFaceDetection)
                .putExtra("scaleUpIfNeeded", params.scaleUpIfNeeded).putExtra(MediaStore.EXTRA_OUTPUT, params.uri);
    }

    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        if (context == null || uri == null)
            return null;

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
