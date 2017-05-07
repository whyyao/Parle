package cs48.project.com.parl.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cs48.project.com.parl.R;
import cs48.project.com.parl.core.logout.LogoutPresenter;
import cs48.project.com.parl.ui.adapters.ConvoListingPagerAdapter;
import cs48.project.com.parl.ui.fragments.ConvoFragment;
import cs48.project.com.parl.ui.fragments.SettingFragment;

public class ConvoListingActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TabLayout mTabLayoutUserListing;
    private ViewPager mViewPagerUserListing;

    private LogoutPresenter mLogoutPresenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ConvoListingActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int flags) {
        Intent intent = new Intent(context, ConvoListingActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convo_listing);
        bindViews();
        init();
    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayoutUserListing = (TabLayout) findViewById(R.id.tab_layout_user_listing);
        mViewPagerUserListing = (ViewPager) findViewById(R.id.view_pager_user_listing);
    }

    private void init() {
        //set the toolbar
        setSupportActionBar(mToolbar);

        //set the view pager adapter
        ConvoListingPagerAdapter convoListingPagerAdapter = new ConvoListingPagerAdapter(getSupportFragmentManager());
        mViewPagerUserListing.setAdapter(convoListingPagerAdapter);

        //add
        setupViewPager(mViewPagerUserListing);

        //attach tab layout with view pager
        mTabLayoutUserListing.setupWithViewPager(mViewPagerUserListing);

        setupTabIcons();
//        mLogoutPresenter = new LogoutPresenter(this);
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_conversation_icon,
                R.drawable.ic_me_icon
        };
        mTabLayoutUserListing.getTabAt(0).setIcon(tabIcons[0]);
        mTabLayoutUserListing.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ConvoListingPagerAdapter adapter = new ConvoListingPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ConvoFragment().newInstance(ConvoFragment.TYPE_ALL), "All Users");
        adapter.addFrag(new SettingFragment(), "ME");
        viewPager.setAdapter(adapter);
    }






}
