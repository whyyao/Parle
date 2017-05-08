package cs48.project.com.parl.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cs48.project.com.parl.R;
import cs48.project.com.parl.models.Conversation;

/**
 * Created by yaoyuan on 4/24/17.
 */

public class ConvoListingRecyclerAdapter extends RecyclerView.Adapter<ConvoListingRecyclerAdapter.ViewHolder> {
    private List<Conversation> mConversation;

    publi c ConvoListingRecyclerAdapter(List<Conversation> conversations) {
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
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Conversation conversation = mConversation.get(position);
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

 //       Log.e("last message", conversation.lastMessage);
//        if(conversation.receiverUid.equals(currentUserUid)){
//            FirebaseDatabase.getInstance().getReference().child("users").child(conversation.senderUid).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    User displayUser = dataSnapshot.getValue(User.class);
//                    userName = displayUser.userName;
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
//        else{
//            FirebaseDatabase.getInstance().getReference().child("users").child(conversation.receiverUid).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    User displayUser = dataSnapshot.getValue(User.class);
//                    userName = displayUser.userName;
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
 //       Log.e("username",userName);
        holder.txtUsername.setText("abc");
        String alphabet = "abc".substring(0, 1);


        holder.txtUserAlphabet.setText(alphabet);
        holder.textLastMessage.setText(conversation.lastMessage);

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

        ViewHolder(View itemView) {
            super(itemView);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.contact_pic);
            txtUsername = (TextView) itemView.findViewById(R.id.contact_name);
            textLastMessage = (TextView) itemView.findViewById(R.id.message_preview);
            textDate = (TextView) itemView.findViewById(R.id.date);
            textTime = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
