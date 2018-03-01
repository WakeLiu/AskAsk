package com.tinystartup.juniormaster.model.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionItem {
    protected int id;
    protected String catalog;
    protected List<String> tags;
    protected String content;
    protected String attachment;
    protected Boolean solved;
    protected String type;
    protected String createdAt;
    protected String updatedAt;

    protected int userid;
    protected String surname;
    protected String school;

    public QuestionItem(JSONObject object) throws JSONException
    {
        id = object.getInt("id");
        type = object.getString("type");
        content = object.getString("content");
        if (object.getJSONArray("images").length() > 0) {
            attachment = object.getJSONArray("images").getJSONObject(0).getString("filename");
        }
        solved = (object.getInt("solved") == 1);
        createdAt = object.getString("created_at");
        updatedAt = object.getString("updated_at");

        tags = new ArrayList<String>();
        catalog = "";
        JSONArray tagArray = object.getJSONArray("tags");
        for (int i = 0; i < tagArray.length(); i++) {
            JSONObject tagObject = tagArray.getJSONObject(i);

            if (tagObject.getString("type").equals("母章節") || tagObject.getString("type").equals("子章節")) {
                catalog = tagObject.getString("name");
            } else {
                tags.add(tagObject.getString("name"));
            }

        }

        JSONObject userObject = object.getJSONObject("user").getJSONObject("userable");
        userid = userObject.getInt("id");
        surname = userObject.getString("name").substring(0, 1);

        JSONObject schoolObject = object.getJSONObject("school");
        school = schoolObject.getString("fullname").substring(5);
    }

    public int getId() {
        return id;
    }

    public String getCatalog() {
        return catalog;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getContent() {
        return content;
    }

    public String getAttachment() {
        return attachment;
    }

    public Boolean getSolved() {
        return solved;
    }

    public String getType() {
        return type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getUserid() {
        return userid;
    }

    public String getSurname() {
        return surname;
    }

    public String getSchool() {
        return school;
    }
}