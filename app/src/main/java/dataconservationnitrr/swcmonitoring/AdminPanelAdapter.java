package dataconservationnitrr.swcmonitoring;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



/**
 * Created by Ketan-PC on 12/18/2017.
 */

public class AdminPanelAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Approved", "Pending"};


    public AdminPanelAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0) {
            return new ApprovedFragment();
        }
        else {
            return new WaitingFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
