package cs48.project.com.parl.core.users.getOne;

import cs48.project.com.parl.models.User;

/**
 * Created by yaoyuan on 5/9/17.
 */

public class GetOneUserPresenter implements GetOneUserContract.Presenter, GetOneUserContract.OnGetOneUserListener {
    private GetOneUserContract.View mView;
    private GetOneUserInteractor mGetOneUserInteractor;

    public GetOneUserPresenter(GetOneUserContract.View view) {
        this.mView = view;
        mGetOneUserInteractor = new GetOneUserInteractor(this);
    }

    @Override
    public void getOneUserWithEmail(String email) {
        mGetOneUserInteractor.getOneUserFromFirebaseWithEmail(email);
    }


    @Override
    public void onGetOneUserSuccess(User user) {
        mView.onGetOneUserSuccess(user);
    }

    @Override
    public void onGetOneUserFailure(String message) {
        mView.onGetOneUserFailure(message);
    }
}
