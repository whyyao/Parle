package cs48.project.com.parl.core.contacts.getAll;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cs48.project.com.parl.utils.Constants;

/**
 * Created by jakebliss on 5/7/17.
 */

public class GetContactsInteractor implements GetContactsContract.Interactor{
    private static final String TAG = "GetOneUserInteractor";

    private GetContactsContract.OnGetContactsUsersListener mOnGetAllUsersListener;

    public GetContactsInteractor(GetContactsContract.OnGetContactsUsersListener onGetContactsUsersListener) {
        this.mOnGetAllUsersListener = onGetContactsUsersListener;
    }

    private String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public void getContactsUsersFromFirebase() {
        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_CONTACTS).child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                List<String> users = new ArrayList<>();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    String Uid = dataSnapshotChild.getValue(String.class);
//                    if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                        users.add(user);
//                    }
                    System.out.println(Uid);
                }
                mOnGetAllUsersListener.onGetContactsUsersSuccess(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnGetAllUsersListener.onGetContactsUsersFailure(databaseError.getMessage());
            }
        });
    }
}
