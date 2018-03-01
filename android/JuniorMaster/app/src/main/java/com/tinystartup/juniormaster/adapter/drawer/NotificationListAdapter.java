package com.tinystartup.juniormaster.adapter.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.model.app.NotificationItem;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationListAdapter extends BaseAdapter {
    private static final Logger logger = Logger.getLogger(NotificationListAdapter.class);

    private Context context;
    private LayoutInflater inflater;
    private List<NotificationItem> notificationList;

    public NotificationListAdapter(Context context) {
        this.context = context;
        this.notificationList = new ArrayList<NotificationItem>();
    }

    public void updateContent(JSONObject object) {
        this.notificationList.clear();

        try
        {
            JSONArray array = object.getJSONArray("data");

            for (int i = 0; i < array.length(); i++)
            {
                NotificationItem item = new NotificationItem(array.getJSONObject(i));
                this.notificationList.add(item);
            }

            notifyDataSetInvalidated();
        }
        catch (JSONException e)
        {
            logger.error("updateContent()", e);
        }
    }

    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Object getItem(int location) {
        return notificationList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NotificationItem item = notificationList.get(position);

        if (inflater == null) {
            inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_notification_item, null);
        }

        TextView title = (TextView)convertView.findViewById(R.id.title);
        title.setText(item.getTitle());

        TextView content = (TextView)convertView.findViewById(R.id.content);
        content.setText(item.getContent());

        ImageView picture = (ImageView)convertView.findViewById(R.id.image);
        picture.setImageDrawable(context.getResources().getDrawable(R.drawable.admin));

        return convertView;
    }
}