package cs48.project.com.parl.core.conversation.delete;

/**
 * Created by yaoyuan on 5/30/17.
 */

public class DeleteConversationPresenter implements DeleteConversationContract.OnDeleteConversationListener, DeleteConversationContract.Presenter{

        private DeleteConversationContract.View mView;
        private DeleteConversationInteractor mDeleteConversationInteractor;

         public DeleteConversationPresenter(DeleteConversationContract.View view) {
            this.mView = view;
            mDeleteConversationInteractor = new DeleteConversationInteractor(this);
        }

        @Override
        public void deleteConversation(String uid) {
            mDeleteConversationInteractor.deleteConversationFromFirebaseUser(uid);
        }

        @Override
        public void onDeleteConversationSuccess() {
            mView.onDeleteConversationSuccess();
        }

        @Override
        public void onDeleteConversationFailure(String message) {
            mView.onDeleteConversationFailure(message);
        }


}
