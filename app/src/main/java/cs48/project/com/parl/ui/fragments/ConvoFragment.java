package cs48.project.com.parl.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.conversation.ConversationContract;
import cs48.project.com.parl.core.conversation.ConversationPresenter;
import cs48.project.com.parl.core.contacts.add.AddContactContract;
import cs48.project.com.parl.core.contacts.add.AddContactPresenter;
import cs48.project.com.parl.core.users.getall.GetUsersContract;
import cs48.project.com.parl.core.users.getall.GetUsersPresenter;
import cs48.project.com.parl.models.Conversation;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.ui.activities.ChatActivity;
import cs48.project.com.parl.ui.adapters.ConvoListingRecyclerAdapter;
import cs48.project.com.parl.utils.ItemClickSupport;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConvoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConvoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConvoFragment extends Fragment implements ConversationContract.View, GetUsersContract.View, ItemClickSupport.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{
    public static final String ARG_TYPE = "type";
    public static final String TYPE_CHATS = "type_chats";
    public static final String TYPE_ALL = "type_all";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewAllUserListing;

    private ConvoListingRecyclerAdapter mUserListingRecyclerAdapter;

    private GetUsersPresenter mGetUsersPresenter;

    private ConversationPresenter mConversationPresenter;

    public static ConvoFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        ConvoFragment fragment = new ConvoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ConvoFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_convo, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerViewAllUserListing = (RecyclerView) view.findViewById(R.id.recycler_view_all_user_listing);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mConversationPresenter = new ConversationPresenter(this);
        mGetUsersPresenter = new GetUsersPresenter(this);

        getConversations();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        ItemClickSupport.addTo(mRecyclerViewAllUserListing)
                .setOnItemClickListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onRefresh() {
        getConversations();
    }

//    private void getUsers() {
//        if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_CHATS)) {
//
//        } else if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_ALL)) {
//            mGetUsersPresenter.getAllUsers();
//        }
//    }
    private void getConversations(){
        Log.e("1","trying to get conversations");
        mConversationPresenter.getConversation();
    }

    private String FirebaseReceiverUid;
    private String FirebaseEmail;
    private String FirebaseFirebaseToken;
    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        Conversation conversation = mUserListingRecyclerAdapter.getConversation(position);
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        if(conversation.receiverUid == currentUserUid){
            FirebaseReceiverUid = conversation.senderUid;
        }
        else{
            FirebaseReceiverUid = conversation.receiverUid;
        }

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseReceiverUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User receiver = dataSnapshot.getValue(User.class);
                FirebaseEmail = receiver.email;
                FirebaseFirebaseToken = receiver.firebaseToken;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ChatActivity.startActivity(getActivity(),FirebaseEmail,FirebaseReceiverUid,FirebaseFirebaseToken);
    }

    @Override
    public void onGetAllUsersSuccess(List<User> users) {

//        User testUser = users.get(0);
//        Log.e("testUseid", testUser.uid);

//        Log.e("skipped","presenter");
    }

    @Override
    public void onGetAllUsersFailure(String message) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetChatUsersSuccess(List<User> users) {
    }

    @Override
    public void onGetChatUsersFailure(String message) {

    }

    public void onSendConversationSuccess(){

        }

    public void onSendConversationFailure(String message){

    }

    public void onGetConversationSuccess(List<Conversation> conversations){
        Log.e("1","getConversation success");
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        //Log.e("output",conversations.get(0).lastMessage);
        mUserListingRecyclerAdapter = new ConvoListingRecyclerAdapter(conversations);
        mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
        mUserListingRecyclerAdapter.notifyDataSetChanged();
    }

    public void onGetConversationFailure(String message){

    }
}
