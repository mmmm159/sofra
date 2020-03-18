package com.example.sofra.utils;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.local.SharedPreference;
import com.example.sofra.view.activity.HomeActivity;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Utils {


    public static void replaceFragment(FragmentManager manager, int res, Fragment fragment) {

        manager.beginTransaction().replace(res, fragment).commit();
    }


    public static void showProgressBar(@Nullable View viewToHide1, @Nullable View viewToHide2, ProgressBar progressBar) {

        if (viewToHide2 != null)
            viewToHide2.setVisibility(View.GONE);

        if (viewToHide1 != null)
            viewToHide1.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);

    }

    public static MultipartBody.Part convertFileToMultipart(String pathImageFile, String Key) {
        if (pathImageFile != null) {
            File file = new File(pathImageFile);
            RequestBody reqFileSelect = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part Imagebody = MultipartBody.Part.createFormData(Key, file.getName(), reqFileSelect);
            return Imagebody;
        } else {
            return null;
        }
    }

    public static void onLoadImageFromUrl(ImageView imageView, String URL, Context context) {
        Glide.with(context)
                .load(URL)
                .into(imageView);
    }

    public static boolean isUserRestaurant(Activity activity) {

        int userType = SharedPreference.loadInt(activity, SharedPreference.USER_TYPE_KEY);
        return userType == SharedPreference.USER_TYPE_SELL;
    }

    public static void selectImage(Context activity,
                                   Action<ArrayList<AlbumFile>> action) {


        Album.initialize(AlbumConfig.newBuilder(activity)
                .setAlbumLoader(new MediaLoader())
                .build());
        Album.image(activity) // Image selection.
                .singleChoice()
                .camera(true)
                .columnCount(3)
                .onResult(action)
                .start();


    }

    public static void showIfRecylerViewIsEmpty(View viewToShow, View viewToGo, ProgressBar progressBar) {
        viewToShow.setVisibility(View.VISIBLE);
        viewToGo.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }






    public static RequestBody convertToRequestBody(String part) {
        try {
            if (!part.equals("")) {
                return RequestBody.create(MediaType.parse("multipart/form-data"), part);

            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    public static void customToast(Activity activity, String ToastTitle, boolean failed) {

        LayoutInflater inflater = activity.getLayoutInflater();
        int imgIconId;

        if (failed) {
            imgIconId = R.drawable.ic_wrong ;
        } else {
            imgIconId = R.drawable.ic_check_circle_green_24dp;
        }

        View layout = inflater.inflate(R.layout.toast, activity.findViewById(R.id.toast_container_root));

        TextView text = layout.findViewById(R.id.toast_tv);
        ImageView imgIcon = layout.findViewById(R.id.toast_iv);

        imgIcon.setImageResource(imgIconId);
        text.setText(ToastTitle);

        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


}
