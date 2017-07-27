package fiu.com.skillcourt.ui.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.ui.base.BaseActivity;
import fiu.com.skillcourt.ui.base.BaseFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;


import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by pedrocarrillo on 11/21/16.
 */

public class StatisticsFragment extends BaseFragment {

    ArrayList<Long> scoresList = new ArrayList<Long>();
    ArrayList<Long> datesList = new ArrayList<Long>();
    ArrayList<Long> greenList = new ArrayList<Long>();
    ArrayList<Long> totalList = new ArrayList<Long>();
    ArrayList<Long> timeList = new ArrayList<Long>();

    private static final String ARG_PLAYERID = "playerID";
    private String mPlayerID;

    protected FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;

    public static StatisticsFragment newInstance(String playerID) {
        StatisticsFragment fragment = new StatisticsFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PLAYERID, playerID);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlayerID = getArguments().getString(ARG_PLAYERID);
        } else {
            mPlayerID = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID;

        if(mPlayerID != null){
            userID = mPlayerID;
        } else {
            userID = user.getUid();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users/" + userID).child("games");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                scoresList.clear();
                datesList.clear();
                greenList.clear();
                totalList.clear();
                timeList.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Map<String, String> map = (Map<String, String>) postSnapshot.getValue();

                    Object score = map.get((Object) "score");
                    scoresList.add((Long) score);

                    //Object date = map.get((Object) "date");
                    //datesList.add((Long) date);

                    Object red = map.get((Object) "greenHits");
                    greenList.add((Long) red);

                    Object total = map.get((Object) "totalHits");
                    totalList.add((Long) total);

                    //Object times = map.get((Object) "gameTimeTotal");
                    //timeList.add((Long) times);
                }

                createHitIcons(greenList, totalList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    protected  void createHitIcons (ArrayList<Long> greenHits, ArrayList<Long> totalHits)
    {

        TextView toChange = (TextView) getView().findViewById(R.id.gamesPlayed);

        if(mPlayerID != null){
            toChange.setText("This player have played  " + greenHits.size() + "  SkillCourt games! " +
                    " Wow!");
        } else {
            toChange.setText("I've Played  " + greenHits.size() + "  SkillCourt games! " +
                    " Wow!");
        }




        TextView greenTV = (TextView) getView().findViewById(R.id.greenhits);
        TextView redTV = (TextView) getView().findViewById(R.id.redhits);
        TextView pointsTV = (TextView) getView().findViewById(R.id.points);

        int totalGreen = 0;
        int totalRed = 0;
        int totalPoints = 0;
        if ((greenList.size() != 0) || (totalList.size() != 0)) {
            for (int i = 0; i < greenHits.size(); i++) {
                totalGreen = totalGreen + Integer.parseInt(greenHits.get(i).toString());
                totalPoints = Integer.parseInt(totalHits.get(i).toString());
            }

            totalRed = totalPoints - totalGreen;
        }

        greenTV.setText(Long.toString(totalGreen)); //leave this line to assign a specific text
        redTV.setText(Long.toString(totalRed)); //leave this line to assign a specific text
        pointsTV.setText(Long.toString(totalPoints)); //leave this line to assign a specific text
    }

}


