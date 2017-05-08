package cs48.project.com.parl.core.conversation;

import android.content.Context;

import java.util.List;

import cs48.project.com.parl.models.Conversation;

/**
 * Created by yaoyuan on 5/6/17.
 */

public class ConversationPresenter implements ConversationContract.Presenter, ConversationContract.OnSendConversationListener,
        ConversationContract.OnGetConversationListener{

    private ConversationContract.View mView;
    private ConversationInteractor mConversationInteractor;

    public ConversationPresenter(ConversationContract.View view) {
        this.mView = view;
        mConversationInteractor = new ConversationInteractor(this, this);
    }

    @Override
    public void sendConversation(Context context, Conversation Conversation, String receiverFirebaseToken) {
        mConversationInteractor.sendConversationToFirebaseUser(context, Conversation, receiverFirebaseToken);
    }

    @Override
    public void getConversation() {
        //Log.e("go","here");
        mConversationInteractor.getConversationFromFirebaseUser();
    }

    @Override
    public void onSendConversationSuccess() {
        mView.onSendConversationSuccess();
    }

    @Override
    public void onSendConversationFailure(String message) {
        mView.onSendConversationFailure(message);
    }

    @Override
    public void onGetConversationSuccess(List<Conversation> conversations) {
        mView.onGetConversationSuccess(conversations);
    }

    @Override
    public void onGetConversationFailure(String message) {
        mView.onGetConversationFailure(message);
    }
}
