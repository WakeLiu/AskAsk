package com.tinystartup.juniormaster.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tinystartup.juniormaster.activity.pager.QuestionListFragment;
import com.tinystartup.juniormaster.activity.pager.SearchFragment;
import com.tinystartup.juniormaster.activity.pager.ChallengeListFragment;
import com.tinystartup.juniormaster.activity.pager.StarredQuestionsFragment;

public class SwipeViewPagerAdapter extends FragmentPagerAdapter {

    private ChallengeListFragment mChallengeListFragment = null;
    private SearchFragment mSearchFragment = null;
    private QuestionListFragment mQuestionListFragment = null;
    private StarredQuestionsFragment mStarredQuestionsFragment = null;

    public SwipeViewPagerAdapter(FragmentManager fm,
                                 ChallengeListFragment cf,
                                 SearchFragment sf,
                                 QuestionListFragment qf,
                                 StarredQuestionsFragment mf) {
        super(fm);

        mChallengeListFragment = cf;
        mSearchFragment = sf;
        mQuestionListFragment = qf;
        mStarredQuestionsFragment = mf;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0: return mChallengeListFragment;
            case 1: return mSearchFragment;
            case 2: return mQuestionListFragment;
            case 3: return mStarredQuestionsFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0)
        {
            return "段考複習任務";
        }
        else if (position == 1)
        {
            return "搜尋問題";
        }
        else if (position == 2)
        {
            return "問題列表";
        }
        else if (position == 3)
        {
            return "關注的問題";
        }

        return "未定義";
    }
}