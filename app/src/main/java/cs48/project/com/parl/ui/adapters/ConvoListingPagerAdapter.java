package cs48.project.com.parl.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaoyuan on 4/23/17.
 */

public class ConvoListingPagerAdapter extends FragmentPagerAdapter{
    private static final List<Fragment> sFragments = new ArrayList<>();
    private static final List<String> sTitles = new ArrayList<>();

    public ConvoListingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return sFragments.get(position);
    }

    @Override
    public int getCount() {
        return sFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
        //sTitles.get(position)
    }

    public void addFrag(Fragment fragment, String title){
        if(this.getCount() <2){
            sFragments.add(fragment);
            sTitles.add(title);}
    }




}

