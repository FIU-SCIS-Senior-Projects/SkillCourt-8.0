package fiu.com.skillcourt.ui.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.LauncherActivity;
import fiu.com.skillcourt.ui.base.BaseActivity;
import fiu.com.skillcourt.ui.coach_dashboard.CoachingDashboardActivity;
import fiu.com.skillcourt.ui.coach_dashboard.CoachingFragment;
import fiu.com.skillcourt.ui.creategame.CreateGameActivity;
import fiu.com.skillcourt.ui.dynamicsteps.DynamicStepsFragment;
import fiu.com.skillcourt.ui.statistics.StatisticsFragment;
import fiu.com.skillcourt.ui.statistics.StatsActivity;

public class MainDashboardActivity extends BaseActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    FloatingActionButton fabStartGame;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("users");
    DatabaseReference mUserIDref = mUsersRef.child(user.getUid());
    DatabaseReference mRoleRef = mUserIDref.child("role");
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setToolbar();
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        fabStartGame = (FloatingActionButton) findViewById(R.id.fb_start_game);


        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email != null) {
            TextView tvWelcome = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_welcome);
            tvWelcome.setText("Welcome " + email);
        }

        showCoachOptions();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                String title;
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        title = getString(R.string.home_title);
                        replaceFragment(new MainDashboardFragment(), true);
                        getSupportActionBar().setTitle(title);
                        fabStartGame.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.dyn_seq:
                        title = getString(R.string.dynamic_seq_title);
                        replaceFragment(new DynamicStepsFragment(), true);
                        getSupportActionBar().setTitle(title);
                        fabStartGame.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.statistics:
                        title = getString(R.string.Statistics);
                        Intent intent1 = new Intent(MainDashboardActivity.this, StatsActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.coaching_features:
                        title = getString(R.string.Coaching);
                        replaceFragment(new CoachingFragment(), true);
                        getSupportActionBar().setTitle(title);
                        fabStartGame.setVisibility(View.GONE);
                        return true;
                    case R.id.action_sign_out:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainDashboardActivity.this, LauncherActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return true;

                }
            }

        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        navigationView.getMenu().performIdentifierAction(R.id.home, 0);
        fabStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainDashboardActivity.this, CreateGameActivity.class);
                startActivity(intent);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void showCoachOptions() {

        mRoleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userRole = dataSnapshot.getValue(String.class);


                if (userRole.equals("coach")) {
                    navigationView.getMenu().findItem(R.id.coaching_features).setVisible(true);
                } else {
                    navigationView.getMenu().findItem(R.id.coaching_features).setVisible(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void checkChild() {
        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild(user.getUid())) {
                    initializeUser();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initializeUser(){

    }
}
