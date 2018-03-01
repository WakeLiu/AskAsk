package com.tinystartup.juniormaster.activity.drawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.AppController;
import com.tinystartup.juniormaster.adapter.drawer.PersonalItemListAdapter;
import com.tinystartup.juniormaster.model.app.NavDrawerItem;
import com.tinystartup.juniormaster.model.app.ProfileItem;
import com.tinystartup.juniormaster.model.network.RequestListener;
import com.tinystartup.juniormaster.activity.common.CircularNetworkImageView;

import org.apache.log4j.Logger;

public class PersonalDrawerFragment extends Fragment implements RecyclerView.OnItemTouchListener, RequestListener.OnReceivedMyProfileListener {
    private static final Logger logger = Logger.getLogger(PersonalDrawerFragment.class);

    public interface OnItemTouchListener {
        void OnItemTouchEvent(View view, int position);
    }

    private OnItemTouchListener mOnItemTouchListener = null;

    private TextView mNameTextView;
    private TextView mSchoolTextView;
    private TextView mGradeTextView;
    private CircularNetworkImageView mProfileImageView;
    private RecyclerView mRecyclerView;

    private PersonalItemListAdapter mPersonalItemListAdapter;
    private static String[] mPersonalItems = null;
    private GestureDetector mGestureDetector = null;
    private ImageLoader mImageLoader = AppController.getInstance().getRequestCenter().getImageLoader();

    public PersonalDrawerFragment() {

    }

    public void setOnItemTouchListener(OnItemTouchListener listener) {
        mOnItemTouchListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < mPersonalItems.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(mPersonalItems[i]);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        mPersonalItems = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);

        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_personal_drawer, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        mPersonalItemListAdapter = new PersonalItemListAdapter(getActivity(), getData());
        mRecyclerView.setAdapter(mPersonalItemListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnItemTouchListener(this);

        mNameTextView = (TextView) layout.findViewById(R.id.name);
        mSchoolTextView = (TextView) layout.findViewById(R.id.school);
        mGradeTextView = (TextView) layout.findViewById(R.id.grade);

        mProfileImageView = (CircularNetworkImageView) layout.findViewById(R.id.profile);
        //mProfileImageView.setImageUrl("http://demo.junior-master.markchen.cc/profile1.jpg", mImageLoader);

        return layout;
    }

    public void reloadProfile() {
        AppController.getInstance().getRequestCenter().makeGetMyProfileRequest(this);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());

        if (child != null && mGestureDetector.onTouchEvent(e)) {
            logger.debug("onInterceptTouchEvent(): onTouchEvent ");

            mOnItemTouchListener.OnItemTouchEvent(child, rv.getChildAdapterPosition(child));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public void OnReceivedMyProfile(ProfileItem profileItem) {

        mNameTextView.setText(profileItem.getName() + " (" + profileItem.getNickName() + ")");
        mSchoolTextView.setText(profileItem.getSchoolName());
        mGradeTextView.setText(profileItem.getGrade() + "學生");
        if (profileItem.getPhotoUrl() != null) {
            mProfileImageView.setImageUrl(profileItem.getPhotoUrl(), mImageLoader);
        }
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 50;
        int targetHeight = 50;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);

        return targetBitmap;
    }
}