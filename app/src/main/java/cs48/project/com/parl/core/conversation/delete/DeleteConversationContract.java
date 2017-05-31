package cs48.project.com.parl.core.conversation.delete;

/**
 * Created by yaoyuan on 5/30/17.
 */

public interface DeleteConversationContract {
    interface View {
        void onDeleteConversationSuccess();
        void onDeleteConversationFailure(String message);

    }

    interface Presenter {
        void deleteConversation(String uid);

    }

    interface Interactor {
        void deleteConversationFromFirebaseUser(String uid);
    }

    interface OnDeleteConversationListener {
        void onDeleteConversationSuccess();

        void onDeleteConversationFailure(String message);
    }
}
