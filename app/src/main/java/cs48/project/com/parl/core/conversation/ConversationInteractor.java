
package cs48.project.com.parl.core.conversation;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cs48.project.com.parl.models.Conversation;

/**
 * Created by jakebliss on 5/6/17.
 */

public class ConversationInteractor implements ConversationContract.Interactor{
    private static final String TAG = "ConversationInteractor";
    private ConversationContract.OnGetConversationListener mOnGetConversationListener;
    private ConversationContract.OnSendConversationListener mOnSendConversationListener;

    public ConversationInteractor(ConversationContract.OnGetConversationListener onGetConversationListener) {
        this.mOnGetConversationListener = onGetConversationListener;
    }

    public ConversationInteractor(ConversationContract.OnSendConversationListener onSendConversationListener) {
        this.mOnSendConversationListener = onSendConversationListener;
    }

    public ConversationInteractor(ConversationContract.OnSendConversationListener onSendConversationListener,
                          ConversationContract.OnGetConversationListener onGetConversationsListener) {
        this.mOnSendConversationListener = onSendConversationListener;
        this.mOnGetConversationListener = onGetConversationsListener;
    }

    public void sendConversationToFirebaseUser(final Context context, final Conversation conversation, final String receiverFirebaseToken) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("conversations").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseReference.child("conversations").child(conversation.senderUid).child(conversation.receiverUid).setValue(conversation);
                databaseReference.child("conversations").child(conversation.receiverUid).child(conversation.senderUid).setValue(conversation);
                mOnSendConversationListener.onSendConversationSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public void getConversationFromFirebaseUser() {
        //Log.d("e",users.get(0).userName);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d("myUid",myUid);
        databaseReference.child("conversations").child(myUid).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("go","here");

                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                List<Conversation> conversations = new ArrayList<>();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    Conversation conversation = dataSnapshotChild.getValue(Conversation.class);
                    conversations.add(conversation);
                }
                mOnGetConversationListener.onGetConversationSuccess(conversations);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
