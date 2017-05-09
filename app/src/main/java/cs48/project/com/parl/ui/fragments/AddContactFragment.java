package cs48.project.com.parl.ui.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.contacts.add.AddContactContract;
import cs48.project.com.parl.core.contacts.add.AddContactPresenter;
import cs48.project.com.parl.core.login.LoginPresenter;

/**
 * Created by jakebliss on 5/8/17.
 */

public class AddContactFragment extends Fragment implements View.OnClickListener, AddContactContract.View{
    private AddContactPresenter mAddContactPresenter;
    private EditText mETxtUsername;
    private Button mBtnSearch;
    private ProgressDialog mProgressDialog;

    public static AddContactFragment newInstance() {
        Bundle args = new Bundle();
        AddContactFragment fragment = new AddContactFragment();
        fragment.setArguments(args);
        return fragment;
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mAddContactPresenter = new AddContactPresenter(this);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        mBtnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.button_search:
                onAddContact(view);
                //onSearch(view);
                break;
        }
    }

    private void onAddContact(View view){
        String searchUsername = mETxtUsername.getText().toString();
        mAddContactPresenter.addContact(getActivity(), searchUsername);
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
//    public void onSearch(View view){
//        String searchUsername = mETxtUsername.getText().toString();
//
//        mProgressDialog.show();
//    }
}
