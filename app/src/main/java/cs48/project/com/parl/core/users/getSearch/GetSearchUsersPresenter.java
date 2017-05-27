package cs48.project.com.parl.core.users.getSearch;

import com.google.api.services.bigquery.Bigquery;

import java.util.List;

import cs48.project.com.parl.core.users.getSearch.GetSearchUsersInteractor;
import cs48.project.com.parl.core.users.getall.GetNearbyUsersContract;
import cs48.project.com.parl.models.User;

/**
 * Created by jakebliss on 5/27/17.
 */

public class GetSearchUsersPresenter implements GetSearchUsersContract.Presenter, GetSearchUsersContract.OnGetSearchUsersListener {
    private GetSearchUsersContract.View mView;
    private GetSearchUsersInteractor mGetSearchUsersInteractor;

    public GetSearchUsersPresenter(GetSearchUsersContract.View view) {
        this.mView = view;
        mGetSearchUsersInteractor = new GetSearchUsersInteractor(this);
    }

    @Override
    public void getSearchUsers(List<String> searchUsers) {
        mGetSearchUsersInteractor.getSearchUsersFromFirebase(searchUsers);
    }


    @Override
    public void onGetSearchUsersSuccess(List<User> users) {
        mView.onGetSearchUsersSuccess(users);
    }

    @Override
    public void onGetSearchUsersFailure(String message) {
        mView.onGetSearchUsersFailure(message);
    }
}
