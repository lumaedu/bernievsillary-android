package com.zkhaider.bernievshillary.views.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zkhaider.bernievshillary.R;
import com.zkhaider.bernievshillary.data.Question;
import com.zkhaider.bernievshillary.data.Questions;
import com.zkhaider.bernievshillary.utils.JSONUtils;
import com.zkhaider.bernievshillary.views.managers.IFragmentManager;
import com.zkhaider.bernievshillary.widgets.CircleIndicatorsView;
import com.zkhaider.bernievshillary.widgets.SimpleAnimationListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollUpdateListener;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ScrollViewOverScrollDecorAdapter;

/**
 * Created by ZkHaider on 5/29/16.
 */

public class QuestionsFragment extends Fragment {

    /*********************************************************************************************
     * Constants
     *********************************************************************************************/

    private static final String TAG = QuestionsFragment.class.getSimpleName();

    /*********************************************************************************************
     * Views
     *********************************************************************************************/

    /**
     * Layouts
     */
    @Bind(R.id.svQuestions)                         public ScrollView svQuestions;
    @Bind(R.id.llQuestions)                         public LinearLayout llQuestions;
    @Bind(R.id.llQuestion)                          public LinearLayout llQuestionTopView;
    @Bind(R.id.flQuestionButtons)                   public FrameLayout flQuestionBottomView;
    @Bind(R.id.llButtons)                           public LinearLayout llButtons;
    @Bind(R.id.llLegend)                            public LinearLayout llLegend;
    @Bind(R.id.llPositionsContainer)                public LinearLayout llPositionsContainer;
    @Bind(R.id.llBernieContainer)                   public LinearLayout llBernieContainer;
    @Bind(R.id.llHillaryContainer)                  public LinearLayout llHillaryContainer;
    @Bind(R.id.flCircleIndicatorsContainer)         public FrameLayout flCircleIndicatorsContainer;

    /**
     * Views
     */
    @Bind(R.id.vOverscroll)                         public View vOverscroll;
    @Bind(R.id.vNestedOverscroll)                   public View vNestedOverscroll;
    @Bind(R.id.ciDots)                              public CircleIndicatorsView ciDots;

    /**
     * TextViews
     */
    @Bind(R.id.tvQuestion)                          public TextView tvQuestion;
    @Bind(R.id.tvChangeYourAnswer)                  public TextView tvChangeYourAnswer;

    /**
     * Buttons
     */
    @Bind(R.id.btNotSure)                           public Button btNotSure;
    @Bind(R.id.btNextQuestion)                      public Button btNextQuestion;

    /**
     * Bernie Positions
     */
    @Bind(R.id.ivPastRecordIcon)                    public ImageView ivBerniePastRecordIcon;
    @Bind(R.id.tvPastRecord)                        public TextView tvBerniePastRecord;
    @Bind(R.id.tvPastRecordDetail)                  public TextView tvBerniePastRecordDetail;
    @Bind(R.id.ivCurrentPositionIcon)               public ImageView ivBernieCurrentPositionIcon;
    @Bind(R.id.tvCurrentPosition)                   public TextView tvBernieCurrentPosition;
    @Bind(R.id.tvCurrentPositionDetail)             public TextView tvBernieCurrentPositionDetail;

    /**
     * Hillary Positions
     */
    @Bind(R.id.ivHillaryPastRecordIcon)                    public ImageView ivHillaryPastRecordIcon;
    @Bind(R.id.tvHillaryPastRecord)                        public TextView tvHillaryPastRecord;
    @Bind(R.id.tvHillaryPastRecordDetail)                  public TextView tvHillaryPastRecordDetail;
    @Bind(R.id.ivHillaryCurrentPositionIcon)               public ImageView ivHillaryCurrentPositionIcon;
    @Bind(R.id.tvHillaryCurrentPosition)                   public TextView tvHillaryCurrentPosition;
    @Bind(R.id.tvHillaryCurrentPositionDetail)             public TextView tvHillaryCurrentPositionDetail;

    /*********************************************************************************************
     * Variables
     *********************************************************************************************/

    /**
     * Communication
     */
    private IFragmentManager mFragmentManager;

    /**
     * Our questions
     */
    private List<Question> mQuestions = new ArrayList<>();

    /**
     * Indexes for current question
     */
    private int mCurrentQuestionIndex = 0;

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

