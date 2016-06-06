package com.zkhaider.bernievshillary.views.fragments;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zkhaider.bernievshillary.R;
import com.zkhaider.bernievshillary.views.managers.IFragmentManager;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    /**
     * ImageViews
     */
    @Bind(R.id.ivWinner)                                public ImageView ivWinner;
    @Bind(R.id.ivRays)                                  public ImageView ivRays;

    /**
     * TextViews
     */
    @Bind(R.id.tvHigherScore)                           public TextView tvHigherScore;
    @Bind(R.id.tvLowerScore)                            public TextView tvLowerScore;

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
    private float mBernieScore = 0.0f;
    private float mHillaryScore = 0.0f;

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

    private void setScores() {

        // Set our scores into the text view
        if (mBernieScore > mHillaryScore) {

            String higherScore = String.valueOf(mBernieScore * 100.0f) + "%";
            tvHigherScore.setText(higherScore);

            String lowerScore = String.valueOf(mHillaryScore * 100.0f) + "%";
            tvLowerScore.setText(lowerScore);

        } else {

            String higherScore = String.valueOf(mHillaryScore * 100.0f) + "%";
            tvHigherScore.setText(higherScore);

            String lowerScore = String.valueOf(mBernieScore * 100.0f) + "%";
            tvLowerScore.setText(lowerScore);

        }
    }
}
