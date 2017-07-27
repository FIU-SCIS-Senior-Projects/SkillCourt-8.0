package fiu.com.skillcourt.ui.search_user;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.base.BaseActivity;
import fiu.com.skillcourt.ui.dashboard.MainDashboardFragment;

/**
 * Created by jpdur on 10/26/2016.
 */

public class SearchUserActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        String teamID = getIntent().getExtras().getString("teamID");

        if (savedInstanceState == null) {
            replaceFragment(SearchUserFragment.newInstance(teamID), false);
        }
        setToolbar();
    }


}
