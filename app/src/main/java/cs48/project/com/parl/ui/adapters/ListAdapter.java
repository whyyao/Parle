package cs48.project.com.parl.ui.adapters;

import cs48.project.com.parl.ui.RowHolder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cs48.project.com.parl.R;

/**
 * Created by jakebliss on 5/27/17.
 */

public class ListAdapter extends BaseAdapter implements Filterable{
    List mData;
    List mStringFilterList;
    ValueFilter valueFilter;
    private LayoutInflater inflater;

    public ListAdapter(List cancel_type) {
        mData=cancel_type;
        mStringFilterList = cancel_type;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        RowHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_contact_listing, parent, false);
            holder = new RowHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }
        else {
            holder = (RowHolder) convertView.getTag();
        }

        holder.textView.setText(mData.get(position).toString());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
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
