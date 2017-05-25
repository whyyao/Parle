package cs48.project.com.parl.ui.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.ui.adapters.ContactListingRecyclerAdapter;
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

/**
 * Created by jakebliss on 5/8/17.
 */

public class AddContactFragment extends Fragment implements View.OnClickListener, AddContactContract.View, GetOneUserContract.View, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private AddContactPresenter mAddContactPresenter;
    private EditText mETxtUsername;
    private Button mBtnSearch;
    private ProgressDialog mProgressDialog;
    GoogleApiClient mGoogleApiClient;
    private Message mPubMessage;
    private MessageListener mMessageListener;
    private ArrayAdapter<String> mNearbyDevicesArrayAdapter;
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerViewAllUserListing;

    private ContactListingRecyclerAdapter mUserListingRecyclerAdapter;

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
        mMessageListener = new MessageListener() {
            String messageAsString;

            @Override
            public void onFound(Message message) {
                messageAsString = new String(message.getContent());
            }

            @Override
            public void onLost(Message message) {
                messageAsString = null;
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
        mRecyclerViewAllUserListing = (RecyclerView) view.findViewById(R.id.recycler_view_nearby);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(getActivity(), this)
                .build();

        mAddContactPresenter = new AddContactPresenter(this);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        mBtnSearch.setOnClickListener(this);

        ItemClickSupport.addTo(mRecyclerViewAllUserListing)
                .setOnItemClickListener();

        mGetOneUserPresenter = new GetOneUserPresenter(this);

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
            case R.id.recycler_view_nearby:
                onAddContact(view);
        }
    }

    private void onAddContact(View view){
        String target = mETxtUsername.getText().toString();
        mGetOneUserPresenter.getOneUser(target);
      //  mAddContactPresenter.addContact(getActivity(), target);

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
        mAddContactPresenter.addContact(getActivity(),user.uid);

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
