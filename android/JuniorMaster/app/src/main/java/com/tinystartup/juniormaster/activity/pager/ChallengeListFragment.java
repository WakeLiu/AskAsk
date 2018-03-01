package com.tinystartup.juniormaster.activity.pager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.AppController;
import com.tinystartup.juniormaster.adapter.pager.ChallengeListAdapter;
import com.tinystartup.juniormaster.model.app.ChallengeListItem;
import com.tinystartup.juniormaster.model.network.RequestListener;

import org.apache.log4j.Logger;

import java.util.List;


public class ChallengeListFragment extends Fragment implements RequestListener.OnReceivedChallengeListListener {
    private static final Logger logger = Logger.getLogger(ChallengeListFragment.class);

    private static final String api_token = "o6H5cDMnDkTGlJCsMB0M4Ur69gUWRK3b";
    private static final String url = "http://demo.junior-master.markchen.cc/challenges.json";

    private ExpandableListView mExpandableListView;
    private ChallengeListAdapter mExpandableListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_challenge_list, container, false);

        mExpandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        mExpandableListAdapter = new ChallengeListAdapter(getContext());
        mExpandableListView.setAdapter(mExpandableListAdapter);

        AppController.getInstance().getRequestCenter().makeGetChallengeListRequest(this);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void OnReceivedChallengeList(List<ChallengeListItem> items) {
        mExpandableListAdapter.updateContent(items);
        mExpandableListAdapter.notifyDataSetChanged();
    }
}