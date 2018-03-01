package com.tinystartup.juniormaster.model.app;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationItem {

    protected String notifytime;
    protected String title;
    protected String content;
    protected String status;
    protected String type;


    public NotificationItem(JSONObject object) throws JSONException {

        notifytime = object.getString("Notifytime");
        title = object.getString("Title");
        content = object.getString("Content");
        status = object.getString("Status");
        type = object.getString("Type");
    }

    public String getNotifytime() {
        return notifytime;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
}
