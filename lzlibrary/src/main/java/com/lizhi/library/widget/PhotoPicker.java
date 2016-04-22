package com.lizhi.library.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.external.volley.http.LoadControler;
import com.external.volley.http.RequestManager;
import com.external.volley.http.RequestMap;
import com.lizhi.library.R;
import com.lizhi.library.utils.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jacob on 15/5/8.
 */
public class PhotoPicker extends AlertDialog.Builder {

    Context mContext;
    public AlertDialog dialog;
    public static final int IMAGE_CHOOSE_CODE = 10;
    public static final int CAMERA_REQUEST_CODE = 11;
    public static final int RESULT_REQUEST_CODE = 12;
    public static final String IMAGE_FILE_NAME = "pic.jpg"; // 拍照默认图片名称
    public static final String IMAGE_AVATAR = "avatar.jpg";// 裁剪之后头像默认名称
    private static final String UPLOAD_URL = "http://www.splashpadmobile.com/upload.php";
    private UploadSuccessListener uploadSuccessListener;

    public PhotoPicker(Context context) {
        super(context);
        this.mContext = context;
        RequestManager.getInstance().init(mContext.getApplicationContext());
        init();
    }

    private void init() {
        setTitle("更换头像")
                .setItems(R.array.photo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                takePicture(dialog);
                                break;

                            case 1:
                                loadFromAlbum(dialog);
                                break;
                        }

                    }
                });
        //builder.create().show();
        dialog = create();

        if (!((Activity) mContext).isFinishing()) {
            dialog.show();
            dialogTitleColor(dialog);
            dialogTitleLineColor(dialog);
            dialogTitleBgColor(dialog);
            dialogContentBgColor(dialog);
        }
    }


    private void takePicture(DialogInterface dialog) {
        Intent intentFromCapture = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = new File(path, IMAGE_FILE_NAME);
        intentFromCapture.putExtra(
                MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(file));
        ((Activity) mContext).startActivityForResult(intentFromCapture,
                CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    private void loadFromAlbum(DialogInterface dialog) {
        if (Environment.getExternalStorageState()
                .equals("mounted")) {
            Intent intentFromGallery = new Intent();
            intentFromGallery.setType("image/*");
            intentFromGallery
                    .setAction(Intent.ACTION_GET_CONTENT);
            ((Activity) mContext).startActivityForResult(intentFromGallery,
                    IMAGE_CHOOSE_CODE);
            dialog.dismiss();
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        ((Activity) mContext).startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    public File getImage(Intent data) {
        File tempFile = null;
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            tempFile = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                            + "/" + IMAGE_AVATAR);

            BufferedOutputStream bos;
            try {
                tempFile.createNewFile();
                bos = new BufferedOutputStream(new FileOutputStream(tempFile));
                photo.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                bos.flush();
                bos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            String filePath = null;
            Uri _uri = data.getData();
            Log.d("", "URI = " + _uri);
            if (_uri != null && "content".equals(_uri.getScheme())) {
                Cursor cursor = mContext.getContentResolver().query(_uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                cursor.moveToFirst();
                filePath = cursor.getString(0);
                cursor.close();
            } else {
                filePath = _uri.getPath();
            }

            tempFile = new File(filePath);
        }

        dialog.dismiss();

        return tempFile;

    }

    public void uploadAvatarToServer(File file, String url,RequestMap params) {

        LoadControler mLoadControler = RequestManager.getInstance().post(url, params, new RequestManager.RequestListener() {
            @Override
            public void onRequest() {

            }

            @Override
            public void onSuccess(String response, String url, int actionId) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String imageURL = jsonObject.optString("data");

                    if(uploadSuccessListener != null)
                    {
                        uploadSuccessListener.onUploadSuccess(imageURL);
                    }

//                    MMPreference mmPreference = MMPreference.getInstance(mContext);
//                    USER user = mmPreference.getUser();
//                    user.avatar = imageURL;
//                    mmPreference.setUser(user);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {

            }
        }, 2);
    }

    private void dialogTitleColor(Dialog dialog) {
        int textViewId = mContext.getResources().getIdentifier("android:id/alertTitle", null, null);
        TextView tv = (TextView) dialog.findViewById(textViewId);
        tv.setTextColor(mContext.getResources().getColor(R.color.lz_theme_color));
    }

    private void dialogTitleBgColor(Dialog dialog) {
        int textViewId = mContext.getResources().getIdentifier("android:id/topPanel", null, null);
        LinearLayout tv = (LinearLayout) dialog.findViewById(textViewId);
        tv.setBackgroundColor(mContext.getResources().getColor(R.color.white));
    }

    private void dialogContentBgColor(Dialog dialog) {
        int textViewId = mContext.getResources().getIdentifier("android:id/contentPanel", null, null);
        LinearLayout tv = (LinearLayout) dialog.findViewById(textViewId);
        tv.setBackgroundColor(mContext.getResources().getColor(R.color.white));
    }

    private void dialogTitleLineColor(Dialog dialog) {
        if (dialog != null) {
            String dividers[] = {
                    "android:id/topPanel", "android:id/titleDividerTop", "android:id/titleDivider"
            };

            for (int i = 0; i < dividers.length; ++i) {
                int divierId = mContext.getResources().getIdentifier(dividers[i], null, null);
                View divider = dialog.findViewById(divierId);
                if (divider != null) {
                    divider.setBackgroundColor(mContext.getResources().getColor(R.color.lz_theme_color));
                }
            }
        }
    }

    public void setUploadSuccessListener(UploadSuccessListener uploadSuccessListener) {
        this.uploadSuccessListener = uploadSuccessListener;
    }

    public interface UploadSuccessListener
    {
        public void onUploadSuccess(String picURL);
    }
}
