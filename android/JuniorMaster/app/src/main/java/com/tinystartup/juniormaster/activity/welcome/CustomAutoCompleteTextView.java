package com.tinystartup.juniormaster.activity.welcome;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;

public class CustomAutoCompleteTextView extends AutoCompleteTextView implements View.OnClickListener, View.OnFocusChangeListener {

    public CustomAutoCompleteTextView  (Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        setOnFocusChangeListener(this);
    }

    public CustomAutoCompleteTextView  (Context context) {
        super(context);
        setOnClickListener(this);
        setOnFocusChangeListener(this);
    }

    public CustomAutoCompleteTextView  (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnClickListener(this);
        setOnFocusChangeListener(this);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    public void onClick(View v) {
        showDropDown();
    }

    @Override
    public void onFocusChange(final View v, boolean hasFocus) {

        if (hasFocus) {
            performFiltering(getText(), 0);
            showDropDown();
        } else {
            String text = getText().toString();
            ListAdapter adapter = getAdapter();

            for (int i = 0; i < adapter.getCount(); i++) {
                String candidate = adapter.getItem(i).toString();
                if (text.equals(candidate) || (candidate.length() > 5 && text.equals(candidate.substring(5)))) {
                    return;
                }
            }

            setText("");
        }
    }
}
