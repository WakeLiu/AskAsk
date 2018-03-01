package com.tinystartup.juniormaster.adapter.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.model.app.ProfileItem;

import java.util.HashMap;
import java.util.Map;

public class ProfileListAdapter extends BaseAdapter {
    private static final int HDR_BASIC = 0;
    private static final int HDR_SCHOOL = 5;
    private static final int HDR_DEVICE = 9;

    private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;

    private static final String[] LIST = { "基本資料", "姓名", "暱稱", "性別", "年級", "學校資料", "地區", "立案單位", "學校名稱", "裝置資料", "裝置序號" };
    private Map<String, String> mDataMap;

    public ProfileListAdapter(Context context) {
        mContext = context;
        mDataMap = new HashMap<String, String>(){{
            put("姓名", "<姓名>");
            put("暱稱", "<暱稱>");
            put("性別", "<性別>");
            put("年級", "<年級>");
            put("地區", "<地區>");
            put("立案單位", "<立案單位>");
            put("學校名稱", "<學校名稱>");
            put("裝置序號", "<裝置序號>");
        }};
    }

    public void update(ProfileItem profile) {
        mDataMap.put("姓名", profile.getName());
        mDataMap.put("暱稱", profile.getNickName());
        mDataMap.put("性別", profile.getGender());
        mDataMap.put("年級", profile.getGrade());
        mDataMap.put("地區", profile.getSchoolDistrict());
        mDataMap.put("立案單位", profile.getSchoolType());
        mDataMap.put("學校名稱", profile.getSchoolName());
        mDataMap.put("裝置序號", "550e8400-e29b-41d4-a716-446655440000");

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return LIST.length;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String headerText = getHeader(position);
        if (headerText != null) {

            if (convertView == null || convertView.getTag() == LIST_ITEM) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_profile_header, parent, false);
                convertView.setTag(LIST_HEADER);
            }

            TextView headerTextView = (TextView)convertView.findViewById(R.id.lv_list_hdr);
            headerTextView.setText(headerText);
            return convertView;
        }

        if(convertView == null || convertView.getTag() == LIST_HEADER) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_profile_item, parent, false);
            convertView.setTag(LIST_ITEM);
        }

        TextView header = (TextView)convertView.findViewById(R.id.lv_item_header);
        header.setText(LIST[position % LIST.length]);

        TextView subtext = (TextView)convertView.findViewById(R.id.lv_item_subtext);
        subtext.setText(mDataMap.get(LIST[position % LIST.length]));

        //Set last divider in a sublist invisible
        View divider = convertView.findViewById(R.id.item_separator);
        if(position == HDR_DEVICE -1) {
            divider.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private String getHeader(int position) {

        if(position == HDR_BASIC || position == HDR_SCHOOL || position == HDR_DEVICE) {
            return LIST[position];
        }

        return null;
    }

    private final Context mContext;
}