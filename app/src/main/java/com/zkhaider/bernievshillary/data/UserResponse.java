package com.zkhaider.bernievshillary.data;

/**
 * Created by ZkHaider on 6/4/16.
 */

public class UserResponse {

    private Question mQuestion;
    private int mResponse;          // -1 against, 0 not sure, 1 for
    private float mBernieScore;
    private float mHillaryScore;

    public UserResponse(Question question, int response) {
        this.mQuestion = question;
        this.mResponse = response;
        calculateScore();
    }

    private void calculateScore() {

        // If the user voted neutral (NOT SURE) then we just return 0 for each individual score
        if (mResponse == 0) {
            this.mBernieScore = 0.0f;
            this.mHillaryScore = 0.0f;
            return;
        }

        // Else we check if the user voted for or against the question and if so go ahead and calculate
        // a score for that based on each candidate

        /**
         * Calculate Bernie score
         */
        calculateBernieScore();

        /**
         * Calculate Hillary score
         */
        calculateHillaryScore();

    }

    private void calculateBernieScore() {

        // Get Bernie's record and current positions
        int mBernieRecordPosition = mQuestion.getBernieSandersRecord();
        int mBernieCurrentPosition = mQuestion.getBernieSandersPosition();

        this.mBernieScore = 0.0f;
        // A neutral position of the candidate will also give them positive score if they were not against the user's position
        if (mBernieRecordPosition == 0 || mBernieCurrentPosition == 0) {
            if (mBernieRecordPosition == mResponse || mBernieCurrentPosition == mResponse)
                mBernieScore += 1.0f;

            // Else just do normal matching
        } else {

            if (mBernieCurrentPosition == mResponse)
                mBernieScore += 0.5f;

            if (mBernieRecordPosition == mResponse)
                mBernieScore += 0.5f;
        }
    }

    private void calculateHillaryScore() {

        int mHillaryRecordPosition = mQuestion.getHillaryClintonRecord();
        int mHillaryCurrentPosition = mQuestion.getHillaryClintonPosition();

        this.mHillaryScore = 0.0f;
        // A neutral position of the candidate will also give them positive score if they were not against the user's position
        if (mHillaryRecordPosition == 0 || mHillaryCurrentPosition == 0) {
            if (mHillaryRecordPosition == mResponse || mHillaryCurrentPosition == mResponse)
                mHillaryScore += 1.0f;

            // Else just do normal matching
        } else {

            if (mHillaryCurrentPosition == mResponse)
                mHillaryScore += 0.5f;

            if (mHillaryRecordPosition == mResponse)
                mHillaryScore += 0.5f;
        }
    }

    public float getBernieScore() {
        return mBernieScore;
    }

    public float getHillaryScore() {
        return mHillaryScore;
    }

    public int getResponse() {
        return mResponse;
    }

    public Question getQuestion() {
        return mQuestion;
    }

    public boolean isDuplicateQuestionOf(Question question) {

        // Get the other question id
        String id = question.getId();

        // Check if it matches this user response questions id
        return mQuestion.getId().equals(id);
    }

}
