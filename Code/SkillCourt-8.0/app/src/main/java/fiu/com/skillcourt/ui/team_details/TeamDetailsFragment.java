package fiu.com.skillcourt.ui.team_details;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import fiu.com.skillcourt.ui.base.BaseFragment;
import fiu.com.skillcourt.ui.coach_dashboard.CoachingFragment;
import fiu.com.skillcourt.ui.new_team.NewTeam;
import fiu.com.skillcourt.ui.search_user.SearchUserActivity;
import fiu.com.skillcourt.ui.statistics.StatsActivity;


public class TeamDetailsFragment extends BaseFragment {
    private static final String ARG_TEAMID = "teamID";
    private String mTeamID;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    TextView tv_team_name;
    TextView tv_team_desc;

    List<Player> rosterList = new ArrayList<>();

    public TeamDetailsFragment() {
        // Required empty public constructor
    }

    public static TeamDetailsFragment newInstance(String teamID) {
        TeamDetailsFragment fragment = new TeamDetailsFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TEAMID, teamID);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeamID = getArguments().getString(ARG_TEAMID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tv_team_name = (TextView) getView().findViewById(R.id.textView_team_name);
        tv_team_desc = (TextView) getView().findViewById(R.id.textView_team_desc);
        ListView lv_team_roster = (ListView) getView().findViewById(R.id.list_view_team_roster);

        DatabaseReference mTeamRef = mRootRef.child("users")
                .child(user.getUid()).child("teams").child(mTeamID);

        DatabaseReference teamNameRef = mTeamRef.child("name");
        DatabaseReference teamDescRef = mTeamRef.child("description");
        DatabaseReference teamRosterRef = mTeamRef.child("roster");


        teamNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                tv_team_name.setText((CharSequence) snap.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        teamDescRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                tv_team_desc.setText((CharSequence) snap.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        teamRosterRef.addValueEventListener(
                new ValueEventListener() {
                    public void onDataChange(DataSnapshot snapshot) {
                        rosterList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            String playerNickname = postSnapshot.getValue(String.class);
                            Player player = new Player(postSnapshot.getKey(), playerNickname);
                            rosterList.add(player);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        ArrayAdapter<Player> arrayAdapter = new ArrayAdapter<Player>(getContext(), android.R.layout.simple_list_item_1, rosterList);

        lv_team_roster.setAdapter(arrayAdapter);

        lv_team_roster.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                String selectedUserID = rosterList.get(position).getId();
                Intent intent = new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("playerID",selectedUserID);
                startActivity(intent);

            }
        });

        FloatingActionButton myFabAdd = (FloatingActionButton) getView().findViewById(R.id.floatActionButton_addPlayer);
        myFabAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchUserActivity.class);
                intent.putExtra("teamID",mTeamID);
                startActivity(intent);
            }
        });
    }


    static class Player{
        String id;
        String nickname;

        public Player() {
        }

        public Player(String id, String nickname) {
            this.id = id;
            this.nickname = nickname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setMickname(String nickname) {
            this.nickname = nickname;
        }

        @Override
        public String toString() {
            return nickname;
        }
    }
}
