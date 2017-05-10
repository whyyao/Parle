package cs48.project.com.parl.core.users.getone;

import cs48.project.com.parl.models.User;

/**
 * Created by yaoyuan on 5/9/17.
 */

public interface GetoneUserContract {
    interface View {
        void onGetOneUserSuccess(User users);

        void onGetOneUserFailure(String message);

        void onGetChatUsersSuccess(User users);

        void onGetChatUsersFailure(String message);
    }

    interface Presenter {
        void GetOneUser();

        void getChatUsers();
    }

    interface Interactor {
        void getOneUserFromFirebase();

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
