package cs48.project.com.parl.core.logout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by yaoyuan on 4/24/17.
 */

public class LogoutInteractor implements LogoutContract.Interactor {
    private LogoutContract.OnLogoutListener mOnLogoutListener;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public LogoutInteractor(LogoutContract.OnLogoutListener onLogoutListener) {
        mOnLogoutListener = onLogoutListener;
    }

    @Override
    public void performFirebaseLogout() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference.child("users").child(mUid).child("loggedIn").setValue(false);
            FirebaseAuth.getInstance().signOut();
            mOnLogoutListener.onSuccess("Successfully logged out!");
        } else {
            mOnLogoutListener.onFailure("No user logged in yet!");
        }
    }
}
