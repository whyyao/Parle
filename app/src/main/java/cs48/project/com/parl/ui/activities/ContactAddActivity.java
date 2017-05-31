package cs48.project.com.parl.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.app.ProgressDialog;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telecom.Call;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Filter;
import android.widget.Filterable;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.contacts.add.AddContactContract;
import cs48.project.com.parl.core.contacts.add.AddContactPresenter;
import cs48.project.com.parl.core.users.getOne.GetOneUserContract;
import cs48.project.com.parl.core.users.getOne.GetOneUserPresenter;
import cs48.project.com.parl.core.users.getall.GetUsersContract;
import cs48.project.com.parl.core.users.getall.GetUsersPresenter;
import cs48.project.com.parl.models.User;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


import cs48.project.com.parl.ui.adapters.NearbyUsersListingRecyclerAdapter;
import cs48.project.com.parl.ui.adapters.ListAdapter;
import cs48.project.com.parl.utils.Constants;
import cs48.project.com.parl.utils.ItemClickSupport;

/**
 * Created by jakebliss on 5/8/17.
 * Some Code adapted From http://www.journaldev.com/12478/android-searchview-example-tutorial
 * ***
 */

public class ContactAddActivity extends AppCompatActivity implements AddContactContract.View, View.OnClickListener, GetOneUserContract.View,
                                        GetUsersContract.View, GetUsersContract.Presenter{
    private Toolbar mToolbar;
    public static final String ARG_TYPE = "type";
    public static final String TYPE_CHATS = "type_chats";
    public static final String TYPE_ALL = "type_all";
    private AddContactPresenter mAddContactPresenter;
    private ProgressDialog mProgressDialog;
    private SearchView mUserSearchViews;
    private GetOneUserPresenter mGetOneUserPresenter;
    private GetUsersPresenter mGetUsersPresenter;
    private ListView mListView;
    private TextView mNearbyButton;
    ListAdapter mAdapter;
    private TextView mTxtSuggested;
    private List<User> mSearchUsers = new ArrayList<>();
    private boolean searchListenerFlag = false;


    public static void startIntent(Context context, List<User> suggestedUsers) {
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
        init();
    }


    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mUserSearchViews = (SearchView) findViewById(R.id.find_user_search_view);
        mListView = (ListView) findViewById(R.id.list_view_search);
        mNearbyButton = (TextView) findViewById(R.id.nearby_button);
        mTxtSuggested = (TextView) findViewById(R.id.suggested_text_view);
    }

    private void init() {
        // set the toolbar
        setSupportActionBar(mToolbar);
        mAddContactPresenter = new AddContactPresenter(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        mGetOneUserPresenter = new GetOneUserPresenter(this);
        mGetUsersPresenter = new GetUsersPresenter(this);
        getAllUsers();

        mNearbyButton.setOnClickListener(this);

        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                User newContact = (User) parent.getItemAtPosition(position);
                String newContactUid = newContact.uid.toString();
              AddSearchContact(newContactUid);
            }
        });
    }

    public void initializeSearchListener(final List<User> mSearchUsers)
    {
        mAdapter = new ListAdapter(mSearchUsers, true);
        mListView.setAdapter(mAdapter);
        mUserSearchViews.setQueryHint("Search");
        mUserSearchViews.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mTxtSuggested.setVisibility(View.GONE);
                mAdapter.setFilterList(mSearchUsers);
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.nearby_button:
                startActivity(new Intent(this, AddNearbyActivity.class));
                break;
        }
    }

    public void AddSearchContact(String newContactId)
    {
        System.out.println(newContactId);
        mGetOneUserPresenter.getOneUser(newContactId);
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

    @Override
    public void getAllUsers()
    {
        mGetUsersPresenter.getAllUsers();
    }

    @Override
    public void onGetAllUsersSuccess(List<User> users)
    {
        mSearchUsers = users;
        if(searchListenerFlag == false) {
            initializeSearchListener(mSearchUsers);
            searchListenerFlag = true;
        }
    }

    @Override
    public void onGetAllUsersFailure(String message)
    {

    }

    @Override
    public void onGetOneUserSuccess(User user){
        mAddContactPresenter.addContact(this,user);
    }

    @Override
    public void onGetOneUserFailure(String message){
        Toast.makeText(this, "search failed", Toast.LENGTH_SHORT).show();

    }


}
