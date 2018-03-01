package com.tinystartup.juniormaster.model.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionDetail {
    protected int id;
    protected String content;
    protected Boolean solved;
    protected String createdAt;
    protected String updatedAt;
    protected String grade;
    protected String school;
    protected String surname;
    protected List<String> images;
    protected List<TagItem> tags;

    public QuestionDetail(JSONObject object) throws JSONException
    {
        id = object.getInt("id");
        content = object.getString("content");
        solved = (object.getInt("solved") == 1);
        createdAt = object.getString("created_at");
        updatedAt = object.getString("updated_at");
        grade = object.getString("grade");

        JSONObject schoolObject = object.getJSONObject("school");
        school = schoolObject.getString("fullname").substring(5);

        JSONObject userObject = object.getJSONObject("user").getJSONObject("userable");
        surname = userObject.getString("name").substring(0, 1);

        images = new ArrayList<String>();
        JSONArray imageArray = object.getJSONArray("images");
        for (int i = 0; i < imageArray.length(); i++) {
            JSONObject imageObject = imageArray.getJSONObject(i);
            images.add(imageObject.getString("filename"));
        }

        tags = new ArrayList<TagItem>();
        JSONArray tagArray = object.getJSONArray("tags");
        for (int i = 0; i < tagArray.length(); i++) {
            JSONObject tagObject = tagArray.getJSONObject(i);
            tags.add(new TagItem(tagObject));
        }
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Boolean getSolved() {
        return solved;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getGrade() {
        return grade;
    }

    public String getSchool() {
        return school;
    }

    public String getSurname() {
        return surname;
    }

    public List<String> getImages() {
        return images;
    }

    public List<TagItem> getTags() {
        return tags;
    }
}