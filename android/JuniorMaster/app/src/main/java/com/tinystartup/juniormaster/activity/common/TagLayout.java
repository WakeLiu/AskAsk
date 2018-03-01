package com.tinystartup.juniormaster.activity.common;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tinystartup.juniormaster.R;

import org.apache.log4j.Logger;

public class TagLayout extends LinearLayout {
    private static Logger logger = Logger.getLogger(TagLayout.class);

    public interface TagEventListener {
        void OnTagClicked(View view, Type type, String text);
        void OnTagLongClicked(View view, Type type, String text);
    }

    public enum Type {
        ASKING,
        SOLVED,
        CHAPTER,
        SCHOOL,
        SOURCE,
        ADD_CHAPTER,
        ADD_SCHOOL,
        ADD_SOURCE,
        BUTTON
    }

    public TagLayout(Context context) {
        this(context, null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean mInitialized = false;
    private int mTagOffset = 8, mMaxWidth = 0;
    private LinearLayout mCurrentTagLine = null;
    private LayoutInflater mLayoutInflater = null;
    private TagEventListener mTagEventListener = null;

    public void inititalize(final LayoutInflater inflater, final int maxWidth, final TagEventListener listener) {

        mLayoutInflater = inflater;
        mMaxWidth = maxWidth;
        mTagEventListener = listener;
        mInitialized = true;
    }

    public void reset() {
        this.removeAllViews();

        mTagOffset = 8;
        mCurrentTagLine = null;
    }

    public void addTag(final Type type, final String content) {
        addTag(type, content, false);
    }

    public void addTag(final Type type, final String content, boolean oneLine) {

        if (!mInitialized) {
            logger.error("addTag() - not initialized.");
            return;
        }

        if (mCurrentTagLine == null) {
            mCurrentTagLine = (LinearLayout) mLayoutInflater.inflate(R.layout.list_tag_line, this, false);
            this.addView(mCurrentTagLine);
        }

        TextView tagView = (TextView) mLayoutInflater.inflate(R.layout.list_tag_item, mCurrentTagLine, false);
        tagView.setText(content);

        if (mTagEventListener != null) {
            tagView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTagEventListener.OnTagClicked(v, type, content);
                }
            });
            tagView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View arg0) {
                    mTagEventListener.OnTagLongClicked(arg0, type, content);
                    return true;    // <- set to true
                }
            });
        }

        switch (type) {
            case ASKING:
                tagView.setBackgroundResource(R.drawable.bg_tag_asking);
                break;
            case SOLVED:
                tagView.setBackgroundResource(R.drawable.bg_tag_solved);
                break;
            case CHAPTER:
            case ADD_CHAPTER:
                tagView.setBackgroundResource(R.drawable.bg_tag_catalog);
                break;
            case SCHOOL:
            case ADD_SCHOOL:
                tagView.setBackgroundResource(R.drawable.bg_tag_school);
                break;
            case SOURCE:
            case ADD_SOURCE:
                tagView.setBackgroundResource(R.drawable.bg_tag_other);
                break;
            case BUTTON:
                tagView.setBackgroundResource(R.drawable.bg_tag_button);
                break;
        }

        if (type == Type.ADD_CHAPTER || type == Type.ADD_SCHOOL || type == Type.ADD_SOURCE || type == Type.BUTTON) {
            tagView.setTextColor(Color.DKGRAY);
        }

        if (!oneLine) {
            tagView.measure(0, 0);
            mTagOffset += tagView.getMeasuredWidth() + 8;

            if (mTagOffset >= mMaxWidth) {
                mCurrentTagLine = (LinearLayout) mLayoutInflater.inflate(R.layout.list_tag_line, this, false);
                this.addView(mCurrentTagLine);
                mTagOffset = tagView.getMeasuredWidth() + 16;
            }
        }

        mCurrentTagLine.addView(tagView);
    }
}
