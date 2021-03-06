package cs48.project.com.parl.core.chat;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cs48.project.com.parl.fcm.FcmNotificationBuilder;
import cs48.project.com.parl.models.Chat;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.utils.Constants;
import cs48.project.com.parl.utils.SharedPrefUtil;

/**
 * Created by yaoyuan on 4/22/17.
 */
//123
public class ChatInteractor implements ChatContract.Interactor {
    private static final String TAG = "ChatInteractor";

    private ChatContract.OnSendMessageListener mOnSendMessageListener;
    private ChatContract.OnGetMessagesListener mOnGetMessagesListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    String username;
    boolean isLoggedIn;
    private String mUid;

    //CONSTRUCTORS
    public ChatInteractor(ChatContract.OnSendMessageListener onSendMessageListener) {
        this.mOnSendMessageListener = onSendMessageListener;
    }

    public ChatInteractor(ChatContract.OnGetMessagesListener onGetMessagesListener) {
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    public ChatInteractor(ChatContract.OnSendMessageListener onSendMessageListener,
                          ChatContract.OnGetMessagesListener onGetMessagesListener) {
        this.mOnSendMessageListener = onSendMessageListener;
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    //MEMBER FUNCTIONS
    @Override
    public void sendMessageToFirebaseUser(final Context context, final Chat chat, final String receiverFirebaseToken) {
        final String room_type_1 = chat.senderUid + "_" + chat.receiverUid;
        final String room_type_2 = chat.receiverUid + "_" + chat.senderUid;
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

//        databaseReference.child("users").child(chat.senderUid).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        databaseReference.child(Constants.ARG_USERS).child(chat.receiverUid).child("loggedIn").getRef().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isLoggedIn = dataSnapshot.getValue(boolean.class);
                System.out.println(isLoggedIn);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        databaseReference.child(Constants.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (dataSnapshot.hasChild(room_type_1)) {
 //                   Conversation convo = new Conversation(chat.message, chat.timestamp);
                    Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_1 + " exists");
                    databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(chat.timestamp)).setValue(chat);
 //                   databaseReference.child(Constants.ARG_LAST).child(room_type_1).setValue(convo);
                } else if (dataSnapshot.hasChild(room_type_2)) {
 //                   Conversation convo = new Conversation(chat.message, chat.timestamp);
                    Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_2 + " exists");
                    databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_type_2).child(String.valueOf(chat.timestamp)).setValue(chat);
   //                 databaseReference.child(Constants.ARG_LAST).child(room_type_2).setValue(convo);
                } else { //creates new chatroom if it doesnt exist
  //                  Conversation convo = new Conversation(chat.message, chat.timestamp);
                    Log.e(TAG, "sendMessageToFirebaseUser: success");
                    databaseReference.child(Constants.ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(chat.timestamp)).setValue(chat);
  //                  databaseReference.child(Constants.ARG_LAST).child(room_type_1).setValue(convo);
                    getMessageFromFirebaseUser(chat.senderUid, chat.receiverUid);
                }

                System.out.println(isLoggedIn);
                if(isLoggedIn == true ) {
                    System.out.println("Inside If");
                    // send push notification to the receiver
                    sendPushNotificationToReceiver(username, chat.translatedMessage, chat.senderUid,
                            new SharedPrefUtil(context).getString(Constants.ARG_FIREBASE_TOKEN),
                            //only send push notification if allowed
                            receiverFirebaseToken);
                    mOnSendMessageListener.onSendMessageSuccess();
                }
            }

            @Override
            //checks for any errors if message cant send
            public void onCancelled(DatabaseError databaseError) {
                mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });
    }

    private void sendPushNotificationToReceiver(String username, String message, String uid, String firebaseToken,
                                                String receiverFirebaseToken)
    {

        FcmNotificationBuilder.initialize()
                .title(username)
                .message(message)
                .username(username)
                .uid(uid)
                .firebaseToken(firebaseToken)
                .receiverFirebaseToken(receiverFirebaseToken)
                .send();
    }

    @Override
    //get message from database for the receiver
    public void getMessageFromFirebaseUser(String senderUid, String receiverUid) {
        final String room_type_1 = senderUid + "_" + receiverUid;
        final String room_type_2 = receiverUid + "_" + senderUid;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(Constants.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_1 + " exists");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constants.ARG_CHAT_ROOMS)
                            .child(room_type_1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            Log.e(TAG,chat.message);
                            mOnGetMessagesListener.onGetMessagesSuccess(chat);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_2 + " exists");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constants.ARG_CHAT_ROOMS)
                            .child(room_type_2).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            Log.e(TAG,chat.message);
                            mOnGetMessagesListener.onGetMessagesSuccess(chat);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                } else {
                    Log.e(TAG, "getMessageFromFirebaseUser: no such room available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });
    }
}
