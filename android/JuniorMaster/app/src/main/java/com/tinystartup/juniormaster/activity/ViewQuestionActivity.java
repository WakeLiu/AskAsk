package com.tinystartup.juniormaster.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.adapter.question.DiscussionListAdapter;
import com.tinystartup.juniormaster.AppController;
import com.tinystartup.juniormaster.model.app.QuestionDetail;
import com.tinystartup.juniormaster.model.app.TagItem;
import com.tinystartup.juniormaster.model.network.RequestListener;
import com.tinystartup.juniormaster.activity.common.TagLayout;

import org.apache.log4j.Logger;

import java.util.List;

public class ViewQuestionActivity extends AppCompatActivity implements RequestListener.OnReceivedQuestionDeatilListener {
    private static final Logger logger = Logger.getLogger(ViewQuestionActivity.class);

    private int mID;
    private boolean mStarred;
    private TagLayout mTagLayout;
    private NetworkImageView mProfilePic;
    private TextView mPersonalDescription;
    private TextView mTimeStamp;
    private WebView mQuestionView;
    private WebView mAnswerView;
    private ImageLoader mImageLoader = AppController.getInstance().getRequestCenter().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mID = getIntent().getIntExtra("id", 0);
        mTagLayout = (TagLayout) findViewById(R.id.tags);
        mProfilePic = (NetworkImageView) findViewById(R.id.profilePic);
        mTimeStamp = (TextView) findViewById(R.id.timestamp);
        mPersonalDescription = (TextView) findViewById(R.id.name);

        final int maxWidth = getWindowManager().getDefaultDisplay().getWidth();
        mTagLayout.inititalize(getLayoutInflater(), (int)(maxWidth * 0.9), null);

        mQuestionView = (WebView) findViewById(R.id.question);
        mQuestionView.getSettings().setJavaScriptEnabled(true);
        mQuestionView.getSettings().setDomStorageEnabled(true);
        mQuestionView.setBackgroundColor(Color.TRANSPARENT);

        mAnswerView = (WebView) findViewById(R.id.answer);
        mAnswerView.getSettings().setJavaScriptEnabled(true);
        mAnswerView.getSettings().setDomStorageEnabled(true);
        mAnswerView.setBackgroundColor(Color.TRANSPARENT);

        ListView listView = (ListView) findViewById(R.id.discussionBoard);
        DiscussionListAdapter adapter = new DiscussionListAdapter(this, (LinearLayout)findViewById(R.id.discussSection), null);
        listView.setAdapter(adapter);

        AppController.getInstance().getRequestCenter().makeGetQuestionDetailRequest(mID, this);
        mStarred = AppController.getInstance().isStarredItem(mID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        menu.findItem(R.id.action_star).setIcon(mStarred ? R.drawable.ic_stared : R.drawable.ic_unstared);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_star) {

            mStarred = !mStarred;

            if (mStarred) {
                item.setIcon(R.drawable.ic_stared);
                AppController.getInstance().addStarredItem(mID);
            } else {
                item.setIcon(R.drawable.ic_unstared);
                AppController.getInstance().removeStarredItem(mID);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnReceivedQuestionDeatil(QuestionDetail detail) {
        mTagLayout.reset();

        if (detail.getSolved()) {
            mTagLayout.addTag(TagLayout.Type.SOLVED, "已解決");
        } else {
            mTagLayout.addTag(TagLayout.Type.ASKING, "發問中");
        }

        mTagLayout.addTag(TagLayout.Type.SCHOOL, detail.getSchool());
        List<TagItem> tagList = detail.getTags();
        for (int i = 0; i < tagList.size(); i++) {
            TagItem tagItem = tagList.get(i);

            if (tagItem.getType() == TagItem.Type.CHAPTER) {
                mTagLayout.addTag(TagLayout.Type.CHAPTER, tagItem.getName());

            } else {
                mTagLayout.addTag(TagLayout.Type.SOURCE, tagItem.getName());
            }

        }

        mProfilePic.setImageUrl("http://demo.junior-master.markchen.cc/profile1.jpg", mImageLoader);
        mPersonalDescription.setText(detail.getSchool() + " " + detail.getGrade() + " " + detail.getSurname() + "同學");
        mTimeStamp.setText("提問於" + detail.getCreatedAt());

        mQuestionView.addJavascriptInterface(new WebContent(detail.getContent(), detail.getImages().get(0)), "data");
        mQuestionView.loadUrl("file:///android_asset/content.html");

        if (detail.getSolved()) {
            mAnswerView.addJavascriptInterface(new WebContent("", ""), "data");
            mAnswerView.loadUrl("file:///android_asset/content.html");
        }
    }

    public class WebContent {

        private String content;
        private String attachment;

        public WebContent(String content, String attachment) {
            this.content = content;
            this.attachment = attachment;
        }

        @JavascriptInterface
        public String getContent() {
            return this.content;
        }

        @JavascriptInterface
        public String getAttachment() {
            return this.attachment;
        }
    }
}
