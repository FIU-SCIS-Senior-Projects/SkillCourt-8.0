package fiu.com.skillcourt.ui.creategame;

import android.os.Bundle;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.base.BaseActivity;

public class CreateGameActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        if (savedInstanceState == null) {
            replaceFragment(CreateGameFragment.newInstance(), false);
        }
        setNavigationToolbar();
    }

}
