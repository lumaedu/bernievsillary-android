package com.zkhaider.bernievshillary.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ZkHaider on 6/3/16.
 */

public class Questions {

    @SerializedName("questions")                            private List<Question> mQuestions;

    public List<Question> getQuestions() {
        return mQuestions;
    }

}
