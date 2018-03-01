package com.tinystartup.juniormaster.activity.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.tinystartup.juniormaster.R;

import org.apache.log4j.Logger;

public class TaggingLayout extends LinearLayout {
    private static Logger logger = Logger.getLogger(TaggingLayout.class);

    public TaggingLayout(Context context) {
        this(context, null);
    }

    public TaggingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaggingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean mInitialized = false;
    private int mTagOffset = 8, mMaxWidth = 0;
    private LinearLayout mCurrentTagLine = null;
    private LayoutInflater mLayoutInflater = null;

    public void inititalize(final LayoutInflater inflater, final int maxWidth) {

        mLayoutInflater = inflater;
        mMaxWidth = maxWidth;
        mInitialized = true;
    }

    public void reset() {
        this.removeAllViews();

        mTagOffset = 8;
        mCurrentTagLine = null;
    }

    public void addCandidate(final String content) {

        if (!mInitialized) {
            logger.error("addTag() - not initialized.");
            return;
        }

        if (mCurrentTagLine == null) {
            mCurrentTagLine = (LinearLayout) mLayoutInflater.inflate(R.layout.list_tagging_line, this, false);
            this.addView(mCurrentTagLine);
        }

        CheckBox checkBox = (CheckBox) mLayoutInflater.inflate(R.layout.list_tagging_item, mCurrentTagLine, false);
        checkBox.setText(content);
        checkBox.measure(0, 0);
        mTagOffset += checkBox.getMeasuredWidth() + 8;

        if (mTagOffset >= mMaxWidth) {
            mCurrentTagLine = (LinearLayout) mLayoutInflater.inflate(R.layout.list_tagging_line, this, false);
            this.addView(mCurrentTagLine);
            mTagOffset = checkBox.getMeasuredWidth() + 16;
        }

        mCurrentTagLine.addView(checkBox);
    }
}
