package cs48.project.com.parl.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.firebase.auth.FirebaseAuth;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.contacts.add.AddContactContract;
import cs48.project.com.parl.core.contacts.add.AddContactPresenter;
import cs48.project.com.parl.core.contacts.getAll.GetContactsPresenter;
import cs48.project.com.parl.core.users.getNearby.GetNearbyUsersPresenter;
import cs48.project.com.parl.core.users.getOne.GetOneUserContract;
import cs48.project.com.parl.core.users.getOne.GetOneUserPresenter;
import cs48.project.com.parl.core.users.getall.GetNearbyUsersContract;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.models.ChatMessage;
import cs48.project.com.parl.ui.adapters.NearbyUsersListingRecyclerAdapter;
import cs48.project.com.parl.utils.ItemClickSupport;

/**
 * Created by jakebliss on 5/26/17.**
 */

public class NearbyFragment extends Fragment implements AddContactContract.View, GetNearbyUsersContract.View,
        ItemClickSupport.OnItemClickListener, GoogleApiClient.ConnectionCallbacks, GetOneUserContract.View,
        GoogleApiClient.OnConnectionFailedListener, SwipeRefreshLayout.OnRefreshListener{
    public static final String ARG_TYPE = "type";
    public static final String TYPE_CHATS = "type_chats";
    public static final String TYPE_ALL = "type_all";
    private AddContactPresenter mAddContactPresenter;
    private ProgressDialog mProgressDialog;
    GoogleApiClient mGoogleApiClient;
    private Message mPubMessage;
    private FirebaseAuth mAuth;
    private MessageListener mMessageListener;
    private List<String> mNearbyDevicesList = new ArrayList<>();
    private RecyclerView mRecyclerViewAllUserListing;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GetNearbyUsersPresenter mGetNearbyUsersPresenter;
    private NearbyUsersListingRecyclerAdapter mUserListingRecyclerAdapter;
    private GetOneUserPresenter mGetOneUserPresenter;
    private static final String ENCODE = "UTF-8";
    private Strategy mStrategy = new Strategy.Builder()
            .setDiscoveryMode(Strategy.DISCOVERY_MODE_DEFAULT)
            .setDistanceType(Strategy.DISTANCE_TYPE_DEFAULT)
            .setTtlSeconds(Strategy.TTL_SECONDS_DEFAULT)
            .build();
    private GetContactsPresenter mContactsInteractor;
    private List<String> contacts = new ArrayList<>();

    public static NearbyFragment newInstance() {
        Bundle args = new Bundle();
        NearbyFragment fragment = new NearbyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        String test = "QQ0B62oIS3Vt73d37xNWgB33iQa2";
      //  mNearbyDevicesList.add(test);
        mMessageListener = new MessageListener() {

            @Override
            public void onFound(Message message) {

//                System.out.println("Inside on found");
//                System.out.println(message.getContent());
                String decodedMessage;
                try {
                    decodedMessage = new String(message.getContent(), ENCODE);
                    System.out.println(ChatMessage.fromJson(decodedMessage));
                    if(!mNearbyDevicesList.contains(ChatMessage.fromJson(decodedMessage).getText())) {
                        mNearbyDevicesList.add(ChatMessage.fromJson(decodedMessage).getText());
                    }
                    getUsers();
                    System.out.println(mNearbyDevicesList);

                } catch (UnsupportedEncodingException e) {

                }


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
        String curUser = mAuth.getInstance().getCurrentUser().getUid();
        final ChatMessage chatMessage = new ChatMessage(curUser, System.currentTimeMillis());
        byte [] content;
        try {
            content = chatMessage.toString().getBytes(ENCODE);
        } catch (UnsupportedEncodingException e) {
            return;
        }
        mPubMessage = new Message(content, ChatMessage.TYPE_USER_CHAT);
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
        View fragmentView = inflater.inflate(R.layout.fragment_nearby, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
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
        mGetOneUserPresenter = new GetOneUserPresenter(this);

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
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        String newContactUid = mUserListingRecyclerAdapter.getUser(position).uid;
        onAddNearbyContact(newContactUid);
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

    private void onAddNearbyContact(String newContactUid){
        mGetOneUserPresenter.getOneUser(newContactUid);
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

}
