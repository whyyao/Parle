package cs48.project.com.parl.core.users.getone;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cs48.project.com.parl.core.users.getall.GetUsersContract;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.utils.Constants;

/**
 * Created by yaoyuan on 5/9/17.
 */

public class GetoneUserInteractor implements GetoneUserContract.Interactor {
    private static final String TAG = "GetUsersInteractor";

    private GetUsersContract.OnGetOneUserListener mOnGetOneUserListener;

    public GetUsersInteractor(GetUsersContract.OnGetOneUserListener onGetOneUserListener) {
        this.mOnGetOneUserListener = onGetOneUserListener;
    }


    @Override
    public void getOneUserFromFirebase() {
        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                List<User> users = new ArrayList<>();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    User user = dataSnapshotChild.getValue(User.class);
                    if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        users.add(user);
                    }
                }
                mOnGetOneUserListener.onGetOneUserSuccess(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnGetOneUserListener.onGetOneUserFailure(databaseError.getMessage());
            }
        });
    }
}
