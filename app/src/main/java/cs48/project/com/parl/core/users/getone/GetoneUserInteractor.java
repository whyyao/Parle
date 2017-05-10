//package cs48.project.com.parl.core.users.getOne;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.Iterator;
//
//import cs48.project.com.parl.models.User;
//import cs48.project.com.parl.utils.Constants;
//
//
///**
// * Created by yaoyuan on 5/9/17.
// */
//
//public class GetOneUserInteractor implements GetOneUserContract.Interactor {
//    private static final String TAG = "GetOneUserInteractor";
//
//    private GetOneUserContract.OnGetOneUserListener mOnGetOneUserListener;
//
//    public GetOneUserInteractor(GetOneUserContract.OnGetOneUserListener onGetOneUserListener) {
//        this.mOnGetOneUserListener = onGetOneUserListener;
//    }
//
//
//    @Override
//    public void getOneUserFromFirebaseWithEmail(final String email) {
//        FirebaseDatabase.getInstance().getReference().child(Constants.ARG_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
//                while (dataSnapshots.hasNext()) {
//                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
//                    User user = dataSnapshotChild.getValue(User.class);
//                    if (user.email.equals(email)) {
//                        mOnGetOneUserListener.onGetOneUserSuccess(user);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                mOnGetOneUserListener.onGetOneUserFailure(databaseError.getMessage());
//            }
//        });
//    }
//}
