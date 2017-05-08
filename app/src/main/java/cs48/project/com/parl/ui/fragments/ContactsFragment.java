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

import java.util.List;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.contacts.add.AddContactContract;
import cs48.project.com.parl.core.contacts.add.AddContactPresenter;
import cs48.project.com.parl.core.users.getall.GetUsersContract;
import cs48.project.com.parl.core.users.getall.GetUsersPresenter;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.ui.activities.ChatActivity;
import cs48.project.com.parl.ui.adapters.ContactListingRecyclerAdapter;
import cs48.project.com.parl.ui.adapters.ContactListingRecyclerAdapter;
import cs48.project.com.parl.utils.ItemClickSupport;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class ContactsFragment extends Fragment implements View.OnClickListener, AddContactContract.View, GetUsersContract.View, ItemClickSupport.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{
    public static final String ARG_TYPE = "type";
    public static final String TYPE_CHATS = "type_chats";
    public static final String TYPE_ALL = "type_all";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewAllUserListing;

    private ContactListingRecyclerAdapter mUserListingRecyclerAdapter;

    private GetUsersPresenter mGetUsersPresenter;

    public static ContactsFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ContactsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_contacts, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerViewAllUserListing = (RecyclerView) view.findViewById(R.id.recycler_view_all_user_listing);
        mBtnTest = (Button) view.findViewById(R.id.addContactFab);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mGetUsersPresenter = new GetUsersPresenter(this);
        getUsers();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mAddContactPresenter = new AddContactPresenter(this);

        mBtnTest.setOnClickListener(this);
        ItemClickSupport.addTo(mRecyclerViewAllUserListing)
                .setOnItemClickListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        getUsers();
    }

    private void getUsers() {
        if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_CHATS)) {

        } else if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_ALL)) {
            mGetUsersPresenter.getAllUsers();
        }
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        ChatActivity.startActivity(getActivity(),
                mUserListingRecyclerAdapter.getUser(position).userName,
                mUserListingRecyclerAdapter.getUser(position).uid,
                mUserListingRecyclerAdapter.getUser(position).firebaseToken,
                mUserListingRecyclerAdapter.getUser(position).language);
    }

    @Override
    public void onGetAllUsersSuccess(List<User> users) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mUserListingRecyclerAdapter = new ContactListingRecyclerAdapter(users);
        mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
        mUserListingRecyclerAdapter.notifyDataSetChanged();
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

    private AddContactPresenter mAddContactPresenter;

    //buttons
    private Button mBtnTest;

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        String value = "yes";
        Log.d("Inside on Click: ", value);
        switch (viewId){
            case R.id.addContactFab:
                onAddContact(view);
                break;
        }
    }

    private String newContactId = "newone23";
    private void onAddContact(View view){
        mAddContactPresenter.addContact(getActivity(), newContactId);
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
}
