package cs48.project.com.parl.core.users.getall;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cs48.project.com.parl.core.contacts.getAll.GetContactsContract;
import cs48.project.com.parl.core.contacts.getAll.GetContactsInteractor;
import cs48.project.com.parl.core.contacts.getAll.GetContactsPresenter;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.ui.adapters.ContactListingRecyclerAdapter;
import cs48.project.com.parl.utils.Constants;

/**
 * Created by yaoyuan on 4/24/17.
 */

public class GetUsersInteractor implements GetUsersContract.Interactor{
    private static final String TAG = "GetOneUserInteractor";

    private GetUsersContract.OnGetAllUsersListener mOnGetAllUsersListener;


    public GetUsersInteractor(GetUsersContract.OnGetAllUsersListener onGetAllUsersListener) {
        this.mOnGetAllUsersListener = onGetAllUsersListener;

    }


    @Override
    public void getAllUsersFromFirebase(final List<String> contacts) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                List<User> users = new ArrayList<>();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    User user = dataSnapshotChild.getValue(User.class);
                    if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        //check to see if its a contact already
                        System.out.println(contacts);
                        if(!contacts.contains(user.uid.toString())) {
                            System.out.println(user);
                            users.add(user);
                        }
                    }
                }
                mOnGetAllUsersListener.onGetAllUsersSuccess(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnGetAllUsersListener.onGetAllUsersFailure(databaseError.getMessage());
            }
        });
    }


}

