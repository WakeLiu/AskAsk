package com.tinystartup.juniormaster.adapter.common;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class ChapterFilterAdapter extends ArrayAdapter implements Filterable {
    final List<String> chapters;

    public ChapterFilterAdapter(Context context, int resource, List<String> chapters) {
        super(context, resource, chapters);

        this.chapters = new ArrayList<>(chapters);
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults results = new FilterResults();
                final ArrayList<String> matched = new ArrayList<String>();

                String keyword = constraint.toString();

                for (String candidate : chapters) {

                    for (int i = 0; i < candidate.length(); i++) {
                        if (candidate.startsWith(keyword, i)) {
                            matched.add(candidate);
                            break;
                        }
                    }
                }

                results.values = matched;
                results.count = matched.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if(results != null && results.count > 0) {
                    ChapterFilterAdapter.this.clear();
                    ChapterFilterAdapter.this.addAll((List<String>) results.values);
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
