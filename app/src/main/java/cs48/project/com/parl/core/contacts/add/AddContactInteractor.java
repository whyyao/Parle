package cs48.project.com.parl.core.contacts.add;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cs48.project.com.parl.R;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.utils.Constants;

/**
 * Created by jakebliss on 5/7/17.
 */

public class AddContactInteractor implements AddContactContract {
    private AddContactContract.OnContactDatabaseListener mOnContactDatabaseListener;

    public AddContactInteractor(AddContactContract.OnContactDatabaseListener onContactDatabaseListener) {
        this.mOnContactDatabaseListener = onContactDatabaseListener;
    }

    public void addContactToDatabase(final Context context, User user) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        String curUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("Adding contact for: ", curUserUid);
        database.child(Constants.ARG_CONTACTS)
                .child(user.uid)
                .child(curUserUid)
                .setValue(curUserUid);
        database.child(Constants.ARG_CONTACTS)
                .child(curUserUid)
                .child(user.uid)
                .setValue(user.uid)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mOnContactDatabaseListener.onSuccess("Contact add successfully!");
                        } else {
                            mOnContactDatabaseListener.onFailure(context.getString(R.string.user_unable_to_add));
                        }
                    }
                });
    }
}
