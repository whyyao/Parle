package cs48.project.com.parl.ui.adapters;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import java.io.File;
import java.util.List;

import cs48.project.com.parl.R;
import cs48.project.com.parl.models.Chat;
import cs48.project.com.parl.utils.BasicImageDownloader;
import cs48.project.com.parl.utils.BasicImageDownloader.ImageError;
import cs48.project.com.parl.utils.BasicImageDownloader.OnImageLoaderListener;
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
    private Bitmap.CompressFormat mFormat = Bitmap.CompressFormat.JPEG;
    private void configureMyChatViewHolder(final MyChatViewHolder myChatViewHolder, int position) {
        final Chat chat = mChats.get(position);
        final String fileName = chat.senderUid;
        File pictureFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "parle" + File.separator + fileName + "." + mFormat.name().toLowerCase());

        System.out.println("absolute pass is: "+pictureFile.getAbsolutePath());
        if(pictureFile.exists()){
            System.out.println("trying to load image");
            Bitmap b = BasicImageDownloader.readFromDisk(pictureFile);
            myChatViewHolder.txtUserAlphabet.setVisibility(View.GONE);
            myChatViewHolder.profPicView.setVisibility(View.VISIBLE);
            myChatViewHolder.profPicView.setImageBitmap(b);
        }else{
            FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).child(fileName).child("photoURL").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String pictureURL = dataSnapshot.getValue(String.class);
                    // Log.d("getting user", user.userName);

                    boolean isPhoto = pictureURL != null;

                    if (isPhoto) {
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
                                myChatViewHolder.txtUserAlphabet.setVisibility(View.GONE);
                                myChatViewHolder.profPicView.setVisibility(View.VISIBLE);
                                myChatViewHolder.profPicView.setImageBitmap(result);
                            }
                        });
                        downloader.download(pictureURL, true);
                        //System.out.println("there is a photo");
                        //new DownloadImageTask(holder.imagePhoto).execute(pictureURL);
                    } else {
                        //System.out.println("there isnt a photo");
                        myChatViewHolder.txtUserAlphabet.setVisibility(View.VISIBLE);
                        myChatViewHolder.profPicView.setVisibility(View.GONE);
                        String alphabet = chat.sender.substring(0, 1);
                        myChatViewHolder.txtUserAlphabet.setText(alphabet);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }


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

        final String fileName = chat.senderUid;
        File pictureFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "parle" + File.separator + fileName + "." + mFormat.name().toLowerCase());

        System.out.println("absolute pass is: "+pictureFile.getAbsolutePath());
        if(pictureFile.exists()){
            System.out.println("trying to load image");
            Bitmap b = BasicImageDownloader.readFromDisk(pictureFile);
            otherChatViewHolder.txtUserAlphabet.setVisibility(View.GONE);
            otherChatViewHolder.profPicView.setVisibility(View.VISIBLE);
            otherChatViewHolder.profPicView.setImageBitmap(b);
        }else{
            FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).child(fileName).child("photoURL").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String pictureURL = dataSnapshot.getValue(String.class);
                    // Log.d("getting user", user.userName);

                    boolean isPhoto = pictureURL != null;

                    if (isPhoto) {
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
                                otherChatViewHolder.txtUserAlphabet.setVisibility(View.GONE);
                                otherChatViewHolder.profPicView.setVisibility(View.VISIBLE);
                                otherChatViewHolder.profPicView.setImageBitmap(result);
                            }
                        });
                        downloader.download(pictureURL, true);
                        //System.out.println("there is a photo");
                        //new DownloadImageTask(holder.imagePhoto).execute(pictureURL);
                    } else {
                        //System.out.println("there isnt a photo");
                        otherChatViewHolder.txtUserAlphabet.setVisibility(View.VISIBLE);
                        otherChatViewHolder.profPicView.setVisibility(View.GONE);
                        String alphabet = chat.sender.substring(0, 1);
                        otherChatViewHolder.txtUserAlphabet.setText(alphabet);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }

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
}
