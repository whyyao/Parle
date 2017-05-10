package cs48.project.com.parl.core.users.getOne;

import cs48.project.com.parl.models.User;

/**
 * Created by yaoyuan on 5/9/17.
 */

public interface GetOneUserContract {
    interface View {
        void onGetOneUserSuccess(User users);

        void onGetOneUserFailure(String message);

        void onGetChatUsersSuccess(User users);

        void onGetChatUsersFailure(String message);
    }

    interface Presenter {
        void getOneUserWithEmail(String email);

    }

    interface Interactor {
        void getOneUserFromFirebaseWithEmail(String email);

    }

    interface OnGetOneUserListener {
        void onGetOneUserSuccess(User users);

        void onGetOneUserFailure(String message);
    }

    interface OnGetChatUsersListener {
        void onGetChatUsersSuccess(User users);

        void onGetChatUsersFailure(String message);
    }
}
