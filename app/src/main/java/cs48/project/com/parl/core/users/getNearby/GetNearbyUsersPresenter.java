package cs48.project.com.parl.core.users.getNearby;

import java.util.ArrayList;
import java.util.List;

import cs48.project.com.parl.core.contacts.getAll.GetContactsContract;
import cs48.project.com.parl.core.contacts.getAll.GetContactsPresenter;
import cs48.project.com.parl.core.users.getall.GetNearbyUsersContract;
import cs48.project.com.parl.core.users.getall.GetUsersContract;
import cs48.project.com.parl.core.users.getall.GetUsersInteractor;
import cs48.project.com.parl.models.User;

/**
 * Created by jakebliss on 5/25/17.
 */

public class GetNearbyUsersPresenter implements GetNearbyUsersContract.Presenter, GetNearbyUsersContract.OnGetNearbyUsersListener,GetContactsContract.View {
    private GetNearbyUsersContract.View mView;
    private GetNearbyUsersInteractor mGetNearbyUsersInteractor;
    private GetContactsPresenter mContactsInteractor;
    private List<String> contacts = new ArrayList<>();
    private List<String> nearby;

    public GetNearbyUsersPresenter(GetNearbyUsersContract.View view) {
        this.mView = view;
        mGetNearbyUsersInteractor = new GetNearbyUsersInteractor(this);
        mContactsInteractor = new GetContactsPresenter(this);
    }

    @Override
    public void getNearbyUsers(List<String> nearbyUsers) {
        nearby = nearbyUsers;
        mContactsInteractor.getContactsUsers();

    }


    @Override
    public void onGetNearbyUsersSuccess(List<User> users) {

        mView.onGetNearbyUsersSuccess(users);
    }

    @Override
    public void onGetNearbyUsersFailure(String message) {
        mView.onGetNearbyUsersFailure(message);
    }

    @Override
    public void onGetContactsUsersSuccess(List<User> users){

        for(User user: users) {
            contacts.add(user.uid.toString());
        }
        mGetNearbyUsersInteractor.getNearbyUsersFromFirebase(nearby, contacts);
    }

    @Override
    public void onGetContactsUsersFailure(String message){
    }
}
