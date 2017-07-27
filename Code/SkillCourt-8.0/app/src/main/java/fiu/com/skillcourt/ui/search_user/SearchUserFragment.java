package fiu.com.skillcourt.ui.search_user;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

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
import fiu.com.skillcourt.ui.coach_dashboard.CoachingDashboardActivity;
import fiu.com.skillcourt.ui.coach_dashboard.CoachingFragment;
import fiu.com.skillcourt.ui.team_details.TeamDetailsActivity;


public class SearchUserFragment extends Fragment {
    private static final String ARG_TEAMID = "teamID";
    private String mTeamID;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("users");
    private Menu menu;
    private List<Player> playersResultList = new ArrayList<>();
    ListView listViewPlayersResult;

    public SearchUserFragment() {
        // Required empty public constructor
    }

    public static SearchUserFragment newInstance(String teamID) {
        SearchUserFragment fragment = new SearchUserFragment();
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

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_user, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.options_menu, menu);

        this.menu = menu;

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        listViewPlayersResult = (ListView) getView().findViewById(R.id.list_view_players_results);

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){

                    @Override
                    public boolean onQueryTextChange (String newText) {
                        return true;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        mUserRef.orderByChild("email").startAt(query)
                                .addValueEventListener(
                                        new ValueEventListener() {
                                            public void onDataChange(DataSnapshot snapshot) {
                                                playersResultList.clear();
                                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                    String email = postSnapshot.child("email").getValue(String.class);
                                                    String id = postSnapshot.getKey();
                                                    Player player = new Player(id,email);
                                                    playersResultList.add(player);
                                                }

                                                showResultList();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) { }
                                        });
                        return false;
                    }
                });

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(
                getActivity().getApplicationContext(), getActivity().getClass());
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName));

    }

    public void showResultList(){
        ArrayAdapter<Player> arrayAdapter = new ArrayAdapter<Player>(getContext(), android.R.layout.simple_list_item_1, playersResultList);

        listViewPlayersResult.setAdapter(arrayAdapter);

        listViewPlayersResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                final String selectedPlayerID = playersResultList.get(position).getId();
                String selectedPlayerEmail = playersResultList.get(position).getEmail();

                final EditText input = new EditText(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                AlertDialog ad = new AlertDialog.Builder(getContext())
                        .create();
                ad.setCancelable(false);
                ad.setTitle("Do you want to add "+selectedPlayerEmail+ " to your team?");
                ad.setMessage("Add a nickname and then click on add");
                ad.setView(input);
                ad.setButton(getContext().getString(R.string.add_text), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        mUserRef.child(user.getUid()).child("teams").child(mTeamID)
                                .child("roster").child(selectedPlayerID).setValue(input.getText().toString());
                        dialog.dismiss();
                        getActivity().onBackPressed();
                    }
                });
                ad.show();
            }
        });
    }

    static class Player{
        String id;
        String email;

        public Player() {
        }

        public Player(String id, String email) {
            this.id = id;
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            return email;
        }
    }
}
