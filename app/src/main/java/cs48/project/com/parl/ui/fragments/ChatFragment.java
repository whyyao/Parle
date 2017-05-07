package cs48.project.com.parl.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.LanguageListOption;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

/*
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;*/

import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.chat.ChatContract;
import cs48.project.com.parl.core.chat.ChatPresenter;
import cs48.project.com.parl.events.PushNotificationEvent;
import cs48.project.com.parl.models.Chat;
import cs48.project.com.parl.ui.adapters.ChatRecyclerAdapter;
import cs48.project.com.parl.utils.Constants;

public class ChatFragment extends Fragment implements ChatContract.View, TextView.OnEditorActionListener {
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;

    private ProgressDialog mProgressDialog;

    private ChatRecyclerAdapter mChatRecyclerAdapter;

    private ChatPresenter mChatPresenter;

    public static ChatFragment newInstance(String receiver,
                                           String receiverUid,
                                           String firebaseToken) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_RECEIVER, receiver);
        args.putString(Constants.ARG_RECEIVER_UID, receiverUid);
        args.putString(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_chat, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mRecyclerViewChat = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        mETxtMessage = (EditText) view.findViewById(R.id.edit_text_message);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mETxtMessage.setOnEditorActionListener(this);

        mChatPresenter = new ChatPresenter(this);
        mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getArguments().getString(Constants.ARG_RECEIVER_UID));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            sendMessage();
            return true;
        }
        return false;
    }

    private void sendMessage() {
        String userLang = "en";
        String message = mETxtMessage.getText().toString();

        Translator myTranslator = new Translator();
        String translatedMessage = myTranslator.startThread(message,userLang);

        System.out.println(translatedMessage + "This is the translated message");
        String receiver = getArguments().getString(Constants.ARG_RECEIVER);
        String receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);
        String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String receiverFirebaseToken = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);
        Chat chat = new Chat(sender,
                receiver,
                senderUid,
                receiverUid,
                message,
                translatedMessage,
                System.currentTimeMillis());
        mChatPresenter.sendMessage(getActivity().getApplicationContext(),
                chat,
                receiverFirebaseToken);
    }

    @Override
    public void onSendMessageSuccess() {
        mETxtMessage.setText("");
        Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendMessageFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetMessagesSuccess(Chat chat) {
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new ChatRecyclerAdapter(new ArrayList<Chat>());
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }
        mChatRecyclerAdapter.add(chat);
        mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
    }

    @Override
    public void onGetMessagesFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onPushNotificationEvent(PushNotificationEvent pushNotificationEvent) {
        if (mChatRecyclerAdapter == null || mChatRecyclerAdapter.getItemCount() == 0) {
            mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    pushNotificationEvent.getUid());
        }
    }
}
