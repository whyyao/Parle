package cs48.project.com.parl.ui.activities;
//*
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cs48.project.com.parl.ParleMainApp;
import cs48.project.com.parl.R;
import cs48.project.com.parl.ui.fragments.ChatFragment;
import cs48.project.com.parl.utils.Constants;
//...
public class ChatActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static void startActivity(Context context,
                                     String receiver,
                                     String receiverUid,
                                     String firebaseToken,
                                     String language) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.ARG_RECEIVER_USERNAME, receiver);
        intent.putExtra(Constants.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        intent.putExtra(Constants.ARG_RECEIVER_LANGUAGE, language);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_chat);
        bindViews();
        init();

//        databaseReference.child(Constants.ARG_USERS).child(getIntent().getExtras().getString(Constants.ARG_RECEIVER_UID)).child("userName").getRef().addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String userName = dataSnapshot.getValue(String.class);
//                mToolbar.setTitle(userName);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });
    }


    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void init() {
        // set the toolbar
        setSupportActionBar(mToolbar);

        // set toolbar title
        mToolbar.setTitle(getIntent().getExtras().getString(Constants.ARG_RECEIVER_USERNAME));

        // set the register screen fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_chat,
                ChatFragment.newInstance(getIntent().getExtras().getString(Constants.ARG_RECEIVER_USERNAME),
                        getIntent().getExtras().getString(Constants.ARG_RECEIVER_UID),
                        getIntent().getExtras().getString(Constants.ARG_FIREBASE_TOKEN),
                        getIntent().getExtras().getString(Constants.ARG_RECEIVER_LANGUAGE)),
                ChatFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ParleMainApp.setChatActivityOpen(true);
    }

    @Override
    protected void onPause() {
        Log.d("Reach","pause");
        super.onPause();
        ParleMainApp.setChatActivityOpen(false);
    }
}
