package com.zkhaider.bernievshillary.views.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zkhaider.bernievshillary.R;
import com.zkhaider.bernievshillary.views.managers.IFragmentManager;
import com.zkhaider.bernievshillary.widgets.BlurringView;
import com.zkhaider.bernievshillary.widgets.SimpleAnimationListener;
import com.zkhaider.bernievshillary.widgets.TriangleBackgroundView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZkHaider on 6/3/16.
 */

public class ResultsFragment extends Fragment {

    /*********************************************************************************************
     * Constants
     *********************************************************************************************/

    private static final String TAG = ResultsFragment.class.getSimpleName();

    /*********************************************************************************************
     * Views
     *********************************************************************************************/

    /**
     * Layouts
     */
    @Bind(R.id.flResults)                               public FrameLayout flResults;

    /**
     * Custom Views
     */
    @Bind(R.id.tv1)                                     public TriangleBackgroundView triangleViewTop;
    @Bind(R.id.tv2)                                     public TriangleBackgroundView triangleViewBottom;
    @Bind(R.id.bvBlur)                                  public BlurringView bvBlur;

    /**
     * ImageViews
     */
    @Bind(R.id.ivWinner)                                public ImageView ivWinner;
    @Bind(R.id.ivLoser)                                 public ImageView ivLoser;
    @Bind(R.id.ivRays)                                  public ImageView ivRays;

    /**
     * TextViews
     */
    @Bind(R.id.tvHigherScore)                           public TextView tvHigherScore;
    @Bind(R.id.tvLowerScore)                            public TextView tvLowerScore;
    @Bind(R.id.tvWinner)                                public TextView tvWinner;
    @Bind(R.id.tvLoser)                                 public TextView tvLoser;

    /*********************************************************************************************
     * Variables
     *********************************************************************************************/

    /**
     * Communication
     */
    private IFragmentManager mFragmentManager;

    /**
     * Bernie and Hillary Scores
     */
    private float mBernieScore = 0;
    private float mHillaryScore = 0;

    /**
     * Sharing
     */
    private ShareActionProvider mShareActionProvider;

    /*********************************************************************************************
     * Fragment LifeCycle Methods
     *********************************************************************************************/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mFragmentManager = (IFragmentManager) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get our score values
        initializeBundle();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        ButterKnife.bind(this, view);

        initializeBlur();

        // Load in rays to imageview
        Glide.with(getActivity())
                .load(R.drawable.rays)
                .centerCrop()
                .into(ivRays);

        // Animate our rays by rotating them
        animateRays();

        // Go ahead and set our scores
        setScores();

        return view;
    }

    /*********************************************************************************************
     * Initialization Methods
     *********************************************************************************************/

    private void initializeBundle() {

        // Get our arguments
        Bundle bundle = getArguments();
        if (bundle != null) {

            // Get our bernie and hillary scores
            this.mBernieScore = bundle.getFloat("bernieScore");
            this.mHillaryScore = bundle.getFloat("hillaryScore");
        }
    }

    /**
     * Initializes the blurring view with a transparent overlay color.
     */
    public void initializeBlur() {
        bvBlur.setOverlayColor(getResources().getColor(R.color.bg_glass));
        bvBlur.setBlurredRadius(25);
        bvBlur.setBlurredView(flResults);
        bvBlur.setVisibility(View.INVISIBLE);
        bvBlur.setAlpha(1.0f);
    }

    /*********************************************************************************************
     * OnClick Methods
     *********************************************************************************************/

    @OnClick(R.id.flResults)
    public void onClickScreen() {

//        bvBlur.invalidate();
//        bvBlur.setVisibility(View.VISIBLE);

        takeScreenShotAndShareIt();
    }

    /*********************************************************************************************
     * UI Animations Methods
     *********************************************************************************************/

    private void animateRays() {

        ivWinner.post(new Runnable() {
            @Override
            public void run() {

                /**
                 * We need to rotate the rays imageview to achieve a rotation effect
                 */

                PropertyValuesHolder pvRotation = PropertyValuesHolder.ofFloat(View.ROTATION, 0, 360);

                ObjectAnimator rotationAnimator = ObjectAnimator.ofPropertyValuesHolder(ivRays, pvRotation);
                rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
                rotationAnimator.setDuration(4000);
                rotationAnimator.setInterpolator(new LinearInterpolator());
                rotationAnimator.start();
            }
        });
    }

    private void animateInBlur() {

        String fileName = "bernievshillary.png";
        File file = storeScreenShot(getActivity(), takeScreenShot(), fileName);
        share(getActivity(), file, fileName);
    }

    /*********************************************************************************************
     * Accessory Methods
     *********************************************************************************************/

    private void setScores() {

        // Set our scores into the text view
        if (mBernieScore > mHillaryScore) {

            String higherScore = String.valueOf((int) (mBernieScore * 100)) + "%";
            tvHigherScore.setText(higherScore);

            String lowerScore = String.valueOf((int) (mHillaryScore * 100)) + "%";
            tvLowerScore.setText(lowerScore);

            // If bernie won then change nothing in the view
            // TODO : change nothing for the triangle views or images

        } else {

            String higherScore = String.valueOf((int) (mHillaryScore * 100)) + "%";
            tvHigherScore.setText(higherScore);

            String lowerScore = String.valueOf((int) (mBernieScore * 100)) + "%";
            tvLowerScore.setText(lowerScore);

            // Hillary won so go ahead and change the colors of the background triangles

            // We want to change the foreground of the top triangle view to hillary color
            triangleViewTop.setForegroundColor(getResources().getColor(R.color.hillaryColor));
            triangleViewTop.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            // Set the bottom triangle view to foreground transparent and background to bernie
            triangleViewBottom.setForegroundColor(getResources().getColor(android.R.color.transparent));
            triangleViewBottom.setBackgroundColor(getResources().getColor(R.color.bernieColor));

            // We need to flip the imageviews horizontally
            ivWinner.setRotationY(180.0f);
            ivLoser.setRotationY(180.0f);

            // Change textviews
            tvWinner.setText(R.string.hillary_clinton);
            tvLoser.setText(R.string.bernie_sanders);

            // Set bernie image on bottom and hillary image on the top
            Glide.with(getActivity())
                    .load(R.drawable.hillary_big)
                    .into(ivWinner);
            Glide.with(getActivity())
                    .load(R.drawable.bernie_big)
                    .into(ivLoser);
        }
    }

    private void takeScreenShotAndShareIt() {

        // Animate in our blur
        animateInBlur();
    }

    private Bitmap takeScreenShot() {

        View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        View screenView = rootView.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private File storeScreenShot(Context context, Bitmap bitmap, String fileName) {
        // save bitmap to cache directory

        File cachePath = null;
        try {

            cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/" + fileName); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            return cachePath;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cachePath;
    }

    private void share(Context context, File file, String fileName) {

        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, fileName);
        Uri contentUri = FileProvider.getUriForFile(context, "com.zkhaider.bernievshillary.fileprovider", newFile);

        if (contentUri != null) {

            String formatter = "I got %d%% with %s";
            int berniePercent = (int) mBernieScore * 100;
            int hillaryPercent = (int) mHillaryScore * 100;
            String text = (mBernieScore > mHillaryScore) ? String.format(Locale.getDefault(), formatter, berniePercent, "Bernie") :
                    String.format(Locale.getDefault(), formatter, hillaryPercent, "Hillary");


            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getActivity().getContentResolver().getType(contentUri));
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(shareIntent, "Share results"));
        }
    }
}
