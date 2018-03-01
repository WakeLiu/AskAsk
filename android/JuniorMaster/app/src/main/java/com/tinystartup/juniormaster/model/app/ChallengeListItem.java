package com.tinystartup.juniormaster.model.app;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class ChallengeListItem implements Serializable {

    public enum Type { KEYPOINT, CLASSIC, PRACTICE, NONE }
    public enum Status { NOTSOLVED, SOLVED, UPDATED, NONE }

    private int id;
    private Type type;
    private Status status;
    private String title;

    public ChallengeListItem(JSONObject object) throws JSONException
    {
        id = object.getInt("id");

        switch (object.getString("type")) {
            case "KEYPOINT":
                type = Type.KEYPOINT;
                break;
            case "CLASSIC":
                type = Type.CLASSIC;
                break;
            case "PRACTICE":
                type = Type.PRACTICE;
                break;
            default:
                type = Type.NONE;
                break;
        }

        try {
            title = new String(object.getString("title").getBytes("ISO-8859-1"), "UTF-8");
        }catch (UnsupportedEncodingException exception) {

        }


        switch (object.getString("status")) {
            case "NOT-SOLVED":
                status = Status.NOTSOLVED;
                break;
            case "SOLVED":
                status = Status.SOLVED;
                break;
            case "UPDATED":
                status = Status.UPDATED;
                break;
            default:
                status = Status.NONE;
                break;
        }
    }

    public int getTypeIndex() {
        return type.ordinal();
    }

    public int getID() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        return status;
    }

}
