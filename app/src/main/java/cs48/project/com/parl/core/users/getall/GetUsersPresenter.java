package cs48.project.com.parl.core.users.getall;

import java.util.ArrayList;
import java.util.List;

import cs48.project.com.parl.core.contacts.getAll.GetContactsContract;
import cs48.project.com.parl.core.contacts.getAll.GetContactsPresenter;
import cs48.project.com.parl.models.User;

/**
 * Created by yaoyuan on 4/24/17.
 */


public class GetUsersPresenter implements GetUsersContract.Presenter, GetUsersContract.OnGetAllUsersListener, GetContactsContract.View  {
    private GetUsersContract.View mView;
    private GetUsersInteractor mGetUsersInteractor;
    private GetContactsPresenter mContactsInteractor;
    private List<String> contacts = new ArrayList<>();

    public GetUsersPresenter(GetUsersContract.View view) {
        this.mView = view;
        mGetUsersInteractor = new GetUsersInteractor(this);
        mContactsInteractor = new GetContactsPresenter(this);


    }


    @Override
    public void getAllUsers() {
        mContactsInteractor.getContactsUsers();
    }


    @Override
    public void onGetAllUsersSuccess(List<User> users) {
        mView.onGetAllUsersSuccess(users);
    }

    @Override
    public void onGetAllUsersFailure(String message) {
        mView.onGetAllUsersFailure(message);
    }

    @Override
    public void onGetContactsUsersSuccess(List<User> users){

        for(User user: users) {
            contacts.add(user.uid.toString());
        }
        mGetUsersInteractor.getAllUsersFromFirebase(contacts);
    }

    @Override
    public void onGetContactsUsersFailure(String message){
    }
}
