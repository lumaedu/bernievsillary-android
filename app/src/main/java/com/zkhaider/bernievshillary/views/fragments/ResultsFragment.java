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
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zkhaider.bernievshillary.R;
import com.zkhaider.bernievshillary.views.managers.IFragmentManager;

import java.util.ArrayList;
import java.util.List;

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

    /*********************************************************************************************
     * Variables
     *********************************************************************************************/

    /**
     * Communication
     */
    private IFragmentManager mFragmentManager;

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

        animateRays();

        return view;
    }

    /*********************************************************************************************
     * Initialization Methods
     *********************************************************************************************/

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
                rotationAnimator.setDuration(2000);
                rotationAnimator.setInterpolator(new LinearInterpolator());
                rotationAnimator.start();
            }
        });


    }
}
