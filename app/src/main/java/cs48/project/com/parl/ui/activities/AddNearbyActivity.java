package cs48.project.com.parl.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cs48.project.com.parl.R;
import cs48.project.com.parl.ui.fragments.AddContactFragment;
import cs48.project.com.parl.ui.fragments.NearbyFragment;
/**
 * Created by jakebliss on 5/26/17.
 */

public class AddNearbyActivity  extends AppCompatActivity {
    private Toolbar mToolbar;


    public static void startIntent(Context context) {
        Intent intent = new Intent(context, AddNearbyActivity.class);
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
        setContentView(R.layout.activity_nearby);
        bindViews();
        init();

    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void init() {
        // set the toolbar
        setSupportActionBar(mToolbar);

        // set the addContact screen fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_nearby,
                NearbyFragment.newInstance(),
                NearbyFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}
