package com.hunt.huntsound;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

//Created by Developer on 25.06.17.

public class SupportClass {

    public static int LEFT_DINAMIC = 1023;
    public static int RIGHT_DINAMIC = 1025;
    public static int MONO_DINAMIC = 1027;

    public static void ToastMessage(Context context, String value){
        Toast toast = Toast.makeText(context, value, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static String checkStringToNullAndTrim(String value){
        if(value != null){
            return value.trim();
        }else{
            return "";
        }
    }

    public static DialogFragmentInfo DialogMessageInfo(FragmentManager frg, String value) {
        DialogFragmentInfo dialog = new DialogFragmentInfo();
        dialog.show(frg, value);
        return dialog;
    }

    public static DisplayImageOptions displayImageOptions(){
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(800))
                .build();
    }
}