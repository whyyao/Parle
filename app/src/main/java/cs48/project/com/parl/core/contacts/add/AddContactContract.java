package cs48.project.com.parl.core.contacts.add;

import android.content.Context;

import cs48.project.com.parl.models.User;

/**
 * Created by jakebliss on 5/7/17.
 */

public interface AddContactContract {
    interface View {
        void onAddContactSuccess(String message);

        void onAddContactFailure(String message);
    }

    interface Presenter {
        void addContact(Context context, User user);
    }

    interface Interactor {
        void addContactToDatabase(Context context, User user);
    }

    interface OnContactDatabaseListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
