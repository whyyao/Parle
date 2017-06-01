package cs48.project.com.parl.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.users.getOne.GetOneUserPresenter;
import cs48.project.com.parl.models.RowHolder;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.utils.BasicImageDownloader;
import cs48.project.com.parl.utils.BasicImageDownloader.ImageError;
import cs48.project.com.parl.utils.BasicImageDownloader.OnImageLoaderListener;

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
    private boolean plusFlag = false;
    private Bitmap.CompressFormat mFormat = Bitmap.CompressFormat.JPEG;

    public ListAdapter(List<User> allUsers, boolean newPlusFlag) {
        plusFlag = newPlusFlag;
        mData = allUsers;
        mStringFilterList = new ArrayList<User>(allUsers);
        notifyDataSetChanged();
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
    public User getItem(int position) {
        return mData.get(position);
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


        final RowHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_row_item, null);
            holder = new RowHolder();
            holder.txtUsername = (TextView) convertView.findViewById(R.id.search_username);
            holder.txtUserAlphabet = (TextView) convertView.findViewById(R.id.search_user_pic);
            holder.txtUserEmail = (TextView) convertView.findViewById(R.id.search_email);
            holder.contactpic = (ImageView) convertView.findViewById(R.id.contact_prof_pic);
            holder.txtViewPlus = (TextView) convertView.findViewById(R.id.textView);
            if(plusFlag == false)
            {
                holder.txtViewPlus.setVisibility(convertView.GONE);
            }
            String pictureURL = mData.get(position).photoURL;
            boolean isPhoto = pictureURL != null;
            if(!isPhoto) {
                holder.txtUserAlphabet.setVisibility(View.VISIBLE);
                holder.contactpic.setVisibility(View.GONE);
                String alphabet = mData.get(position).userName.substring(0, 1);
                holder.txtUserAlphabet.setText(alphabet);
            }else {
                final String fileName = mData.get(position).uid;
                File pictureFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "parle" + File.separator + fileName + "." + mFormat.name().toLowerCase());

                System.out.println("absolute pass is: "+pictureFile.getAbsolutePath());
                if(pictureFile.exists()){
                    System.out.println("trying to load image");
                    Bitmap b = BasicImageDownloader.readFromDisk(pictureFile);
                    holder.txtUserAlphabet.setVisibility(View.GONE);
                    holder.contactpic.setVisibility(View.VISIBLE);
                    holder.contactpic.setImageBitmap(b);
                }else{
                    final BasicImageDownloader downloader = new BasicImageDownloader(new OnImageLoaderListener() {
                        @Override
                        public void onError(ImageError error) {error.printStackTrace();}
                        @Override
                        public void onProgressChange(int percent) {}
                        @Override
                        public void onComplete(Bitmap result) {
                            final File myImageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                                    File.separator + "parle" + File.separator + fileName + "." + mFormat.name().toLowerCase());
                                    BasicImageDownloader.writeToDisk(myImageFile, result, new BasicImageDownloader.OnBitmapSaveListener() {
                                        @Override
                                        public void onBitmapSaved() {}

                                        @Override
                                        public void onBitmapSaveError(ImageError error) {error.printStackTrace();}
                                    }, mFormat, false);
                                    holder.txtUserAlphabet.setVisibility(View.GONE);
                                    holder.contactpic.setVisibility(View.VISIBLE);
                                    holder.contactpic.setImageBitmap(result);
                                }
                            });
                            downloader.download(pictureURL, true);
                        }
            }
            holder.txtUsername.setText(mData.get(position).userName.toString());
            holder.txtUserEmail.setText(mData.get(position).email.toString());
        } else {
            holder = (RowHolder)convertView.getTag();
        }


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
        protected FilterResults performFiltering(final CharSequence constraint) {
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
                List suggestedUsers = new ArrayList();
                for(User user : mStringFilterList)
                {
                    suggestedUsers.add(user);
                }
//                mStringFilterList.clear();
                results.count =  suggestedUsers.size();
                results.values = suggestedUsers;
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
