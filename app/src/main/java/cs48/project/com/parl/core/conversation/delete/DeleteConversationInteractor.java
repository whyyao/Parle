package cs48.project.com.parl.core.conversation.delete;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by yaoyuan on 5/30/17.
 */

public class DeleteConversationInteractor implements DeleteConversationContract.Interactor {
    private DeleteConversationContract.OnDeleteConversationListener mOnDeleteConversationListener;

    public DeleteConversationInteractor(DeleteConversationContract.OnDeleteConversationListener onDeleteConversationListener) {
        this.mOnDeleteConversationListener = onDeleteConversationListener;
    }

    private String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public void deleteConversationFromFirebaseUser(final String uid) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("conversations").child(myUid).child(uid).removeValue();


        mOnDeleteConversationListener.onDeleteConversationSuccess();
    }

}
