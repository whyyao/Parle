package cs48.project.com.parl.core.contacts.getAll;

import java.util.List;

import cs48.project.com.parl.models.User;

/**
 * Created by jakebliss on 5/7/17.
 */

public class GetContactsPresenter implements GetContactsContract.Presenter, GetContactsContract.OnGetContactsUsersListener{
    private GetContactsContract.View mView;
    private GetContactsInteractor mGetContractsInteractor;

    public GetContactsPresenter(GetContactsContract.View view) {
        this.mView = view;
        mGetContractsInteractor = new GetContactsInteractor(this);
    }

    @Override
    public void getContactsUsers() {
        mGetContractsInteractor.getContactsUsersFromFirebase();
    }


    @Override
    public void onGetContactsUsersSuccess(List<User> uids) {
        mView.onGetContactsUsersSuccess(uids);
    }

    @Override
    public void onGetContactsUsersFailure(String message) {
        mView.onGetContactsUsersFailure(message);
    }
}
