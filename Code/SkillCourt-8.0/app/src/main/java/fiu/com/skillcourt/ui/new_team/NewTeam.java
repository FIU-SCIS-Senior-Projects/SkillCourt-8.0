package fiu.com.skillcourt.ui.new_team;

import android.os.Bundle;
import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.base.BaseActivity;

public class NewTeam extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        if (savedInstanceState == null) {
            replaceFragment(NewTeamFragment.newInstance(), false);
        }
        setToolbar();
    }
}
