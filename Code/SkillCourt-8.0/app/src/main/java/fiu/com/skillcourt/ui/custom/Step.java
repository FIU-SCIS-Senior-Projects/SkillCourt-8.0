package fiu.com.skillcourt.ui.custom;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import fiu.com.skillcourt.R;
import fiu.com.skillcourt.manager.StepManager;
import pedrocarrillo.com.materialstepperlibrary.StepView;

/**
 * Created by pedrocarrillo on 9/21/16.
 */

public class Step extends StepView {

    private RadioGroup rgPads;

    public Step(Context context) {
        super(context);
    }

    @Override
    public int customStepResource() {
        return R.layout.step_layout;
    }

    @Override
    public void setupViews() {
        rgPads = (RadioGroup) findViewById(R.id.rg_pads);

        for (int i = 0; i < StepManager.getInstance().getTotalPads(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText("Pad # "+(i+1));
            radioButton.setTag(i + 1);
            rgPads.addView(radioButton);
        }
    }

    @Override
    public boolean onStepSuccess() {
        RadioButton radioButton = (RadioButton)findViewById(rgPads.getCheckedRadioButtonId());
        int radioValue = (Integer) radioButton.getTag();
        StepManager.getInstance().addStep(stepNumber,radioValue);
        return true;
    }

    @Override
    public String showTitle() {
        return "Step "+ stepNumber;
    }

    @Override
    public void onStepCancel() {
        StepManager.getInstance().removeStep(stepNumber);
    }

    @Override
    public String showSelection() {
        RadioButton radioButton = (RadioButton)findViewById(rgPads.getCheckedRadioButtonId());
        int radioValue = (Integer) radioButton.getTag();
        return "Pad # "+ radioValue;
    }
}
