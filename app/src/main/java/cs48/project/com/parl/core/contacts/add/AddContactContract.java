package cs48.project.com.parl.core.contacts.add;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by jakebliss on 5/7/17.
 */

public interface AddContactContract {
    interface View {
        void onAddContactSuccess(String message);

        void onAddContactFailure(String message);
    }

    interface Presenter {
        void addContact(Context context, FirebaseUser firebaseUser);
    }

    interface Interactor {
        void addContactToDatabase(Context context, FirebaseUser firebaseUser);
    }

    interface OnContactDatabaseListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
