package cs48.project.com.parl.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseUser;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.registration.RegisterContract;
import cs48.project.com.parl.core.registration.RegisterPresenter;
import cs48.project.com.parl.core.users.add.AddUserContract;
import cs48.project.com.parl.core.users.add.AddUserPresenter;
import cs48.project.com.parl.ui.activities.ConvoListingActivity;
import cs48.project.com.parl.ui.activities.RegisterActivity;

//import cs48.project.com.parl.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener, RegisterContract.View, AddUserContract.View, AdapterView.OnItemSelectedListener {
    private static final String TAG = RegisterFragment.class.getSimpleName();

    private RegisterPresenter mRegisterPresenter;
    private AddUserPresenter mAddUserPresenter;

    private EditText mETxtEmail, mETxtPassword, mETxtUsername;
    private Button mBtnRegister;

    private ProgressDialog mProgressDialog;

    private Spinner spinner;
    private static final String[] paths = {"Arabic", "Bengali", "Chinese (Simplified)", "Chinese (Traditional)", "English", "French", "Hindi", "Italian", "Japanese", "Portuguese", "Russian", "Spanish"};
    private String language;

    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_register, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mETxtEmail = (EditText) view.findViewById(R.id.edit_text_email_id);
        mETxtPassword = (EditText) view.findViewById(R.id.edit_text_password);
        mETxtUsername = (EditText) view.findViewById(R.id.edit_text_username);
        mBtnRegister = (Button) view.findViewById(R.id.button_register);
        spinner = (Spinner) view.findViewById(R.id.spinner);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mRegisterPresenter = new RegisterPresenter(this);
        mAddUserPresenter = new AddUserPresenter(this);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mBtnRegister.setOnClickListener(this);

        //text box is filled with each element in the array
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(position) {
            case 0:
                language = "ar";
                break;
            case 1:
                language = "bn";
                break;
            case 2:
                language = "zh-CN";
                break;
            case 3:
                language = "zh-TW";
                break;
            case 4:
                language = "en";
                break;
            case 5:
                language = "fr";
                break;
            case 6:
                language = "hi";
                break;
            case 7:
                language = "it";
                break;
            case 8:
                language = "ja";
                break;
            case 9:
                language = "pt";
                break;
            case 10:
                language = "ru";
                break;
            case 11:
                language = "es";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.button_register:
                onRegister(view);
                break;
        }
    }

    private void onRegister(View view) {
        String emailId = mETxtEmail.getText().toString();
        String password = mETxtPassword.getText().toString();
        mRegisterPresenter.register(getActivity(), emailId, password);
        mProgressDialog.show();
    }

    @Override
    public void onRegistrationSuccess(FirebaseUser firebaseUser) {
        mProgressDialog.setMessage(getString(R.string.adding_user_to_db));
        Toast.makeText(getActivity(), "Registration Successful!", Toast.LENGTH_SHORT).show();
        String username = mETxtUsername.getText().toString();
        mAddUserPresenter.addUser(getActivity().getApplicationContext(), firebaseUser, username, language);
    }

    @Override
    public void onRegistrationFailure(String message) {
        mProgressDialog.dismiss();
        mProgressDialog.setMessage(getString(R.string.please_wait));
        Log.e(TAG, "onRegistrationFailure: " + message);
        Toast.makeText(getActivity(), "Registration failed!+\n" + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAddUserSuccess(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        ConvoListingActivity.startActivity(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void onAddUserFailure(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}