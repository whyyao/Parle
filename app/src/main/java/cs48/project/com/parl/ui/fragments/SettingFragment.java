package cs48.project.com.parl.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
//import android.net.URI;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.logout.LogoutContract;
import cs48.project.com.parl.core.logout.LogoutPresenter;
import cs48.project.com.parl.models.User;
import cs48.project.com.parl.ui.activities.ConvoListingActivity;
import cs48.project.com.parl.ui.activities.LoginActivity;

import static cs48.project.com.parl.R.string.username;
import static cs48.project.com.parl.utils.Constants.convertFromAcronym;
//123
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
    private TextView emailTextView;
    private String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private LogoutPresenter mLogoutPresenter;
    private Button mBtnLogout;
    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private ImageButton pictureButton;
    private static final int RC_PHOTO_PICKER = 2;
    // ALT ENTER TO LINK ;)
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotoStorageReference;

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
                setEmailTextView();
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
        usernameTextView = (TextView) view.findViewById(R.id.User_Name);
        languageTextView = (TextView) view.findViewById(R.id.User_Language);
        emailTextView = (TextView) view.findViewById(R.id.User_Email);
        mBtnLogout = (Button) view.findViewById(R.id.setting_logout);
        pictureButton = (ImageButton) view.findViewById(R.id.userImage);
        pictureButton.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent, "Complete actions using"), RC_PHOTO_PICKER);
            }
        });
     }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

   // @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode,resultCode,data);
//        if (requestCode == RC_PHOTO_PICKER) {
//            Uri selectedImageUri = data.getData();
//            StorageReference photoRef = mChatPhotoStorageReference.child(selectedImageUri.getLastPathSegment());
//            photoRef.putFile(selectedImageUri).addOnSuccessListener
//                    (this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Uri downloadUrl taskSnapshot.getDownloadUrl();
//                            FriendlyMessage.
//                        }
//                    });
//    }
    private void init(){
        setUsernameTextView();
        setLanguageTextView();
        setEmailTextView();
        mLogoutPresenter = new LogoutPresenter(this);
        mBtnLogout.setOnClickListener(this);
        mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotoStorageReference = mFirebaseStorage.getReference().child("chat_photos");
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

    private void setEmailTextView() {
        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        if (curUser != null) {
            mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            databaseReference.child("users").child(mUid).getRef().addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    String email = user.email;
                    System.out.println("EMAIL IS " + email);
                    emailTextView.setText(email);
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
