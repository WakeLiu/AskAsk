package com.tinystartup.juniormaster.adapter.common;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class SchoolFilterAdapter extends ArrayAdapter implements Filterable {
    final AutoCompleteTextView zone;
    final List<String> schools;

    public SchoolFilterAdapter(Context context, int resource, List<String> schools) {
        super(context, resource, schools);

        this.zone = null;
        this.schools = new ArrayList<>(schools);
    }

    public SchoolFilterAdapter(Context context, int resource, AutoCompleteTextView zone, List<String> schools) {
        super(context, resource, schools);

        this.zone = zone;
        this.schools = new ArrayList<>(schools);
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults results = new FilterResults();
                final ArrayList<String> matched = new ArrayList<String>();

                String keyword = constraint.toString();
                if (zone != null && keyword.length() == 0) {
                    keyword = zone.getText().toString() + keyword;
                }

                for (String candidate : schools) {

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
                    SchoolFilterAdapter.this.clear();
                    SchoolFilterAdapter.this.addAll((List<String>) results.values);
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
