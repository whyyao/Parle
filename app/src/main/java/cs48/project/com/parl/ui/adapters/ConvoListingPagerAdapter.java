package cs48.project.com.parl.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cs48.project.com.parl.ui.fragments.ConvoFragment;

/**
 * Created by yaoyuan on 4/23/17.
 */

public class ConvoListingPagerAdapter extends FragmentPagerAdapter{
    private static final Fragment[] sFragments = new Fragment[]{/*UsersFragment.newInstance(UsersFragment.TYPE_CHATS),*/
            ConvoFragment.newInstance(ConvoFragment.TYPE_ALL)};
    private static final String[] sTitles = new String[]{/*"Chats",*/
            "All Users"};

    public ConvoListingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return sFragments[position];
    }

    @Override
    public int getCount() {
        return sFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return sTitles[position];
    }
}
