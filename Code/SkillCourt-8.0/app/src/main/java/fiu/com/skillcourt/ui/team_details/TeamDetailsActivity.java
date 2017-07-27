package fiu.com.skillcourt.ui.team_details;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.base.BaseActivity;

public class TeamDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        String teamID = getIntent().getExtras().getString("teamID");

        if (savedInstanceState == null) {
            replaceFragment(TeamDetailsFragment.newInstance(teamID), false);
        }
        setToolbar();
    }

}
