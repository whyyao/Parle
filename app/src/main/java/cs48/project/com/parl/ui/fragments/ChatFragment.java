package cs48.project.com.parl.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cs48.project.com.parl.Manifest;
import cs48.project.com.parl.R;
import cs48.project.com.parl.core.chat.ChatContract;
import cs48.project.com.parl.core.chat.ChatPresenter;
import cs48.project.com.parl.core.conversation.ConversationContract;
import cs48.project.com.parl.core.conversation.ConversationPresenter;
import cs48.project.com.parl.core.translation.Translator;
import cs48.project.com.parl.core.users.getOne.GetMyUserName;
import cs48.project.com.parl.events.PushNotificationEvent;
import cs48.project.com.parl.models.Chat;
import cs48.project.com.parl.models.Conversation;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.ui.adapters.ChatRecyclerAdapter;
import cs48.project.com.parl.utils.Constants;

import static android.app.Activity.RESULT_OK;

/*
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;*/

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment implements ChatContract.View, ConversationContract.View {
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;
    private ProgressDialog mProgressDialog;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private ChatRecyclerAdapter mChatRecyclerAdapter;
    private static final int RC_PHOTO_PICKER = 2;

    private ChatPresenter mChatPresenter;
    private ConversationPresenter mConversationPresenter;
    GetMyUserName myUserName;
    private Button mSendButton;
    private ImageButton mPhotoPickerButton;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mPhotoStorageReference;

    public static ChatFragment newInstance(String receiver,
                                           String receiverUid,
                                           String firebaseToken,
                                           String language) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_RECEIVER_USERNAME, receiver);
        args.putString(Constants.ARG_RECEIVER_UID, receiverUid);
        args.putString(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        args.putString(Constants.ARG_RECEIVER_LANGUAGE, language);
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
        mSendButton = (Button) view.findViewById(R.id.sendButton);
        mPhotoPickerButton = (ImageButton) view.findViewById(R.id.photoPickerButton);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mFirebaseStorage = FirebaseStorage.getInstance();
        mPhotoStorageReference = mFirebaseStorage.getReference().child("chat_photos");
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);


        mChatPresenter = new ChatPresenter(this);
        mConversationPresenter = new ConversationPresenter(this);

        mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getArguments().getString(Constants.ARG_RECEIVER_UID));
        myUserName = new GetMyUserName();
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // TODO: Fire an intent to show an image picker
            }
        });

        // Enable Send button when there's text to send
        mETxtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mETxtMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();

            }
        });

        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
        }

//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (actionId == EditorInfo.IME_ACTION_SEND) {
//            sendMessage();
//            return true;
//        }
//        return false;
//    }
    private String senderLang;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data ){
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            StorageReference photoRef = mPhotoStorageReference.child(selectedImageUri.getLastPathSegment());
            photoRef.putFile(selectedImageUri).addOnSuccessListener
                    (this.getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    String receiver = getArguments().getString(Constants.ARG_RECEIVER_USERNAME);
                                    String receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);
                                    String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                    String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    String receiverFirebaseToken = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);
                                    Chat chat = new Chat(myUserName.userName,
                                            receiver,
                                            senderUid,
                                            receiverUid,
                                            Constants.ARG_PHOTO,
                                            Constants.ARG_PHOTO,
                                            System.currentTimeMillis(),
                                            downloadUrl.toString());
                                    mChatPresenter.sendMessage(getActivity().getApplicationContext(),
                                            chat,
                                            receiverFirebaseToken);
                                    Conversation conversation = new Conversation(senderUid, receiverUid, Constants.ARG_PHOTO, Constants.ARG_PHOTO, System.currentTimeMillis(),
                                            myUserName.userName, receiver);

                                    mConversationPresenter.sendConversation(getActivity().getApplicationContext(), conversation, receiverFirebaseToken);
                                }
                            }
                    );
        }
    }
    //private String myUsername;
    private void sendMessage() {

        String translatedMessage = null;

        String recieverLang = getArguments().getString(Constants.ARG_RECEIVER_LANGUAGE);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constants.ARG_USERS).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                senderLang = currentUser.language;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String message = mETxtMessage.getText().toString();

        if (senderLang != recieverLang) {
            Translator myTranslator = new Translator();
            translatedMessage = myTranslator.startThread(message,senderLang,recieverLang);
        }
        else {
            translatedMessage = message;
        }

        System.out.println(translatedMessage + " This is the translated message");
        String receiver = getArguments().getString(Constants.ARG_RECEIVER_USERNAME);
        String receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);
        String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String receiverFirebaseToken = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);
        Chat chat = new Chat(myUserName.userName,
                receiver,
                senderUid,
                receiverUid,
                message,
                translatedMessage,
                System.currentTimeMillis());
        mChatPresenter.sendMessage(getActivity().getApplicationContext(),
                chat,
                receiverFirebaseToken);
        Conversation conversation = new Conversation(senderUid, receiverUid, message, translatedMessage, System.currentTimeMillis(),
                                                    myUserName.userName, receiver);

        mConversationPresenter.sendConversation(getActivity().getApplicationContext(), conversation, receiverFirebaseToken);
    }

    @Override
    public void onSendMessageSuccess() {
        mETxtMessage.setText("");
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
        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendConversationSuccess(){

    }

    @Override
    public void onSendConversationFailure(String message){

    }

    @Override
    public void onGetConversationSuccess(List<Conversation> conversations){

    }

    @Override
    public void onGetConversationFailure(String message){

    }


    @Subscribe
    public void onPushNotificationEvent(PushNotificationEvent pushNotificationEvent) {
        if (mChatRecyclerAdapter == null || mChatRecyclerAdapter.getItemCount() == 0) {
            mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    pushNotificationEvent.getUid());
        }
    }
}
