package fiu.com.skillcourt.ui.startgame;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.interfaces.Constants;
import fiu.com.skillcourt.ui.base.BaseActivity;

public class StartGameActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_base);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (savedInstanceState == null) {
            List<String> sequences = null;
            //TODO: RE-DO the custom sequences.
//            if (getIntent().hasExtra(Constants.TAG_SEQUENCE)) {
//                sequences = (List<String>) getIntent().getSerializableExtra(Constants.TAG_SEQUENCE);
//            }
            replaceFragment(StartGameFragment.newInstance(sequences == null ? new ArrayList<String>() : new ArrayList<String>(sequences)), false);
        }
        setNavigationToolbar();
    }

}
