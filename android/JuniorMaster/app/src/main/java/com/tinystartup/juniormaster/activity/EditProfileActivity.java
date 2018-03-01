package com.tinystartup.juniormaster.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.tinystartup.juniormaster.AppController;
import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.activity.common.CircularNetworkImageView;
import com.tinystartup.juniormaster.adapter.drawer.ProfileListAdapter;
import com.tinystartup.juniormaster.model.app.ProfileItem;
import com.tinystartup.juniormaster.model.network.RequestListener;

public class EditProfileActivity extends AppCompatActivity implements RequestListener.OnReceivedMyProfileListener {

    private CircularNetworkImageView mProfileImageView;
    private TextView mTextView;
    private ListView mListView;
    private ProfileListAdapter mProfileListAdapter;
    private ImageLoader mImageLoader = AppController.getInstance().getRequestCenter().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mProfileImageView = (CircularNetworkImageView) findViewById(R.id.photo);
        mProfileImageView.setImageUrl("http://demo.junior-master.markchen.cc/profile1.jpg", mImageLoader);
        mTextView = (TextView) findViewById(R.id.label);

        mListView = (ListView)findViewById(R.id.list);
        mProfileListAdapter = new ProfileListAdapter(this);
        mListView.setAdapter(mProfileListAdapter);

        AppController.getInstance().getRequestCenter().makeGetMyProfileRequest(this);
    }

    @Override
    public void OnReceivedMyProfile(ProfileItem profileItem) {
        mProfileImageView.setImageUrl(profileItem.getPhotoUrl(), mImageLoader);
        mTextView.setText(profileItem.getName() + "(" + profileItem.getNickName() + ")");
        mProfileListAdapter.update(profileItem);
    }
}
