package com.zkhaider.bernievshillary.views;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zkhaider.bernievshillary.views.fragments.QuestionsFragment;
import com.zkhaider.bernievshillary.views.fragments.ResultsFragment;
import com.zkhaider.bernievshillary.views.managers.IFragmentManager;
import com.zkhaider.bernievshillary.R;
import com.zkhaider.bernievshillary.utils.ScreenSizeHelper;
import com.zkhaider.bernievshillary.widgets.SimpleAnimationListener;
import com.zkhaider.bernievshillary.widgets.TriangleBackgroundView;
import com.zkhaider.bernievshillary.views.fragments.SplashFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements IFragmentManager {

    /*********************************************************************************************
     * Constants
     *********************************************************************************************/

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String TAG_SPLASH = "tag_splash";
    private static final String TAG_QUESTION = "tag_question";
    private static final String TAG_RESULTS = "tag_results";

    /*********************************************************************************************
     * Views
     *********************************************************************************************/

    /**
     * Layouts
     */
    @Bind(R.id.flMainActivity)                          public FrameLayout flMainActivity;
    @Bind(R.id.flContainer)                             public FrameLayout flContainer;
    @Bind(R.id.llBernie)                                public LinearLayout llBernie;
    @Bind(R.id.llHillary)                               public LinearLayout llHillary;
    @Bind(R.id.llText)                                  public LinearLayout llText;
    @Bind(R.id.llGetStarted)                            public LinearLayout llGetStarted;

    /**
     * Triangle Backgrounds
     */
    @Bind(R.id.tv1)                                     public TriangleBackgroundView tv1;
    @Bind(R.id.tv2)                                     public TriangleBackgroundView tv2;

    /**
     * Bernie or Hillary
     */
    @Bind(R.id.tvBernie)                                public TextView tvBernie;
    @Bind(R.id.ivOr)                                    public ImageView ivOr;
    @Bind(R.id.tvHillary)                               public TextView tvHillary;

    /**
     * ImageViews
     */
    @Bind(R.id.ivBernieBig)                             public ImageView ivBernieBig;
    @Bind(R.id.ivHillaryBig)                            public ImageView ivHillaryBig;

    /*********************************************************************************************
     * Variables
     *********************************************************************************************/

    private boolean mSplashShown = false;
    private float twoThirdHeight;
    private float thirdHeight;

    /*********************************************************************************************
     * Activity Lifecycle Methods
     *********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Load images
        loadImages();

        showSplash();
        mSplashShown = true;
    }

    /*********************************************************************************************
     * Initialization Methods
     *********************************************************************************************/

    private void loadImages() {
        Glide.with(this)
                .load(R.drawable.bernie_big)
                .into(ivBernieBig);
        Glide.with(this)
                .load(R.drawable.hillary_big)
                .into(ivHillaryBig);
    }

    /*********************************************************************************************
     * OnClick Methods
     *********************************************************************************************/

    @OnClick(R.id.btGetStarted)
    public void getStartedClicked() {

        /**
         * We want to animate the rest of the height
         */
        float restOfHeight = ScreenSizeHelper.getDisplayHeightPixels(this) * 1.4f;

        PropertyValuesHolder pvRestOfHeight = PropertyValuesHolder.ofFloat("restOfHeight", thirdHeight, restOfHeight);
        PropertyValuesHolder pvOtherHeight = PropertyValuesHolder.ofFloat("otherHeight", twoThirdHeight, 0);

        ValueAnimator heightAnimator = ValueAnimator.ofPropertyValuesHolder(pvRestOfHeight, pvOtherHeight);
        heightAnimator.setDuration(400);
        heightAnimator.setInterpolator(new DecelerateInterpolator());
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float restOfHeight = (float) animation.getAnimatedValue("restOfHeight");
                float otherHeight = (float) animation.getAnimatedValue("otherHeight");

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) restOfHeight
                );
                params.gravity = Gravity.BOTTOM;
                llGetStarted.setLayoutParams(params);

                tv1.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) otherHeight
                ));
                tv2.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) otherHeight
                ));

                llBernie.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) otherHeight
                ));
                llHillary.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) otherHeight
                ));

                llText.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) otherHeight
                ));

                llGetStarted.requestLayout();
                tv1.requestLayout();
                tv2.requestLayout();
                llBernie.requestLayout();
                llHillary.requestLayout();
                llText.requestLayout();
            }
        });
        heightAnimator.addListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                // Go ahead and set the background of the layout to the desert color
                flMainActivity.setBackgroundColor(Color.parseColor("#EDEAE3"));

                removeViewsButKeepFrameLayoutContainer();
            }
        });
        heightAnimator.start();

    }

    /*********************************************************************************************
     * UI Methods
     *********************************************************************************************/

    private void animateText() {

        // Set alphas and scale to 0.0 and make visible
        tvBernie.setAlpha(0.0f);
        tvBernie.setScaleX(0.0f);
        tvBernie.setScaleY(0.0f);
        ivOr.setAlpha(0.0f);
        ivOr.setScaleX(0.0f);
        ivOr.setScaleY(0.0f);
        tvHillary.setAlpha(0.0f);
        tvHillary.setScaleX(0.0f);
        tvHillary.setScaleY(0.0f);

        tvBernie.setVisibility(View.VISIBLE);
        ivOr.setVisibility(View.VISIBLE);
        tvHillary.setVisibility(View.VISIBLE);

        // We need to set scale and alphas to 1.0f for texts
        tvBernie.animate()
                .alpha(1.0f)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(400)
                .start();

        ivOr.animate()
                .alpha(1.0f)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(400)
                .setStartDelay(200)
                .start();

        tvHillary.animate()
                .alpha(1.0f)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(400)
                .setStartDelay(400)
                .setListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        // Animate changes
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animateBounds();
                            }
                        }, 1500);
                    }
                })
                .start();
    }

    /**
     * User a {@link android.animation.ValueAnimator} with {@link android.animation.PropertyValuesHolder}
     * to animate the changes.
     */
    private void animateBounds() {

        // Get screen height 1/3
        float fullHeight = ScreenSizeHelper.getDisplayHeightPixels(this);
        this.twoThirdHeight = ScreenSizeHelper.getDisplayHeightPixels(this) * 2 / 3;
        this.thirdHeight = fullHeight - twoThirdHeight;

        // Initialize our properties
        PropertyValuesHolder pvHeightRatio1 = PropertyValuesHolder.ofFloat("heightRatio1", 0.39f, 0.50f);
        PropertyValuesHolder pvHeightRatio2 = PropertyValuesHolder.ofFloat("heightRatio2", 0.65f, 0.50f);
        PropertyValuesHolder pvHeight       = PropertyValuesHolder.ofFloat("height", fullHeight, twoThirdHeight);
        PropertyValuesHolder pvButtonHeight = PropertyValuesHolder.ofFloat("buttonHeight", 0, fullHeight - twoThirdHeight);

        // Set ValueAnimator
        ValueAnimator maskAnimator = ValueAnimator.ofPropertyValuesHolder(pvHeightRatio1, pvHeightRatio2, pvHeight, pvButtonHeight);
        maskAnimator.setInterpolator(new OvershootInterpolator(1.2f));
        maskAnimator.setDuration(600);
        maskAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float ratioDifference1 = (float) animation.getAnimatedValue("heightRatio1");
                float ratioDifference2 = (float) animation.getAnimatedValue("heightRatio2");
                float height = (float) animation.getAnimatedValue("height");
                float buttonHeight = (float) animation.getAnimatedValue("buttonHeight");

                // Reset our paths
                tv1.resetPath();
                tv2.resetPath();

                tv1.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) height
                ));
                tv2.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) height
                ));

                llBernie.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) (height * 0.8f)
                ));
                llHillary.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) (height * 1.0f)
                ));

                llText.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) height
                ));

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) buttonHeight
                );
                params.gravity = Gravity.BOTTOM;
                llGetStarted.setLayoutParams(params);

                // Subtract or add differences to both triangle layouts and invalidate
                tv1.setHeightRatio1(ratioDifference1);
                tv1.setHeightRatio2(ratioDifference2);
                tv2.setHeightRatio1(ratioDifference1);
                tv2.setHeightRatio2(ratioDifference2);
                tv1.invalidate();
                tv2.invalidate();
                tv1.requestLayout();
                tv2.requestLayout();
                llBernie.requestLayout();
                llHillary.requestLayout();
                llText.requestLayout();
                llGetStarted.requestLayout();
            }
        });
        maskAnimator.start();
    }

    private void removeViewsButKeepFrameLayoutContainer() {

        // Remove views
        int viewChildCount = flMainActivity.getChildCount();
        for (int i = 0; i < viewChildCount; i++) {
            View view = flMainActivity.getChildAt(i);
            if (view instanceof ViewGroup) {
                if (view instanceof FrameLayout) {
                    // Do nothing we need the container
                } else {
                    flMainActivity.removeView(view);
                }
            } else {
                flMainActivity.removeView(view);
            }
        }

        // Go ahead and show questions fragment
        showQuestionsFragment();
    }

    /*********************************************************************************************
     * IFragmentManager Methods
     *********************************************************************************************/

    @Override
    public void removeSplash() {

        flContainer.animate()
                .alpha(0.0f)
                .setDuration(400)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        final SplashFragment splashFragment = (SplashFragment) getSupportFragmentManager().findFragmentByTag(TAG_SPLASH);
                        if (splashFragment != null) {

                            if (!isFinishing()) {
                                getSupportFragmentManager().beginTransaction()
                                        .remove(splashFragment)
                                        .commit();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        animateText();
                                    }
                                }, 100);
                            }
                        }
                    }
                })
                .start();

    }

    @Override
    public void goToResultsFragment(Bundle bundle) {

        QuestionsFragment questionsFragment = (QuestionsFragment) getSupportFragmentManager().findFragmentByTag(TAG_QUESTION);
        if (questionsFragment != null) {

            if (!isFinishing()) {
                getSupportFragmentManager().beginTransaction()
                        .remove(questionsFragment)
                        .commit();

                ResultsFragment resultsFragment = new ResultsFragment();
                resultsFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.flContainer, resultsFragment, TAG_RESULTS)
                        .commit();
            }
        }
    }

    /*********************************************************************************************
     * Accessory Methods
     *********************************************************************************************/

    private void showSplash() {

        getSupportFragmentManager().beginTransaction()
                .add(R.id.flContainer, new SplashFragment(), TAG_SPLASH)
                .addToBackStack(null)
                .commit();
    }

    private void showQuestionsFragment() {

        // Set container alpha to 1.0f again
        flContainer.setAlpha(1.0f);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.flContainer, new QuestionsFragment(), TAG_QUESTION)
                .commit();
    }

}
