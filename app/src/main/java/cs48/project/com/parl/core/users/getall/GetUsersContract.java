package cs48.project.com.parl.core.users.getall;

import java.util.List;

import cs48.project.com.parl.models.User;

/**
 * Created by yaoyuan on 4/24/17.
 */

public interface GetUsersContract {
    interface View {
        void onGetAllUsersSuccess(List<User> users);

        void onGetAllUsersFailure(String message);

    }

    interface Presenter {
        void getAllUsers();

    }

    interface Interactor {
        void getAllUsersFromFirebase(List<String> contacts);

    }

    interface OnGetAllUsersListener {
        void onGetAllUsersSuccess(List<User> users);

        void onGetAllUsersFailure(String message);
    }

}