        initializeQuestions();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions, container, false);
        ButterKnife.bind(this, view);

        initializeQuestionViews();

        // Get the measured height of this view
        llQuestionTopView.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateHeight();
            }
        }, 200);

        initializeOverScrollListener();

        return view;
    }

    /*********************************************************************************************
     * Initialization Methods
     *********************************************************************************************/

    private void initializeQuestions() {

        // Load questions from raw json file
        String questionsJSON = JSONUtils.readJSONFromResources(getResources(), R.raw.questions);
        Questions questions = JSONUtils.convertToModel(questionsJSON, Questions.class);

        // Randomly pick 10 questions
        List<Question> questionList = questions != null ? questions.getQuestions() : null;

        if (questionList != null && mQuestions != null) {
            Collections.shuffle(questionList);
            for (int i = 0; i < 10; i++)
                mQuestions.add(questionList.get(i));
        }
    }

    private void initializeQuestionViews() {

        // Get the first question
        Question question = mQuestions.get(0);

        // Set question
        tvQuestion.setText(question.getQuestion());

        // Set question into positions views
        setQuestionIntoPositionViews(question);
    }

    private void initializeOverScrollListener() {

        // Initialize our scroll bounce effect
        VerticalOverScrollBounceEffectDecorator decor =
                new VerticalOverScrollBounceEffectDecorator(new ScrollViewOverScrollDecorAdapter(svQuestions));

        // Set our overscroll update listener
        decor.setOverScrollUpdateListener(new IOverScrollUpdateListener() {
            @Override
            public void onOverScrollUpdate(IOverScrollDecor decor, int state, float offset) {

                final View view = decor.getView();
                if (offset > 0) {
                    // 'view' is currently being over-scrolled from the top.
                    vOverscroll.setLayoutParams(new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            (int) (offset * 1.1)
                    ));
                    vOverscroll.requestLayout();

                    vNestedOverscroll.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            (int) offset
                    ));
                    vNestedOverscroll.requestLayout();


                } else if (offset < 0) {
                    // 'view' is currently being over-scrolled from the bottom.
                } else {

                    vOverscroll.setLayoutParams(new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            0
                    ));
                    vOverscroll.requestLayout();

                    vNestedOverscroll.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            (int) offset
                    ));
                    vNestedOverscroll.requestLayout();
                    // No over-scroll is in-effect.
                    // This is synonymous with having (state == STATE_IDLE).
                }
            }
        });
    }

    /*********************************************************************************************
     * OnClick Methods
     *********************************************************************************************/

    @OnClick(R.id.tvChangeYourAnswer)
    public void changeYourAnswerClicked() {

        // Animate out the positions view
        animateOutPositions();

        // Scroll to the top
        svQuestions.post(new Runnable() {
            @Override
            public void run() {

                // Use an object animator to smooth scroll scrollview back to 0
                int scrollY = svQuestions.getScrollY();
                ObjectAnimator scrollAnimator = ObjectAnimator.ofInt(svQuestions, "scrollY", scrollY, 0);
                scrollAnimator.setDuration(300);
                scrollAnimator.setInterpolator(new DecelerateInterpolator());
                scrollAnimator.start();
            }
        });

        // Hide legend
        llLegend.setVisibility(View.INVISIBLE);
        btNotSure.setVisibility(View.VISIBLE);

        // Animate in circle indicator dots
        tvChangeYourAnswer.setVisibility(View.INVISIBLE);
        animateInCiDots();

        // Animate in our buttons
        animateInButtons();
    }

    @OnClick(R.id.btNotSure)
    public void notSureClicked() {

        /**
         * We need to set the height of the top view to around 56 dp and animate the height change.
         * We also need to show a "change your answer button" where the ciDots are, and insert a
         * bernie vs. hillary comparison.
         */

        animateInNextContainer();
    }

    @OnClick(R.id.btYes)
    public void yesButtonClicked() {

        /**
         * We need to set the height of the top view to around 56 dp and animate the height change.
         * We also need to show a "change your answer button" where the ciDots are, and insert a
         * bernie vs. hillary comparison.
         */

        animateInNextContainer();
    }

    @OnClick(R.id.btNo)
    public void noButtonClicked() {

        /**
         * We need to set the height of the top view to around 56 dp and animate the height change.
         * We also need to show a "change your answer button" where the ciDots are, and insert a
         * bernie vs. hillary comparison.
         */

        animateInNextContainer();
    }

    @OnClick(R.id.btNextQuestion)
    public void nextQuestionClicked() {

        if (mCurrentQuestionIndex < mQuestions.size()) {

            /**
             * Snap scroll back to scroll y 0 increment circle incdicator dots and change position and
             * other views
             */

            // Animate out the positions view
            animateOutPositions();

            // Scroll to the top
            svQuestions.post(new Runnable() {
                @Override
                public void run() {

                    // Use an object animator to smooth scroll scrollview back to 0
                    int scrollY = svQuestions.getScrollY();
                    ObjectAnimator scrollAnimator = ObjectAnimator.ofInt(svQuestions, "scrollY", scrollY, 0);
                    scrollAnimator.setDuration(200);
                    scrollAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    scrollAnimator.start();
                }
            });

            // Go to next question
            goToNextQuestion();

            // Hide legend
            llLegend.setVisibility(View.INVISIBLE);
            btNotSure.setVisibility(View.VISIBLE);

            // Animate in circle indicator dots
            tvChangeYourAnswer.setVisibility(View.INVISIBLE);
            animateInCiDots();

            // Animate in our buttons
            animateInButtons();

        } else {

            /**
             * We are at show results we need to animate up and fade out the position views and
             * show the results fragment
             */
            animateUpAndFadeAwayViews();
        }
    }

    /*********************************************************************************************
     * UI Animations Methods
     *********************************************************************************************/

    private void animateHeight() {

        int measuredHeight = llQuestionTopView.getMeasuredHeight();
        llQuestionTopView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0
        ));

        llQuestionTopView.setAlpha(1.0f);

        PropertyValuesHolder pvHeight = PropertyValuesHolder.ofFloat("height", 0, measuredHeight);
        PropertyValuesHolder pvAlpha = PropertyValuesHolder.ofFloat("alpha", 0, 1);

        ValueAnimator heightAnimator = ValueAnimator.ofPropertyValuesHolder(pvHeight, pvAlpha);
        heightAnimator.setInterpolator(new OvershootInterpolator());
        heightAnimator.setDuration(400);
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float height = (float) animation.getAnimatedValue("height");
                float alpha = (float) animation.getAnimatedValue("alpha");

                llQuestionTopView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) height
                ));

                llQuestionTopView.requestLayout();

                flQuestionBottomView.setAlpha(alpha);
            }
        });
        heightAnimator.addListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                //
                llButtons.animate()
                        .alpha(1.0f)
                        .setDuration(400)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setStartDelay(200)
                        .start();
            }
        });
        heightAnimator.start();
    }

    private void animateInNextContainer() {

        // Hide circle indicators and show change your answer button
        ciDots.setVisibility(View.INVISIBLE);
        tvChangeYourAnswer.setVisibility(View.VISIBLE);

        // Show legend
        btNotSure.setVisibility(View.GONE);
        llLegend.setVisibility(View.VISIBLE);

        // Show the positions
        animateInPositions();

        // Immediately scroll to the bottom of the top view minus the frame container
        svQuestions.post(new Runnable() {
            @Override
            public void run() {
                int distanceToScroll = llQuestionTopView.getMeasuredHeight() -
                        flCircleIndicatorsContainer.getMeasuredHeight() - flCircleIndicatorsContainer.getMeasuredHeight() / 2;

                // Use an object animator to smooth scroll scrollview to posotion
                ObjectAnimator scrollAnimator = ObjectAnimator.ofInt(svQuestions, "scrollY", 0, distanceToScroll);
                scrollAnimator.setDuration(300);
                scrollAnimator.setInterpolator(new DecelerateInterpolator());
                scrollAnimator.start();

                // Animate out buttons
                animateOutButtons();
            }
        });
    }

    private void animateOutButtons() {

        llButtons.animate()
                .alpha(0.0f)
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        llButtons.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    private void animateInButtons() {

        llButtons.setAlpha(0.0f);
        llButtons.setVisibility(View.VISIBLE);
        llButtons.animate()
                .alpha(1.0f)
                .setDuration(200)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(null)
                .start();
    }

    private void animateInPositions() {

        llPositionsContainer.setVisibility(View.VISIBLE);

        // Animate and translate bernie and hillary container
        llBernieContainer.setTranslationY(30);
        llHillaryContainer.setTranslationY(30);
        llBernieContainer.animate()
                .alpha(1.0f)
                .translationY(-30)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(null)
                .start();
        llHillaryContainer.animate()
                .alpha(1.0f)
                .translationY(-30)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(null)
                .start();
    }

    private void animateOutPositions() {

        PropertyValuesHolder pvAlpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f);
        ValueAnimator alphaAnimator = ValueAnimator.ofPropertyValuesHolder(pvAlpha);
        alphaAnimator.setDuration(200);
        alphaAnimator.setInterpolator(new DecelerateInterpolator());
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float alpha = (float) animation.getAnimatedValue("alpha");

                llBernieContainer.setAlpha(alpha);
                llHillaryContainer.setAlpha(alpha);

            }
        });
        alphaAnimator.addListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                llPositionsContainer.setVisibility(View.GONE);
            }
        });

        alphaAnimator.start();
    }

    private void animateInCiDots() {

        ciDots.setAlpha(0.0f);
        ciDots.setVisibility(View.VISIBLE);
        ciDots.animate()
                .alpha(1.0f)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(400)
                .start();
    }

    private void animateUpAndFadeAwayViews() {

        // Animate and translate bernie and hillary container
        llBernieContainer.animate()
                .alpha(0.0f)
                .translationY(-500)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(600)
                .start();
        llHillaryContainer.animate()
                .alpha(0.0f)
                .translationY(-500)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(600)
                .start();
        btNextQuestion.animate()
                .alpha(0.0f)
                .translationY(-500)
                .setDuration(600)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        // Go to results fragment
                        mFragmentManager.goToResultsFragment();
                    }
                })
                .start();
    }

    /*********************************************************************************************
     * Accesory Methods
     *********************************************************************************************/

    private void goToNextQuestion() {

        // Increment our index
        mCurrentQuestionIndex++;

        // Remove top question and pick question
        if (mCurrentQuestionIndex < mQuestions.size()) {

            ciDots.setCurrentCircleIndex(mCurrentQuestionIndex);

            Question question = mQuestions.get(mCurrentQuestionIndex);
            tvQuestion.setText(question.getQuestion());
            tvQuestion.requestLayout();
            setQuestionIntoPositionViews(question);

            if (mCurrentQuestionIndex == mQuestions.size() - 1) {
                btNextQuestion.setText(R.string.show_results);
                mCurrentQuestionIndex++;
            }
        }
    }

    private void setQuestionIntoPositionViews(Question question) {

        /**
         * Set Bernie Views
         */

        int currentBerniePosition = question.getBernieSandersPosition();
        int pastBerniePosition = question.getBernieSandersRecord();
        String currentBernieDetail = question.getBerniePositionText();
        String pastBernieDetail = question.getBernieSandersRecordText();

        switch (currentBerniePosition) {
            // Disagrees
            case -1: {

                // Set icon
                ivBernieCurrentPositionIcon.setImageResource(R.drawable.icon_disagree);

                // Set text view header color
                int disagreeColor = getResources().getColor(R.color.disagree);
                tvBernieCurrentPosition.setTextColor(disagreeColor);

                // Set detail text
                tvBernieCurrentPositionDetail.setText(Html.fromHtml(currentBernieDetail));
                tvBernieCurrentPositionDetail.setMovementMethod(new LinkMovementMethod());

                break;
            }
            // Neutral
            case 0: {

                // Set icon
                ivBernieCurrentPositionIcon.setImageResource(R.drawable.icon_neutral);

                // Set text view header color
                int neutralColor = getResources().getColor(R.color.neutral);
                tvBernieCurrentPosition.setTextColor(neutralColor);

                // Set detail text
                tvBernieCurrentPositionDetail.setText(Html.fromHtml(currentBernieDetail));
                tvBernieCurrentPositionDetail.setMovementMethod(new LinkMovementMethod());

                break;
            }
            // Agrees
            case 1: {

                // Set icon
                ivBernieCurrentPositionIcon.setImageResource(R.drawable.icon_agree);

                // Set text view header color
                int agreeColor = getResources().getColor(R.color.agree);
                tvBernieCurrentPosition.setTextColor(agreeColor);

                // Set detail text
                tvBernieCurrentPositionDetail.setText(Html.fromHtml(currentBernieDetail));
                tvBernieCurrentPositionDetail.setMovementMethod(new LinkMovementMethod());

                break;
            }
        }

        switch (pastBerniePosition) {
            // Disagrees
            case -1: {

                // Set icon
                ivBerniePastRecordIcon.setImageResource(R.drawable.icon_disagree);

                // Set text view header color
                int disagreeColor = getResources().getColor(R.color.disagree);
                tvBerniePastRecord.setTextColor(disagreeColor);

                // Set detail text
                tvBerniePastRecordDetail.setText(Html.fromHtml(pastBernieDetail));
                tvBerniePastRecordDetail.setMovementMethod(new LinkMovementMethod());

                break;
            }
            // Neutral
            case 0: {

                // Set icon
                ivBerniePastRecordIcon.setImageResource(R.drawable.icon_neutral);

                // Set text view header color
                int neutralColor = getResources().getColor(R.color.neutral);
                tvBerniePastRecord.setTextColor(neutralColor);

                // Set detail text
                tvBerniePastRecordDetail.setText(Html.fromHtml(pastBernieDetail));
                tvBerniePastRecordDetail.setMovementMethod(new LinkMovementMethod());

                break;
            }
            // Agrees
            case 1: {

                // Set icon
                ivBerniePastRecordIcon.setImageResource(R.drawable.icon_agree);

                // Set text view header color
                int agreeColor = getResources().getColor(R.color.agree);
                tvBerniePastRecord.setTextColor(agreeColor);

                // Set detail text
                tvBerniePastRecordDetail.setText(Html.fromHtml(pastBernieDetail));
                tvBerniePastRecordDetail.setMovementMethod(new LinkMovementMethod());

                break;
            }
        }

        /**
         * Set Hillary Views
         */

        int currentHillaryPosition = question.getHillaryClintonPosition();
        int pastHillaryPosition = question.getHillaryClintonRecord();
        String currentHillaryDetail = question.getHillaryClintonPositionText();
        String pastHillaryDetail = question.getHillaryClintonRecordText();

        switch (currentHillaryPosition) {
            // Disagrees
            case -1: {

                // Set icon
                ivHillaryCurrentPositionIcon.setImageResource(R.drawable.icon_disagree);

                // Set text view header color
                int disagreeColor = getResources().getColor(R.color.disagree);
                tvHillaryCurrentPosition.setTextColor(disagreeColor);

                // Set detail text
                tvHillaryCurrentPositionDetail.setText(Html.fromHtml(currentHillaryDetail));
                tvHillaryCurrentPositionDetail.setMovementMethod(new LinkMovementMethod());

                break;
            }
            // Neutral
            case 0: {

                // Set icon
                ivHillaryCurrentPositionIcon.setImageResource(R.drawable.icon_neutral);

                // Set text view header color
                int neutralColor = getResources().getColor(R.color.neutral);
                tvHillaryCurrentPosition.setTextColor(neutralColor);

                // Set detail text
                tvHillaryCurrentPositionDetail.setText(Html.fromHtml(currentHillaryDetail));
                tvHillaryCurrentPositionDetail.setMovementMethod(new LinkMovementMethod());

                break;
            }
            // Agrees
            case 1: {

                // Set icon
                ivHillaryCurrentPositionIcon.setImageResource(R.drawable.icon_agree);

                // Set text view header color
                int agreeColor = getResources().getColor(R.color.agree);
                tvHillaryCurrentPosition.setTextColor(agreeColor);

                // Set detail text
                tvHillaryCurrentPositionDetail.setText(Html.fromHtml(currentHillaryDetail));
                tvHillaryCurrentPositionDetail.setMovementMethod(new LinkMovementMethod());

                break;
            }
        }

        switch (pastHillaryPosition) {
            // Disagrees
            case -1: {

                // Set icon
                ivHillaryPastRecordIcon.setImageResource(R.drawable.icon_disagree);

                // Set text view header color
                int disagreeColor = getResources().getColor(R.color.disagree);
                tvHillaryPastRecord.setTextColor(disagreeColor);

                // Set detail text
                tvHillaryPastRecordDetail.setText(Html.fromHtml(pastHillaryDetail));
                tvHillaryPastRecordDetail.setMovementMethod(new LinkMovementMethod());

                break;
            }
            // Neutral
            case 0: {

                // Set icon
                ivHillaryPastRecordIcon.setImageResource(R.drawable.icon_neutral);

                // Set text view header color
                int neutralColor = getResources().getColor(R.color.neutral);
                tvHillaryPastRecord.setTextColor(neutralColor);

                // Set detail text
                tvHillaryPastRecordDetail.setText(Html.fromHtml(pastHillaryDetail));
                tvHillaryPastRecordDetail.setMovementMethod(new LinkMovementMethod());

                break;
            }
            // Agrees
            case 1: {

                // Set icon
                ivHillaryPastRecordIcon.setImageResource(R.drawable.icon_agree);

                // Set text view header color
                int agreeColor = getResources().getColor(R.color.agree);
                tvHillaryPastRecord.setTextColor(agreeColor);

                // Set detail text
                tvHillaryPastRecordDetail.setText(Html.fromHtml(pastHillaryDetail));
                tvHillaryPastRecordDetail.setMovementMethod(new LinkMovementMethod());

                break;
            }
        }

        llQuestionTopView.requestLayout();
        flQuestionBottomView.requestLayout();
        llPositionsContainer.requestLayout();
        flCircleIndicatorsContainer.requestLayout();
    }

}
