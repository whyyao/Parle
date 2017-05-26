package cs48.project.com.parl.core.contacts.getAll;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cs48.project.com.parl.models.User;
import cs48.project.com.parl.utils.Constants;

/**
 * Created by jakebliss on 5/7/17.
 */

public class GetContactsInteractor implements GetContactsContract.Interactor{
    private static final String TAG = "GetOneUserInteractor";
  //  private List<User> result;
    private GetContactsContract.OnGetContactsUsersListener mOnGetAllUsersListener;

    public GetContactsInteractor(GetContactsContract.OnGetContactsUsersListener onGetContactsUsersListener) {
        this.mOnGetAllUsersListener = onGetContactsUsersListener;
      //  result = new ArrayList<>();
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
                        users.add(Uid);
//                    }
                    System.out.println(Uid);
                }
                getContactsFromUsers(users);
               // mOnGetAllUsersListener.onGetContactsUsersSuccess(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //mOnGetAllUsersListener.onGetContactsUsersFailure(databaseError.getMessage());
            }
        });
    }

    public void getContactsFromUsers(final List<String> uids) {
            FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                    List<User> resultUsers = new ArrayList<>();
                    while (dataSnapshots.hasNext()) {
                        DataSnapshot dataSnapshotChild = dataSnapshots.next();
                        User user = dataSnapshotChild.getValue(User.class);
                        //System.out.println(user.userName);
                        for(int i=0;i<uids.size();i++) {
                            if (user.uid.equals(uids.get(i))) {
                                resultUsers.add(user);
                            }
                        }
                    }
                    mOnGetAllUsersListener.onGetContactsUsersSuccess(resultUsers);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    ///mOnGetOneUserListener.onGetOneUserFailure(databaseError.getMessage());
                }
            });
        }
}
