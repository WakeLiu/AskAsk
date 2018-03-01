package com.tinystartup.juniormaster.activity.pager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.AppController;
import com.tinystartup.juniormaster.adapter.pager.QuestionListAdapter;
import com.tinystartup.juniormaster.model.app.QuestionItem;
import com.tinystartup.juniormaster.model.network.RequestListener;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QuestionListFragment extends Fragment implements RequestListener.OnReceivedQuestionListListener {
    private static final Logger logger = Logger.getLogger(QuestionListFragment.class);

    private boolean canLoad = true;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private QuestionListAdapter mQuestionListAdapter;

    private Map<String, String> mParams;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mQuestionListAdapter = new QuestionListAdapter(getContext());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.question_list);
        mRecyclerView.setAdapter(mQuestionListAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (canLoad) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            _search_next();
                        }
                    }
                }
            }
        });

        return view;
    }

    public void searchAll() {
        mParams = new HashMap<String, String>();
        mParams.put("page", "0");
        _search_next();
    }

    public void search(Map<String, String> params) {
        mParams = params;
        mParams.put("page", "0");
        _search_next();
    }

    private void _search_next() {
        canLoad = false;
        mParams.put("page", String.valueOf(Integer.valueOf(mParams.get("page")) + 1));
        AppController.getInstance().getRequestCenter().makeGetQuestionListRequest(mParams, this);
    }

    @Override
    public void OnReceivedQuestionList(int page, List<QuestionItem> items) {
        canLoad = true;

        if (page == 1) {
            mQuestionListAdapter.setQuestionList(items);
        } else {
            mQuestionListAdapter.appendQuestionList(items);
        }
    }

    // alternative if we don't implement staring questions for users
    private void saveResponse(JSONObject response) {
        try
        {
            final FileOutputStream stream = getContext().openFileOutput("tempQuestions.json", Context.MODE_PRIVATE);
            final OutputStreamWriter writer = new OutputStreamWriter(stream);

            writer.write(response.toString());
            writer.close();
        }
        catch (Exception e)
        {
            logger.error("saveResponse()", e);
        }
    }

    private HashMap<String, String> getSchoolHashMap() {
        JSONObject source = loadSchoolMappings();
        HashMap<String, String> map = new HashMap<String, String>();

        Iterator<String> iter = source.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try
            {
                String value = source.getString(key);
                map.put(key, value);
            }
            catch (JSONException e)
            {
                logger.error("getSchoolHashMap()", e);
            }
        }

        return map;
    }

    private JSONObject loadSchoolMappings() {
        try
        {
            final InputStream stream = getActivity().getAssets().open("school_map.json");
            final byte[] data = new byte[stream.available()];

            stream.read(data);
            stream.close();

            return new JSONObject(new String(data));
        }
        catch (Exception e)
        {
            logger.error("loadSchoolMappings()", e);
        }

        return null;
    }
}