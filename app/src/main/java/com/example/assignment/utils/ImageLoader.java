package com.example.assignment.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.InputStream;

public class ImageLoader {

    public static void loadImage(Context context, Uri imageUri, ImageView imageView) {
        if (imageUri != null) {
            Glide.with(context)
                    .load(imageUri)
                    .into(imageView);
        }
    }

    public static Bitmap loadBitmapFromUri(Context context, Uri imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
