package com.tinystartup.juniormaster.model.app;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileItem {

    private int mId;
    private int mSchoolId;
    private int mAccountStatus;

    private String mName;
    private String mNickName;
    private String mGrade;
    private String mGender;
    private String mSchoolDistrict;
    private String mSchoolType;
    private String mSchoolName;
    private String mSchoolFullName;
    private String mPhotoUrl;

    public ProfileItem(JSONObject object) throws JSONException {
        mId = object.getInt("id");
        mSchoolId = object.getInt("school_id");
        mAccountStatus = object.getInt("status");

        mName = object.getString("name");
        mNickName = object.getString("nickname");
        mGrade = object.getString("grade");
        mGender = object.getString("gender");
        mSchoolDistrict = object.getJSONObject("school").getString("district");
        mSchoolName = object.getJSONObject("school").getString("name");
        mSchoolFullName = object.getJSONObject("school").getString("fullname");
        if (!object.isNull("photo")) {
            mPhotoUrl = object.getJSONObject("photo").getString("filename");
        } else {
            mPhotoUrl = null;
        }

        mSchoolType = mSchoolName.substring(0, mSchoolName.indexOf("立") + 1);
        mSchoolName = mSchoolName.substring(mSchoolName.indexOf("立") + 1);
    }

    public int getId() {
        return mId;
    }

    public int getSchoolId() {
        return mSchoolId;
    }

    public int getAccountStatus() {
        return mAccountStatus;
    }

    public String getName() {
        return mName;
    }

    public String getNickName() {
        return mNickName;
    }

    public String getGrade() {
        return mGrade;
    }

    public String getGender() {
        return mGender;
    }

    public String getSchoolDistrict() {
        return mSchoolDistrict;
    }

    public String getSchoolType() {
        return mSchoolType;
    }

    public String getSchoolName() {
        return mSchoolName;
    }

    public String getSchoolFullName() {
        return mSchoolFullName;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }
}
