package com.tinystartup.juniormaster.activity.pager;

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

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class StarredQuestionsFragment extends Fragment {
    private static final Logger logger = Logger.getLogger(StarredQuestionsFragment.class);

    private RecyclerView mRecyclerView;
    private QuestionListAdapter mQuestionListAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        mQuestionListAdapter = new QuestionListAdapter(getContext());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.question_list);
        mRecyclerView.setAdapter(mQuestionListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        JSONObject object = loadResponse();
        //mQuestionListAdapter.setQuestionList(filterStarredQuestions(object));

        return view;
    }

    private JSONObject filterStarredQuestions(JSONObject response){
        try
        {
            JSONArray dataArray = response.getJSONArray("data");
            JSONArray filteredArray = new JSONArray();

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject questionObject = dataArray.getJSONObject(i);

                if (AppController.getInstance().isStarredItem(questionObject.getInt("id"))) {
                    filteredArray.put(questionObject);
                }
            }
            response.remove("data");
            response.put("data", filteredArray);
        }
        catch (Exception e)
        {
            logger.error("filterStarredQuestions()", e);
        }

        return response;
    }

    // alternative if we don't implement staring questions for users
    private JSONObject loadResponse() {
        try
        {
            final FileInputStream stream = getContext().openFileInput("tempQuestions.json");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            final StringBuilder builder = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();

            return new JSONObject(builder.toString());
        }
        catch (Exception e)
        {
            logger.error("loadResponse()", e);
        }

        return null;
    }
}