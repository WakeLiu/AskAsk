package com.tinystartup.juniormaster.activity.pager;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;

import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.AppController;
import com.tinystartup.juniormaster.adapter.common.ChapterFilterAdapter;
import com.tinystartup.juniormaster.adapter.common.SchoolFilterAdapter;
import com.tinystartup.juniormaster.activity.common.TagLayout;
import com.tinystartup.juniormaster.activity.welcome.CustomAutoCompleteTextView;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchFragment extends Fragment implements TagLayout.TagEventListener {
    private static Logger logger = Logger.getLogger(SearchFragment.class);

    public interface OnSearchClickedListener {
        void OnSearchEvent(String title, Map<String, String> option);
    }

    private OnSearchClickedListener mListener;
    private List<String> mTargetChapterList;
    private List<String> mTargetSchoolList;
    private List<String> mTargetSourceList;
    private SwitchCompat mExamSwitch;
    private SwitchCompat mAskingSwitch;
    private TagLayout mTagLayout;
    private Dialog mChapterDialog;
    private Dialog mSchoolDialog;
    private Dialog mSourcingDialog;

    public SearchFragment() {
        // Required empty public constructor
    }

    public void setOnSearchClickedListener(OnSearchClickedListener listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Map<String, String> getSearchOptions() {
        Map<String, String> options = new HashMap<String, String>();
        String builder = "";

        if (mAskingSwitch.isChecked()) {
            options.put("sovled", "0");
        }

        List<String> targetSchoolIdList = getTargetSchoolIdList();
        if (targetSchoolIdList.size() > 0) {
            options.put("school_id", TextUtils.join(",", targetSchoolIdList));
        }

        List<String> targetTagIdList = new ArrayList<String>();
        targetTagIdList.addAll(getTargetChapterIdList());
        targetTagIdList.addAll(getTargetSourceIdList());
        if (targetTagIdList.size() > 0) {
            options.put("tag_id", TextUtils.join(",", targetTagIdList));
        }

        return options;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        final int maxWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        final int maxHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();

        mTargetChapterList = new ArrayList<String>(); //getExamRange();
        mTargetSchoolList = new ArrayList<String>();
        mTargetSourceList = new ArrayList<String>();

        mExamSwitch = (SwitchCompat) rootView.findViewById(R.id.switch_examrange);
        mAskingSwitch = (SwitchCompat) rootView.findViewById(R.id.switch_status);
        mTagLayout = (TagLayout) rootView.findViewById(R.id.tags);
        mTagLayout.inititalize(inflater, (int) (maxWidth * 0.95), this);

        mExamSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTargetChapterList = getExamRange();
                } else {
                    mTargetChapterList.clear();
                }
                rePopulateTags();
            }
        });

        mAskingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rePopulateTags();
            }
        });

        mChapterDialog = createChapterDialog(maxWidth, (int)(maxHeight * 0.8));
        mSchoolDialog = createSchoolDialog(maxWidth, (int) (maxHeight * 0.8));
        mSourcingDialog = createSourcingDialog(maxWidth, (int)(maxHeight * 0.8));

        rePopulateTags();

        // Inflate the layout for this fragment
        return rootView;
    }

    public Dialog createChapterDialog(int maxWidth, int maxHeight) {
        final Dialog dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.tag_chapter_slector);
        dialog.getWindow().setLayout(maxWidth, maxHeight);

        final List<String> chapters = AppController.getInstance().getChapterList();
        final ArrayAdapter<String> chapterAdapter = new ChapterFilterAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, chapters);
        final AutoCompleteTextView chapterSelectView = (CustomAutoCompleteTextView) dialog.findViewById(R.id.chapter);
        chapterSelectView.setAdapter(chapterAdapter);
        chapterSelectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chapter = chapterAdapter.getItem(position).toString();

                if (!mTargetChapterList.contains(chapter)) {
                    List<String> temp = new ArrayList<String>(mTargetChapterList);
                    mExamSwitch.setChecked(false);

                    mTargetChapterList = temp;
                    mTargetChapterList.add(chapter);
                }
                rePopulateTags();
                dialog.dismiss();
                chapterSelectView.setText("");
            }
        });

        return dialog;
    }

    public Dialog createSchoolDialog(int maxWidth, int maxHeight) {
        final Dialog dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.tag_school_slector);
        dialog.getWindow().setLayout(maxWidth, maxHeight);

        final List<String> schools = AppController.getInstance().getSchoolList();
        final ArrayAdapter<String> schoolAdapter = new SchoolFilterAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, schools);
        final AutoCompleteTextView schoolSelectView = (CustomAutoCompleteTextView) dialog.findViewById(R.id.school);
        schoolSelectView.setAdapter(schoolAdapter);
        schoolSelectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String school = schoolAdapter.getItem(position).toString();

                if (!mTargetSchoolList.contains(school)) {
                    mTargetSchoolList.add(school);
                }
                rePopulateTags();
                dialog.dismiss();
                schoolSelectView.setText("");
            }
        });

        return dialog;
    }

    public Dialog createSourcingDialog(int maxWidth, int maxHeight) {
        final Dialog dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.tag_source_slector);
        dialog.getWindow().setLayout(maxWidth, maxHeight);

        final List<String> sources = AppController.getInstance().getSourceList();
        final ArrayAdapter<String> sourceAdapter = new SchoolFilterAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, sources);
        final AutoCompleteTextView sourceSelectView = (CustomAutoCompleteTextView) dialog.findViewById(R.id.source);
        sourceSelectView.setAdapter(sourceAdapter);
        sourceSelectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String source = sourceAdapter.getItem(position).toString();

                if (!mTargetSourceList.contains(source)) {
                    mTargetSourceList.add(source);
                }
                rePopulateTags();
                dialog.dismiss();
                sourceSelectView.setText("");
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void rePopulateTags() {
        mTagLayout.reset();

        if (mAskingSwitch.isChecked()) {
            mTagLayout.addTag(TagLayout.Type.ASKING, "發問中");
        }

        if (!mExamSwitch.isChecked() && mTargetChapterList.size() == 0) {
            mTagLayout.addTag(TagLayout.Type.CHAPTER, "全部");
        } else {
            for (String tagStr : mTargetChapterList) {
                mTagLayout.addTag(TagLayout.Type.CHAPTER, tagStr);
            }
        }

        for (String schoolStr : mTargetSchoolList) {
            mTagLayout.addTag(TagLayout.Type.SCHOOL, schoolStr);
        }

        for (String sourceStr : mTargetSourceList) {
            mTagLayout.addTag(TagLayout.Type.SOURCE, sourceStr);
        }

        mTagLayout.addTag(TagLayout.Type.ADD_CHAPTER, "加入章節條件");
        mTagLayout.addTag(TagLayout.Type.ADD_SCHOOL, "加入學校條件");
        mTagLayout.addTag(TagLayout.Type.ADD_SOURCE, "加入來源條件");
        mTagLayout.addTag(TagLayout.Type.BUTTON, "開始搜尋");
    }

    private static List<String> getExamRange() {
        List<String> collection = new ArrayList<String>();

        collection.add("分數的運算");
        collection.add("一元一次方程式");

        return  collection;
    }

    public void OnTagClicked(View view, TagLayout.Type type, String text) {
        logger.debug("Type: " + type.toString() + " Text: " + text + " clicked.");

        if (type == TagLayout.Type.ADD_CHAPTER) {
            mChapterDialog.show();
        } else if (type == TagLayout.Type.ADD_SCHOOL) {
            mSchoolDialog.show();
        } else if (type == TagLayout.Type.ADD_SOURCE) {
            mSourcingDialog.show();
        } else  if (type == TagLayout.Type.ASKING) {

            mListener.OnSearchEvent("條件搜尋 - 發問中", new HashMap<String, String>() {{
                put("solved", "0");
            }});
        } else  if (type == TagLayout.Type.CHAPTER) {
            final String id = AppController.getInstance().getChapterHashMap().get(text);
            mListener.OnSearchEvent("條件搜尋 - 章節", new HashMap<String, String>() {{
                put("tag_id", id);
            }});
        } else  if (type == TagLayout.Type.SCHOOL) {
            final String id = AppController.getInstance().getSchoolHashMap().get(text);
            mListener.OnSearchEvent("條件搜尋 - 學校", new HashMap<String, String>() {{
                put("school_id", id);
            }});
        } else  if (type == TagLayout.Type.SOURCE) {
            final String id = AppController.getInstance().getSourceHashMap().get(text);
            mListener.OnSearchEvent("條件搜尋 - 來源", new HashMap<String, String>() {{
                put("tag_id", id);
            }});
        }  else if (type == TagLayout.Type.BUTTON) {
            mListener.OnSearchEvent("條件搜尋", getSearchOptions());
        }

        getActivity().setTitle(text);
    }

    public void OnTagLongClicked(View view, TagLayout.Type type, String text) {
        logger.debug("Type: " + type.toString() + " Text: " + text + " long clicked.");

        if (type == TagLayout.Type.CHAPTER) {
            mTargetChapterList.remove(text);
            rePopulateTags();
        } else if (type == TagLayout.Type.SCHOOL) {
            mTargetSchoolList.remove(text);
            rePopulateTags();
        } else if (type == TagLayout.Type.SOURCE) {
            mTargetSourceList.remove(text);
            rePopulateTags();
        }
    }

    private List<String> getTargetChapterIdList() {
        HashMap<String, String> dict = AppController.getInstance().getChapterHashMap();
        List<String> target = new ArrayList<String>();

        for (String item: mTargetChapterList) {
            target.add(dict.get(item));
        }

        return target;
    }

    private List<String> getTargetSchoolIdList() {
        HashMap<String, String> dict = AppController.getInstance().getSchoolHashMap();
        List<String> target = new ArrayList<String>();

        for (String item: mTargetSchoolList) {
            target.add(dict.get(item));
        }

        return target;
    }

    private List<String> getTargetSourceIdList() {
        HashMap<String, String> dict = AppController.getInstance().getSourceHashMap();
        List<String> target = new ArrayList<String>();

        for (String item: mTargetSourceList) {
            target.add(dict.get(item));
        }

        return target;
    }
}