package fiu.com.skillcourt.ui.new_team;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fiu.com.skillcourt.ui.LauncherActivity;
import fiu.com.skillcourt.ui.base.BaseFragment;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.coach_dashboard.CoachingFragment;

import static android.R.attr.name;

public class NewTeamFragment extends BaseFragment {

    EditText tv_team_name;
    EditText tv_team_desc;
    Button btn_save_new_team;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mTeamRef = mRootRef.child("users").child(user.getUid()).child("teams");
    DatabaseReference newTeamRef = mTeamRef.push();


    public static NewTeamFragment newInstance() {
        return new NewTeamFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_new_team, container, false);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tv_team_name = (EditText) getView().findViewById(R.id.team_name);
        tv_team_desc = (EditText) getView().findViewById(R.id.new_team_description);
        btn_save_new_team = (Button) getView().findViewById(R.id.button_save_new_team);

        btn_save_new_team.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newTeamRef.child("name").setValue(tv_team_name.getText().toString());
                        newTeamRef.child("description").setValue(tv_team_desc.getText().toString());

                       // getFragmentManager().popBackStackImmediate();
                        getActivity().onBackPressed();
                        //TODO: give confirmation of added team and return to list of teams
                    }
                }

        );



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LauncherActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
