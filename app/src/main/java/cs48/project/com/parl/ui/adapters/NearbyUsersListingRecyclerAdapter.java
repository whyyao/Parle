package cs48.project.com.parl.ui.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.nearby.Nearby;

import java.io.InputStream;
import java.util.List;

import cs48.project.com.parl.R;
import cs48.project.com.parl.models.User;

/**
 * Created by jakebliss on 5/24/17.
 */

public class NearbyUsersListingRecyclerAdapter extends RecyclerView.Adapter<NearbyUsersListingRecyclerAdapter.ViewHolder>{
    private List<User> mUsers;

    public NearbyUsersListingRecyclerAdapter(List<User> users) {
        this.mUsers = users;
    }

    public void add(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUsers.get(position);
        String pictureURL = user.photoURL;
        boolean isPhoto = pictureURL != null;
        if (isPhoto) {
            holder.txtUserAlphabet.setVisibility(View.GONE);
            holder.contactpic.setVisibility(View.VISIBLE);
            new DownloadImageTask(holder.contactpic).execute(pictureURL);
        } else {
            holder.txtUserAlphabet.setVisibility(View.VISIBLE);
            holder.contactpic.setVisibility(View.GONE);
            String alphabet = user.userName.substring(0, 1);
            holder.txtUserAlphabet.setText(alphabet);
        }
        holder.txtUserAlphabet.setText(user.userName.substring(0,1).toString());
        holder.txtUsername.setText(user.userName.toString());
        holder.txtUserEmail.setText(user.email.toString());
    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    public User getUser(int position) {
        return mUsers.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserAlphabet, txtUsername, txtUserEmail;
        private ImageView contactpic;

        ViewHolder(View itemView) {
            super(itemView);
            txtUsername = (TextView) itemView.findViewById(R.id.search_username);
            txtUserEmail = (TextView) itemView.findViewById(R.id.search_email);
            contactpic = (ImageView) itemView.findViewById(R.id.contact_prof_pic);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.search_user_pic);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
