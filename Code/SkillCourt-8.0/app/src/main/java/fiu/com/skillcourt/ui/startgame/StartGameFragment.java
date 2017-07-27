package fiu.com.skillcourt.ui.startgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.entities.Game;
import fiu.com.skillcourt.game.SkillCourtGame;
import fiu.com.skillcourt.game.SkillCourtManager;
import fiu.com.skillcourt.interfaces.Constants;
import fiu.com.skillcourt.ui.base.ArduinosCommunicationFragment;
import fiu.com.skillcourt.ui.base.ArduinosStartCommunicationFragment;
import fiu.com.skillcourt.ui.creategame.CreateGameActivity;

/**
 * @author pedrocarrillo
 */
public class StartGameFragment extends ArduinosCommunicationFragment implements StartGameView, View.OnClickListener{

    private TextView tvTimer;
    private ProgressBar progressBar;
    private TextView tvHits;
    private TextView tvRedHits;
    private TextView tvGreenHits;
    private TextView tvScore;
    private TextView tvAccuracy;
    private LinearLayout btnContainer, tvContainer;
    private RelativeLayout timerContainer;

    StartGamePresenter startGamePresenter;

    //Firebase instances
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userDatabaseReference;

    public static StartGameFragment newInstance(ArrayList<String> sequences) {
        StartGameFragment startGameFragment = new StartGameFragment();
        if (!sequences.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.TAG_SEQUENCE, sequences);
            startGameFragment.setArguments(bundle);
        }
        return startGameFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> sequences = new ArrayList<>();
        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.TAG_SEQUENCE))
                sequences = (ArrayList) getArguments().getSerializable(Constants.TAG_SEQUENCE);
        }
        startGamePresenter = new StartGamePresenter(this, sequences);
        firebaseDatabase = FirebaseDatabase.getInstance();
        userDatabaseReference = firebaseDatabase.getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_game, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTimer = (TextView)view.findViewById(R.id.tvTimer);
        tvAccuracy = (TextView)view.findViewById(R.id.tv_accuracy);
        tvGreenHits = (TextView)view.findViewById(R.id.tv_green_hits);
        tvRedHits = (TextView) view.findViewById(R.id.tv_red_hits);
        tvHits = (TextView)view.findViewById(R.id.tv_hits);
        tvScore = (TextView)view.findViewById(R.id.tv_score);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        btnContainer = (LinearLayout)view.findViewById(R.id.btn_containers);
        tvContainer = (LinearLayout)view.findViewById(R.id.tv_container);
        timerContainer = (RelativeLayout)view.findViewById(R.id.timer_container);
        Button btnNewGame, btnSaveAndPlay, btnPlayAgain, btnSaveAndNewGame;
        btnNewGame = (Button) view.findViewById(R.id.btn_new_game);
        btnPlayAgain = (Button) view.findViewById(R.id.btn_play_again);
        btnSaveAndPlay = (Button) view.findViewById(R.id.btn_save_play_again);
        btnSaveAndNewGame = (Button) view.findViewById(R.id.btn_save_and_new_game);
        btnNewGame.setOnClickListener(this);
        btnPlayAgain.setOnClickListener(this);
        btnSaveAndPlay.setOnClickListener(this);
        btnSaveAndNewGame.setOnClickListener(this);
        startGamePresenter.startGame();
    }

    @Override
    public void onPause() {
        super.onPause();
        startGamePresenter.cancelGame();
    }

    @Override
    public void onResume() {
        super.onResume();
        startGamePresenter.onResume(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startGamePresenter.cancelGame();
    }

    @Override
    public void setProgressTotal(int seconds) {
        progressBar.setMax(seconds);
    }

    @Override
    public void setTimerText(String time) {
        tvTimer.setText(time);
    }

    @Override
    public void changeProgressView(long seconds) {
        progressBar.setProgress((int)seconds);
    }

    @Override
    public void updateResult(final float totalHits,final float greenHits, final float redHits,final int score,final int accuracy) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvHits.setText(String.valueOf(totalHits));
                tvRedHits.setText(String.valueOf(redHits));
                tvGreenHits.setText(String.valueOf(greenHits));
                tvScore.setText(String.valueOf(score));
                tvAccuracy.setText(String.valueOf(accuracy) + " %");
            }
        });
    }

    @Override
    public void setupInitGame() {
        timerContainer.setVisibility(View.VISIBLE);
        btnContainer.setVisibility(View.GONE);
        tvContainer.setVisibility(View.GONE);
    }

    @Override
    public void setupFinishGame() {
        timerContainer.setVisibility(View.GONE);
        btnContainer.setVisibility(View.VISIBLE);
        tvContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_new_game) {
            Intent intent = new Intent(getActivity(), CreateGameActivity.class);
            startActivity(intent);
            fragmentListener.closeActivity();
        } else if (view.getId() == R.id.btn_save_and_new_game) {
            startGamePresenter.saveFirebase();
            Intent intent = new Intent(getActivity(), CreateGameActivity.class);
            startActivity(intent);
            fragmentListener.closeActivity();
        } else if (view.getId() == R.id.btn_save_play_again) {
            startGamePresenter.saveFirebase();
            startGamePresenter.playAgain();
        } else if (view.getId() == R.id.btn_play_again) {
            startGamePresenter.playAgain();
        }
    }

    @Override
    public void saveFirebase(Game game) {
        userDatabaseReference.child("games").push().setValue(game);
    }

}
