package cs48.project.com.parl.core.users.getSearch;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cs48.project.com.parl.models.User;
import cs48.project.com.parl.utils.Constants;

/**
 * Created by jakebliss on 5/27/17.
 */

public class GetSearchUsersInteractor implements GetSearchUsersContract.Interactor{
    private static final String TAG = "GetSearchUsersInteractor";

    private GetSearchUsersContract.OnGetSearchUsersListener mOnGetSearchUsersListener;

    public GetSearchUsersInteractor(GetSearchUsersContract.OnGetSearchUsersListener onGetSearchUsersListener) {
        this.mOnGetSearchUsersListener = onGetSearchUsersListener;
    }


    @Override
    public void getSearchUsersFromFirebase(List<String> searchUsers) {
        final List<User> users = new ArrayList<>();
        for(String user: searchUsers)
        {
            FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot data = dataSnapshot;
                    User user = data.getValue(User.class);
                    if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        users.add(user);
                    }

                    mOnGetSearchUsersListener.onGetSearchUsersSuccess(users);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mOnGetSearchUsersListener.onGetSearchUsersFailure(databaseError.getMessage());
                }
            });
        }
    }
}
