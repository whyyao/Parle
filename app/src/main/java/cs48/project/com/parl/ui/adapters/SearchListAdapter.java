package cs48.project.com.parl.ui.adapters;

/**
 * Created by jakebliss on 5/27/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import cs48.project.com.parl.R;

public class SearchListAdapter extends BaseAdapter implements Filterable{
    List<String> mData;
    List mStringFilterList;
    ValueFilter valueFilter;
    private LayoutInflater inflater;

    public SearchListAdapter(List cancel_type){
        mData = cancel_type;
        mStringFilterList = cancel_type;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }


    @Override
    public TextView getView(int position, View convertView, final ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row_item, parent, false);
        view.setText(mData.get(position));

        return view;
    }


    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List filterList = new ArrayList();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).toString().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mData = (List) results.values;
            notifyDataSetChanged();
        }

    }

}
