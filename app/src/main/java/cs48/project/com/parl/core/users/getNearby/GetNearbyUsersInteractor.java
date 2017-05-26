package cs48.project.com.parl.core.users.getNearby;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cs48.project.com.parl.core.users.getall.GetNearbyUsersContract;
import cs48.project.com.parl.core.users.getall.GetUsersContract;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.utils.Constants;

/**
 * Created by jakebliss on 5/25/17.
 */

public class GetNearbyUsersInteractor implements GetNearbyUsersContract.Interactor{
    private static final String TAG = "GetNearbyUsersInteractor";

    private GetNearbyUsersContract.OnGetNearbyUsersListener mOnGetNearbyUsersListener;

    public GetNearbyUsersInteractor(GetNearbyUsersContract.OnGetNearbyUsersListener onGetNearbyUsersListener) {
        this.mOnGetNearbyUsersListener = onGetNearbyUsersListener;
    }


    @Override
    public void getNearbyUsersFromFirebase(List<String> nearbyUsers) {
        final List<User> users = new ArrayList<>();
        for(String user: nearbyUsers)
        {
            FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot data = dataSnapshot;
                    User user = data.getValue(User.class);
                    if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        users.add(user);
                    }

                mOnGetNearbyUsersListener.onGetNearbyUsersSuccess(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnGetNearbyUsersListener.onGetNearbyUsersFailure(databaseError.getMessage());
            }
        });
        }
    }
}
