package com.tinystartup.juniormaster.adapter.pager;

import com.android.volley.toolbox.NetworkImageView;
import com.tinystartup.juniormaster.activity.ViewQuestionActivity;
import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.AppController;
import com.tinystartup.juniormaster.model.app.QuestionItem;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.tinystartup.juniormaster.activity.common.TagLayout;

import org.apache.log4j.Logger;

public class QuestionListAdapter extends RecyclerView.Adapter {
    private static final Logger logger = Logger.getLogger(QuestionListAdapter.class);

    private Context context;
    private List<QuestionItem> questionList;
    private final ImageLoader imageLoader = AppController.getInstance().getRequestCenter().getImageLoader();

    public QuestionListAdapter(Context context) {
        this.context = context;
        this.questionList = new ArrayList<QuestionItem>();
    }

    public void setQuestionList(List<QuestionItem> items) {
        this.questionList.clear();
        this.questionList.addAll(items);
        notifyDataSetChanged();
    }

    public void appendQuestionList(List<QuestionItem> items) {
        this.questionList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public QuestionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemLayoutView = layoutInflater.inflate(R.layout.list_question_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(layoutInflater, itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final QuestionListAdapter.ViewHolder viewHolder = (QuestionListAdapter.ViewHolder)holder;
        final QuestionItem item = questionList.get(position);

        viewHolder.tagLayout.reset();

        if (item.getSolved()) {
            viewHolder.tagLayout.addTag(TagLayout.Type.SOLVED, "已解決", true);
        } else {
            viewHolder.tagLayout.addTag(TagLayout.Type.ASKING, "發問中", true);
        }

        if (item.getCatalog().length() > 0) {
            viewHolder.tagLayout.addTag(TagLayout.Type.CHAPTER, item.getCatalog(), true);
        }

        viewHolder.tagLayout.addTag(TagLayout.Type.SCHOOL, item.getSchool(), true);

        for (String tagString: item.getTags()) {
            viewHolder.tagLayout.addTag(TagLayout.Type.SOURCE, tagString, true);
        }

        viewHolder.textView.setText(item.getSurname() + "同學問說：" + item.getContent());
        viewHolder.imageView.setImageUrl(item.getAttachment(), imageLoader);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ViewQuestionActivity.class);
                intent.putExtra("id", item.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TagLayout tagLayout;
        public TextView textView;
        public NetworkImageView imageView;

        public ViewHolder(LayoutInflater inflater, View itemView) {
            super(itemView);

            tagLayout = (TagLayout) itemView.findViewById(R.id.tags);
            textView = (TextView) itemView.findViewById(R.id.content);
            imageView = (NetworkImageView) itemView.findViewById(R.id.attachment);

            tagLayout.inititalize(inflater, 0, null);
        }
    }
}