package cs48.project.com.parl.ui.adapters;

import cs48.project.com.parl.core.users.getOne.GetOneUserContract;
import cs48.project.com.parl.core.users.getOne.GetOneUserPresenter;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.ui.RowHolder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cs48.project.com.parl.R;

/**
 * Created by jakebliss on 5/27/17.
 */

public class ListAdapter extends BaseAdapter implements Filterable{
    private List <User> mData;
    List<User> mStringFilterList;
    ValueFilter valueFilter;
    private LayoutInflater inflater;
    private GetOneUserPresenter mGetOneUserPresenter;
    private List<String> searchUsers = new ArrayList<>();

    public ListAdapter(List<User> allUsers) {
        mData = allUsers;
        mStringFilterList = new ArrayList<User>(allUsers);
    }

    public void setFilterList(List<User> allUsers){
        System.out.println("inside set");
        mData = allUsers;
        mStringFilterList = new ArrayList<User>(allUsers);
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
        if (inflater == null) {
            inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        RowHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_row_item, null);
            holder = new RowHolder();
            holder.txtUserAlphabet = (TextView)convertView.findViewById(R.id.search_user_pic);
            holder.txtUsername = (TextView) convertView.findViewById(R.id.search_username);
            holder.txtUserEmail = (TextView) convertView.findViewById(R.id.search_email);
            convertView.setTag(holder);
        } else {
            holder = (RowHolder)convertView.getTag();
        }
        holder.txtUserAlphabet.setText(mData.get(position).userName.substring(0,1).toString());
        holder.txtUsername.setText(mData.get(position).userName.toString());
        holder.txtUserEmail.setText(mData.get(position).email.toString());
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
                    if ((mStringFilterList.get(i).userName.toString().toUpperCase()).contains(constraint.toString().toUpperCase()) ||
                            (mStringFilterList.get(i).email.toString().toUpperCase()).contains(constraint.toString().toUpperCase()))
                    {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                mStringFilterList.clear();
                results.count =  mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mData = (List<User>) results.values;
            notifyDataSetChanged();
        }

    }
    public User getUser(int position) {
        return mStringFilterList.get(position);
    }
}
