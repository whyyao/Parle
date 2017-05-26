package cs48.project.com.parl.core.users.getNearby;

import java.util.List;

import cs48.project.com.parl.core.users.getall.GetNearbyUsersContract;
import cs48.project.com.parl.core.users.getall.GetUsersContract;
import cs48.project.com.parl.core.users.getall.GetUsersInteractor;
import cs48.project.com.parl.models.User;

/**
 * Created by jakebliss on 5/25/17.
 */

public class GetNearbyUsersPresenter implements GetNearbyUsersContract.Presenter, GetNearbyUsersContract.OnGetNearbyUsersListener {
    private GetNearbyUsersContract.View mView;
    private GetNearbyUsersInteractor mGetNearbyUsersInteractor;

    public GetNearbyUsersPresenter(GetNearbyUsersContract.View view) {
        this.mView = view;
        mGetNearbyUsersInteractor = new GetNearbyUsersInteractor(this);
    }

    @Override
    public void getNearbyUsers(List<String> nearbyUsers) {
        mGetNearbyUsersInteractor.getNearbyUsersFromFirebase(nearbyUsers);
    }


    @Override
    public void onGetNearbyUsersSuccess(List<User> users) {
        mView.onGetNearbyUsersSuccess(users);
    }

    @Override
    public void onGetNearbyUsersFailure(String message) {
        mView.onGetNearbyUsersFailure(message);
    }
}
