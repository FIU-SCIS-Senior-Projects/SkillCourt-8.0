package fiu.com.skillcourt.ui.creategame;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Spinner;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.TimePicker;

import java.util.HashMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.game.SkillCourtGame;
import fiu.com.skillcourt.game.SkillCourtManager;
import fiu.com.skillcourt.interfaces.Constants;
import fiu.com.skillcourt.ui.base.ArduinosStartCommunicationFragment;
import fiu.com.skillcourt.ui.base.Utils;
import fiu.com.skillcourt.ui.custom.GameModePickerFragment;
import fiu.com.skillcourt.ui.custom.NumberPickerFragment;
import fiu.com.skillcourt.ui.custom.TimePickerFragment;
import fiu.com.skillcourt.ui.startgame.StartGameActivity;


public class CreateGameFragment extends ArduinosStartCommunicationFragment implements TimePickerDialog.OnTimeSetListener, DialogInterface.OnClickListener, NumberPickerFragment.NumericDialogListener,AdapterView.OnItemSelectedListener {


    Button btnTime, btnGameMode, btnStartGame, btnTimeObjective;
    LinearLayout modeTimerContainer;
    TimePickerFragment timePickerFragment = new TimePickerFragment();
    GameModePickerFragment gameModePickerFragment = new GameModePickerFragment();

    HashMap myData;
    Spinner spinner;
    HashMap<String,String> spinnerMap = new HashMap<String, String>();
    protected FirebaseAuth mAuth;
    ArrayList<String> mySequences = new ArrayList<String>();
    HashMap globalSequences=new HashMap();
    ArrayAdapter<String> spinnerArrayAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference sequences =database.getReference("sequences");
    String defaultKey="";
    HashMap<String,String> defaultSequence = new HashMap<String, String>();

    NumberPickerFragment numberPickerFragment = new NumberPickerFragment();

    private int time = -1;
    private int frequencyTime = -1;
    private SkillCourtGame.GameMode selectedGameMode;

