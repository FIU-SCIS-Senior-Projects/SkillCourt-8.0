package fiu.com.skillcourt.ui.login;

import android.os.Bundle;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.base.BaseActivity;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

}

