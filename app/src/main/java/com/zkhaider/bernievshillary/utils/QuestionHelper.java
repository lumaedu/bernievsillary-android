package com.zkhaider.bernievshillary.utils;

import com.zkhaider.bernievshillary.data.UserResponse;

import java.util.List;

/**
 * Created by ZkHaider on 6/4/16.
 */

public class QuestionHelper {

    public static float calculateBernieScore(List<UserResponse> userResponses, int questionsSize) {

        // Initializer for total score
        float totalBernieScore = 0.0f;

        // Sum up the total bernie scores
        for (UserResponse userResponse : userResponses) {

            totalBernieScore += userResponse.getBernieScore();
        }

        // Go ahead and normalize the score - aka divide by the total number of questions
        return totalBernieScore / (float) questionsSize;
    }

    public static float calculateHillaryScore(List<UserResponse> userResponses, int questionsSize) {

        // Initializer for total score
        float totalHillaryScore = 0.0f;

        // Sum up the total bernie scores
        for (UserResponse userResponse : userResponses) {

            totalHillaryScore += userResponse.getHillaryScore();
        }

        // Go ahead and normalize the score - aka divide by the total number of questions
        return totalHillaryScore / (float) questionsSize;
    }

}
