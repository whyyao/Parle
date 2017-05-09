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

        void onGetChatUsersSuccess(List<User> users);

        void onGetChatUsersFailure(String message);
    }

    interface Presenter {
        void getAllUsers();

    }

    interface Interactor {
        void getAllUsersFromFirebase();

    }

    interface OnGetAllUsersListener {
        void onGetAllUsersSuccess(List<User> users);

        void onGetAllUsersFailure(String message);
    }

    interface OnGetChatUsersListener {
        void onGetChatUsersSuccess(List<User> users);

        void onGetChatUsersFailure(String message);
    }
}