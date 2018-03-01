package com.tinystartup.juniormaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.adapter.pager.SwipeViewPagerAdapter;
import com.tinystartup.juniormaster.AppController;
import com.tinystartup.juniormaster.model.network.volley.PostRequest;
import com.tinystartup.juniormaster.activity.drawer.NotificationDrawerFragment;
import com.tinystartup.juniormaster.activity.drawer.PersonalDrawerFragment;
import com.tinystartup.juniormaster.activity.pager.ChallengeListFragment;
import com.tinystartup.juniormaster.activity.pager.QuestionListFragment;
import com.tinystartup.juniormaster.activity.pager.SearchFragment;
import com.tinystartup.juniormaster.activity.pager.StarredQuestionsFragment;
import com.tinystartup.juniormaster.activity.pager.common.SlidingTabLayout;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        PersonalDrawerFragment.OnItemTouchListener,
        SearchFragment.OnSearchClickedListener {
    private static Logger logger = Logger.getLogger(MainActivity.class);

    // navigation drawer components
    private DrawerLayout mDrawerLayout;
    private PersonalDrawerFragment mPersonalDrawerFragment;
    private NotificationDrawerFragment mNotificationDrawerFragment;
    private ActionBarDrawerToggle mDrawerToggle;

    // swipe view pager components
    private ViewPager mViewPager;
    private ChallengeListFragment mChallengeListFragment;
    private SearchFragment mSearchFragment;
    private QuestionListFragment mQuestionListFragment;
    private StarredQuestionsFragment mStarredQuestionsFragment;
    private SwipeViewPagerAdapter mSwipeViewPagerAdapter;
    private SlidingTabLayout mSlidingTabLayout;

    private boolean tempExample2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logger.debug("onCreate()");

        // setup to use android.support.v7.widget.Toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("國中數學複習利器");

        // setup navigation drawers
        setupNavigationDrawers(toolbar);

        // setup swipe view pager
        setupSwipeViewPager();

        // setup floating action button
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AskQuestionActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setupNavigationDrawers(final Toolbar toolbar) {
        mDrawerLayout =  (DrawerLayout) findViewById(R.id.drawer_layout);
        mPersonalDrawerFragment = (PersonalDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_personal_drawer);
        mPersonalDrawerFragment.setOnItemTouchListener(this);
        mNotificationDrawerFragment = (NotificationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_notification_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    logger.debug("onDrawerOpened() - left");
                    mPersonalDrawerFragment.reloadProfile();

                } else {
                    logger.debug("onDrawerOpened() - right");
                }

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public void setupSwipeViewPager() {
        mChallengeListFragment = new ChallengeListFragment();
        mSearchFragment = new SearchFragment();
        mSearchFragment.setOnSearchClickedListener(this);
        mQuestionListFragment = new QuestionListFragment();
        mStarredQuestionsFragment = new StarredQuestionsFragment();
        mSwipeViewPagerAdapter = new SwipeViewPagerAdapter(
                getSupportFragmentManager(),
                mChallengeListFragment,
                mSearchFragment,
                mQuestionListFragment,
                mStarredQuestionsFragment);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSwipeViewPagerAdapter);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onTabFragmentSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notify) {
            mDrawerLayout.openDrawer(Gravity.RIGHT);
            tempExample2 = !tempExample2;
            item.setIcon(tempExample2 ? R.drawable.ic_comment_alert : R.drawable.ic_comment);
        }

        return super.onOptionsItemSelected(item);
    }

    public void OnItemTouchEvent(final View view, int position) {
        logger.debug("OnItemTouchEvent() - " + String.valueOf(position));

        mDrawerLayout.closeDrawers();

        PostRequest postRequest;
        switch (position) {
            case 0:
                AppController.getInstance().getRequestCenter().makeLoginRequest("cathy@junior.com", "admin", null);
                break;
            case 1:
                AppController.getInstance().getRequestCenter().makeLoginRequest("watermelon@junior.com", "admin", null);
                break;
            case 2:
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
                break;
            case 3:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
            case 4:
                //OnSearchEvent("我的學校", "/mine/school");
                Toast.makeText(view.getContext(), "還沒準備好唷Orz", Toast.LENGTH_LONG).show();
                break;
            case 5:
                OnSearchEvent("我的提問", new HashMap<String, String>(){{
                    put("mine", "1");
                }});
                break;
            case 6:
                //OnSearchEvent("尚未實做", mSearchFragment.getSearchOptions());
                Toast.makeText(view.getContext(), "還沒準備好唷Orz", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(view.getContext(), "還沒準備好唷Orz", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private int mLastPosition = 0;
    private boolean mInternalScroll = false;
    public void onTabFragmentSelected(int position) {
        logger.debug("onPageSelected() - page " + String.valueOf(position));

        switch (position) {
            case 1: setTitle("搜尋"); break;
            case 2:
                if (!mInternalScroll) {

                    if (mLastPosition == 1) {
                        setTitle("條件搜尋");
                        logger.debug("onTabFragmentSelected() - search triggered by scrolling");
                        mQuestionListFragment.search(mSearchFragment.getSearchOptions());

                    } else {
                        logger.debug("onTabFragmentSelected() - search triggered by scrolling");
                        setTitle("全部問題");
                        mQuestionListFragment.searchAll();
                    }
                }
                break;
            default: setTitle("國中數學複習利器"); break;
        }

        mLastPosition = position;
        mInternalScroll = false;
    }

    public void OnSearchEvent(String title, Map<String, String> options) {
        setTitle(title);
        mInternalScroll = true;
        mQuestionListFragment.search(options);
        mViewPager.setCurrentItem(2);
    }
}
