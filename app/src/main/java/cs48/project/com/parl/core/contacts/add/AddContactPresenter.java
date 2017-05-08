package cs48.project.com.parl.core.contacts.add;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

import cs48.project.com.parl.core.users.add.AddUserContract;
import cs48.project.com.parl.core.users.add.AddUserInteractor;

/**
 * Created by jakebliss on 5/7/17.
 */

public class AddContactPresenter implements AddContactContract, AddContactContract.OnContactDatabaseListener{
    private AddContactContract.View mView;
    private AddContactInteractor mAddUserInteractor;

    public AddContactPresenter(AddContactContract.View view) {
        this.mView = view;
        mAddUserInteractor = new AddContactInteractor(this);
    }

    public void addContact(Context context, String newContactUid) {
        mAddUserInteractor.addContactToDatabase(context, newContactUid);
    }

    @Override
    public void onSuccess(String message) {
        mView.onAddContactSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        mView.onAddContactFailure(message);
    }
}
