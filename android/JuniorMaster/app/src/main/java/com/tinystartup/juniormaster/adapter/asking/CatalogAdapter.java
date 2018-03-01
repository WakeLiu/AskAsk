package com.tinystartup.juniormaster.adapter.asking;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tinystartup.juniormaster.R;

public class CatalogAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;

    private LinearLayout catalogLayout;
    private TextView catalogContent;
    private ExpandableListView expandableListView;

    public CatalogAdapter(Context context,
                          List<String> expandableListTitle,
                          HashMap<String, List<String>> expandableListDetail,
                          LinearLayout catalogLayout,
                          TextView catalogContent,
                          ExpandableListView expandableListView) {

        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.catalogLayout = catalogLayout;
        this.catalogContent = catalogContent;
        this.expandableListView = expandableListView;
        this.catalogLayout.removeView(this.expandableListView);

        this.catalogContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CatalogAdapter.this.expandableListView.getParent() == null) {
                    showExpandableList();
                }
                else {
                    hideExpandableList();
                }
            }
        });
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_catalog_item, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);

        expandedListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catalogContent.setText(expandedListText);
                hideExpandableList();
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
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
            convertView = layoutInflater.inflate(R.layout.list_catalog_group, null);
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

    private void showExpandableList() {
        CatalogAdapter.this.catalogLayout.addView(CatalogAdapter.this.expandableListView);
    }

    private void hideExpandableList() {
        CatalogAdapter.this.catalogLayout.removeView(CatalogAdapter.this.expandableListView);
    }
}