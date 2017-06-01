package cs48.project.com.parl.ui.adapters;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cs48.project.com.parl.R;
import cs48.project.com.parl.models.Conversation;
import cs48.project.com.parl.utils.BasicImageDownloader;
import cs48.project.com.parl.utils.BasicImageDownloader.ImageError;
import cs48.project.com.parl.utils.BasicImageDownloader.OnImageLoaderListener;
import cs48.project.com.parl.utils.Constants;

/**
 * Created by yaoyuan on 4/24/17.
 */
//.

public class ConvoListingRecyclerAdapter extends RecyclerView.Adapter<ConvoListingRecyclerAdapter.ViewHolder> {
    private List<Conversation> mConversation;

    public ConvoListingRecyclerAdapter(List<Conversation> conversations) {
        this.mConversation = conversations;
    }

    public void add(Conversation conversation) {
        mConversation.add(conversation);
        notifyItemInserted(mConversation.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_convo_listing, parent, false);
        return new ViewHolder(view);
    }
    private String userName;
    private String correctMessage;
    private String correctUid;
    //private File pictureFile;
    private Bitmap.CompressFormat mFormat = Bitmap.CompressFormat.JPEG;
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Conversation conversation = mConversation.get(position);
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        System.out.println(currentUserUid);
        System.out.println(conversation.receiverUid);
        if(currentUserUid.equals(conversation.receiverUid)){
             userName = conversation.senderUserName;
             correctMessage = conversation.translatedLastMessage;
             correctUid = conversation.senderUid;
        }
        else{
             userName = conversation.receiverUserName;
             correctMessage = conversation.unTranslatedLastMessage;
             correctUid = conversation.receiverUid;
        }
        System.out.println("finding user uid"+correctUid);
        final String fileName = correctUid;
        File pictureFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "parle" + File.separator + fileName + "." + mFormat.name().toLowerCase());

        System.out.println("absolute pass is: "+pictureFile.getAbsolutePath());
        if(pictureFile.exists()){
            System.out.println("trying to load image");
            Bitmap b = BasicImageDownloader.readFromDisk(pictureFile);
            holder.txtUserAlphabet.setVisibility(View.GONE);
            holder.imagePhoto.setVisibility(View.VISIBLE);
            holder.imagePhoto.setImageBitmap(b);
        }else{
            FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).child(correctUid).child("photoURL").addListenerForSingleValueEvent(new ValueEventListener() {
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
                                holder.txtUserAlphabet.setVisibility(View.GONE);
                                holder.imagePhoto.setVisibility(View.VISIBLE);
                                holder.imagePhoto.setImageBitmap(result);
                            }
                        });
                        downloader.download(pictureURL, true);
                        System.out.println("there is a photo");
                        //new DownloadImageTask(holder.imagePhoto).execute(pictureURL);
                    } else {
                        System.out.println("there isnt a photo");
                        holder.txtUserAlphabet.setVisibility(View.VISIBLE);
                        holder.imagePhoto.setVisibility(View.GONE);
                        String alphabet = userName.substring(0, 1);
                        holder.txtUserAlphabet.setText(alphabet);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }

//        if(ImageStorage.checkifImageExists(correctUid))
//        {
//            File file = ImageStorage.getImage("/"+correctUid+".jpg");
//            String path = file.getAbsolutePath();
//            if (path != null){
//                System.out.println("directly setting");
//                holder.txtUserAlphabet.setVisibility(View.GONE);
//                holder.imagePhoto.setVisibility(View.VISIBLE);
//                Bitmap b = BitmapFactory.decodeFile(path);
//                holder.imagePhoto.setImageBitmap(b);
//            }
//        } else {
//            FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).child(correctUid).child("photoURL").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    String pictureURL = dataSnapshot.getValue(String.class);
//                    // Log.d("getting user", user.userName);
//
//                    boolean isPhoto = pictureURL != null;
//
//                    if (isPhoto) {
//                        System.out.println("there is a photo");
//                        holder.txtUserAlphabet.setVisibility(View.GONE);
//                        holder.imagePhoto.setVisibility(View.VISIBLE);
//                        new GetImages(pictureURL, holder.imagePhoto, correctUid).execute();
//                        //new DownloadImageTask(holder.imagePhoto).execute(pictureURL);
//                    } else {
//                        System.out.println("there isnt a photo");
//                        holder.txtUserAlphabet.setVisibility(View.VISIBLE);
//                        holder.imagePhoto.setVisibility(View.GONE);
//                        String alphabet = userName.substring(0, 1);
//                        holder.txtUserAlphabet.setText(alphabet);
//                    }
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {}
//            });
//        }




       // System.out.printf("Conversation Print: %s\n", userName);


        holder.txtUsername.setText(userName);

        holder.textLastMessage.setText(correctMessage);

        Date dateObject= new Date(conversation.time);
        String formattedDate=formatDate(dateObject);
        holder.textDate.setText(formattedDate);

        String formattedTime=formatTime(dateObject);
        holder.textTime.setText(formattedTime);

    }

    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat=new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
    private String formatTime(Date dateObject){
        SimpleDateFormat timeFormat=new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    @Override
    public int getItemCount() {
        if (mConversation != null) {
            return mConversation.size();
        }
        return 0;
    }

    public Conversation getConversation(int position) {
        return mConversation.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserAlphabet, txtUsername, textLastMessage, textDate, textTime;
        private ImageView imagePhoto;
        ViewHolder(View itemView) {
            super(itemView);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.contact_pic);
            txtUsername = (TextView) itemView.findViewById(R.id.contact_name);
            textLastMessage = (TextView) itemView.findViewById(R.id.message_preview);
            textDate = (TextView) itemView.findViewById(R.id.date);
            textTime = (TextView) itemView.findViewById(R.id.time);
            imagePhoto = (ImageView) itemView.findViewById(R.id.contact_picture);
        }
    }
//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }

//    private class GetImages extends AsyncTask<String, Void, Object> {
//        private String requestUrl, imagename_;
//        private ImageView view;
//        private Bitmap bitmap ;
//        private FileOutputStream fos;
//        private GetImages(String requestUrl, ImageView view, String _imagename_) {
//            this.requestUrl = requestUrl;
//            this.view = view;
//            this.imagename_ = _imagename_ ;
//        }
//
//        @Override
//        protected Object doInBackground(String... objects) {
//            try {
//                URL url = new URL(requestUrl);
//                URLConnection conn = url.openConnection();
//                bitmap = BitmapFactory.decodeStream(conn.getInputStream());
//            } catch (Exception ex) {
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            //view.setImageBitmap(bitmap);
//            if(!ImageStorage.checkifImageExists(imagename_))
//            {
//                System.out.println("does not exit") ;
//                view.setImageBitmap(bitmap);
//                ImageStorage.saveToSdCard(bitmap, imagename_);
//            }
//        }
//    }
}
