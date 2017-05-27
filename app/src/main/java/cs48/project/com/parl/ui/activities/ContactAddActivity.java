package cs48.project.com.parl.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ListAdapter;
import android.app.SearchManager;


import cs48.project.com.parl.R;
import cs48.project.com.parl.core.contacts.add.AddContactContract;
import cs48.project.com.parl.core.contacts.add.AddContactPresenter;
import cs48.project.com.parl.core.users.getOne.GetOneUserContract;
import cs48.project.com.parl.core.users.getOne.GetOneUserPresenter;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.ui.activities.ChatActivity;
import cs48.project.com.parl.ui.activities.ContactAddActivity;
import cs48.project.com.parl.ui.activities.AddNearbyActivity;

import java.util.ArrayList;
import java.util.List;



import cs48.project.com.parl.ui.fragments.AddContactFragment;

/**
 * Created by jakebliss on 5/8/17.
 */

public class ContactAddActivity extends AppCompatActivity implements AddContactContract.View, View.OnClickListener, GetOneUserContract.View{
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

    ListAdapter adapter;
    final List<String> userList = new ArrayList<>();


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
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }

        init();
    }


    private void bindViews(View view) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mUserSearchViews = (SearchView) view.findViewById(R.id.find_user_search_view);
        mETxtUsername = (EditText) view.findViewById(R.id.edit_text_new_contact_username);
        mBtnSearch = (Button) view.findViewById(R.id.button_search);
    }

    private void init() {
        // set the toolbar
        setSupportActionBar(mToolbar);
        mAddContactPresenter = new AddContactPresenter(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        mBtnSearch.setOnClickListener(this);
        mGetOneUserPresenter = new GetOneUserPresenter(this);

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.

            }
        };

        mUserSearchViews.setOnQueryTextListener(queryTextListener);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.button_search:
                startActivity(new Intent(this, AddNearbyActivity.class));

                //onAddContact(view);
                //onSearch(view);
                break;
        }
    }

    private void onAddContact(View view){
        String target = mETxtUsername.getText().toString();
        mGetOneUserPresenter.getOneUser(target);
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
    public void onGetOneUserSuccess(User user){
        Toast.makeText(getActivity(), user.userName, Toast.LENGTH_SHORT).show();
        mAddContactPresenter.addContact(getActivity(),user);

    }
    @Override
    public void onGetOneUserFailure(String message){
        Toast.makeText(getActivity(), "search failed", Toast.LENGTH_SHORT).show();

    }
}
