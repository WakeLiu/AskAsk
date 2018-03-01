package com.tinystartup.juniormaster.activity.drawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.AppController;
import com.tinystartup.juniormaster.adapter.drawer.NotificationListAdapter;

public class NotificationDrawerFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{
    private static final Logger logger = Logger.getLogger(NotificationDrawerFragment.class);

    private static final String api_token = "o6H5cDMnDkTGlJCsMB0M4Ur69gUWRK3b";
    private static final String url = "http://demo.junior-master.markchen.cc/notification";

    private ListView mListView;
    private NotificationListAdapter mNotificationListAdapter;

    public NotificationDrawerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_drawer, container, false);
        mListView = (ListView) view.findViewById(R.id.notification);
        mNotificationListAdapter = new NotificationListAdapter(getContext());
        mListView.setAdapter(mNotificationListAdapter);

//        if (!loadCacheData())  {
//            logger.info("loadCacheData(): no cached data, try request remote server...");
//
//            GetRequest getRequest = new GetRequest(Request.Method.GET, url, this, this);
//            getRequest.setApiToken(api_token);
//
//            // Adding request to volley request queue
//            AppController.getInstance().addToRequestQueue(getRequest);
//        }

        return view;
    }

    @Override
    public void onResponse(JSONObject response) {
        logger.debug("onResponse() - response data: " + response.toString());

        try
        {
            mNotificationListAdapter.updateContent(response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        logger.error("onErrorResponse() - VolleyError Message: " + ((error.getMessage() != null) ? error.getMessage() : ""));
    }

    public boolean loadCacheData() {
        Cache cache = AppController.getInstance().getRequestCenter().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);

        if (entry == null) { return false; }

        try
        {
            String data = new String(entry.data, "UTF-8");
            logger.debug("cached data: " + data);

            mNotificationListAdapter.updateContent(new JSONObject(data));
        }
        catch (Exception e)
        {
            logger.error("loadCacheData()", e);
        }

        return true;
    }
}