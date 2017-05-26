package cs48.project.com.parl.core.users.getall;

import java.util.List;

import cs48.project.com.parl.models.User;

/**
 * Created by jakebliss on 5/25/17.
 */

public interface GetNearbyUsersContract {
    interface View {
        void onGetNearbyUsersSuccess(List<User> nearbyUsers);

        void onGetNearbyUsersFailure(String message);

//        void onGetChatUsersSuccess(List<User> users);
//
//        void onGetChatUsersFailure(String message);
    }

    interface Presenter {
        void getNearbyUsers(List<String> nearbyUsers);

    }

    interface Interactor {
        void getNearbyUsersFromFirebase(List<String> nearbyUsers);

    }

    interface OnGetNearbyUsersListener {
        void onGetNearbyUsersSuccess(List<User> nearbyUsers);

        void onGetNearbyUsersFailure(String message);
    }

//    interface OnGetChatUsersListener {
//        void onGetChatUsersSuccess(List<User> users);
//
//        void onGetChatUsersFailure(String message);
//    }
}