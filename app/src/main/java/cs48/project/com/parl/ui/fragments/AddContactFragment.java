package cs48.project.com.parl.ui.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.contacts.add.AddContactContract;
import cs48.project.com.parl.core.contacts.add.AddContactPresenter;
import cs48.project.com.parl.core.users.getOne.GetOneUserContract;
import cs48.project.com.parl.core.users.getOne.GetOneUserPresenter;
import cs48.project.com.parl.core.users.getNearby.GetNearbyUsersPresenter;
import cs48.project.com.parl.core.users.getall.GetNearbyUsersContract;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.ui.activities.ChatActivity;
import cs48.project.com.parl.ui.adapters.ContactListingRecyclerAdapter;
import cs48.project.com.parl.ui.adapters.NearbyUsersListingRecyclerAdapter;
import cs48.project.com.parl.utils.ItemClickSupport;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.Connections;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Messages;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakebliss on 5/8/17.
 */

public class AddContactFragment extends Fragment implements AddContactContract.View, GetNearbyUsersContract.View,
        View.OnClickListener, ItemClickSupport.OnItemClickListener, GetOneUserContract.View,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SwipeRefreshLayout.OnRefreshListener{
    public static final String ARG_TYPE = "type";
    public static final String TYPE_CHATS = "type_chats";
    public static final String TYPE_ALL = "type_all";
    private AddContactPresenter mAddContactPresenter;
    private EditText mETxtUsername;
    private Button mBtnSearch;
    private ProgressDialog mProgressDialog;
    GoogleApiClient mGoogleApiClient;
    private Message mPubMessage;
    private MessageListener mMessageListener;
    private List<String> mNearbyDevicesList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerViewAllUserListing;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GetNearbyUsersPresenter mGetNearbyUsersPresenter;
    private NearbyUsersListingRecyclerAdapter mUserListingRecyclerAdapter;

    private GetOneUserPresenter mGetOneUserPresenter;

    public static AddContactFragment newInstance() {
        Bundle args = new Bundle();
        AddContactFragment fragment = new AddContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        String test = "QQ0B62oIS3Vt73d37xNWgB33iQa2";
        mNearbyDevicesList.add(test);
        mMessageListener = new MessageListener() {

            @Override
            public void onFound(Message message) {
                mNearbyDevicesList.add(message.getContent().toString());
            }

            @Override
            public void onLost(Message message) {
                mNearbyDevicesList.remove(message.getContent().toString());
            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(final ConnectionResult results)
    {

    }

    @Override
    public void onConnectionSuspended(int results)
    {

    }

    @Override
    public void onConnected(Bundle connectionHint){
        //public the current users uID
        String curUser = mAuth.getInstance().getCurrentUser().toString();
        byte [] newMessage = curUser.getBytes();
        mPubMessage = new Message(newMessage);
        Nearby.Messages.publish(mGoogleApiClient, mPubMessage);

        //subscribe to messages
        Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }


    @Override
    public void onStop() {
        Nearby.Messages.unpublish(mGoogleApiClient, mPubMessage);
        Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener);
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_add_contact, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mETxtUsername = (EditText) view.findViewById(R.id.edit_text_new_contact_username);
        mBtnSearch = (Button) view.findViewById(R.id.button_search);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mRecyclerViewAllUserListing = (RecyclerView) view.findViewById(R.id.recycler_view_nearby);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mGetNearbyUsersPresenter = new GetNearbyUsersPresenter(this);
        mAddContactPresenter = new AddContactPresenter(this);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        mBtnSearch.setOnClickListener(this);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(getActivity(), this)
                .build();

        getUsers();
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if(mNearbyDevicesList != null)
                    {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                }
            });

        mSwipeRefreshLayout.setOnRefreshListener(this);


        ItemClickSupport.addTo(mRecyclerViewAllUserListing)
                .setOnItemClickListener(this);

        mGetOneUserPresenter = new GetOneUserPresenter(this);
    }

    @Override
    public void onRefresh() {
        getUsers();
    }

    private void getUsers() {
        mGetNearbyUsersPresenter.getNearbyUsers(mNearbyDevicesList);
//        if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_CHATS)) {
//
//        }
//        else if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_ALL)) {
//            mGetNearbyUsersPresenter.getNearbyUsers(mNearbyDevicesList);
//        }
    }

    @Override
    public void onGetNearbyUsersSuccess(List<User> users) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mUserListingRecyclerAdapter = new NearbyUsersListingRecyclerAdapter(users);
        mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
        mUserListingRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetNearbyUsersFailure(String message) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.button_search:
                System.out.println("clicked");

                onAddContact(view);
                //onSearch(view);
                break;
        }
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        String newContactUid = mUserListingRecyclerAdapter.getUser(position).uid;
        onAddNearbyContact(newContactUid);
    }

    private void onAddContact(View view){
        String target = mETxtUsername.getText().toString();
        mGetOneUserPresenter.getOneUser(target);
    }

    private void onAddNearbyContact(String newContactUid){
        mGetOneUserPresenter.getOneUser(newContactUid);
    }

    @Override
    public void onAddContactSuccess(String message) {
//        mProgressDialog.dismiss();
//        ContactListingActivity.startActivity(getActivity(),
//                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Toast.makeText(getActivity(), "Added Contact", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddContactFailure(String message) {
    }

    @Override
    public void onGetOneUserSuccess(User user){
        Toast.makeText(getActivity(), user.userName, Toast.LENGTH_SHORT).show();
        mAddContactPresenter.addContact(getActivity(),user);

    }
    @Override
    public void onGetOneUserFailure(String message){
        Toast.makeText(getActivity(), "search failed", Toast.LENGTH_SHORT).show();

    }
//    public void onSearch(View view){
//        String searchUsername = mETxtUsername.getText().toString();
//
//        mProgressDialog.show();
//    }
}
