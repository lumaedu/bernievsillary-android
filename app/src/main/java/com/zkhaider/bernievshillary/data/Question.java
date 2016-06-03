package com.zkhaider.bernievshillary.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZkHaider on 6/3/16.
 */

public class Question {

    @SerializedName("id")                               private String mId;
    @SerializedName("question")                         private String mQuestion;
    @SerializedName("sortOrder")                        private int mSortOrder;
    @SerializedName("bernieSandersPosition")            private int mBernieSandersPositions;
    @SerializedName("bernieSandersPositionText")        private String mBerniePositionText;
    @SerializedName("bernieSandersRecord")              private int mBernieSandersRecord;
    @SerializedName("bernieSandersRecordText")          private String mBernieSandersRecordText;
    @SerializedName("category")                         private String mCategory;
    @SerializedName("hillaryClintonPosition")           private int mHillaryClintonPosition;
    @SerializedName("hillaryClintonPositionText")       private String mHillaryClintonPositionText;
    @SerializedName("hillaryClintonRecord")             private int mHillaryClintonRecord;
    @SerializedName("hillaryClintonRecordText")         private String mHillaryClintonRecordText;
    @SerializedName("responseBLabel")                   private String mResponseBLabel;
    @SerializedName("responseALabel")                   private String mResponseALabel;

    public String getBerniePositionText() {
        return mBerniePositionText;
    }

    public int getBernieSandersPositions() {
        return mBernieSandersPositions;
    }

    public int getBernieSandersRecord() {
        return mBernieSandersRecord;
    }

    public String getBernieSandersRecordText() {
        return mBernieSandersRecordText;
    }

    public String getCategory() {
        return mCategory;
    }

    public int getHillaryClintonPosition() {
        return mHillaryClintonPosition;
    }

    public String getHillaryClintonPositionText() {
        return mHillaryClintonPositionText;
    }

    public int getHillaryClintonRecord() {
        return mHillaryClintonRecord;
    }

    public String getHillaryClintonRecordText() {
        return mHillaryClintonRecordText;
    }

    public String getId() {
        return mId;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public String getResponseALabel() {
        return mResponseALabel;
    }

    public String getResponseBLabel() {
        return mResponseBLabel;
    }

    public int getSortOrder() {
        return mSortOrder;
    }

}


