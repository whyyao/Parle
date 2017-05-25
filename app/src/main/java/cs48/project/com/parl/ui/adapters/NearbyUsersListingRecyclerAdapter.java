package cs48.project.com.parl.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.nearby.Nearby;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_contact_listing, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUsers.get(position);

        String alphabet = user.userName.substring(0, 1);

        holder.username.setText(user.userName);
        holder.userAlphabet.setText(alphabet);
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
        private TextView userAlphabet, username;

        ViewHolder(View itemView) {
            super(itemView);
            userAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            username = (TextView) itemView.findViewById(R.id.text_view_username);
        }
    }
}
