package com.zkhaider.bernievshillary.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zkhaider.bernievshillary.views.managers.IFragmentManager;
import com.zkhaider.bernievshillary.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZkHaider on 5/29/16.
 */

public class SplashFragment extends Fragment {

    @Bind(R.id.llSplash)                                    public LinearLayout llSplash;

    /**
     * Communication
     */
    private IFragmentManager mFragmentManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mFragmentManager = (IFragmentManager) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Handler handler = new Handler();
        int splashDisplayLength = 1500;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                removeSelf();
            }
        }, splashDisplayLength);
    }

    private void removeSelf() {
        mFragmentManager.removeSplash();
    }

}
