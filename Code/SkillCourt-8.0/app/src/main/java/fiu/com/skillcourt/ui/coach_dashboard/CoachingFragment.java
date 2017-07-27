package fiu.com.skillcourt.ui.coach_dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.CoordinatorLayout;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.LauncherActivity;
import fiu.com.skillcourt.ui.base.BaseFragment;
import fiu.com.skillcourt.ui.new_team.NewTeam;
import fiu.com.skillcourt.ui.team_details.TeamDetailsActivity;

public class CoachingFragment extends BaseFragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mTeamRef = mRootRef.child("users").child(user.getUid()).child("teams");

    ListView lv_teams;
    List<Team> teamList = new ArrayList<>();
    ArrayAdapter<Team> arrayAdapter;


    public static CoachingFragment newInstance() {
        return new CoachingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTeamRef.addValueEventListener(
                new ValueEventListener() {
                    public void onDataChange(DataSnapshot snapshot) {
                        teamList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Team team = postSnapshot.getValue(Team.class);
                            team.setId(postSnapshot.getKey());
                            teamList.add(team);
                        }

                        createListView();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    public void createListView(){
        lv_teams = (ListView) getView().findViewById(R.id.teams_list);
        arrayAdapter = new ArrayAdapter<Team>(getContext(), android.R.layout.simple_list_item_1, teamList);
        lv_teams.setAdapter(arrayAdapter);

        lv_teams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                String selectedTeamID = teamList.get(position).getId();
                Intent intent = new Intent(getActivity(), TeamDetailsActivity.class);
                intent.putExtra("teamID",selectedTeamID);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_coaching, container, false);

        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton3);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewTeam.class);
                startActivity(intent);
            }
        });

        return view;
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

    static class Team {

        String id;
        String name;
        String description;

        public Team() {
        }

        public Team(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public String getId(){return id;}

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString(){
            return this.getName();
        }
    }

}
