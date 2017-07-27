package fiu.com.skillcourt.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.login.LoginActivity;
import fiu.com.skillcourt.ui.register.RegisterActivity;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        tvLogin = (TextView)findViewById(R.id.tv_login);
        tvRegister = (TextView)findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_login) {
            Intent loginIntent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        } else if (v.getId() == R.id.tv_register) {
            Intent loginIntent = new Intent(IntroActivity.this, RegisterActivity.class);
            startActivity(loginIntent);
        }
    }
}
