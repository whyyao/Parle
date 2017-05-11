package cs48.project.com.parl.core.users.getOne;

import cs48.project.com.parl.models.User;

/**
 * Created by yaoyuan on 5/9/17.
 */

public interface GetOneUserContract {
    interface View {
        void onGetOneUserSuccess(User users);

        void onGetOneUserFailure(String message);
    }

    interface Presenter {
        void getOneUser(String target);

    }

    interface Interactor {
        void getOneUserFromFirebase(String target);

    }

    interface OnGetOneUserListener {
        void onGetOneUserSuccess(User users);

        void onGetOneUserFailure(String message);
    }
}
