package cs48.project.com.parl.core.users.getOne;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import cs48.project.com.parl.models.User;
import cs48.project.com.parl.utils.Constants;

/**
 * Created by yaoyuan on 5/10/17.
 4*/

public class GetMyUserName {
    public String userName;

    public GetMyUserName() {
        final String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    User user = dataSnapshotChild.getValue(User.class);
                    if (Uid.equals(user.uid)) {
                        setUserName(user.userName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //mOnGetAllUsersListener.onGetAllUsersFailure(databaseError.getMessage());
            }
        });
    }
    private void setUserName(String username){
        this.userName = username;
    }
}
