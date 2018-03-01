package com.tinystartup.juniormaster.adapter.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.activity.TakeChallengeActivity;
import com.tinystartup.juniormaster.model.app.ChallengeListItem;


import org.apache.log4j.Logger;

public class ChallengeListAdapter extends BaseExpandableListAdapter {
    private static final Logger logger = Logger.getLogger(ChallengeListAdapter.class);

    private Context context;
    private ArrayList<ArrayList<ChallengeListItem>> expandableList;

    public ChallengeListAdapter(Context context) {
        this.context = context;
        this.expandableList = new ArrayList();

        this.expandableList.add(new ArrayList<ChallengeListItem>());
        this.expandableList.add(new ArrayList<ChallengeListItem>());
        this.expandableList.add(new ArrayList<ChallengeListItem>());
    }

    public void updateContent(List<ChallengeListItem> items) {

        for (ChallengeListItem item : items) {
            this.expandableList.get(item.getTypeIndex()).add(item);
        }

        notifyDataSetInvalidated();
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableList.get(listPosition).get(expandedListPosition).getTitle();
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_challenge_item, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);

        expandedListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, TakeChallengeActivity.class);
                intent.putExtra("id", expandableList.get(listPosition).get(expandedListPosition).getID());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableList.get(listPosition).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        switch (listPosition) {
            case 0: return "必懂數學觀念";
            case 1: return "段考經典考題";
            case 2: return "牛刀小試練習題";
        }

        return "None";
    }

    @Override
    public int getGroupCount() {
        return 3;
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_challenge_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}