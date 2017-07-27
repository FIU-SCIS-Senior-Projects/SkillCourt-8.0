package fiu.com.skillcourt.ui.dynamicsteps;

import android.os.Bundle;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.base.BaseActivity;

public class DynamicStepsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        if (savedInstanceState == null) {
            replaceFragment(DynamicStepsFragment.newInstance(), false);
        }
        setNavigationToolbar();
    }

}
