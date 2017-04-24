package cs48.project.com.parl.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by yaoyuan on 4/23/17.
 */

public class ConvoListingAdapter {
    private static final Fragment[] sFragments = new Fragment[]{/*UsersFragment.newInstance(UsersFragment.TYPE_CHATS),*/
            UsersFragment.newInstance(UsersFragment.TYPE_ALL)};
    private static final String[] sTitles = new String[]{/*"Chats",*/
            "All Users"};

    public UserListingPagerAdapter(FragmentManager fm) {
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
