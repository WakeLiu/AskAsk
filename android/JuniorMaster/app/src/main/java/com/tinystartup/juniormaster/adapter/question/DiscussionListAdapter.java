package com.tinystartup.juniormaster.adapter.question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;
import java.util.ArrayList;

import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.model.app.DiscussionItem;

public class DiscussionListAdapter extends BaseAdapter {
    private Context mContext;
    private LinearLayout mParentLayout;
    ArrayList<DiscussionItem> mItemList;

    public DiscussionListAdapter(Context context, LinearLayout section, JSONObject object) {
        mContext = context;
        mParentLayout = section;
        mItemList = new ArrayList<DiscussionItem>();

        mItemList.add(new DiscussionItem("中正國中 陳同學 於 2016-06-01 12:22 說：", "這個是比與比例式的問題啦。"));
        mItemList.add(new DiscussionItem("中正國中 徐同學 於 2016-06-02 13:12 說：", "這題我們班小考也有出耶！"));
        mItemList.add(new DiscussionItem("中正國中 詹同學 於 2016-06-03 14:26 說：", "哈哈哈哈哈哈哈"));
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.list_discuss_item, null);
        TextView usernameView = (TextView) rowView.findViewById(R.id.nameView);
        TextView contentView = (TextView) rowView.findViewById(R.id.contentView);

        usernameView.setText(mItemList.get(position).getUsername());
        contentView.setText(mItemList.get(position).getContent());

        //mParentLayout.requestLayout();

        return rowView;
    }
}
