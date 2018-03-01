package com.tinystartup.juniormaster.model.app;

import org.json.JSONException;
import org.json.JSONObject;

public class TagItem {

    public enum Type {
        CHAPTER,
        SOURCE,
        OTHER
    }

    protected String name;
    protected Type type;

    public TagItem(JSONObject object) throws JSONException
    {
        name = object.getString("name");

        switch (object.getString("type")) {

            case "母章節":
            case "子章節":
                type = Type.CHAPTER;
                break;
            case "來源":
                type = Type.SOURCE;
                break;
            default:
                type = Type.OTHER;
                break;
        }
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
}
