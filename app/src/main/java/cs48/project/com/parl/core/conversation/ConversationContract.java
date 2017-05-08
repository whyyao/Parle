package cs48.project.com.parl.core.conversation;

import android.content.Context;

import java.util.List;

import cs48.project.com.parl.models.Conversation;

/**
 * Created by yaoyuan on 5/6/17.
 */

public interface ConversationContract {
    interface View {
        void onSendConversationSuccess();

        void onSendConversationFailure(String message);

        void onGetConversationSuccess(List<Conversation> conversations);

        void onGetConversationFailure(String message);
    }

    interface Presenter {
        void getConversation();
        void sendConversation(Context context, Conversation conversation, String receiverFirebaseToken);
    }

    interface Interactor {
        void getConversationFromFirebaseUser();
        void sendConversationToFirebaseUser(Context context, Conversation conversation, String receiverFirebaseToken);
    }

    interface OnGetConversationListener {
        void onGetConversationSuccess(List<Conversation> conversations);

        void onGetConversationFailure(String message);
    }

    interface OnSendConversationListener{
        void onSendConversationSuccess();

        void onSendConversationFailure(String message);
    }
}
