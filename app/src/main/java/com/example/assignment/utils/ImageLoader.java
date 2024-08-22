package com.example.assignment.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class ImageLoader {
    public static void loadImage(Context context, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .into(imageView);
    }
}
