package cs48.project.com.parl.core.contacts.add;

import android.content.Context;

import cs48.project.com.parl.models.User;

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

    public void addContact(Context context, User user) {
        mAddUserInteractor.addContactToDatabase(context, user);
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
