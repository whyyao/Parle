package cs48.project.com.parl.core.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cs48.project.com.parl.utils.Constants;
import cs48.project.com.parl.utils.SharedPrefUtil;

import static android.content.ContentValues.TAG;
import android.widget.Toast;
import android.content.Context;

/**
 * Created by yaoyuan on 4/24/17.
 */

public class LoginInteractor implements LoginContract.Interactor {
    private LoginContract.OnLoginListener mOnLoginListener;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String mUid;
    private ProgressDialog mProgressDialog;

    public LoginInteractor(LoginContract.OnLoginListener onLoginListener) {
        this.mOnLoginListener = onLoginListener;
    }


    @Override
    public void performFirebaseLogin(final Activity activity, final String email, String password) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "performFirebaseLogin:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            mOnLoginListener.onSuccess(task.getResult().toString());
                            updateFirebaseToken(task.getResult().getUser().getUid(),
                                    new SharedPrefUtil(activity.getApplicationContext()).getString(Constants.ARG_FIREBASE_TOKEN, null));
                            mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            databaseReference.child("users").child(mUid).child("loggedIn").setValue(true);
                        } else {
                            mOnLoginListener.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }

    private void updateFirebaseToken(String uid, String token) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_FIREBASE_TOKEN)
                .setValue(token);
    }
}