    public static CreateGameFragment newInstance() {
        return new CreateGameFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //return view;
        return inflater.inflate(R.layout.fragment_create_game, container, false);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        time = i*60+i1;
        numberPickerFragment.setupNumberPicker(time);
        btnTime.setText(String.format("%02d:%02d",i,+i1));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnTime = (Button) view.findViewById(R.id.btn_time);
        btnGameMode = (Button) view.findViewById(R.id.btn_gameMode);
        btnStartGame = (Button) view.findViewById(R.id.start_game);
        btnTimeObjective = (Button)view.findViewById(R.id.time_objective_button);
        modeTimerContainer = (LinearLayout)view.findViewById(R.id.mode_timer_container);
        timePickerFragment.setOnTimerPickerDialog(CreateGameFragment.this);
        gameModePickerFragment.setOnClickListener(this);
        numberPickerFragment.setOnClickListener(this);

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerFragment.show(getChildFragmentManager(), "time_picker");
            }
        });

        btnGameMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameModePickerFragment.show(getChildFragmentManager(), "game_mode");
            }
        });

        btnTimeObjective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (time <= 0) {
                    Snackbar.make(getView(), "Please pick a time first", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (frequencyTime != -1) numberPickerFragment.setValue(frequencyTime);
                    numberPickerFragment.show(getChildFragmentManager(), "number_picker");
                }
            }
        });

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    SkillCourtManager.getInstance().getGame().setGameTimeTotal(time);
                    SkillCourtManager.getInstance().getGame().setGameMode(selectedGameMode);
                    if (selectedGameMode == SkillCourtGame.GameMode.BEAT_TIMER) {
                        SkillCourtManager.getInstance().getGame().setTimeObjective(frequencyTime);
                    }
                    Intent intent = new Intent(getActivity(), StartGameActivity.class);
                    intent.putExtra(Constants.TAG_SEQUENCE, defaultSequence);
                    startActivity(intent);
                    fragmentListener.closeActivity();
                } else {
                    Utils.creatSimpleDialog(getActivity(), "Please fill all the fields").show();
                }
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference users =database.getReference("users");
        DatabaseReference myRef = users.child(user.getUid());
        final DatabaseReference mySeq=myRef.child("sequences");


        sequences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                globalSequences=(HashMap)dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //View view = inflater.inflate(R.layout.fragment_create_game, container, false);
        spinner = (Spinner)view.findViewById(R.id.sequence_spinner);

        spinnerArrayAdapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_spinner_item, mySequences);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item );
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);
        mySeq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myData=(HashMap) dataSnapshot.getValue();
                if(myData==null) return;
                Iterator entries = myData.entrySet().iterator();
                mySequences.clear();
                mySequences.add("");
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    String key = entry.getKey().toString();
                    String value=entry.getValue().toString();
                    HashMap item=(HashMap)globalSequences.get(key);
                    if(item==null) continue;
                    if(value.equals("default")){
                        defaultKey=key;
                        defaultSequence=item;



                    }

                    mySequences.add(item.get("name").toString());
                    spinnerMap.put(item.get("name").toString(),key);
                    entries.remove();
                    if(defaultKey!=""){
                        int spinnerPosition = spinnerArrayAdapter.getPosition(((Map.Entry)
                                defaultSequence.entrySet().toArray()[0]).getValue().toString());
                        spinner.setSelection(spinnerPosition);
                    }
                }
                spinnerArrayAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public boolean isValid() {
        if (selectedGameMode != null) {
            if (selectedGameMode == SkillCourtGame.GameMode.HIT_MODE) {
                return time > 0;
            } else if (selectedGameMode == SkillCourtGame.GameMode.BEAT_TIMER) {
                return time > 1 && frequencyTime > 1;
            } else {
                //for other game modes
                return false;
            }
        } else {
            return  false;
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        SkillCourtGame.GameMode gameMode;
        if (which == 0) {
            gameMode = SkillCourtGame.GameMode.BEAT_TIMER;
            btnGameMode.setText("Beat timer");
            modeTimerContainer.setVisibility(View.VISIBLE);

        } else {
            gameMode = SkillCourtGame.GameMode.HIT_MODE;
            btnGameMode.setText("Hit mode");
            modeTimerContainer.setVisibility(View.GONE);
        }
        selectedGameMode = gameMode;
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Object sequence = spinner.getSelectedItem();
        FirebaseUser user ;
        DatabaseReference myRef;
        DatabaseReference mySeq;
        if(sequence.toString()=="" && mAuth!=null){
            defaultKey="";
            defaultSequence=null;
            mAuth = FirebaseAuth.getInstance();
            user= mAuth.getCurrentUser();
            DatabaseReference users =database.getReference("users");
            myRef = users.child(user.getUid());
            mySeq=myRef.child("sequences");
            for(String seq:mySequences)
            {
                if(seq=="") continue;
                String otherid = spinnerMap.get(seq);
                DatabaseReference otherRef=mySeq.child(otherid);
                otherRef.setValue("");
            }
            return;
        }
        else{
            mAuth = FirebaseAuth.getInstance();
            user= mAuth.getCurrentUser();
            DatabaseReference users =database.getReference("users");
            myRef = users.child(user.getUid());
            mySeq=myRef.child("sequences");
        }
        String id = spinnerMap.get(sequence.toString());
        if(id!=null) {
            DatabaseReference saveID = mySeq.child(id);
            saveID.setValue("default");
        }
        for(String seq:mySequences)
        {
            if(seq=="") continue;
            String otherid = spinnerMap.get(seq);
            if(otherid==id || id==null)continue;
            DatabaseReference otherRef=mySeq.child(otherid);
            otherRef.setValue("");
        }
    }

    public void getGlobalSequences(){
        sequences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                globalSequences=(HashMap)dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getMySequences(){
        sequences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myData=(HashMap)dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNumberSelected(int number) {
        btnTimeObjective.setText(String.valueOf(number));
        frequencyTime = number;
    }
}
