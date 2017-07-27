package fiu.com.skillcourt.ui.statistics;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.interfaces.FragmentListener;
import fiu.com.skillcourt.ui.base.BaseActivity;
import fiu.com.skillcourt.ui.statistics.AccuracyFragment;
import fiu.com.skillcourt.ui.statistics.HitsFragment;
import fiu.com.skillcourt.ui.statistics.StatisticsFragment;

/**
 * Created by pedrocarrillo on 9/10/16.
 */

public class StatsActivity extends BaseActivity  {

    FragmentPagerAdapter adapterViewPager;
    ViewPager vpPager;
    static String playerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        setNavigationToolbar();
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        // Get the extras (if there are any)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerID = extras.getString("playerID");
        }
    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return StatisticsFragment.newInstance(playerID);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return AccuracyFragment.newInstance(playerID);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return HitsFragment.newInstance(playerID);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return "Overview";
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return "Accuracy";
                case 2: // Fragment # 1 - This will show SecondFragment
                    return "Red vs. Green Hits";                default:
                    return null;
            }
        }
    }
}

