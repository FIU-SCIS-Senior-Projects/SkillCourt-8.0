package fiu.com.skillcourt.ui.dynamicsteps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.game.Sequences;
import fiu.com.skillcourt.manager.StepManager;
import fiu.com.skillcourt.ui.custom.Step;
import fiu.com.skillcourt.ui.dashboard.MainDashboardActivity;
import fiu.com.skillcourt.ui.startgame.StartGameActivity;
import pedrocarrillo.com.materialstepperlibrary.StepLayout;
import pedrocarrillo.com.materialstepperlibrary.interfaces.StepLayoutResult;

/**
 * A placeholder fragment containing a simple view.
 */
public class DynamicStepsFragment extends Fragment implements View.OnClickListener {

    private StepLayout stepLayout;
    private Button btnSave;
    private Button btn_continue;
    protected FirebaseAuth mAuth;
    protected EditText seq_Name;
    protected NumberPicker np;
    protected FirebaseAuth.AuthStateListener mAuthListener;

    public static DynamicStepsFragment newInstance() {
        return new DynamicStepsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_dynamic_steps, container, false);
        btnSave = (Button) view.findViewById(R.id.btn_save) ;
        seq_Name = (EditText) view.findViewById(R.id.seq_name);
        btn_continue = (Button) view.findViewById(R.id.btn_continue) ;
        np = (NumberPicker) view.findViewById(R.id.np) ;
        btnSave.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stepLayout = (StepLayout) view.findViewById(R.id.stepLayout);

        stepLayout.setStepLayoutResult(new StepLayoutResult() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onCancel() {

            }
        });
        NumberPicker np = (NumberPicker) view.findViewById(R.id.np);
        np.setMaxValue(100);
        np.setMinValue(0);

    }

    @Override
    public void onClick(View v) {
        int numSteps=0;
        switch(v.getId()){
            case R.id.btn_continue:
                btn_continue.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                seq_Name.setVisibility(View.VISIBLE);
                np.setVisibility(View.GONE);
                numSteps=np.getValue();
                for (int i = 0; i < numSteps; i++) {
                    Step step = new Step(getContext());
                    stepLayout.addStepView(step);

                }
                stepLayout.load();
                break;
            case R.id.btn_save:
                if( seq_Name.getText().toString().trim().equals("")){
                    seq_Name.setError( "Sequence Name is required!" );
                }else{
                    mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {
                    SparseIntArray steps = StepManager.getInstance().Steps();
                    HashMap<String, String> sequence = new HashMap<String, String>();
                    sequence.clear();

                    for (int i = 0; i < steps.size(); i++) {
                        int stepNumber = steps.keyAt(i);
                        int stepValue = steps.get(stepNumber);
                        sequence.put(new Integer(stepNumber).toString(), new Integer(stepValue).toString());
                    }

                    //save sequence outside user
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference sequences =database.getReference("sequences");
                    DatabaseReference myKey=sequences.push();
                    Sequences mysequence=new Sequences(seq_Name.getText().toString().trim(),sequence);
                    DatabaseReference newSeq=sequences.child(myKey.getKey());
                    newSeq.setValue(mysequence);


                    //save sequence ID inside user
                    FirebaseUser user = mAuth.getCurrentUser();
                    DatabaseReference users =database.getReference("users");
                    DatabaseReference myRef = users.child(user.getUid());
                    DatabaseReference mySeq=myRef.child("sequences");
                    DatabaseReference saveID=mySeq.child(myKey.getKey());
                    saveID.setValue(myKey.getKey());

                    steps.clear();
                    Intent intent = new Intent(getActivity(), MainDashboardActivity.class);
                    startActivity(intent);
                }
                }break;
        }
    }

}
