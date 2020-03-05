package com.example.sofra.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sofra.R;


public class DialogUtils {

   public static void showDialog(ConstraintLayout constraintLayout , TextView textView , ProgressBar progressBar) {
       progressBar.setVisibility(View.GONE);
       constraintLayout.setVisibility(View.VISIBLE);
       textView.setVisibility(View.GONE);
   }

    public static void showText(ConstraintLayout constraintLayout , TextView textView , ProgressBar progressBar) {
        constraintLayout.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

    }

    public static void showProgressBar(ConstraintLayout constraintLayout , @Nullable TextView textView , ProgressBar progressBar) {

       if (textView!=null)
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        constraintLayout.setVisibility(View.GONE);

    }


    }

