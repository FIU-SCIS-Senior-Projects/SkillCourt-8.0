package fiu.com.skillcourt.manager;

import android.util.SparseIntArray;

/**
 * Created by pedrocarrillo on 9/21/16.
 */
public class StepManager {

    private int totalPads;
    private SparseIntArray customSteps = new SparseIntArray();

    private static StepManager ourInstance = new StepManager();

    public static StepManager getInstance() {
        return ourInstance;
    }

    private StepManager() {
        totalPads = 4;
    }

    public int getTotalPads() {
        return totalPads;
    }

    public void setTotalPads(int totalPads) {
        this.totalPads = totalPads;
    }

    public void addStep(Integer stepNumber, Integer padSelection) {
        customSteps.put(stepNumber, padSelection);
    }

    public void removeStep(Integer stepNumber) {
        customSteps.delete(stepNumber);
    }

    public SparseIntArray Steps ()
    {
        return customSteps;
    }


}
