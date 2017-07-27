package fiu.com.skillcourt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import fiu.com.skillcourt.ui.dashboard.MainDashboardActivity;
import fiu.com.skillcourt.ui.intro.IntroActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This activity will decide where to go at launch. Right now it only starts the LoginActivity.
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainDashboardActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        }
        finish();
    }

}
