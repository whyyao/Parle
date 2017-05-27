package cs48.project.com.parl.core.users.getSearch;

import java.util.List;

import cs48.project.com.parl.models.User;

/**
 * Created by jakebliss on 5/27/17.
 */

public interface GetSearchUsersContract {
    interface View {
        void onGetSearchUsersSuccess(List<User> searchUsers);

        void onGetSearchUsersFailure(String message);

    }

    interface Presenter {
        void getSearchUsers(List<String> searchUsers);

    }

    interface Interactor {
        void getSearchUsersFromFirebase(List<String> searchUsers);

    }

    interface OnGetSearchUsersListener {
        void onGetSearchUsersSuccess(List<User> searchUsers);

        void onGetSearchUsersFailure(String message);
    }
}
