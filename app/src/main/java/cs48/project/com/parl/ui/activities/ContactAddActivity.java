package cs48.project.com.parl.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.app.ProgressDialog;
import android.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import cs48.project.com.parl.R;
import cs48.project.com.parl.core.contacts.add.AddContactContract;
import cs48.project.com.parl.core.contacts.add.AddContactPresenter;
import cs48.project.com.parl.core.users.getOne.GetOneUserContract;
import cs48.project.com.parl.core.users.getOne.GetOneUserPresenter;
import cs48.project.com.parl.core.users.getSearch.GetSearchUsersContract;
import cs48.project.com.parl.core.users.getSearch.GetSearchUsersPresenter;
import cs48.project.com.parl.models.User;


import java.util.ArrayList;
import java.util.List;


import cs48.project.com.parl.ui.adapters.NearbyUsersListingRecyclerAdapter;
import cs48.project.com.parl.utils.ItemClickSupport;

/**
 * Created by jakebliss on 5/8/17.
 */

public class ContactAddActivity extends AppCompatActivity implements AddContactContract.View, View.OnClickListener, GetOneUserContract.View,
                                        ItemClickSupport.OnItemClickListener, GetSearchUsersContract.View{
    private Toolbar mToolbar;
    public static final String ARG_TYPE = "type";
    public static final String TYPE_CHATS = "type_chats";
    public static final String TYPE_ALL = "type_all";
    private AddContactPresenter mAddContactPresenter;
    private EditText mETxtUsername;
    private Button mBtnSearch;
    private ProgressDialog mProgressDialog;
    private SearchView mUserSearchViews;
    private GetOneUserPresenter mGetOneUserPresenter;
    private RecyclerView mRecyclerViewAllUserListing;
    private NearbyUsersListingRecyclerAdapter mUserListingRecyclerAdapter;
    private GetSearchUsersPresenter mGetSearchUsersPresenter;
    List<String> userList = new ArrayList<>();

    public void initializeList(){
        userList.add("2AqbusnnkFdWjWOes0O1q74CWmf1");
        userList.add("81H8KFqGGjZZs2O4aJbk7bj0J4C3");
        userList.add("BhRrwOZavdekUZtitixp6n9IShe2");
        userList.add("D6jme1Sh6EOXVKgiiUGqfDNJMjw1");
        userList.add("GUqk9EOSxef2XD1IIWQGbK9rAj92");
    }

    public static void startIntent(Context context) {
        Intent intent = new Intent(context, ContactAddActivity.class);
        context.startActivity(intent);
    }

    public static void startIntent(Context context, int flags) {
        Intent intent = new Intent(context, ContactAddActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        bindViews();
        Intent intent = getIntent();
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            doMySearch(query);
//        }
        initializeList();
        init();
    }


    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mUserSearchViews = (SearchView) findViewById(R.id.find_user_search_view);
        mRecyclerViewAllUserListing = (RecyclerView) findViewById(R.id.recycler_view_add_contact);
    }

    private void init() {
        // set the toolbar
        setSupportActionBar(mToolbar);
        mGetSearchUsersPresenter = new GetSearchUsersPresenter(this);
        mAddContactPresenter = new AddContactPresenter(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        mGetOneUserPresenter = new GetOneUserPresenter(this);

        ItemClickSupport.addTo(mRecyclerViewAllUserListing)
                .setOnItemClickListener(this);

        getSearchUsers();
//        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
//            public boolean onQueryTextChange(String newText) {
//                // this is your adapter that will be filtered
//                return true;
//            }
//
//            public boolean onQueryTextSubmit(String query) {
//                //Here u can get the value "query" which is entered in the search box.
//
//            }
//        };



//        mUserSearchViews.setOnQueryTextListener(queryTextListener);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

//        switch (viewId) {
//            case R.id.button_search:
//                //startActivity(new Intent(this, AddNearbyActivity.class));
//
//                //onAddContact(view);
//                //onSearch(view);
//                break;
//        }
    }

    public void getSearchUsers()
    {
        mGetSearchUsersPresenter.getSearchUsers(userList);
    }
    @Override
    public void onGetSearchUsersSuccess(List<User> users) {
        mUserListingRecyclerAdapter = new NearbyUsersListingRecyclerAdapter(users);
        mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
        mUserListingRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetSearchUsersFailure(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        String newContactUid = mUserListingRecyclerAdapter.getUser(position).uid;
        onAddSearchContact(newContactUid);
    }


    @Override
    public void onAddContactSuccess(String message) {
//        mProgressDialog.dismiss();
//        ContactListingActivity.startActivity(getActivity(),
//                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Toast.makeText(this, "Added Contact", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddContactFailure(String message) {
    }

    private void onAddSearchContact(String newContactUid){
        mGetOneUserPresenter.getOneUser(newContactUid);
    }

    @Override
    public void onGetOneUserSuccess(User user){
        Toast.makeText(this, user.userName, Toast.LENGTH_SHORT).show();
        mAddContactPresenter.addContact(this,user);

    }

    @Override
    public void onGetOneUserFailure(String message){
        Toast.makeText(this, "search failed", Toast.LENGTH_SHORT).show();

    }
}
