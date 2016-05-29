package com.zkhaider.bernievshillary.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by ZkHaider on 10/16/15.
 * Description: Helper class to assist in getting display metrics about a screen size
 */

public class ScreenSizeHelper {

    private static final String TAG = ScreenSizeHelper.class.getSimpleName();

    public enum Size {
        SMALL,
        NORMAL,
        LARGE,
        XLARGE
    }

    /**
     * Get the default display of the device.
     * @param context
     * @return
     */
    public static Display getDisplay(Context context) {
        WindowManager windowManager = getWindowManager(context);
        return windowManager.getDefaultDisplay();
    }

    /**
     * Returns the display width of the phone in pixel units.
     * @param context
     * @return
     */
    public static int getDisplayWidthPixels(Context context) {
        // To getRestClient display width access display metrics, since .getWidth() in display is deprecated
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager(context).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * Returns the display height of the phone in pixel units.
     * @param context
     * @return
     */
    public static int getDisplayHeightPixels(Context context) {
        // To getRestClient display height access display metrics, since .getWidth() in display is deprecated
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager(context).getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.heightPixels;
    }

    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * Converts pixel units to density independent pixel values dependent on the phone's density.
     * Density values are ldpi, mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi.
     * @param pixel
     * @param context
     * @return
     */
    public static int convertPxToDip(int pixel, Context context) {
        float scale = getScreenDensity(context);
        return (int) ((pixel / scale) + 0.5f);
    }

    public static float convertDipToPx(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public static float convertSpToPx(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    /**
     * Get the size name in human readable format.
     *
     * <p>
     *     Values for configuration tell us what the screen diagonal diameter is for the phone.
     *
     *     Small    =   2-3   Inch Phone
     *     Normal   =   3-5   Inch Phone
     *     Large    =   4-7   Inch Phone
     *     XLarge   =   7-10  Inch Phone
     * </p>
     * @param context
     * @return
     */
    public static String getSizeName(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return "small";
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return "normal";
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return "large";
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return "xlarge";
            default:
                return "undefined";
        }
    }

    /**
     * Multiplier for scaling bitmaps based on screen size resolution.
     * @param resources
     * @return
     */
    public static float getImageFactor(Resources resources) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float multiplier = metrics.density / 3f;
        return multiplier;
    }

    /**
     * Get the windowmanager object of the device.
     * @param context
     * @return
     */
    private static WindowManager getWindowManager(Context context) {
        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

}
