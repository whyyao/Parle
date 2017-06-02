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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.contacts.add.AddContactContract;
import cs48.project.com.parl.core.contacts.add.AddContactPresenter;
import cs48.project.com.parl.core.contacts.getAll.GetContactsContract;
import cs48.project.com.parl.core.contacts.getAll.GetContactsPresenter;
import cs48.project.com.parl.core.users.getall.GetUsersContract;
import cs48.project.com.parl.core.users.getall.GetUsersPresenter;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.ui.activities.ChatActivity;
import cs48.project.com.parl.ui.adapters.ContactListingRecyclerAdapter;
import cs48.project.com.parl.ui.adapters.ContactListingRecyclerAdapter;
import cs48.project.com.parl.ui.adapters.ListAdapter;
import cs48.project.com.parl.utils.ItemClickSupport;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class ContactsFragment extends Fragment implements GetContactsContract.View, SwipeRefreshLayout.OnRefreshListener{
    public static final String ARG_TYPE = "type";
    public static final String TYPE_CHATS = "type_chats";
    public static final String TYPE_ALL = "type_all";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SearchView mUserSearchViews;
    private ListView mListView;
    ListAdapter mAdapter;
    private List<User> mContactList = new ArrayList<>();
    private boolean contactListenerFlag = false;

    private ContactListingRecyclerAdapter mUserListingRecyclerAdapter;

    private GetUsersPresenter mGetUsersPresenter;
    private GetContactsPresenter mGetContactsPresenter;

    public static ContactsFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ContactsFragment(){

    }
    //hi
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_contacts, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mUserSearchViews = (SearchView) view.findViewById(R.id.find_contact_search_view);
        mListView = (ListView) view.findViewById(R.id.list_view_contacts);
        //txtViewPlus.setVisibility(view.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mGetContactsPresenter = new GetContactsPresenter(this);
        getUsers();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(this);
        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                System.out.println("click");
                User selectedUser = (User) parent.getItemAtPosition(position);
                ChatActivity.startActivity(getActivity(),
                        selectedUser.userName,
                        selectedUser.uid,
                        selectedUser.firebaseToken,
                        selectedUser.language);

            }
        });

    }

    public void initializeSearchListener(final List<User> mSearchUsers)
    {
        mAdapter = new ListAdapter(mSearchUsers, false);
        mListView.setAdapter(mAdapter);
        String search = getString(R.string.search);
        mUserSearchViews.setQueryHint(search);
        mUserSearchViews.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.setFilterList(mSearchUsers);
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onRefresh() {
        getUsers();
    }

    private void getUsers() {
        //if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_CHATS)) {

        //} else if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_ALL)) {
            //mGetUsersPresenter.getAllUsers();
            System.out.println("getting contacts");
            mGetContactsPresenter.getContactsUsers();
        //}
    }


    @Override
    public void onGetContactsUsersSuccess(List<User> users){
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mContactList = users;
        if(contactListenerFlag == false) {
            initializeSearchListener(mContactList);
            contactListenerFlag = true;
        }
    }

    @Override
    public void onGetContactsUsersFailure(String message){
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }
}
