package com.example.assignment.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

/**
 * Helper class for loading images using Glide.
 */
public class ImageLoader {

    /**
     * Loads an image from a given URI into an ImageView using Glide.
     * @param context The context of the caller.
     * @param uri The URI of the image to load.
     * @param imageView The ImageView to load the image into.
     */
    public static void loadImage(Context context, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .into(imageView);
    }

    public static void loadImage(Context context, String uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .into(imageView);
    }
}
