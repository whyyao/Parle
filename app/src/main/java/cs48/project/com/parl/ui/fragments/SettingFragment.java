package cs48.project.com.parl.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.logout.LogoutContract;
import cs48.project.com.parl.core.logout.LogoutPresenter;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.ui.activities.ConvoListingActivity;
import cs48.project.com.parl.ui.activities.LoginActivity;

import static cs48.project.com.parl.R.string.username;
import static cs48.project.com.parl.utils.Constants.convertFromAcronym;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment implements View.OnClickListener, LogoutContract.View{
    private TextView usernameTextView;
    private TextView languageTextView;
    private String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private LogoutPresenter mLogoutPresenter;
    private Button mBtnLogout;
    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;



    public SettingFragment () {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth){
                setUsernameTextView();
                setLanguageTextView();

            }
        };
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_setting, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        usernameTextView = (TextView) view.findViewById(R.id.setting_username);
        languageTextView = (TextView) view.findViewById(R.id.setting_language);

        mBtnLogout = (Button) view.findViewById(R.id.setting_logout);
    }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){
        setUsernameTextView();
        setLanguageTextView();

        mLogoutPresenter = new LogoutPresenter(this);
        mBtnLogout.setOnClickListener(this);
    }





    private void setUsernameTextView(){
       FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        if (curUser != null) {
            mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            databaseReference.child("users").child(mUid).getRef().addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    String username = user.userName;
                    usernameTextView.setText(username);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    private void setLanguageTextView(){
        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        if (curUser != null) {
            mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            databaseReference.child("users").child(mUid).getRef().addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    String language = user.language;
                    languageTextView.setText(convertFromAcronym(language));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void logout() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.logout)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mLogoutPresenter.logout();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void onClick(View view){
        int viewId = view.getId();

        switch(viewId){
            case R.id.setting_logout:
                logout();
        }
    }

    @Override
    public void onLogoutSuccess(String message) {
//        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        LoginActivity.startIntent(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void onLogoutFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
