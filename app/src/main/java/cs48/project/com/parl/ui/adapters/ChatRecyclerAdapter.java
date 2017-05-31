package cs48.project.com.parl.ui.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import cs48.project.com.parl.R;
import cs48.project.com.parl.models.Chat;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.utils.Constants;

/**
 * Created by yaoyuan on 4/24/17.
 */

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private List<Chat> mChats;

    public ChatRecyclerAdapter(List<Chat> chats) {
        mChats = chats;
    }

    public void add(Chat chat) {
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }

    public Chat getChat(int position){
        return mChats.get(position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_sender, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_receiver, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    private void changeProfilePic(Chat chat, MyChatViewHolder myChatViewHolder, String pictureURL){
        boolean isPhoto = pictureURL != null;
        if (isPhoto) {
            myChatViewHolder.txtUserAlphabet.setVisibility(View.GONE);
            myChatViewHolder.profPicView.setVisibility(View.VISIBLE);
            new DownloadImageTask(myChatViewHolder.profPicView).execute(pictureURL);
        } else {
            myChatViewHolder.txtUserAlphabet.setVisibility(View.VISIBLE);
            myChatViewHolder.profPicView.setVisibility(View.GONE);
            String alphabet = chat.sender.substring(0, 1);
            myChatViewHolder.txtUserAlphabet.setText(alphabet);
        }
    }

    private void changeProfilePic2(Chat chat, OtherChatViewHolder myChatViewHolder, String pictureURL){
        boolean isPhoto = pictureURL != null;
        if (isPhoto) {
            myChatViewHolder.txtUserAlphabet.setVisibility(View.GONE);
            myChatViewHolder.profPicView.setVisibility(View.VISIBLE);
            new DownloadImageTask(myChatViewHolder.profPicView).execute(pictureURL);
        } else {
            myChatViewHolder.txtUserAlphabet.setVisibility(View.VISIBLE);
            myChatViewHolder.profPicView.setVisibility(View.GONE);
            String alphabet = chat.sender.substring(0, 1);
            myChatViewHolder.txtUserAlphabet.setText(alphabet);
        }
    }



    private void configureMyChatViewHolder(final MyChatViewHolder myChatViewHolder, int position) {
        final Chat chat = mChats.get(position);
        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    User user = dataSnapshotChild.getValue(User.class);
                    //System.out.println(user.userName);
                    if (user.uid.equals(chat.senderUid)) {
                        Log.d("getting user", user.userName);
                        String pictureURL = user.photoURL;
                        changeProfilePic(chat, myChatViewHolder, pictureURL);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        boolean isPhoto = chat.photoURL != null;

        if (isPhoto) {
            myChatViewHolder.txtChatMessage.setVisibility(View.GONE);
            myChatViewHolder.photoImageView.setVisibility(View.VISIBLE);
            Glide.with(myChatViewHolder.photoImageView.getContext())
                    .load(chat.photoURL)
                    .into(myChatViewHolder.photoImageView);
        } else {
            myChatViewHolder.txtChatMessage.setVisibility(View.VISIBLE);
            myChatViewHolder.photoImageView.setVisibility(View.GONE);
            myChatViewHolder.txtChatMessage.setText(chat.message);
        }
    }

    private void configureOtherChatViewHolder(final OtherChatViewHolder otherChatViewHolder, int position) {
        final Chat chat = mChats.get(position);

        String alphabet = chat.sender.substring(0, 1);

//        otherChatViewHolder.txtChatMessage.setText(chat.translatedMessage);
        otherChatViewHolder.txtUserAlphabet.setText(alphabet);

        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    User user = dataSnapshotChild.getValue(User.class);
                    //System.out.println(user.userName);
                    if (user.uid.equals(chat.senderUid)) {
                        Log.d("getting user", user.userName);
                        String pictureURL = user.photoURL;
                        changeProfilePic2(chat, otherChatViewHolder, pictureURL);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        boolean isPhoto = chat.photoURL != null;
        if (isPhoto) {
            otherChatViewHolder.txtChatMessage.setVisibility(View.GONE);
            otherChatViewHolder.photoImageView.setVisibility(View.VISIBLE);
            Glide.with(otherChatViewHolder.photoImageView.getContext())
                    .load(chat.photoURL)
                    .into(otherChatViewHolder.photoImageView);
        } else {
            otherChatViewHolder.txtChatMessage.setVisibility(View.VISIBLE);
            otherChatViewHolder.photoImageView.setVisibility(View.GONE);
            otherChatViewHolder.txtChatMessage.setText(chat.translatedMessage);
        }
    }

    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet;
        private ImageView photoImageView, profPicView;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            photoImageView = (ImageView) itemView.findViewById(R.id.photoImageView);
            profPicView = (ImageView) itemView.findViewById(R.id.prof_pic);
        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet;
        private ImageView photoImageView, profPicView;

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            photoImageView = (ImageView) itemView.findViewById(R.id.photoImageView);
            profPicView = (ImageView) itemView.findViewById(R.id.prof_pic);
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
