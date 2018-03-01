package com.tinystartup.juniormaster.model.app;

import java.io.Serializable;

public class DiscussionItem implements Serializable {
    private String mUsername;
    private String mContent;

    public DiscussionItem(String username, String content)
    {
        mUsername = username;
        mContent = content;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getContent() {
        return mContent;
    }
}
